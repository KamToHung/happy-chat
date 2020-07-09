package happy.chat.client.handler;

import happy.chat.common.protobuf.response.ResponseBody;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@ChannelHandler.Sharable
public class MessageHandler extends SimpleChannelInboundHandler<ResponseBody.UserMessage> {

    private static final Logger logger = LoggerFactory.getLogger(MessageHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ResponseBody.UserMessage msg) throws Exception {
        logger.info(msg.getFromUsername() + "(" + msg.getFromUserId() + ")->" + msg.getMessage());
    }

}
