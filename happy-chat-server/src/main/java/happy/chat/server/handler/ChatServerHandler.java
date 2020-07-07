package happy.chat.server.handler;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import happy.chat.common.protobuf.request.RequestBody;
import happy.chat.server.util.SessionUtils;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 具体业务handler
 *
 * @author Terry
 */
@Component
@ChannelHandler.Sharable
public class ChatServerHandler extends SimpleChannelInboundHandler<RequestBody.RequestMsg> {

    private static final Logger logger = LoggerFactory.getLogger(ChatServerHandler.class);

    private static final ExecutorService SERVICE = new ThreadPoolExecutor(1, 10, 30L, TimeUnit.SECONDS,
            new SynchronousQueue<>(), new ThreadFactoryBuilder().setDaemon(true).setNameFormat("ChatServerHandler-%d").build(),
            new ThreadPoolExecutor.CallerRunsPolicy());

    @Autowired
    private SignOutHandler signOutHandler;

    @Autowired
    private MessageHandler messageHandler;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RequestBody.RequestMsg msg) throws Exception {
        RequestBody.RequestMsg.Command command = msg.getCommand();
        SERVICE.execute(() -> {
            try {
                if (command == RequestBody.RequestMsg.Command.MESSAGE) {
                    messageHandler.channelRead(ctx, msg.getUserMessage());
                } else if (command == RequestBody.RequestMsg.Command.SIGN_OUT) {
                    signOutHandler.channelRead(ctx, msg.getSignOut());
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                //异常关掉channel
                SessionUtils.delSession(ctx.channel());
                ctx.close();
            }
        });
    }

}
