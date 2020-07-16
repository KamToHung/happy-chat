package happy.chat.client.handler;

import happy.chat.common.protobuf.request.RequestBody;
import happy.chat.common.protobuf.response.ResponseBody;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.stereotype.Component;

/**
 * 创建聊天室
 *
 * @author Terry
 *
 */
@Component
@ChannelHandler.Sharable
public class CreateGroupHandler extends SimpleChannelInboundHandler<ResponseBody.ResponseMsg.CreateGroup> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ResponseBody.ResponseMsg.CreateGroup msg) throws Exception {

    }
}
