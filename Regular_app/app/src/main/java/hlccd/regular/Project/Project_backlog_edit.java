package hlccd.regular.Project;
/**
 * 待办事项编辑活动
 * 可以修改日常待办事项的名字，标签，难度，和备注以及截至时间
 * 也可以删除该记录
 *
 * @author hlccd 2020.6.19
 * @version 1.0
 */
import androidx.appcompat.app.AppCompatActivity;

import hlccd.regular.R;
import hlccd.regular.util.WheelView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.LitePal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Project_backlog_edit extends AppCompatActivity {
    private Project_backlog_data backlog;
    private EditText             name;
    private ImageButton          minus;
    private TextView             difficulty;
    private ImageButton          plus;
    private EditText             label;
    private EditText             remark;
    private TextView             timestamp;
    private Button               edit;
    private Button               delete;
    private WheelView            WV_year;
    private WheelView            WV_month;
    private WheelView            WV_day;
    private WheelView            WV_hour;

    private int          ping[]     = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    private int          run[]      = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    private int          year;
    private int          month;
    private int          day;
    private int          hour;
    private List<String> list_year  = new ArrayList<>();
    private List<String> list_month = new ArrayList<>();
    private List<String> list_day   = new ArrayList<>();
    private List<String> list_hour  = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.project_backlog_edit);
        Intent intent = getIntent();
        String s      = ((Intent) intent).getStringExtra("Project_backlog_edit");
        backlog = LitePal.where("timestamp=?", s).find(Project_backlog_data.class).get(0);


        name       = findViewById(R.id.project_backlog_edit_name);
        minus      = findViewById(R.id.project_backlog_edit_minus);
        difficulty = findViewById(R.id.project_backlog_edit_difficulty);
        plus       = findViewById(R.id.project_backlog_edit_plus);
        label      = findViewById(R.id.project_backlog_edit_label);
        remark     = findViewById(R.id.project_backlog_edit_remark);
        timestamp  = findViewById(R.id.project_backlog_edit_timestamp);
        edit       = findViewById(R.id.project_backlog_edit_edit);
        delete     = findViewById(R.id.project_backlog_edit_delete);
        WV_year    = findViewById(R.id.project_backlog_edit_WV_year);
        WV_month   = findViewById(R.id.project_backlog_edit_WV_month);
        WV_day     = findViewById(R.id.project_backlog_edit_WV_day);
        WV_hour    = findViewById(R.id.project_backlog_edit_WV_hour);

        initialize();
        initWheel();

        //降低难度
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (backlog.getDifficulty() <= 4) {
                    backlog.setDifficulty(backlog.getDifficulty() + 1);
                }
                String s = "";
                for (int x = 0; x < backlog.getDifficulty(); x++) {
                    s = s + "☆";
                }
                difficulty.setText(s);
            }
        });
        //增加难度
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (backlog.getDifficulty() >= 2) {
                    backlog.setDifficulty(backlog.getDifficulty() - 1);
                }
                String s = "";
                for (int x = 0; x < backlog.getDifficulty(); x++) {
                    s = s + "☆";
                }
                difficulty.setText(s);
            }
        });
        //提交编辑结果
        //日期错误不予提交
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Long   deadline = 0L;
                //日期错误的情况
                if (year % 4 == 0 && year % 100 != 0) {
                    if (day > run[month]) {
                        Toast.makeText(Project_backlog_edit.this, "日期错误", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else if (year % 400 == 0) {
                    if (day > run[month]) {
                        Toast.makeText(Project_backlog_edit.this, "日期错误", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    if (day > ping[month]) {
                        Toast.makeText(Project_backlog_edit.this, "日期错误", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                //日期合规，获取时间戳并保存
                String           time             = String.format("%04d", year) + "-" + String.format("%02d", month) + "-" + String.format("%02d", day) + " " + String.format("%02d", hour) + ":00:00";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date             date             = null;
                try {
                    date = simpleDateFormat.parse(time);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                deadline = date.getTime();
                if (!name.getText().toString().isEmpty()) {
                    backlog.setName(name.getText().toString());
                    backlog.setL_name(label.getText().toString());
                    backlog.setDeadline(deadline);
                    backlog.setRemark(remark.getText().toString());
                    backlog.save();
                    finish();
                }
            }
        });
        //删除该记录
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backlog.delete();
                finish();
            }
        });
    }

    /**
     * initialize
     * 初始化
     * 将该记录的结果展示于界面之中
     */
    private void initialize() {
        name.setText(backlog.getName());
        String s = "";
        for (int x = 0; x < backlog.getDifficulty(); x++) {
            s = s + "☆";
        }
        difficulty.setText(s);
        label.setText(backlog.getL_name());
        remark.setText(backlog.getRemark());
        timestamp.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(backlog.getTimestamp()));
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(backlog.getDeadline());

        year  = Integer.valueOf(time.substring(0,4));
        month = Integer.valueOf(time.substring(5,7));
        day   = Integer.valueOf(time.substring(8,10));
        hour  = Integer.valueOf(time.substring(11,13));
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

}