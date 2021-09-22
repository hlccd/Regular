package hlccd.regular.Regular;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import hlccd.regular.User.User_data;

/**
 * 执行情况类型,汇总全部的执行记录
 * 包含五个属性:
 * 执行记录名
 * 用户ID，以用户表表的用户ID做外键
 * 难度，1到5分别为:琐事、简单、中等、困难、极难、挑战
 * 开始时间戳
 * 实际执行时长,毫秒级
 * 结束时间戳
 *
 * 表关系:
 * 该类型与用户信息表User_data形成n:1的关系
 *
 * @author hlccd 2020.6.18
 * @version 1.0
 * @version 2.0 新增中间实际执行时长 2021.6.20
 */

public class Regular_data extends LitePalSupport implements Comparable<Regular_data> {
    /**
     * 执行记录名
     */
    @Column (nullable = true, unique = false, defaultValue = "", ignore = false)
    private String name;
    /**
     * 用户ID，以用户表的用户ID做外键
     */
    @Column (nullable = true, unique = false, defaultValue = "0", ignore = false)
    private Long   u_id;
    /**
     * 难度，1到5分别为:琐事、简单、中等、困难、极难、挑战
     */
    @Column (nullable = true, unique = false, defaultValue = "1", ignore = false)
    private int    difficulty;
    /**
     * 开始时间戳
     */
    @Column (nullable = true, unique = false, defaultValue = "0", ignore = false)
    private Long   begin;
    /**
     * 实际执行时长,毫秒级
     */
    @Column (nullable = true, unique = false, defaultValue = "0", ignore = false)
    private Long   duration;

    /**
     * n:1与用户信息表User_data形成表关系
     */
    private User_data user;

    Regular_data() {
    }

    Regular_data(String name, Long u_id, int difficulty, Long begin, Long duration) {
        this.name       = name;
        this.u_id       = u_id;
        this.difficulty = difficulty;
        this.begin      = begin;
        this.duration   = duration;
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

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public Long getBegin() {
        return begin;
    }

    public void setBegin(Long begin) {
        this.begin = begin;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    /**
     * 按照实际执行时长做降序排序
     */
    @Override
    public int compareTo(Regular_data o) {
        if (this.begin > o.begin) {
            return 1;
        } else if (this.begin < o.begin) {
            return -1;
        }
        return 0;
    }
}

