package com.zhang.netty.wechat.client;

import com.zhang.netty.wechat.packet.Packet;
import com.zhang.netty.wechat.packet.request.LoginRequestPacket;
import com.zhang.netty.wechat.packet.PacketCodec;
import com.zhang.netty.wechat.packet.response.LoginResponsePacket;
import com.zhang.netty.wechat.utils.LoginUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

/**
 * description
 *
 * @author zb 2019/07/04 18:57
 */
@Slf4j
public class ClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("客户端开始登陆！");
        // 创建登录对象
        LoginRequestPacket loginRequestPacket = LoginRequestPacket.builder().build();
        loginRequestPacket.setUserId(UUID.randomUUID().toString());
        loginRequestPacket.setUserName("flash");
        loginRequestPacket.setUserPassword("pwd");

        ByteBuf buffer = PacketCodec.INSTANCE.encode(ctx.alloc(), loginRequestPacket);
        /**
         * 发送数据
         */
        ctx.channel().writeAndFlush(buffer);

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        Packet packet = PacketCodec.INSTANCE.decode(byteBuf);
        if (packet instanceof LoginResponsePacket) {
            LoginResponsePacket loginResponse = (LoginResponsePacket) packet;
            if (loginResponse.isSuccess()) {
                log.info("服务端响应：登陆成功！");
                LoginUtils.markLogin(ctx.channel());
            } else {
                log.info("服务端响应：登陆失败！");
            }
        }
    }
}