package happy.chat.server.handler;

import happy.chat.common.protobuf.request.RequestBody;
import happy.chat.common.protobuf.response.ResponseBody;
import happy.chat.server.entity.UserInfo;
import happy.chat.server.util.SessionUtils;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 心跳判断handler
 *
 * @author Terry
 */
@Component
@ChannelHandler.Sharable
public class HeartbeatHandler extends SimpleChannelInboundHandler<RequestBody.RequestMsg.Heartbeat> {

    private static final Logger logger = LoggerFactory.getLogger(MessageHandler.class);

    protected void channelRead0(ChannelHandlerContext ctx, RequestBody.RequestMsg.Heartbeat heartbeatRequest) throws Exception {
        ResponseBody.ResponseMsg.Heartbeat.Builder heartbeatResponse = ResponseBody.ResponseMsg.Heartbeat.newBuilder();
        ctx.writeAndFlush(ResponseBody.ResponseMsg.newBuilder().setCommand(ResponseBody.ResponseMsg.Command.HEARTBEAT)
                .setHeartbeat(heartbeatResponse).build());
        UserInfo userInfo = SessionUtils.getSession(ctx.channel());
        if (userInfo != null) {
            logger.info("客户:" + userInfo.getUsername() + "发起心跳");
        } else {
            logger.info("接收到心跳,channelId:" + ctx.channel().id());
        }
        ctx.writeAndFlush(heartbeatRequest);
    }
}
