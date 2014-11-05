package com.fc.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.nio.charset.Charset;

/**
 * Created by fc on 14-11-3.
 */
public class MongoQueueInitializer extends ChannelInitializer<SocketChannel> {

    private static final StringDecoder DECODER = new StringDecoder(Charset.forName("UTF-8"));
    private static final StringEncoder ENCODER = new StringEncoder(Charset.forName("UTF-8"));

    private static final MongoQueueHandler SERVER_HANDLER = new MongoQueueHandler();

    public MongoQueueInitializer() {
    }

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        // Add the text line codec combination first,
        pipeline.addLast(new DelimiterBasedFrameDecoder(8192, new ByteBuf[] {
                Unpooled.wrappedBuffer(new byte[]{'\r', '\n'}),
        }));
        // the encoder and decoder are static as these are sharable
        pipeline.addLast(DECODER);
        pipeline.addLast(ENCODER);

        // and then business logic.
        pipeline.addLast(SERVER_HANDLER);
    }
}
