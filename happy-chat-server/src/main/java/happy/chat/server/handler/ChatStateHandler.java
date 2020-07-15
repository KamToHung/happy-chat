package happy.chat.server.handler;

import happy.chat.common.protobuf.request.RequestBody;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 判断空闲handler
 *
 * @author Terry
 */
@Component
@ChannelHandler.Sharable
public class ChatStateHandler extends IdleStateHandler {

    public ChatStateHandler() {
        super(10, 0, 0, TimeUnit.SECONDS);
    }

    @Override
    protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) throws Exception {
        ctx.channel().close();
    }

}
