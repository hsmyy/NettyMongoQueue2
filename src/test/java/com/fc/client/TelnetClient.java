package com.fc.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by fc on 14-11-3.
 */
public class TelnetClient {
    static final String HOST = "localhost";
    static final int PORT = 10086;

    public static void main(String[] args) throws Exception {
        // Configure SSL.

        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new TelnetClientInitializer());

            // Start the connection attempt.
            Channel ch = b.connect(HOST, PORT).sync().channel();

            // Read commands from the stdin.
            ChannelFuture lastWriteFuture = null;
            int idx = 0;
            for (;;) {
                String[] command = new String[]{"http://123.123","GET"};

                lastWriteFuture = ch.writeAndFlush(command[idx] + "\n");
                Thread.sleep(1);
                idx = 1 - idx;
            }
//
//            if (lastWriteFuture != null) {
//                lastWriteFuture.sync();
//            }
        } finally {
            group.shutdownGracefully();
        }
    }
}
