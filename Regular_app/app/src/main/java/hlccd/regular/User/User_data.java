package hlccd.regular.User;
/**
 * 用户个人详情信息表
 * 包含九个属性:
 * 用户ID,来自web后端分配
 * 用户密码
 * 用户姓名,长度限制50bytes
 * 积极习惯完成总次数
 * 计划任务执行总次数
 * 计划任务执行总时间(毫秒级)
 * 读书次数
 * 用户token
 * 账号状态(1为登录中,0为未登录)
 *
 * 表关系:
 * 该类型与日常账务类型Bill_record_data形成1:n的关系
 *
 * @author hlccd 2020.6.6
 * @version 1.0
 * @version 2.0 新增用户密码
 */

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.List;

import hlccd.regular.Bill.Bill_budget_data;
import hlccd.regular.Bill.Bill_record_data;

public class User_data extends LitePalSupport implements Comparable<User_data> {
    /**
     * 用户ID,来自web后端
     */
    @Column (nullable = true, unique = true, defaultValue = "0", ignore = false)
    private Long   u_id;
    /**
     * 用户密码
     */
    @Column (nullable = true, unique = false, defaultValue = "", ignore = false)
    private String password;
    /**
     * 用户昵称
     */
    @Column (nullable = true, unique = false, defaultValue = "", ignore = false)
    private String name;
    /**
     * 该用户积极习惯完成总次数
     */
    @Column (nullable = true, unique = false, defaultValue = "0", ignore = false)
    private Long   p_num;
    /**
     * 该用户日常任务执行总次数
     */
    @Column (nullable = true, unique = false, defaultValue = "0", ignore = false)
    private Long   number;
    /**
     * 该用户日常任务执行总时间(毫秒级)
     */
    @Column (nullable = true, unique = false, defaultValue = "0", ignore = false)
    private Long   duration;
    /**
     * 用户读书次数
     */
    @Column (nullable = true, unique = false, defaultValue = "0", ignore = false)
    private Long   b_num;
    /**
     * 用户登录使用token
     */
    @Column (nullable = true, unique = true, defaultValue = "", ignore = false)
    private String token;
    /**
     * 该用户账号状态,1为登录中,0为未登录
     */
    @Column (nullable = true, unique = false, defaultValue = "0", ignore = false)
    private int    state;

    /**
     * 1:n与日常账务表Bill_record_data形成表关系
     */
    private List<Bill_record_data> records = new ArrayList<Bill_record_data>();

    User_data() {
    }

    public Long getU_id() {
        return u_id;
    }

    public void setU_id(Long u_id) {
        this.u_id = u_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getP_num() {
        return p_num;
    }

    public void setP_num(Long p_num) {
        this.p_num = p_num;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Long getB_num() {
        return b_num;
    }

    public void setB_num(Long b_num) {
        this.b_num = b_num;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    /**
     * 对获取到的全部的user进行排序
     * 将已登录的排在最上方
     * @param o
     * @return
     */
    @Override
    public int compareTo(User_data o) {
        return this.state - o.getState();
    }
}
