package zhang.netty.wechat.server;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import zhang.netty.wechat.basehandler.LifeCyCleTestHandler;
import zhang.netty.wechat.basehandler.Spliter;
import zhang.netty.wechat.packet.ecode.PacketDecode;
import zhang.netty.wechat.packet.ecode.PacketEncoder;
import zhang.netty.wechat.server.handler.AuthHandler;
import zhang.netty.wechat.server.handler.LoginServerHandler;
import zhang.netty.wechat.server.handler.MessageServerHandler;

public class NettyServer {

    private static final int PORT = 8000;

    public static void main(String[] args) {
        NioEventLoopGroup boosGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        final ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap
                .group(boosGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) {
                        ch.pipeline().addLast(new LifeCyCleTestHandler());
                        /**
                         * 添加拆包器
                         *
                         * 基于偏移长度的拆包
                         */
                        ch.pipeline().addLast(new Spliter());
                        ch.pipeline().addLast(new PacketDecode());
                        ch.pipeline().addLast(new LoginServerHandler());

                        ch.pipeline().addLast(new AuthHandler());
                        ch.pipeline().addLast(new MessageServerHandler());
                        ch.pipeline().addLast(new PacketEncoder());
                    }
                });


        bind(serverBootstrap, PORT);
    }

    private static void bind(final ServerBootstrap serverBootstrap, final int port) {
        serverBootstrap.bind(port).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println("端口[" + port + "]绑定成功!");
            } else {
                System.err.println("端口[" + port + "]绑定失败!");
            }
        });
    }
}
