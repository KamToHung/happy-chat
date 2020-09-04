package happy.chat.server.handler.status;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(ChatStateHandler.class);

    public ChatStateHandler() {
        super(20, 0, 0, TimeUnit.MINUTES);
    }

    @Override
    protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) throws Exception {
        logger.info("10秒内没有信息,关闭连接");
        ctx.channel().close();
    }

}
