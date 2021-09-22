package hlccd.regular.Project;
/**
 * 日常任务类型
 * 包含九个属性:
 * 任务名
 * 用户ID，以用户表表的用户ID做外键
 * 群组ID,该任务所属的群组ID号,为0则是个人专属
 * 标签，以标签表的名字做外键
 * 难度，1到5分别为:琐事、简单、中等、困难、极难、挑战
 * 总体耗时，该日常任务完成总时间的毫秒数
 * 执行次数，该日常任务累计执行次数
 * 备注,长度限制200bytes
 * 时间戳,添加该日常任务的时间戳
 *
 * 表关系:
 * 该类型与用户信息表User_data形成n:1的关系
 * 该类型与群组信息表Group_data形成n:1的关系(暂时搁置)
 * 该类型与规划标签Project_label形成n:1的关系
 *
 * @author hlccd 2020.6.18
 * @version 1.0
 */

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import hlccd.regular.User.User_data;

public class Project_task_data extends LitePalSupport implements Comparable<Project_task_data> {
    /**
     * 任务名
     */
    @Column (nullable = true, unique = false, defaultValue = "", ignore = false)
    private String name;
    /**
     * 用户ID，以用户表的用户ID做外键
     */
    @Column (nullable = true, unique = false, defaultValue = "0", ignore = false)
    private Long   u_id;
    /**
     * 群组ID，以群组表的用户ID做外键
     */
    @Column (nullable = true, unique = false, defaultValue = "0", ignore = false)
    private Long   g_id;
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
     * 总体耗时，该日常任务完成总时间的毫秒数
     */
    @Column (nullable = true, unique = false, defaultValue = "0", ignore = false)
    private Long   duration;
    /**
     * 执行次数，该日常任务累计执行次数
     */
    @Column (nullable = true, unique = false, defaultValue = "0", ignore = false)
    private Long   number;
    /**
     * 备注,长度限制200bytes
     */
    @Column (nullable = true, unique = false, defaultValue = "", ignore = false)
    private String remark;
    /**
     * 时间戳,添加该日常任务的时间戳
     */
    @Column (nullable = true, unique = false, defaultValue = "0", ignore = false)
    private Long   timestamp;

    /**
     * n:1与用户信息表User_data形成表关系
     */
    private User_data     user;
//    /**
//     * n:1与群组信息表Group_data形成表关系
//     */
//    private Group_data     group;
    /**
     * n:1与规划标签Project_label形成表关系
     */
    private Project_label label;

    Project_task_data() {
    }

    Project_task_data(String name, Long u_id, Long g_id, String l_name, int difficulty, String remark, Long timestamp) {
        this.name       = name;
        this.u_id       = u_id;
        this.g_id       = g_id;
        this.l_name     = l_name;
        this.difficulty = difficulty;
        this.duration   = 0L;
        this.number     = 0L;
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

    public Long getG_id() {
        return g_id;
    }

    public void setG_id(Long g_id) {
        this.g_id = g_id;
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

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
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
     * 按照难度进行降序排序
     * 如果难度相同则按照总体耗时进行降序排序
     * 如果耗时相同则按照次数进行升序排序
     */
    @Override
    public int compareTo(Project_task_data o) {
        if (this.difficulty > o.difficulty) {
            return -1;
        } else if (this.difficulty < o.difficulty) {
            return 1;
        } else {
            if (this.duration > o.duration) {
                return -1;
            } else if (this.duration < o.duration) {
                return 1;
            } else {
                if (this.number > o.number) {
                    return 1;
                } else if (this.number < o.number) {
                    return -1;
                }
            }
        }
        return 0;
    }
}
