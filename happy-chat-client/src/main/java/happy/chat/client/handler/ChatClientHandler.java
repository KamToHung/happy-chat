package happy.chat.client.handler;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import happy.chat.common.protobuf.response.ResponseBody;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
@ChannelHandler.Sharable
public class ChatClientHandler extends SimpleChannelInboundHandler<ResponseBody.ResponseMsg> {

    private static final Logger logger = LoggerFactory.getLogger(ChatClientHandler.class);

    private final ExecutorService service;

    private final SignInHandler signInHandler;

    private final MessageHandler messageHandler;

    private final SignOutHandler signOutHandler;

    private final HeartbeatHandler heartbeatHandler;

    private final CreateGroupHandler createGroupHandler;

    private final GroupMessageHandler groupMessageHandler;

    public ChatClientHandler(SignInHandler signInHandler, MessageHandler messageHandler,
                             SignOutHandler signOutHandler, HeartbeatHandler heartbeatHandler,
                             CreateGroupHandler createGroupHandler, GroupMessageHandler groupMessageHandler) {
        this.signInHandler = signInHandler;
        this.messageHandler = messageHandler;
        this.signOutHandler = signOutHandler;
        this.heartbeatHandler = heartbeatHandler;
        this.createGroupHandler = createGroupHandler;
        this.groupMessageHandler = groupMessageHandler;
        service = new ThreadPoolExecutor(1, 10, 30L, TimeUnit.SECONDS,
                new SynchronousQueue<>(), new ThreadFactoryBuilder().setDaemon(true).setNameFormat("ChatClientHandler-%d").build(),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ResponseBody.ResponseMsg msg) throws Exception {
        ResponseBody.ResponseMsg.Command command = msg.getCommand();
        service.execute(() -> {
            try {
                if (command == ResponseBody.ResponseMsg.Command.SIGN_IN) {
                    signInHandler.channelRead(ctx, msg.getSignIn());
                } else if (command == ResponseBody.ResponseMsg.Command.HEARTBEAT) {
                    heartbeatHandler.channelRead(ctx, msg.getHeartbeat());
                } else if (command == ResponseBody.ResponseMsg.Command.MESSAGE) {
                    messageHandler.channelRead(ctx, msg.getUserMessage());
                } else if (command == ResponseBody.ResponseMsg.Command.SIGN_OUT) {
                    signOutHandler.channelRead(ctx, msg.getSignOut());
                } else if (command == ResponseBody.ResponseMsg.Command.CREATE_GROUP) {
                    createGroupHandler.channelRead(ctx, msg.getSignOut());
                } else if (command == ResponseBody.ResponseMsg.Command.GROUP_MESSAGE) {
                    groupMessageHandler.channelRead(ctx, msg.getSignOut());
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                ctx.close();
            }
        });
    }

    @PreDestroy
    public void destroy() {
        if (service != null) {
            service.shutdown();
        }
    }

}
