package hlccd.regular.Friend;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

public class Friend_message_data extends LitePalSupport implements Comparable<Friend_message_data> {
    /**
     * 该消息发送者头像的图片地址
     */
    @Column (nullable = true, unique = false, defaultValue = "0", ignore = false)
    private int    portrait;
    /**
     * 该消息发送者id
     */
    @Column (nullable = true, unique = false, defaultValue = "0", ignore = false)
    private Long   sender;
    @Column (nullable = true, unique = false, defaultValue = "0", ignore = false)
    private Long   group;
    /**
     * 该消息发送者昵称
     */
    @Column (nullable = true, unique = false, defaultValue = "", ignore = false)
    private String name;
    /**
     * 该消息内容
     */
    @Column (nullable = true, unique = false, defaultValue = "", ignore = false)
    private String mes;
    /**
     * 该消息接收者的id
     */
    @Column (nullable = true, unique = false, defaultValue = "0", ignore = false)
    private Long   master;
    /**
     * 时间戳，该消息对象发送时最近的时间戳
     */
    @Column (nullable = true, unique = false, defaultValue = "0", ignore = false)
    private Long   timestamp;

    Friend_message_data() {
    }

    Friend_message_data(int portrait, Long sender, Long group, String name, String mes, Long master, Long timestamp) {
        this.portrait  = portrait;
        this.sender    = sender;
        this.group     = group;
        this.name      = name;
        this.mes       = mes;
        this.master    = master;
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

    public Long getGroup() {
        return group;
    }

    public void setGroup(Long group) {
        this.group = group;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public Long getMaster() {
        return master;
    }

    public void setMaster(Long master) {
        this.master = master;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * 按照最近时间进行降序排序
     */
    @Override
    public int compareTo(Friend_message_data o) {
        if (this.timestamp > o.timestamp) {
            return -1;
        } else if (this.timestamp < o.timestamp) {
            return 1;
        }
        return 0;
    }
}
