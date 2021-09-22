package hlccd.regular.Project;
/**
 * 待办事项类型
 * 包含八个属性:
 * 待办名
 * 用户ID，以用户表表的用户ID做外键
 * 标签，以标签表的名字做外键
 * 难度，1到5分别为:琐事、简单、中等、困难、极难、挑战
 * 截至日期的时间戳
 * 执行时长,毫秒级
 * 备注,长度限制200bytes
 * 时间戳,添加该待办事项的时间戳
 *
 * 表关系:
 * 该类型与用户信息表User_data形成n:1的关系
 * 该类型与规划标签Project_label形成n:1的关系
 *
 * @author hlccd 2020.6.18
 * @version 1.0
 * @version 2.0 新增执行时长 2020.6.20
 */

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import hlccd.regular.User.User_data;

public class Project_backlog_data extends LitePalSupport implements Comparable<Project_backlog_data> {
    /**
     * 待办名
     */
    @Column (nullable = true, unique = false, defaultValue = "", ignore = false)
    private String name;
    /**
     * 用户ID，以用户表的用户ID做外键
     */
    @Column (nullable = true, unique = false, defaultValue = "0", ignore = false)
    private Long   u_id;
    /**
     * 标签，以标签表的名字做外键
     */
    @Column (nullable = true, unique = false, defaultValue = "", ignore = false)
    private String l_name;
    /**
     * 难度，1到5分别为:琐事、简单、中等、困难、极难、挑战
     */
    @Column (nullable = true, unique = false, defaultValue = "1", ignore = false)
    private int    difficulty;
    /**
     * 截至日期的时间戳
     */
    @Column (nullable = true, unique = false, defaultValue = "0", ignore = false)
    private Long   deadline;
    /**
     * 执行时长,毫秒级
     */
    @Column (nullable = true, unique = false, defaultValue = "0", ignore = false)
    private Long   duration;

    /**
     * 备注,长度限制200bytes
     */
    @Column (nullable = true, unique = false, defaultValue = "", ignore = false)
    private String remark;
    /**
     * 时间戳,添加该待办事项的时间戳
     */
    @Column (nullable = true, unique = false, defaultValue = "0", ignore = false)
    private Long   timestamp;

    /**
     * n:1与用户信息表User_data形成表关系
     */
    private User_data     user;
    /**
     * n:1与规划标签Project_label形成表关系
     */
    private Project_label label;

    Project_backlog_data() {
        this.duration = 0L;
    }

    Project_backlog_data(String name, Long u_id, String l_name, int difficulty, Long deadline, String remark, Long timestamp) {
        this.name       = name;
        this.u_id       = u_id;
        this.l_name     = l_name;
        this.difficulty = difficulty;
        this.deadline   = deadline;
        this.duration   = 0L;
        this.remark     = remark;
        this.timestamp  = timestamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getU_id() {
        return u_id;
    }

    public void setU_id(Long u_id) {
        this.u_id = u_id;
    }

    public String getL_name() {
        return l_name;
    }

    public void setL_name(String l_name) {
        this.l_name = l_name;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public Long getDeadline() {
        return deadline;
    }

    public void setDeadline(Long deadline) {
        this.deadline = deadline;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * 按照截止时间进行升序排序,即早到截至的排在前面
     * 如果截止时间相同则按照难度进行降序排序
     */
    @Override
    public int compareTo(Project_backlog_data o) {
        if (this.deadline > o.deadline) {
            return 1;
        } else if (this.deadline < o.deadline) {
            return -1;
        } else {
            if (this.difficulty > o.difficulty) {
                return -1;
            } else if (this.difficulty < o.difficulty) {
                return 1;
            }
        }
        return 0;
    }
}
