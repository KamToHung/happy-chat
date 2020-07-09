package happy.chat.client.handler;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import happy.chat.common.protobuf.request.RequestBody;
import happy.chat.common.protobuf.response.ResponseBody;
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

@Component
@ChannelHandler.Sharable
public class ChatClientHandler extends SimpleChannelInboundHandler<ResponseBody.ResponseMsg> {

    private static final Logger logger = LoggerFactory.getLogger(ChatClientHandler.class);

    private static final ExecutorService SERVICE = new ThreadPoolExecutor(1, 10, 30L, TimeUnit.SECONDS,
            new SynchronousQueue<>(), new ThreadFactoryBuilder().setDaemon(true).setNameFormat("ChatClientHandler-%d").build(),
            new ThreadPoolExecutor.CallerRunsPolicy());

    @Autowired
    private SignInHandler signInHandler;

    @Autowired
    private MessageHandler messageHandler;

    @Autowired
    private SignOutHandler signOutHandler;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ResponseBody.ResponseMsg msg) throws Exception {
        ResponseBody.ResponseMsg.Command command = msg.getCommand();
        SERVICE.execute(() -> {
            try {
                if (command == ResponseBody.ResponseMsg.Command.SIGN_IN) {
                    signInHandler.channelRead(ctx, msg);
                } else if (command == ResponseBody.ResponseMsg.Command.MESSAGE) {
                    messageHandler.channelRead(ctx, msg);
                } else if (command == ResponseBody.ResponseMsg.Command.SIGN_OUT) {
                    signOutHandler.channelRead(ctx, msg);
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                ctx.close();
            }
        });
    }

}
