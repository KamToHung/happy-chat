package happy.chat.server.handler;

import happy.chat.common.protobuf.request.RequestBody;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * handler初始化
 *
 * @author Terry
 */
@Component
public class ServerInitHandler extends ChannelInitializer<NioSocketChannel> {

//    @Autowired
//    private ChatStateHandler chatStateHandler;

//    @Autowired
//    private SignInHandler signInHandler;
//
//    @Autowired
//    private HeartbeatHandler heartbeatHandler;
//
//    @Autowired
//    private AuthHandler authHandler;

    @Autowired
    private ChatServerHandler chatServerHandler;

    protected void initChannel(NioSocketChannel ch) throws Exception {
        ch.pipeline().addLast(new IdleStateHandler(0,10,0, TimeUnit.SECONDS))
                .addLast(new ProtobufVarint32FrameDecoder())
                .addLast(new ProtobufDecoder(RequestBody.RequestMsg.getDefaultInstance()))
                .addLast(new ProtobufVarint32LengthFieldPrepender())
                .addLast(new ProtobufEncoder())
                .addLast(chatServerHandler);
    }
}
