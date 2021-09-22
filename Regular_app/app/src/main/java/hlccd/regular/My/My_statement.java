package hlccd.regular.My;
/**
 * 月度报表活动
 * 该活动用于展示当前登录用户的月度报表
 * 综合收支记录进行按月汇总的记录
 * 分为收入和支出两种
 *
 * @author hlccd 2020.6.19
 * @version 1.0
 */

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import hlccd.regular.Bill.Bill_record_data;
import hlccd.regular.R;
import hlccd.regular.User.User;
import hlccd.regular.util.WheelView;

import android.os.Bundle;
import android.widget.RadioGroup;

import org.litepal.LitePal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class My_statement extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
    private RadioGroup              RG;
    private RecyclerView            RV;
    private int                     year       = Integer.parseInt(new SimpleDateFormat("yyyy").format(System.currentTimeMillis()));
    private int                     month      = Integer.parseInt(new SimpleDateFormat("MM").format(System.currentTimeMillis()));
    private List<My_statement_data> statements = new ArrayList<>();
    private String                  type       = "消费";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_statement);
        RG = findViewById(R.id.my_statement_RG);
        RV = findViewById(R.id.my_statement_RV);
        //绑定WheelView，实现滑动选择年份和月份
        final WheelView    wheel_year  = findViewById(R.id.my_statement_WV_year);
        final WheelView    wheel_month = findViewById(R.id.my_statement_WV_month);
        final List<String> list_year   = new ArrayList<>();
        final List<String> list_month  = new ArrayList<>();
        //对年份和月份进行添加数据
        for (int i = 0; i < 5; i++) {
            list_year.add(String.valueOf(year - i));
        }
        for (int i = month; i >= 1; i--) {
            list_month.add(String.valueOf(i));
        }
        for (int i = 12; i >= month + 1; i--) {
            list_month.add(String.valueOf(i));
        }
        initialize();
        RG.setOnCheckedChangeListener(this);
        //滑动后根据结果对待查看的月份进行搜索并展示对应数据
        wheel_year.lists(list_year).fontSize(35).showCount(3).selectTip("年").select(0).listener(new WheelView.OnWheelViewItemSelectListener() {
            @Override
            public void onItemSelect(int index) {
                year = Integer.parseInt(list_year.get(index));
                initialize();
            }
        }).build();
        wheel_month.lists(list_month).fontSize(35).showCount(3).selectTip("月").select(0).listener(new WheelView.OnWheelViewItemSelectListener() {
            @Override
            public void onItemSelect(int index) {
                month = Integer.parseInt(list_month.get(index));
                initialize();
            }
        }).build();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        /**
         * 将RadioGroup的两个按钮同消费类型和收入类型进行绑定
         * 点击不同按钮将会修改为对应类型的汇总收支数据
         */
        switch (checkedId) {
            case R.id.my_statement_expend:
                type = "消费";
                initialize();
                break;
            case R.id.my_statement_income:
                type = "收入";
                initialize();
                break;
        }
    }

    /**
     * initialize
     * 搜索当前登录用户的收支数据
     * 并按照月份进行显示
     * 修改月份和年份会同步调用进行修改以实现同步修改界面的效果
     */
    private void initialize() {
        //获取待查看月初的时间戳和下月的时间戳即月末时间戳
        String str      = year + "-" + month;
        String nowMonth = "", nextMonth = "";
        try {
            nowMonth  = dateToStamp(str + "-01 00:00:00");
            nextMonth = dateToStamp(getNextMonth(str) + "-01 00:00:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //清空汇总表中数据用于后续重新添加
        statements.clear();
        //从收支总表中查询待查询月份的记录
        List<Bill_record_data> all = LitePal.where("timestamp>=? and timestamp<? and type=?", nowMonth, nextMonth, type).find(Bill_record_data.class);
        Collections.sort(all);
        int find = 0, n;
        //根据类型而分类
        if (type.equals("消费")) {
            statements.add(new My_statement_data("总支出", 0));
        } else {
            statements.add(new My_statement_data("总收入", 0));
        }
        for (Bill_record_data record : all) {
            //查询当前登录用户的收支记录
            if (record.getU_id().equals(User.getUserId())) {
                if (type.equals(record.getType())) {
                    //按照收支标签进行分类，同一类计入同一组
                    find = 0;
                    for (n = 1; n < statements.size(); n++) {
                        if (statements.get(n).getName().equals(record.getCategory())) {
                            find = n;
                            break;
                        }
                    }
                    //如果没找到说明属于新一组
                    if (find == 0) {
                        statements.add(new My_statement_data(record.getCategory(), 0));
                    }
                    //将总记录和对应记录进行数据添加
                    statements.get(n).setCheek(statements.get(n).getCheek() + record.getMoney());
                    statements.get(0).setCheek(statements.get(0).getCheek() + record.getMoney());
                }
            }
        }
        //对接适配器和数据
        Collections.sort(statements);
        LinearLayoutManager       layoutManager = new LinearLayoutManager(this);
        My_statement_data_adapter adapter       = new My_statement_data_adapter(statements);
        RV.setLayoutManager(layoutManager);
        RV.setAdapter(adapter);
    }

    /**
     * dateToStamp
     * 将日期转化为时间戳并返回
     *
     * @param s 日期
     *
     * @return String格式的时间戳
     */
    private static String dateToStamp(String s) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date             date             = simpleDateFormat.parse(s);
        return String.valueOf(date.getTime());
    }

    /**
     * getNextMonth
     * 获取下个月的日期
     *
     * @param ss 本月的首日日期
     *
     * @return 下月的首日日期
     */
    private static String getNextMonth(String ss) {
        String   nextMonth = "";
        String[] s         = ss.split("-");
        int      year      = Integer.parseInt(s[0]);
        int      month     = Integer.parseInt(s[1]);
        if (month == 12) {
            month = 1;
            year++;
        } else {
            month++;
        }
        nextMonth = year + "-" + month;
        return nextMonth;
    }
}