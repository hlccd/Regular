package hlccd.regular.Bill;
/**
 * 日常账务类型
 * 包含六个属性:
 * 用户ID,以用户信息表的用户ID做外键
 * 金额,十位整数两位小数
 * 类别,以二级账单类型的二级类型名做外键
 * 收支类型,同二级账单类别的收支类型,默认"消费"
 * 备注,长度限制2e5bytes
 * 时间戳,该账务记录时刻的时间戳
 *
 * 表关系:
 * 该类型与用户信息表User_data形成n:1的关系
 * 该类型与二级账单类型Bill_type_second形成n:m的关系
 *
 * @author hlccd 2020.6.6
 * @version 1.0
 */

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import hlccd.regular.User.User_data;

public class Bill_record_data extends LitePalSupport implements Comparable<Bill_record_data> {
    /**
     * 所属用户ID,以用户信息表的用户ID做外键
     */
    @Column (nullable = true, unique = false, defaultValue = "0", ignore = false)
    private Long   u_id;
    /**
     * 金额,十位整数两位小数
     */
    @Column (nullable = true, unique = false, defaultValue = "0.00", ignore = false)
    private Double money;
    /**
     * 账务记录类别,以二级账单类型的二级类型名做外键
     */
    @Column (nullable = true, unique = false, defaultValue = "", ignore = false)
    private String category;
    /**
     * 收支类型,同二级账单类别的收支类型,默认"消费"
     */
    @Column (nullable = true, unique = false, defaultValue = "支出", ignore = false)
    private String type;
    /**
     * 账务记录备注,长度限制2e5bytes
     */
    @Column (nullable = true, unique = false, defaultValue = "", ignore = false)
    private String remark;
    /**
     * 时间戳,该账务记录时刻的时间戳
     */
    @Column (nullable = true, unique = false, defaultValue = "0", ignore = false)
    private Long   timestamp;

    /**
     * n:1与用户信息表User_data形成表关系
     */
    private User_data                   user;
    /**
     * n:m与二级账单类型Bill_type_second_data形成表关系
     */
    private List<Bill_type_second_data> types = new ArrayList<Bill_type_second_data>();

    Bill_record_data() {
    }

    Bill_record_data(Long u_id, Double money, String type, String remark, Long timestamp) {
        this.u_id      = u_id;
        this.money     = new BigDecimal(money).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        this.type      = type;
        this.remark    = remark;
        this.timestamp = timestamp;
    }

    public Long getU_id() {
        return u_id;
    }

    public void setU_id(Long u_id) {
        this.u_id = u_id;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = new BigDecimal(money).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
     * compareTo
     * 时间比较器
     * 使在用sort时按照时间顺序从前到后以此排列
     *
     * @param o 另一个账务记录数据
     *
     * @return int -1为否,1为真,0为相同
     *
     * @author hlccd 2021.6.6
     * @version 1.0
     */
    @Override
    public int compareTo(Bill_record_data o) {
        long i = this.timestamp-o.getTimestamp();
        if (i < 0) {
            return 1;
        } else if (i > 0) {
            return -1;
        }
        return 0;
    }
}
