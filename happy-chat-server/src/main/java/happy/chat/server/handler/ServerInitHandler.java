package happy.chat.server.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ServerInitHandler extends ChannelInitializer {

   @Autowired
   private ChatStateHandler chatStateHandler;

    protected void initChannel(Channel ch) throws Exception {
        ch.pipeline().addLast(chatStateHandler);
    }

}
