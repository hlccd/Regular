package hlccd.regular.Bill;
/**
 * 预算子界面碎片
 * 该界面中上部分在点击后可跳转至账务预算记录添加界面
 * 界面其余部分分别展示本月预算的已用和计划情况
 * 同时展示本月的每一条预算记录
 * 每条记录的右上角点击即可删除该条记录
 *
 * @author hlccd 2020.6.10
 * @version 1.0
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.litepal.LitePal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import hlccd.regular.R;
import hlccd.regular.User.User;

public class Bill_budget extends Fragment implements RadioGroup.OnCheckedChangeListener {
    private View                   view;
    private TextView               month;
    private TextView               surplus;
    private TextView               spending;
    private TextView               plan;
    private TextView               hint;
    private TextView               occupy;
    private RecyclerView           recycler;
    private List<Bill_budget_data> budgets = new ArrayList<Bill_budget_data>();

    private IntentFilter    IF;
    private RefreshReceiver RR;

    //动态刷新
    class RefreshReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            onResume();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        this.view = view;
        month     = view.findViewById(R.id.bill_budget_month);
        surplus   = view.findViewById(R.id.bill_budget_surplus);
        spending  = view.findViewById(R.id.bill_budget_spending);
        plan      = view.findViewById(R.id.bill_budget_plan);
        occupy    = view.findViewById(R.id.bill_budget_occupy);
        recycler  = view.findViewById(R.id.bill_budget_RV);
        occupy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), Bill_budget_add.class));
            }
        });

        initialize();
        LinearLayoutManager      layoutManager = new LinearLayoutManager(getContext());
        Bill_budget_data_adapter adapter       = new Bill_budget_data_adapter(budgets, getContext());
        recycler.setLayoutManager(layoutManager);
        recycler.setAdapter(adapter);

        //当存在预算被删除后进行界面刷新
        IF = new IntentFilter();
        IF.addAction("android.refresh.Bill_budget");
        RR = new RefreshReceiver();
        getContext().registerReceiver(RR, IF);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContext().unregisterReceiver(RR);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bill_budget, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        budgets.clear();
        initialize();
        LinearLayoutManager      layoutManager = new LinearLayoutManager(getContext());
        Bill_budget_data_adapter adapter       = new Bill_budget_data_adapter(budgets, getContext());
        recycler.setLayoutManager(layoutManager);
        recycler.setAdapter(adapter);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

    }

    /**
     * initialize
     * 初始化数据并进行收支记录查找和绑定
     * 同时绑定适配器
     */
    void initialize() {
        //确定当前月份和下月的时间戳
        String years       = new SimpleDateFormat("yyyy").format(System.currentTimeMillis());
        String months      = new SimpleDateFormat("MM").format(System.currentTimeMillis());
        short  year        = Short.valueOf(years);
        short  month       = Short.valueOf(months);
        String year_months = new SimpleDateFormat("yyyy-MM").format(System.currentTimeMillis());
        String nowMonth    = "", nextMonth = "";
        try {
            nowMonth  = dateToStamp(year_months + "-01 00:00:00");
            nextMonth = dateToStamp(getNextMonth(year_months) + "-01 00:00:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //查找当前月份的所有消费记录
        List<Bill_record_data> records = LitePal.where(
                "timestamp>=? and timestamp<? and type='消费'",
                nowMonth,
                nextMonth).
                find(Bill_record_data.class);
        /**
         * 查找所有预算记录
         * 并确定该用户的记录
         * 仅查找同年同月的预算记录
         * 根据预算记录和消费记录确定该记录的支出情况
         */
        budgets = LitePal.findAll(Bill_budget_data.class);
        for (Bill_budget_data budget : budgets) {
            if (budget.getU_id().equals(User.getUserId())) {
                if (budget.getYear() == year && budget.getMonth() == month) {
                    for (Bill_record_data record : records) {
                        if (record.getCategory().equals(budget.getCategory())) {
                            budget.setSpending(budget.getSpending() + record.getMoney());
                        }
                    }
                }
            }
        }
        Collections.sort(budgets);
        //确定本月的计划总额和消费总额并展示
        double plan_all = 0, spending_all = 0;
        for (int x = 0; x < budgets.size(); x++) {
            spending_all += budgets.get(x).getSpending();
            plan_all += budgets.get(x).getPlan();
        }
        this.month.setText(month + "月总预算");
        surplus.setText(String.valueOf(plan_all - spending_all));
        spending.setText("已用：" + spending_all);
        plan.setText(plan_all + "：计划");
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
