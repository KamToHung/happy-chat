package happy.chat.client.handler;

import happy.chat.common.protobuf.request.RequestBody;
import happy.chat.common.protobuf.response.ResponseBody;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.stereotype.Component;

/**
 * 群聊信息
 *
 * @author Terry
 * @since 2020/7/16 9:47
 */
@Component
@ChannelHandler.Sharable
public class GroupMessageHandler extends SimpleChannelInboundHandler<ResponseBody.ResponseMsg.JoinGroup> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ResponseBody.ResponseMsg.JoinGroup msg) throws Exception {

    }

}
