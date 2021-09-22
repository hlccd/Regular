package hlccd.regular.Bill;
/**
 * 一级账单类型
 * 包含两个属性:
 * 一级类型名字,主键,长度限制10bytes
 * 收支类型,长度限制10bytes,默认之为"消费"
 *
 * 表关系:
 * 该类型与二级账单类型Bill_type_second形成1:n的关系
 *
 * @author hlccd 2020.6.6
 * @version 1.0
 */

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.List;

public class Bill_type_root_data extends LitePalSupport {
    /**
     * 一级账单类型名
     */
    @Column (nullable = true, unique = true, defaultValue = "", ignore = false)
    private String name;
    /**
     * 收支类型
     */
    @Column (nullable = true, unique = false, defaultValue = "消费", ignore = false)
    private String type;

    /**
     * 1:n与二级账务类型表Bill_type_second_data形成表关系
     */
    private List<Bill_type_second_data> types = new ArrayList<Bill_type_second_data>();

    public Bill_type_root_data() {
    }

    public Bill_type_root_data(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
