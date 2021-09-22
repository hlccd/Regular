package hlccd.regular.Bill;
/**
 * 二级账单类型
 * 包含六个属性:
 * 二级类型名,长度限制10bytes,主键
 * 一级类型名,长度限制10bytes,以一级账单类型的名字做外键
 * 二级收支类型,同所属一级类型的收支类型,默认"支出"
 * 图片资源地址(主色)
 * 图片资源地址(辅色)
 * 使用次数,用于排序
 *
 * 表关系:
 * 该类型与一级账单类型形成n:1的关系
 * 该类型与日常账务类型形成n:m的关系
 *
 * 功能:
 * 可获取二级账单类型表中按照"收入"和"消费"分类的账单类型总表
 *
 * @author hlccd 2020.6.6
 * @version 1.1
 */

import org.litepal.LitePal;
import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hlccd.regular.R;

public class Bill_type_second_data extends LitePalSupport implements Comparable<Bill_type_second_data> {
    /**
     * 二级账单类型名
     */
    @Column (nullable = true, unique = true, defaultValue = "", ignore = false)
    private String name;
    /**
     * 所属的一级账单类型名
     */
    @Column (nullable = true, unique = false, defaultValue = "", ignore = false)
    private String r_name;
    /**
     * 二级收支类型
     */
    @Column (nullable = true, unique = false, defaultValue = "消费", ignore = false)
    private String type;
    /**
     * 该账单类型图片对应的图片资源地址
     */
    @Column (nullable = true, unique = false, defaultValue = "0", ignore = false)
    private int    address;
    /**
     * 该账单类型的使用次数,用于排序
     */
    @Column (nullable = true, unique = false, defaultValue = "0", ignore = false)
    private Long   number;

    /**
     * n:1与一级账务类型表Bill_type_root_data形成表关系
     */
    private Bill_type_root_data    root;
    /**
     * n:m与日常账务表Bill_record_data形成表关系
     */
    private List<Bill_record_data> records = new ArrayList<Bill_record_data>();

    Bill_type_second_data() {
    }

    public Bill_type_second_data(String name, String r_name, String type, int address) {
        this.name    = name;
        this.r_name  = r_name;
        this.type    = type;
        this.address = address;
        this.number  = Long.valueOf(0);
    }

    public Bill_type_second_data(String name, String r_name, String type, int address, Long number) {
        this.name    = name;
        this.r_name  = r_name;
        this.type    = type;
        this.address = address;
        this.number  = number;
    }

    /**
     * 获取消费总表
     *
     * @return List<Bill_type_second_data> 收支类型为消费的二级账单总表
     */
    public List<Bill_type_second_data> consume() {
        List<Bill_type_second_data> consume = LitePal.where("type=?", "消费").find(Bill_type_second_data.class);
        if(consume.isEmpty()){
            consume.add(new Bill_type_second_data("消费", "一级账单名", "收入", R.drawable.main_bill));
        }else {
            Collections.sort(consume);;
        }
        return consume;
    }

    /**
     * 获取收入总表
     *
     * @return List<Bill_type_second_data> 收支类型为收入的二级账单总表
     */
    public List<Bill_type_second_data> income() {
        List<Bill_type_second_data> income = LitePal.where("type=?", "收入").find(Bill_type_second_data.class);
        if(income.isEmpty()){
            income.add(new Bill_type_second_data("收入", "一级账单名", "收入", R.drawable.main_bill));
        }else {
            Collections.sort(income);;
        }
        return income;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getR_name() {
        return r_name;
    }

    public void setR_name(String r_name) {
        this.r_name = r_name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    @Override
    public int compareTo(Bill_type_second_data o) {
        if (this.number > o.number) {
            return -1;
        }
        if (this.number < o.number) {
            return 1;
        }
        return 0;
    }
}
