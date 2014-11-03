package com.fc.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by fc on 14-11-3.
 */
public class TelnetClientHandler extends SimpleChannelInboundHandler<String> {

    private static Logger LOG = LoggerFactory.getLogger(TelnetClientHandler.class);

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, String msg) {
        LOG.debug(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
