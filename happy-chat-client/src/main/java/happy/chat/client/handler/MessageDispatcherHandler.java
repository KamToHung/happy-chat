package happy.chat.client.handler;

import happy.chat.client.handler.group.CreateGroupHandler;
import happy.chat.client.handler.group.GroupMessageHandler;
import happy.chat.client.handler.message.MessageHandler;
import happy.chat.client.handler.sign.SignInHandler;
import happy.chat.client.handler.sign.SignOutHandler;
import happy.chat.client.handler.status.HeartbeatHandler;
import happy.chat.common.protobuf.request.RequestBody;
import happy.chat.common.protobuf.response.ResponseBody;
import io.netty.channel.*;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@ChannelHandler.Sharable
public class MessageDispatcherHandler extends SimpleChannelInboundHandler<ResponseBody.ResponseMsg> {

    private static final Logger logger = LoggerFactory.getLogger(MessageDispatcherHandler.class);

    private final SignInHandler signInHandler;

    private final MessageHandler messageHandler;

    private final SignOutHandler signOutHandler;

    private final HeartbeatHandler heartbeatHandler;

    private final CreateGroupHandler createGroupHandler;

    private final GroupMessageHandler groupMessageHandler;

    public MessageDispatcherHandler(SignInHandler signInHandler, MessageHandler messageHandler,
                                    SignOutHandler signOutHandler, HeartbeatHandler heartbeatHandler,
                                    CreateGroupHandler createGroupHandler, GroupMessageHandler groupMessageHandler) {
        this.signInHandler = signInHandler;
        this.messageHandler = messageHandler;
        this.signOutHandler = signOutHandler;
        this.heartbeatHandler = heartbeatHandler;
        this.createGroupHandler = createGroupHandler;
        this.groupMessageHandler = groupMessageHandler;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ResponseBody.ResponseMsg msg) throws Exception {
        ResponseBody.ResponseMsg.Command command = msg.getCommand();
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
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            logger.info("发起心跳");
            RequestBody.RequestMsg.Heartbeat.Builder heartBuilder = RequestBody.RequestMsg.Heartbeat.newBuilder();
            ctx.writeAndFlush(RequestBody.RequestMsg.newBuilder()
                    .setCommandValue(RequestBody.RequestMsg.Command.HEARTBEAT_VALUE).setHeartbeat(heartBuilder.build()).build())
                    .addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("channel:{}出异常", ctx.channel().id(), cause);
        // 断开连接
        ctx.channel().close();
    }

}
