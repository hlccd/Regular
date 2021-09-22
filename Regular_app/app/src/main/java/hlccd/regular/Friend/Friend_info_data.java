package hlccd.regular.Friend;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

public class Friend_info_data extends LitePalSupport implements Comparable<Friend_info_data> {
    /**
     * 该消息发送者头像的图片地址
     */
    @Column (nullable = true, unique = false, defaultValue = "0", ignore = false)
    private int    portrait;
    @Column (nullable = true, unique = false, defaultValue = "1", ignore = false)
    private int    type;
    @Column (nullable = true, unique = false, defaultValue = "", ignore = false)
    private String name;
    @Column (nullable = true, unique = false, defaultValue = "0", ignore = false)
    private Long   sender;
    @Column (nullable = true, unique = false, defaultValue = "0", ignore = false)
    private Long   group;

    /**
     * 该消息接收者的id
     */
    @Column (nullable = true, unique = false, defaultValue = "0", ignore = false)
    private Long    master;
    /**
     * 时间戳，该消息对象发送时最近的时间戳
     */
    @Column (nullable = true, unique = false, defaultValue = "0", ignore = false)
    private Long    timestamp;
    private Boolean dispose;

    Friend_info_data() {
    }

    Friend_info_data(int portrait, int type, String name, Long sender, Long group, Long master, Long timestamp, Boolean dispose) {
        this.portrait  = portrait;
        this.type      = type;
        this.name      = name;
        this.sender    = sender;
        this.group     = group;
        this.master    = master;
        this.timestamp = timestamp;
        this.dispose   = dispose;
    }

    public int getPortrait() {
        return portrait;
    }

    public void setPortrait(int portrait) {
        this.portrait = portrait;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Boolean getDispose() {
        return dispose;
    }

    public void setDispose(Boolean dispose) {
        this.dispose = dispose;
    }

    /**
     * 按照最近时间进行降序排序
     */
    @Override
    public int compareTo(Friend_info_data o) {
        if (!this.dispose && o.dispose) {
            return -1;
        } else if (this.dispose && !o.dispose) {
            return 1;
        }
        if (this.timestamp > o.timestamp) {
            return -1;
        } else if (this.timestamp < o.timestamp) {
            return 1;
        }
        return 0;
    }
}
