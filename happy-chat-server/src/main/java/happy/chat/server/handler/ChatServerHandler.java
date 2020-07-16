package happy.chat.server.handler;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import happy.chat.common.protobuf.request.RequestBody;
import happy.chat.server.util.SessionUtils;
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

/**
 * 具体业务handler
 *
 * @author Terry
 */
@Component
@ChannelHandler.Sharable
public class ChatServerHandler extends SimpleChannelInboundHandler<RequestBody.RequestMsg> {

    private static final Logger logger = LoggerFactory.getLogger(ChatServerHandler.class);

    /**
     * 添加线程,多任务进行操作
     */
    private final ExecutorService service;

    private final SignInHandler signInHandler;

    private final HeartbeatHandler heartbeatHandler;

    private final SignOutHandler signOutHandler;

    private final MessageHandler messageHandler;

    private final CreateGroupHandler createGroupHandler;

    private final GroupMessageHandler groupMessageHandler;

    public ChatServerHandler(SignInHandler signInHandler, HeartbeatHandler heartbeatHandler,
                             SignOutHandler signOutHandler, MessageHandler messageHandler,
                             CreateGroupHandler createGroupHandler, GroupMessageHandler groupMessageHandler) {
        this.signInHandler = signInHandler;
        this.heartbeatHandler = heartbeatHandler;
        this.signOutHandler = signOutHandler;
        this.messageHandler = messageHandler;
        this.createGroupHandler = createGroupHandler;
        this.groupMessageHandler = groupMessageHandler;
        service = new ThreadPoolExecutor(1, 20, 30L, TimeUnit.SECONDS,
                new SynchronousQueue<>(), new ThreadFactoryBuilder().setDaemon(true).setNameFormat("ChatServerHandler-%d").build(),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RequestBody.RequestMsg msg) throws Exception {
        RequestBody.RequestMsg.Command command = msg.getCommand();
        service.execute(() -> {
            try {
                if (command == RequestBody.RequestMsg.Command.SIGN_IN) {
                    signInHandler.channelRead(ctx, msg.getSignIn());
                } else if (command == RequestBody.RequestMsg.Command.HEARTBEAT) {
                    heartbeatHandler.channelRead(ctx, msg.getHeartbeat());
                } else if (command == RequestBody.RequestMsg.Command.MESSAGE) {
                    messageHandler.channelRead(ctx, msg.getUserMessage());
                } else if (command == RequestBody.RequestMsg.Command.SIGN_OUT) {
                    signOutHandler.channelRead(ctx, msg.getSignOut());
                } else if (command == RequestBody.RequestMsg.Command.CREATE_GROUP) {
                    createGroupHandler.channelRead(ctx, msg.getSignOut());
                } else if (command == RequestBody.RequestMsg.Command.GROUP_MESSAGE) {
                    groupMessageHandler.channelRead(ctx, msg.getSignOut());
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                //异常关掉channel
                SessionUtils.delSession(ctx.channel());
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
