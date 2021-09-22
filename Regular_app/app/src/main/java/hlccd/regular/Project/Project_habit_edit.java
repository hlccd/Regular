package hlccd.regular.Project;
/**
 * 个人习惯编辑活动
 * 可以修改日常任务记录的名字，标签，难度，和备注
 * 也可以删除该记录
 *
 * @author hlccd 2020.6.19
 * @version 1.0
 */
import androidx.appcompat.app.AppCompatActivity;

import hlccd.regular.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import org.litepal.LitePal;

import java.text.SimpleDateFormat;

public class Project_habit_edit extends AppCompatActivity {
    private Project_habit_data habit;
    private EditText           name;
    private ImageButton        minus;
    private TextView           difficulty;
    private ImageButton        plus;
    private EditText           label;
    private TextView           positive;
    private TextView           negative;
    private EditText           remark;
    private TextView           timestamp;
    private Button             edit;
    private Button             delete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.project_habit_edit);
        Intent intent = getIntent();
        String s      = ((Intent) intent).getStringExtra("Project_habit_edit");
        habit = LitePal.where("timestamp=?", s).find(Project_habit_data.class).get(0);

        name       = findViewById(R.id.project_habit_edit_name);
        minus      = findViewById(R.id.project_habit_edit_minus);
        difficulty = findViewById(R.id.project_habit_edit_difficulty);
        plus       = findViewById(R.id.project_habit_edit_plus);
        label      = findViewById(R.id.project_habit_edit_label);
        positive   = findViewById(R.id.project_habit_edit_positive);
        negative   = findViewById(R.id.project_habit_edit_negative);
        remark     = findViewById(R.id.project_habit_edit_remark);
        timestamp  = findViewById(R.id.project_habit_edit_timestamp);
        edit       = findViewById(R.id.project_habit_edit_edit);
        delete     = findViewById(R.id.project_habit_edit_delete);

        initialize();
        //添加难度
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (habit.getDifficulty() <= 4) {
                    habit.setDifficulty(habit.getDifficulty() + 1);
                }
                String s = "";
                for (int x = 0; x < habit.getDifficulty(); x++) {
                    s = s + "☆";
                }
                difficulty.setText(s);
            }
        });
        //降低难度
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (habit.getDifficulty() >= 2) {
                    habit.setDifficulty(habit.getDifficulty() - 1);
                }
                String s = "";
                for (int x = 0; x < habit.getDifficulty(); x++) {
                    s = s + "☆";
                }
                difficulty.setText(s);
            }
        });
        //提交编辑结果并关闭活动
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!name.getText().toString().isEmpty()) {
                    habit.setName(name.getText().toString());
                    habit.setL_name(label.getText().toString());
                    habit.setRemark(remark.getText().toString());
                    habit.save();
                    finish();
                }
            }
        });
        //删除记录并关闭活动
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                habit.delete();
                finish();
            }
        });
    }

    /**
     * initialize
     * 初始化
     * 将获取到的个人习惯记录展示于界面中
     */
    void initialize() {
        name.setText(habit.getName());
        String s = "";
        for (int x = 0; x < habit.getDifficulty(); x++) {
            s = s + "☆";
        }
        difficulty.setText(s);
        label.setText(habit.getL_name());
        positive.setText(habit.getPositive()+"");
        negative.setText(habit.getNegative()+"");
        remark.setText(habit.getRemark());
        timestamp.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(habit.getTimestamp()));
    }
}