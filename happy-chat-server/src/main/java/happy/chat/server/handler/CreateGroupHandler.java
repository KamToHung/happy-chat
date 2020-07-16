package happy.chat.server.handler;

import happy.chat.common.protobuf.request.RequestBody;
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
public class CreateGroupHandler extends SimpleChannelInboundHandler<RequestBody.RequestMsg.CreateGroup> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RequestBody.RequestMsg.CreateGroup msg) throws Exception {

    }
}
