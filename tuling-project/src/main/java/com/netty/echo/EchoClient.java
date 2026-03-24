package com.netty.echo;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * @Description
 * @Author: xiewu
 * @Date: 2022/7/21 20:22
 */
public class EchoClient {

    private final int port = 8888;
    private final String host = "127.0.0.1";

    public static void main(String[] args) throws InterruptedException {
        EchoClient echoClient = new EchoClient();
        echoClient.start();
    }

    public void start() throws InterruptedException {
        /*线程组*/
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    /*指定使用NIO的通信模式*/
                    .channel(NioSocketChannel.class)
                    /*指定服务器的IP地址和端口*/
                    .remoteAddress(new InetSocketAddress(host,port))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new EchoClientHandler());
                        }
                    });
            /*异步连接到服务器，sync()会阻塞到完成*/
            ChannelFuture channelFuture = bootstrap.connect().sync();
            /*阻塞当前线程，直到客户端的Channel被关闭*/
            channelFuture.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }
    }
}
