package com.huxing.study.echo;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

public class EchoClient {
  private final String host;
  private final int port;

  public EchoClient(String host, int port) {
    this.host = host;
    this.port = port;
  }

  public void start() throws Exception {
    EventLoopGroup group = new NioEventLoopGroup();

    try {
      Bootstrap bootstrap = new Bootstrap();
      bootstrap.group(group)
          .channel(NioSocketChannel.class)
          .remoteAddress(new InetSocketAddress(host, port))
          .handler(new ChannelInitializer<SocketChannel>() {

            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
              ch.pipeline().addLast(new EchoClientHandler());
            }
          });
      ChannelFuture channelFuture = bootstrap.connect().sync();
      channelFuture.channel().closeFuture().sync();

    } finally {
      group.shutdownGracefully().sync();
    }
  }

  public static void main(String[] args) throws Exception {
    String host;
    int port;
    if (args.length != 2) {
      System.out.println("Usage: " + EchoClient.class.getSimpleName() + " <host> <port>");
       host = "10.8.10.28";
       port = 12121;
    } else {
      host = args[0];
      port = Integer.parseInt(args[1]);
    }

    new EchoClient(host, port).start();
  }
}
