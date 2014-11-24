package com.fc.server.protocol;

import io.netty.channel.ChannelHandlerContext;

/**
 * Created by fc on 14-11-24.
 */
public interface OneSideProtocol {
    public void answer(ChannelHandlerContext ctx, String request);
}
