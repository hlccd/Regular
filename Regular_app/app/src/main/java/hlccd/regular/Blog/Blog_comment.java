package hlccd.regular.Blog;

import java.util.List;

public class Blog_comment {
    private String           portrait;
    private String           name;
    private Long             id;
    private Long             timestamp;
    private String           message;
    private Long             endorse;
    private List<Blog_reply> replies;
    //待添加循环评论

    public Blog_comment() {
    }

    public Blog_comment(String portrait, String name, Long id, Long timestamp, String message, Long endorse, List<Blog_reply> replies) {
        this.portrait  = portrait;
        this.name      = name;
        this.id        = id;
        this.timestamp = timestamp;
        this.message   = message;
        this.endorse   = endorse;
        this.replies   = replies;
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

    public List<Blog_reply> getReplies() {
        return replies;
    }

    public void setReplies(List<Blog_reply> replies) {
        this.replies = replies;
    }
}
