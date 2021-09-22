package hlccd.regular.util;
/**
 * GridView展示数据
 * 该类数据主要包括图片和文本描述两类
 * 其中内置将账单二级类型转化为该类型函数
 *
 * @author hlccd 2020.6.8
 * @version 1.0
 */

import java.util.ArrayList;
import java.util.List;

import hlccd.regular.Bill.Bill_type_second_data;

public class GV_data {
    private int    image;
    private String describe;

    public GV_data() {
    }

    GV_data(int image, String describe) {
        this.image    = image;
        this.describe = describe;
    }

    /**
     * 账单二级类型数组转化为该类型数组
     */
    public List<GV_data> ToGV(List<Bill_type_second_data> types) {
        List<GV_data> data = new ArrayList<>();
        for (Bill_type_second_data type : types) {
            data.add(new GV_data(type.getAddress(), type.getName()));
        }
        return data;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }
}