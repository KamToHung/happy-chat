package happy.chat.server.handler.message;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import happy.chat.common.protobuf.request.RequestBody;
import happy.chat.common.protobuf.response.ResponseBody;
import happy.chat.server.entity.UserInfo;
import happy.chat.server.util.SessionUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 信息handler
 *
 * @author Terry
 */
@Component
@ChannelHandler.Sharable
public class MessageHandler extends SimpleChannelInboundHandler<RequestBody.RequestMsg.UserMessage> {

    private static final Logger logger = LoggerFactory.getLogger(MessageHandler.class);

    /**
     * 添加线程,多任务进行操作
     */
    private final ExecutorService service;

    @Autowired
    public MessageHandler() {
        //为什么业务逻辑阻塞的情况下建议使用业务自己的线程池？netty是串行的执行handler链，
        //worker里面的EventLoop的实现实际上是SingleThreadEventLoop,也就是说netty io线程executor实际上是单线程的。
        //如果某个业务逻辑导致其中一个handler链阻塞，其他分配到这个handler链的链接都会阻塞，但这个时候很有可能其他的handler链是空闲状态。
        //故如果有阻塞逻辑不采用自己的线程池就有可能造成在压力不大的情况下造成部分client阻塞
        //知道哪些业务阻塞就在哪个handler处理.两个不同handler不会互相阻塞
        //如果阻塞了,比如例子中的心跳和发送信息,如果同一个channel正在处理信息handler(此时阻塞),那么心跳等处理信息handler处理完后才会执行
        service = new ThreadPoolExecutor(0, 20, 30L, TimeUnit.SECONDS,
                new SynchronousQueue<>(), new ThreadFactoryBuilder().setDaemon(true).setNameFormat("ChatServerHandler-%d").build(),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RequestBody.RequestMsg.UserMessage userMessageRequest) throws Exception {
        service.execute(() -> {
            //获取接收方channel
            Channel toUserChannel = SessionUtils.getChannel(userMessageRequest.getToUserId());
            if (toUserChannel == null) {
                logger.warn("用户:[" + userMessageRequest.getToUserId() + "]不在线,发送失败");
                return;
            }
            if (!toUserChannel.isActive()) {
                logger.error("用户:[" + userMessageRequest.getToUserId() + "]连接未激活,发送失败");
                return;
            }
            Channel channel = ctx.channel();
            //获取请求中的session
            UserInfo userInfo = SessionUtils.getSession(channel);
            ResponseBody.ResponseMsg.UserMessage.Builder messageResponse = ResponseBody.ResponseMsg.UserMessage.newBuilder();
            messageResponse.setFromUserId(userInfo.getUserId());
            messageResponse.setFromUsername(userInfo.getUsername());
            messageResponse.setMessage(userMessageRequest.getMessage());
            //测试耗时操作
            if (!userInfo.getUsername().equals("xiaoming")) {
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e) {
                }
            }
            toUserChannel.writeAndFlush(ResponseBody.ResponseMsg.newBuilder().setCommand(ResponseBody.ResponseMsg.Command.MESSAGE)
                    .setUserMessage(messageResponse));
        });
    }

    @PreDestroy
    public void destroy() {
        if (service != null) {
            service.shutdown();
        }
    }

}
