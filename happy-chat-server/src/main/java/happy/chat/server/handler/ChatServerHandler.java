package happy.chat.server.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.springframework.stereotype.Component;

@Component
@ChannelHandler.Sharable
public class ChatServerHandler extends ChannelInboundHandlerAdapter {
}
