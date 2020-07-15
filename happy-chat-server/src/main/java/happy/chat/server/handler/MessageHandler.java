package happy.chat.server.handler;

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
import org.springframework.stereotype.Component;

/**
 * 信息handler
 *
 * @author Terry
 */
@Component
@ChannelHandler.Sharable
public class MessageHandler extends SimpleChannelInboundHandler<RequestBody.RequestMsg.UserMessage> {

    private static final Logger logger = LoggerFactory.getLogger(MessageHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RequestBody.RequestMsg.UserMessage userMessageRequest) throws Exception {
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
        toUserChannel.writeAndFlush(messageResponse);
    }

}
