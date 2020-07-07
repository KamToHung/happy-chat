package happy.chat.server.handler;


import happy.chat.server.util.SessionUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.springframework.stereotype.Component;

/**
 * 判断是否登录成功handler.判断成功后移出
 *
 * @author Terry
 */
@Component
@ChannelHandler.Sharable
public class AuthHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //判断是否现在,不在关掉,在的话移出此handler
        Channel channel = ctx.channel();
        if (SessionUtils.getSession(channel) == null) {
            channel.close();
        } else {
            channel.pipeline().remove(this);
            super.channelRead(ctx, msg);
        }
    }
}
