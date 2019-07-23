package zhang.netty.wechat.packet.request;

import com.zhang.netty.wechat.packet.Packet;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * description:创建群组的请求
 *
 * @author zb 2019/07/23 14:41
 */
@Setter
@Getter
public class CreateGroupRequestPacket extends Packet {

    /**
     * 参加群聊的用户ID
     */
    private List<String> useridList;
    private String message;

    @Override
    public Byte getCommand() {
        return GROUPCHAT_REQUEST;
    }
}
