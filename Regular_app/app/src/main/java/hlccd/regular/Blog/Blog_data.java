package hlccd.regular.Blog;

import java.util.ArrayList;
import java.util.List;

public class Blog_data {
    private String       portrait;
    private String       name;
    private List<String> img;
    private String       message;
    private List<String> circles;
    private Long         timestamp;
    private Long         endorse;
    private Long         comment;

    public Blog_data() {
    }

    public Blog_data(String portrait, String name, List<String> img, String message, List<String> circles, Long timestamp, Long endorse, Long comment) {
        this.portrait  = portrait;
        this.name      = name;
        this.img       = img;
        this.message   = message;
        this.circles   = circles;
        this.timestamp = timestamp;
        this.endorse   = endorse;
        this.comment   = comment;
    }
    public List<String> getCircleList(){
        List<String> list=new ArrayList<>();
        list.add("吃喝玩乐");
        list.add("问题研究");
        list.add("分享生活");
        list.add("竞赛组队");
        list.add("兴趣爱好");
        list.add("学术交流");
        list.add("闲聊杂谈");
        return list;
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

    public List<String> getImg() {
        return img;
    }

    public void setImg(List<String> img) {
        this.img = img;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getCircles() {
        return circles;
    }

    public void setCircles(List<String> circles) {
        this.circles = circles;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Long getEndorse() {
        return endorse;
    }

    public void setEndorse(Long endorse) {
        this.endorse = endorse;
    }

    public Long getComment() {
        return comment;
    }

    public void setComment(Long comment) {
        this.comment = comment;
    }
}
