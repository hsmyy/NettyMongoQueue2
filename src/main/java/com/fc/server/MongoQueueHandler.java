package com.fc.server;

import com.fc.queue.MongoQueue;
import com.fc.queue.URL;
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
    private MongoQueue mongoQueue = new MongoQueue();

    private static AtomicInteger pv = new AtomicInteger(0);

    private Logger LOG = LoggerFactory.getLogger(MongoQueueHandler.class);

    @Override
    public void messageReceived(ChannelHandlerContext ctx, String request) {
        String response;
        try{
            System.out.println("request is:" + request);
            if(request.equals("GET")){
                URL url = mongoQueue.dequeue();
                response = url != null ? url.getUrl() : "";
                response += DELIMIT;
            }else if(request.equals("LEN")){
                long len = mongoQueue.len();
                response = String.valueOf(len) + DELIMIT;
            }else{
                URL url = new URL(request, System.currentTimeMillis());
                mongoQueue.enqueue(url);
                response = "OK" + DELIMIT;
            }
            System.out.println("response is:" + response);
            pv.incrementAndGet();
            if(pv.get() % 100 == 0){
                LOG.debug("pv is " + pv.get());
            }
        }catch(Exception e){
            e.printStackTrace();
            response = "ERROR" + DELIMIT;
        }
        ChannelFuture future = ctx.writeAndFlush(response);
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
