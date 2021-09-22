package hlccd.regular.Project;
/**
 * 规划标签类型
 * 包含两个属性:
 * 标签名，唯一
 * 该标签使用次数
 *
 * 表关系:
 * 该类型与个人习惯表Project_habit_data形成1:n的关系
 * 该类型与日常任务表Project_task_data形成1:n的关系
 * 该类型与待办事项表Project_backlog_data形成1:n的关系
 *
 * @author hlccd 2020.6.18
 * @version 1.0
 */

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.List;

public class Project_label extends LitePalSupport implements Comparable<Project_label> {
    /**
     * 标签名，唯一
     */
    @Column (nullable = true, unique = true, defaultValue = "", ignore = false)
    private String name;
    /**
     * 该标签使用次数
     */
    @Column (nullable = true, unique = false, defaultValue = "0", ignore = false)
    private Long   number;

    /**
     * 1:n与个人习惯表Project_habit形成表关系
     */
    private List<Project_habit_data>   habits   = new ArrayList<Project_habit_data>();
    /**
     * 1:n与日常任务表Project_task_data形成表关系
     */
    private List<Project_task_data>    tasks    = new ArrayList<Project_task_data>();
    /**
     * 1:n与待办事项表Project_backlog_data形成表关系
     */
    private List<Project_backlog_data> backlogs = new ArrayList<Project_backlog_data>();


    Project_label(String name) {
        this.name   = name;
        this.number = Long.valueOf(0);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    /**
     * 将标签根据使用频率按照降序进行排序
     * @param o
     * @return
     */
    @Override
    public int compareTo(Project_label o) {
        if (this.number > o.number) {
            return 1;
        } else if (this.number < o.number) {
            return -1;
        }
        return 0;
    }
}
