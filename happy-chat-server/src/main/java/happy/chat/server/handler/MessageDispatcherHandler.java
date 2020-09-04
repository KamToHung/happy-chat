package happy.chat.server.handler;

import happy.chat.common.protobuf.request.RequestBody;
import happy.chat.server.handler.group.CreateGroupHandler;
import happy.chat.server.handler.group.GroupMessageHandler;
import happy.chat.server.handler.message.MessageHandler;
import happy.chat.server.handler.status.HeartbeatHandler;
import happy.chat.server.handler.sign.SignInHandler;
import happy.chat.server.handler.sign.SignOutHandler;
import happy.chat.server.util.SessionUtils;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 具体业务handler
 *
 * @author Terry
 */
@Component
@ChannelHandler.Sharable
public class MessageDispatcherHandler extends SimpleChannelInboundHandler<RequestBody.RequestMsg> {

    private static final Logger logger = LoggerFactory.getLogger(MessageDispatcherHandler.class);

    private final SignInHandler signInHandler;

    private final HeartbeatHandler heartbeatHandler;

    private final SignOutHandler signOutHandler;

    private final MessageHandler messageHandler;

    private final CreateGroupHandler createGroupHandler;

    private final GroupMessageHandler groupMessageHandler;

    public MessageDispatcherHandler(SignInHandler signInHandler, HeartbeatHandler heartbeatHandler,
                                    SignOutHandler signOutHandler, MessageHandler messageHandler,
                                    CreateGroupHandler createGroupHandler, GroupMessageHandler groupMessageHandler) {
        this.signInHandler = signInHandler;
        this.heartbeatHandler = heartbeatHandler;
        this.signOutHandler = signOutHandler;
        this.messageHandler = messageHandler;
        this.createGroupHandler = createGroupHandler;
        this.groupMessageHandler = groupMessageHandler;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RequestBody.RequestMsg msg) {
        RequestBody.RequestMsg.Command command = msg.getCommand();
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
                createGroupHandler.channelRead(ctx, msg.getCreateGroup());
            } else if (command == RequestBody.RequestMsg.Command.GROUP_MESSAGE) {
                groupMessageHandler.channelRead(ctx, msg.getGroupMessage());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            //异常关掉channel
            SessionUtils.delSession(ctx.channel());
            ctx.close();
        }
    }

}
