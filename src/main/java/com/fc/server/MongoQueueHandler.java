package com.fc.server;

import com.fc.queue.MongoQueue;
import com.fc.queue.URL;
import com.fc.server.protocol.MongoQueueProtocol;
import com.fc.server.protocol.TelnetProtocol;
import io.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by fc on 14-11-1.
 */
@ChannelHandler.Sharable
public class MongoQueueHandler extends SimpleChannelInboundHandler<String> {

    public static final String DELIMIT = "\r\n";

    private Logger LOG = LoggerFactory.getLogger(MongoQueueHandler.class);

    private TelnetProtocol protocol = new MongoQueueProtocol();

    @Override
    public void messageReceived(ChannelHandlerContext ctx, String request) {
       protocol.answer(ctx, request);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
