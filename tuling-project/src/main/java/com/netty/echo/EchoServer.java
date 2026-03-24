package com.netty.echo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * @Description
 * @Author: xiewu
 * @Date: 2022/7/21 20:00
 */
public class EchoServer {

    private final int port = 8888;

    public static void main(String[] args) throws InterruptedException {
        EchoServer echoServer = new EchoServer();
        echoServer.start();
    }

    public void start() throws InterruptedException {
        final EchoServerHandler echoServerHandler = new EchoServerHandler();
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(group)
                    /*指定使用NIO的通信模式*/
                    .channel(NioServerSocketChannel.class)
                    /*指定监听端口*/
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(echoServerHandler);
                        }
                    });
            /*异步绑定到服务器，sync()会阻塞到完成*/
            ChannelFuture sync = bootstrap.bind().sync();
            /*阻塞当前线程，直到服务器的ServerChannel被关闭*/
            sync.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }
    }
}
