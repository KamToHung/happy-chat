package happy.chat.client.handler;


import happy.chat.common.HappyChatProperties;
import happy.chat.common.protobuf.request.RequestBody;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@ChannelHandler.Sharable
public class HeartbeatHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(HeartbeatHandler.class);

    private final HappyChatProperties properties;

    @Autowired
    public HeartbeatHandler(HappyChatProperties properties) {
        this.properties = properties;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        logger.info("服务器收到客户端心跳,返回");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        schedule(ctx);
        super.channelActive(ctx);
    }

    private void schedule(ChannelHandlerContext ctx) {
        ctx.executor().schedule(() -> {
            if (ctx.channel().isActive()) {
                RequestBody.RequestMsg.Heartbeat.Builder heartBuilder = RequestBody.RequestMsg.Heartbeat.newBuilder();
                ctx.writeAndFlush(RequestBody.RequestMsg.newBuilder()
                        .setCommandValue(RequestBody.RequestMsg.Command.HEARTBEAT_VALUE).setHeartbeat(heartBuilder.build()).build());
                schedule(ctx);
            }
        }, properties.getClient().getHeartbeatTime(), TimeUnit.SECONDS);
    }


}
