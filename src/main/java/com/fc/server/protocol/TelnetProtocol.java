package com.fc.server.protocol;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by fc on 14-11-13.
 */
public abstract class TelnetProtocol implements OneSideProtocol{

    public static final String DELIMIT = "\r\n";
    private static final String ERROR = "ERROR" + DELIMIT;

    public void answer(ChannelHandlerContext ctx, String request){
        String response;
        try{
            response = process(request);
        }catch(Exception e){
            e.printStackTrace();
            response = ERROR;
        }
        ChannelFuture future = ctx.writeAndFlush(response + DELIMIT);
    }

    protected abstract String process(String request);
}
