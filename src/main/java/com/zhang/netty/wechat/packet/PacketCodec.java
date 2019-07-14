package com.zhang.netty.wechat.packet;

import com.zhang.netty.wechat.constant.SerializeManager;
import com.zhang.netty.wechat.packet.request.LoginRequestPacket;
import com.zhang.netty.wechat.packet.request.MessageRequestPacket;
import com.zhang.netty.wechat.packet.response.LoginResponsePacket;
import com.zhang.netty.wechat.packet.response.MessageResponsePacket;
import com.zhang.netty.wechat.serializer.Serializer;
import com.zhang.netty.wechat.serializer.fast.JSONSerializer;
import io.netty.buffer.ByteBuf;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

//TODO 待调整

/**
 * 解码/编码器
 */
@Slf4j
public class PacketCodec {

    public static final int MAGIC_NUMBER = 0x12345678;
    public static final PacketCodec INSTANCE = new PacketCodec();
    private final Map<Byte, Class<? extends Packet>> packetTypeMap;


    private PacketCodec() {
        packetTypeMap = new HashMap<>();
        packetTypeMap.put(LoginRequestPacket.LOGIN_REQUEST, LoginRequestPacket.class);
        packetTypeMap.put(LoginResponsePacket.LOGIN_RESPONSE,LoginResponsePacket.class);
        packetTypeMap.put(MessageRequestPacket.MESSAGE_REQUEST,MessageRequestPacket.class);
        packetTypeMap.put(MessageResponsePacket.MESSAGE_RESPONSE,MessageResponsePacket.class);
    }


    public void encode(ByteBuf byteBuf, Packet packet) {
        // 2. 序列化 java 对象
        Serializer serializer = new JSONSerializer();
        byte[] bytes = serializer.serialize(packet);
        // 3. 实际编码过程
        byteBuf.writeInt(MAGIC_NUMBER);
        byteBuf.writeByte(packet.getVersion());
        byteBuf.writeByte(serializer.getSerializerAlgorithm());
        byteBuf.writeByte(packet.getCommand());
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);
    }


    public Packet decode(ByteBuf byteBuf) {
        // 跳过 magic number
        byteBuf.skipBytes(4);

        // 跳过版本号
        byteBuf.skipBytes(1);

        // 序列化算法
        byte serializeAlgorithm = byteBuf.readByte();

        // 指令
        byte command = byteBuf.readByte();

        // 数据包长度
        int length = byteBuf.readInt();

        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);

        log.info("command:"+command+";"+"serializeAlgorithm:"+serializeAlgorithm);

        Class<? extends Packet> requestType = getRequestType(command);
        Serializer serializer = getSerializer(serializeAlgorithm);

        if (requestType != null && serializer != null) {
            Packet pa = serializer.deserialize(requestType, bytes);
            if(pa instanceof MessageRequestPacket) {
                log.info("message:"+((MessageRequestPacket) pa).getMessage());
            }else if(pa instanceof MessageResponsePacket) {
                log.info("message:"+((MessageResponsePacket) pa).getMessage());

            }
            return pa;
        }

        return null;
    }

    private Serializer getSerializer(byte serializeAlgorithm) {
        return SerializeManager.getSerializer(serializeAlgorithm);
    }

    private Class<? extends Packet> getRequestType(byte command) {

        return packetTypeMap.get(command);
    }
}