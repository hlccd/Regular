package hlccd.regular.Project;
/**
 * 规划待办事项界面碎片
 * 该界面中输入名称后点击添加即可新增个人习惯
 * 记录默认的截至时间为当前时间
 * 可通过滑动下方滑轮进行修改截至时间
 * 若日期不合理则无法添加
 *
 * @author hlccd 2020.6.19
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.litepal.LitePal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import hlccd.regular.Homepage;
import hlccd.regular.R;
import hlccd.regular.User.User;
import hlccd.regular.util.WheelView;

public class Project_backlog extends Fragment {
    private View         view;
    private EditText     edit;
    private ImageView    add;
    private RecyclerView RV;
    private WheelView    WV_year;
    private WheelView    WV_month;
    private WheelView    WV_day;
    private WheelView    WV_hour;

    private List<Project_backlog_data> backlogs = new ArrayList<>();
    private LinearLayoutManager        layoutManager;
    private Project_backlog_data_adapter adapter;

    private Calendar     now        = Calendar.getInstance();
    //按照平年和闰年以及月份确定日期上限
    private int          ping[]     = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    private int          run[]      = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    //确定当前时间
    private int          year       = now.get(Calendar.YEAR);
    private int          month      = (now.get(Calendar.MONTH) + 1);
    private int          day        = now.get(Calendar.DAY_OF_MONTH);
    private int          hour       = now.get(Calendar.HOUR_OF_DAY);
    private List<String> list_year  = new ArrayList<>();
    private List<String> list_month = new ArrayList<>();
    private List<String> list_day   = new ArrayList<>();
    private List<String> list_hour  = new ArrayList<>();

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
        IF        = new IntentFilter();
        IF.addAction("android.refresh.Project_backlog");
        RR = new RefreshReceiver();
        getContext().registerReceiver(RR, IF);
        edit     = view.findViewById(R.id.project_backlog_edit);
        add      = view.findViewById(R.id.project_backlog_add);
        RV       = view.findViewById(R.id.project_backlog_RV);
        WV_year  = view.findViewById(R.id.project_backlog_WV_year);
        WV_month = view.findViewById(R.id.project_backlog_WV_month);
        WV_day   = view.findViewById(R.id.project_backlog_WV_day);
        WV_hour  = view.findViewById(R.id.project_backlog_WV_hour);


        initWheel();
        initialize();

        //增加待办事项记录
        //若日期不合规则不予添加
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name     = edit.getText().toString();
                Long   deadline = 0L;
                //日期不合规，不予添加的情况
                if (year % 4 == 0 && year % 100 != 0) {
                    if (day > run[month]) {
                        Toast.makeText(getContext(), "日期错误", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else if (year % 400 == 0) {
                    if (day > run[month]) {
                        Toast.makeText(getContext(), "日期错误", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    if (day > ping[month]) {
                        Toast.makeText(getContext(), "日期错误", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                //日期合规，获取对应时间的时间戳
                String           time             = String.format("%04d", year) + "-" + String.format("%02d", month) + "-" + String.format("%02d", day) + " " + String.format("%02d", hour) + ":00:00";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date             date             = null;
                try {
                    date = simpleDateFormat.parse(time);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                deadline = date.getTime();
                //添加待办事项记录
                if (!name.isEmpty()) {
                    Project_backlog_data backlog = new Project_backlog_data();
                    backlog.setName(name);
                    backlog.setU_id(User.getUserId());
                    backlog.setL_name("");
                    backlog.setDifficulty(1);
                    backlog.setDeadline(deadline);
                    backlog.setRemark("");
                    backlog.setTimestamp(System.currentTimeMillis());
                    backlog.save();
                    edit.setText("");
                    onResume();
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.project_backlog, container, false);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContext().unregisterReceiver(RR);
    }

    @Override
    public void onResume() {
        super.onResume();
        initWheel();
        initialize();
    }

    /**
     * initWheel
     * 初始化滚轮滑动数据
     */
    private void initWheel() {
        //情况四组数据用以后续添加
        list_year.clear();
        list_month.clear();
        list_day.clear();
        list_hour.clear();
        //添加年数据
        for (int i = 0; i < 5; i++) {
            list_year.add(String.valueOf(year - i));
        }
        for (int i = 5; i > 0; i--) {
            list_year.add(String.valueOf(year + i));
        }
        //添加月数据
        for (int i = month; i >= 1; i--) {
            list_month.add(String.valueOf(i));
        }
        for (int i = 12; i >= month + 1; i--) {
            list_month.add(String.valueOf(i));
        }
        //添加日数据
        for (int i = day; i >= 1; i--) {
            list_day.add(String.valueOf(i));
        }
        for (int i = 31; i >= day + 1; i--) {
            list_day.add(String.valueOf(i));
        }
        //添加小时数据
        for (int i = hour; i >= 1; i--) {
            list_hour.add(String.valueOf(i));
        }
        for (int i = 24; i >= hour + 1; i--) {
            list_hour.add(String.valueOf(i));
        }
        //滑动修改年份
        WV_year.lists(list_year).fontSize(35).showCount(3).selectTip("年").select(0).listener(new WheelView.OnWheelViewItemSelectListener() {
            @Override
            public void onItemSelect(int index) {
                year = Integer.parseInt(list_year.get(index));
                onResume();
            }
        }).build();
        //滑动修改月份
        WV_month.lists(list_month).fontSize(35).showCount(3).selectTip("月").select(0).listener(new WheelView.OnWheelViewItemSelectListener() {
            @Override
            public void onItemSelect(int index) {
                month = Integer.parseInt(list_month.get(index));
                onResume();
            }
        }).build();
        //滑动修改日期
        WV_day.lists(list_day).fontSize(35).showCount(3).selectTip("日").select(0).listener(new WheelView.OnWheelViewItemSelectListener() {
            @Override
            public void onItemSelect(int index) {
                day = Integer.parseInt(list_day.get(index));
                onResume();
            }
        }).build();
        //滑动修改小时
        WV_hour.lists(list_hour).fontSize(35).showCount(3).selectTip("时").select(0).listener(new WheelView.OnWheelViewItemSelectListener() {
            @Override
            public void onItemSelect(int index) {
                hour = Integer.parseInt(list_hour.get(index));
                onResume();
            }
        }).build();
    }

    /**
     * initialize
     * 初始化
     * 展示该用户所有待办事项
     * 绑定适配器
     */
    private void initialize() {
        List<Project_backlog_data> all = LitePal.findAll(Project_backlog_data.class);
        backlogs = new ArrayList<>();
        for (int x = 0; x < all.size(); x++) {
            if (Objects.equals(all.get(x).getU_id(), User.getUserId())) {
                if (all.get(x).getDuration()<1L){
                    backlogs.add(all.get(x));
                }
            }
        }
        Collections.sort(backlogs);
        layoutManager = new LinearLayoutManager(getContext());
        RV.setLayoutManager(layoutManager);
        adapter = new Project_backlog_data_adapter((Homepage)getActivity(), backlogs);
        RV.setAdapter(adapter);
    }
}