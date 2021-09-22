package hlccd.regular.Bill;
/**
 * 记录子界面碎片
 * 该界面中含有一个点击按钮,点击后可跳转至账务记录添加界面
 * 界面其余部分分别展示本月收入和消费总情况
 * 按钮下方按日期展示每日的账务数据
 *
 * @author hlccd 2020.6.6
 * @version 1.0
 */

import android.content.Intent;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.litepal.LitePal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hlccd.regular.R;
import hlccd.regular.User.User;

public class Bill_record extends Fragment {
    private TextView                     income;
    private TextView                     consume;
    private ImageButton                  add;
    private RecyclerView                 RV;
    private View                         view;
    private TextView                     hint;
    private List<List<Bill_record_data>> records = new ArrayList<>();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        this.view = view;
        income    = view.findViewById(R.id.bill_record_income);
        consume   = view.findViewById(R.id.bill_record_consume);
        add       = view.findViewById(R.id.bill_record_add);
        RV        = view.findViewById(R.id.bill_record_RV);
        hint      = view.findViewById(R.id.bill_record_hint);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Bill_record_add.class));
            }
        });
        initialize();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bill_record, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initialize();
    }

    /**
     * initialize_data
     * 对数据进行初始化
     * 若账号未登录则展示用户ID为-1的用户的数据
     * 如果账号已登录则展示该用户的日常账务记录
     * 初始化为该账户本月收支记录
     *
     * @author hlccd 2021.6.6
     * @version 1.0
     */
    private void initialize() {
        //确定当然系统中的月份和日期
        SimpleDateFormat month_format = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat date_format  = new SimpleDateFormat("yyyy-MM-dd");
        //按照当前日期和月份初始化
        String month = month_format.format(System.currentTimeMillis());
        String date  = date_format.format(System.currentTimeMillis());
        //归零本月收入和消费数据
        double income_money  = 0;
        double consume_money = 0;
        //从数据库中查找出所有隶属于当前登录用户的账务记录数据,并按照日期进行排序
        String                 u_id = String.valueOf(User.getUserId());
        List<Bill_record_data> all  = LitePal.where("u_id=?", u_id).find(Bill_record_data.class);
        Collections.sort(all);
        //建立一个记录某一日的账务记录表,用以按日期区分账务记录
        List<Bill_record_data> partial = new ArrayList<>();
        //清空嵌条账务记录表,并根据查询结果按不同日期插入表中
        records.clear();
        for (Bill_record_data data : all) {
            //如果该条账务记录的日期与前一条的日期不同,应该进行分表
            if (!date_format.format(data.getTimestamp()).equals(date)) {
                //如果前一天的账务记录表不是空表,则应该插入嵌套账务记录表中
                if (!partial.isEmpty()) {
                    records.add(partial);
                }
                //将新一日的账务记录表初始化,并根据新记录的日期修改新表日期
                partial = new ArrayList<>();
                date    = date_format.format(data.getTimestamp());
            }
            //将该条账务记录加入按日区分的账务记录表
            partial.add(data);
            //将本月收支情况进行记录
            if (month_format.format(data.getTimestamp()).equals(month)) {
                if (data.getType().equals("收入")) {
                    income_money += data.getMoney();
                } else if (data.getType().equals("消费")) {
                    consume_money += data.getMoney();
                }
            }
        }
        //添加最早一天的数据以补全全部该用户日常账务记录
        records.add(partial);

        //将本月收入与消费情况展示到界面上
        income.setText("" + income_money);
        consume.setText("" + consume_money);
        //加粗
        TextPaint tpl = income.getPaint();
        tpl.setFakeBoldText(true);
        TextPaint tpr = consume.getPaint();
        tpr.setFakeBoldText(true);


        if (records.get(0).isEmpty()) {
            RV.setVisibility(View.GONE);
            hint.setVisibility(View.VISIBLE);
        } else {
            hint.setVisibility(View.GONE);
            RV.setVisibility(View.VISIBLE);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            RV.setLayoutManager(layoutManager);
            Bill_record_RV_adapter adapter = new Bill_record_RV_adapter(getContext(), records);
            RV.setAdapter(adapter);
        }
    }
}
