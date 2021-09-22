package hlccd.regular.Blog;

public class Blog_reply {
    private String portrait;
    private String name;
    private Long   id;
    private String replier;
    private Long   other;
    private Long   timestamp;
    private String message;
    private Long   endorse;

    public Blog_reply() {
    }

    public Blog_reply(String portrait, String name, Long id, String replier, Long other, Long timestamp, String message, Long endorse) {
        this.portrait  = portrait;
        this.name      = name;
        this.id        = id;
        this.replier   = replier;
        this.other     = other;
        this.timestamp = timestamp;
        this.message   = message;
        this.endorse   = endorse;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReplier() {
        return replier;
    }

    public void setReplier(String replier) {
        this.replier = replier;
    }

    public Long getOther() {
        return other;
    }

    public void setOther(Long other) {
        this.other = other;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getEndorse() {
        return endorse;
    }

    public void setEndorse(Long endorse) {
        this.endorse = endorse;
    }
}
