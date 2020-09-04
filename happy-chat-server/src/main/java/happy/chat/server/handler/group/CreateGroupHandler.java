package happy.chat.server.handler.group;

import com.google.protobuf.ProtocolStringList;
import happy.chat.common.protobuf.request.RequestBody;
import happy.chat.server.util.ChannelGroupUtils;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * 创建聊天室
 *
 * @author Terry
 */
@Component
@ChannelHandler.Sharable
public class CreateGroupHandler extends SimpleChannelInboundHandler<RequestBody.RequestMsg.CreateGroup> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RequestBody.RequestMsg.CreateGroup requestBody) throws Exception {
        //新建channelGroup
        ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
        String groupId = UUID.randomUUID().toString();
        ProtocolStringList userIds = requestBody.getUserIdsList();
        userIds.forEach(userId -> {

        });
    }
}
