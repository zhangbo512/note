package com.zhang.netty.wechat.client.handler;

import com.zhang.netty.wechat.packet.response.MessageResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * description
 *
 * @author zb 2019/07/04 22:42
 */
@Slf4j
public class MessageClientHandler extends SimpleChannelInboundHandler<MessageResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessageResponsePacket messageResponsePacket) throws Exception {
        log.info("客户端收到服务端响应：---->"+messageResponsePacket.getMessage());
    }
}