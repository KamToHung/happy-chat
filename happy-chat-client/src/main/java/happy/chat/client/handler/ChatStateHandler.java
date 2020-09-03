package happy.chat.client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 检测是否超时
 *
 * @author Terry
 * @since 2020/9/3 17:12
 */
@Component
public class ChatStateHandler extends IdleStateHandler {

    private static final Logger logger = LoggerFactory.getLogger(ChatStateHandler.class);


    public ChatStateHandler() {
        super(10, 0, 0, TimeUnit.SECONDS);
    }

}
