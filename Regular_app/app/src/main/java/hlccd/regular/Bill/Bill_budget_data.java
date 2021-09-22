package hlccd.regular.Bill;
/**
 * 账务月度预算类型
 * 包含七个属性:
 * 该预算的年份,联合主键之一
 * 该预算的月份,联合主键之一
 * 该预算的类别,联合主键之一
 * 该预算所属用户ID,联合主键之一
 * 计划金额,十位整数位,两位小数位
 * 已用金额,十位整数位,两位小数位
 * 备注,长度限制2e5bytes
 *
 * 表关系:
 * 该类型与用户信息表User_data形成n:1的关系
 * 该类型与二级账单类型Bill_type_second形成n:m的关系
 *
 * @author hlccd 2020.6.10
 * @version 1.0
 */

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.List;

import hlccd.regular.User.User_data;

public class Bill_budget_data extends LitePalSupport implements Comparable<Bill_budget_data> {
    /**
     * 该预算的年份,联合主键之一
     */
    @Column (nullable = true, unique = false, defaultValue = "0", ignore = false)
    private short  year;
    /**
     * 该预算的月份,联合主键之一
     */
    @Column (nullable = true, unique = false, defaultValue = "0", ignore = false)
    private short  month;
    /**
     * 该预算的类型,以二级类型名做外键,联合主键之一
     */
    @Column (nullable = true, unique = false, defaultValue = "", ignore = false)
    private String category;
    /**
     * 该预算所属用户ID,以用户ID号做外键,联合主键之一
     */
    @Column (nullable = true, unique = false, defaultValue = "", ignore = false)
    private Long   u_id;
    /**
     * 计划金额,十位整数位,两位小数位
     */
    @Column (nullable = true, unique = false, defaultValue = "", ignore = false)
    private Double plan;
    /**
     * 已用金额,十位整数位,两位小数位
     */
    @Column (nullable = true, unique = false, defaultValue = "", ignore = false)
    private Double spending;
    /**
     * 月度预算备注,长度限制2e5bytes
     */
    @Column (nullable = true, unique = false, defaultValue = "", ignore = false)
    private String remark;


    /**
     * n:1与用户信息表User_data形成表关系
     */
    private User_data                   user;
    /**
     * n:m与二级账单类型Bill_type_second_data形成表关系
     */
    private List<Bill_type_second_data> types = new ArrayList<Bill_type_second_data>();

    public Bill_budget_data() {
    }

    public Bill_budget_data(short year, byte month, String category, Long u_id, Double plan) {
        this.year     = year;
        this.month    = month;
        this.category = category;
        this.u_id     = u_id;
        this.plan     = plan;
        this.spending = Double.valueOf(0);
        this.remark   = "";
    }

    public Bill_budget_data(short year, byte month, String category, Long u_id, Double plan, Double spending, String remark) {
        this.year     = year;
        this.month    = month;
        this.category = category;
        this.u_id     = u_id;
        this.plan     = plan;
        this.spending = spending;
        this.remark   = remark;
    }

    public short getYear() {
        return year;
    }

    public void setYear(short year) {
        this.year = year;
    }

    public short getMonth() {
        return month;
    }

    public void setMonth(short month) {
        this.month = month;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Long getU_id() {
        return u_id;
    }

    public void setU_id(Long u_id) {
        this.u_id = u_id;
    }

    public Double getPlan() {
        return plan;
    }

    public void setPlan(Double plan) {
        this.plan = plan;
    }

    public Double getSpending() {
        return spending;
    }

    public void setSpending(Double spending) {
        this.spending = spending;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * compareTo
     * 时间比较器
     * 使在用sort时按照剩余可用进行降序排序
     *
     * @param o 另一个预算记录数据
     *
     * @return int -1为否,1为真,0为相同
     *
     * @author hlccd 2021.6.6
     * @version 1.0
     */
    @Override
    public int compareTo(Bill_budget_data o) {
        if (this.plan - this.spending > o.plan - this.spending) {
            return 1;
        } else if (this.plan - this.spending < o.plan - this.spending) {
            return -1;
        }
        return 0;
    }
}
