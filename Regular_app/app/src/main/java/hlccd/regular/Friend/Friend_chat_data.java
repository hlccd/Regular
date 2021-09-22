package hlccd.regular.Friend;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

public class Friend_chat_data extends LitePalSupport implements Comparable<Friend_chat_data> {
    /**
     * 该发送者的头像的图片地址
     */
    @Column (nullable = true, unique = false, defaultValue = "0", ignore = false)
    private int    portrait;
    /**
     * 该发送者的ID
     */
    @Column (nullable = true, unique = false, defaultValue = "0", ignore = false)
    private Long   sender;
    /**
     * 该消息所属群组的ID
     */
    @Column (nullable = true, unique = false, defaultValue = "0", ignore = false)
    private Long   group;
    /**
     * 该发送者的昵称
     */
    @Column (nullable = true, unique = false, defaultValue = "", ignore = false)
    private String name;
    /**
     * 该消息所属人的ID
     */
    @Column (nullable = true, unique = false, defaultValue = "0", ignore = false)
    private Long   master;
    /**
     * 该消息所属的另一方的ID
     */
    @Column (nullable = true, unique = false, defaultValue = "0", ignore = false)
    private Long   belong;
    /**
     * 消息内容
     */
    @Column (nullable = true, unique = false, defaultValue = "", ignore = false)
    private String message;
    /**
     * 该消息收到时刻的时间戳
     */
    @Column (nullable = true, unique = false, defaultValue = "0", ignore = false)
    private Long   timestamp;

    Friend_chat_data() {

    }

    Friend_chat_data(int portrait, Long sender, String name, Long group, Long master, Long belong, String message, Long timestamp) {
        this.portrait  = portrait;
        this.sender    = sender;
        this.name      = name;
        this.group     = group;
        this.master    = master;
        this.belong    = belong;
        this.message   = message;
        this.timestamp = timestamp;
    }

    public int getPortrait() {
        return portrait;
    }

    public void setPortrait(int portrait) {
        this.portrait = portrait;
    }

    public Long getSender() {
        return sender;
    }

    public void setSender(Long sender) {
        this.sender = sender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getGroup() {
        return group;
    }

    public void setGroup(Long group) {
        this.group = group;
    }

    public Long getMaster() {
        return master;
    }

    public void setMaster(Long master) {
        this.master = master;
    }

    public Long getBelong() {
        return belong;
    }

    public void setBelong(Long belong) {
        this.belong = belong;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * 按照消息收到的顺序进行排序
     */
    @Override
    public int compareTo(Friend_chat_data o) {
        if (this.timestamp > o.timestamp) {
            return 1;
        } else if (this.timestamp < o.timestamp) {
            return -1;
        }
        return 0;
    }
}
