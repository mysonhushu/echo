package com.huxing.study.echo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

public class EchoServer {
  private final int port;

  public EchoServer(int port) {
    this.port = port;
  }

  public void start() throws Exception {
    final EchoServerHandler serverHandler = new EchoServerHandler();
    EventLoopGroup group = new NioEventLoopGroup();

    try {
      ServerBootstrap serverBootstrap = new ServerBootstrap();
      serverBootstrap.group(group)
          .channel(NioServerSocketChannel.class)
          .localAddress(new InetSocketAddress(port))
          .childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
              socketChannel.pipeline().addLast(serverHandler);
            }
          });

      ChannelFuture channelFuture = serverBootstrap.bind().sync();
      channelFuture.channel().closeFuture().sync();
    } finally {
      group.shutdownGracefully().sync();
    }

  }

  public static void main(String[] args) throws Exception {
    int port;
    if(args.length != 1) {
      System.out.println("Usage:" + EchoServer.class.getSimpleName() + "  <port>");
      System.out.println("use port  12121 by default");
      port = 12121;
    } else {
      port = Integer.parseInt(args[0]);
    }
    new EchoServer(port).start();
  }
}
