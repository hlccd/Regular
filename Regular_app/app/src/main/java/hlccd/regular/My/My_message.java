package hlccd.regular.My;
/**
 * 个人信息活动
 * 该活动用于展示当前登录用户的详情信息
 * 其中包括：
 * 用户昵称
 * 用户ID
 * 积极习惯完成次数
 * 消极习惯完成次数
 * 任务执行总次数
 * 任务执行总时间（min）
 * 收入总额
 * 收入总次数
 * 消费总额
 * 消费总次数
 *
 * 后续将添加修改用户昵称等渠道
 *
 * @author hlccd 2020.6.19
 * @version 1.0
 */

import androidx.appcompat.app.AppCompatActivity;

import hlccd.regular.Bill.Bill_record_data;
import hlccd.regular.Project.Project_habit_data;
import hlccd.regular.R;
import hlccd.regular.User.User;
import hlccd.regular.Regular.Regular_data;

import android.os.Bundle;
import android.widget.TextView;

import org.litepal.LitePal;

import java.util.List;

public class My_message extends AppCompatActivity {
    private TextView name;
    private TextView uid;
    private TextView positive;
    private TextView negative;
    private TextView regular;
    private TextView duration;

    private TextView income;
    private TextView income_num;
    private TextView consume;
    private TextView consume_num;

    //待展示的信息数据
    Long pos = 0L, neg = 0L, dur = 0L, imp = 0L, incN = 0L, conN = 0L;
    Double inc = 0D, con = 0D;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_message);
        name        = findViewById(R.id.my_message_name);
        uid         = findViewById(R.id.my_message_uid);
        positive    = findViewById(R.id.my_message_positive);
        negative = findViewById(R.id.my_message_negative);
        regular  = findViewById(R.id.my_message_regular);
        duration = findViewById(R.id.my_message_duration);
        income      = findViewById(R.id.my_message_income);
        income_num  = findViewById(R.id.my_message_income_num);
        consume     = findViewById(R.id.my_message_consume);
        consume_num = findViewById(R.id.my_message_consume_num);
        //展示当前登录的用户昵称和用户ID
        name.setText(User.getUserName());
        uid.setText(User.getUserId() + "");
        /**
         * 获取用户个人习惯全记录
         * 从用户习惯全记录中找出与当前登录用户ID相同的记录
         * 并将该用户曾执行的积极习惯和消极习惯次数进行统计并展示
         */
        List<Project_habit_data> habits = LitePal.findAll(Project_habit_data.class);
        for (Project_habit_data habit : habits) {
            if (habit.getU_id() == User.getUserId()) {
                pos += habit.getPositive();
                neg += habit.getNegative();
            }
        }
        positive.setText(pos / 60000 + "min");
        negative.setText(neg / 60000 + "min");
//        /**
//         * 获取用户日常任务全记录
//         * 从用户日常任务全记录中找出与当前登录用户ID相同的记录
//         * 并将该用户曾执行的日常任务次数和累计时间进行统计并换算成分钟进行展示
//         */
//        List<Project_task_data> tasks = LitePal.findAll(Project_task_data.class);
//        for (Project_task_data task : tasks) {
//            if (task.getU_id() == User.getUserId()) {
//                dur += task.getDuration();
//                imp += task.getNumber();
//            }
//        }
        /**
         * 获取用户执行全记录
         * 从用户执行全记录中找出与当前登录用户ID相同的记录
         * 并将该用户曾执行的次数和累计时间进行统计并换算成分钟进行展示
         */
        List<Regular_data> all = LitePal.findAll(Regular_data.class);
        for (Regular_data implement : all) {
            if (implement.getU_id() == User.getUserId()) {
                dur += implement.getDuration();
                imp++;
            }
        }
        regular.setText(imp + "次");
        duration.setText(dur / 60000 + "min");
        /**
         * 获取用户收支全记录
         * 从用户收支全记录中找出与当前登录用户ID相同的记录
         * 并将该用户曾记录的收支情况进行统计并展示
         */
        List<Bill_record_data> records = LitePal.findAll(Bill_record_data.class);
        for (Bill_record_data record : records) {
            if (record.getU_id() == User.getUserId()) {
                if (record.getType().equals("消费")) {
                    conN++;
                    con += record.getMoney();
                } else {
                    incN++;
                    inc += record.getMoney();
                }
            }
        }
        income.setText(String.format("%.2f", inc) + "元");
        income_num.setText(incN + "笔");
        consume.setText(String.format("%.2f", con) + "元");
        consume_num.setText(conN + "笔");
    }
}