package hlccd.regular.Project;
/**
 * 日常任务编辑活动
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

public class Project_task_edit extends AppCompatActivity {
    private Project_task_data task;
    private EditText          name;
    private ImageButton       minus;
    private TextView          difficulty;
    private ImageButton       plus;
    private EditText          label;
    private TextView          number;
    private TextView          duration;
    private EditText          remark;
    private TextView          timestamp;
    private Button            edit;
    private Button            delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.project_task_edit);
        Intent intent = getIntent();
        String s      = ((Intent) intent).getStringExtra("Project_task_edit");
        task = LitePal.where("timestamp=?", s).find(Project_task_data.class).get(0);


        name       = findViewById(R.id.project_task_edit_name);
        minus      = findViewById(R.id.project_task_edit_minus);
        difficulty = findViewById(R.id.project_task_edit_difficulty);
        plus       = findViewById(R.id.project_task_edit_plus);
        label      = findViewById(R.id.project_task_edit_label);
        number     = findViewById(R.id.project_task_edit_number);
        duration   = findViewById(R.id.project_task_edit_duration);
        remark     = findViewById(R.id.project_task_edit_remark);
        timestamp  = findViewById(R.id.project_task_edit_timestamp);
        edit       = findViewById(R.id.project_task_edit_edit);
        delete     = findViewById(R.id.project_task_edit_delete);

        initialize();
        //增加难度
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (task.getDifficulty() <= 4) {
                    task.setDifficulty(task.getDifficulty() + 1);
                }
                String s = "";
                for (int x = 0; x < task.getDifficulty(); x++) {
                    s = s + "☆";
                }
                difficulty.setText(s);
            }
        });
        //减少难度
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (task.getDifficulty() >= 2) {
                    task.setDifficulty(task.getDifficulty() - 1);
                }
                String s = "";
                for (int x = 0; x < task.getDifficulty(); x++) {
                    s = s + "☆";
                }
                difficulty.setText(s);
            }
        });
        //删除该记录
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task.delete();
                finish();
            }
        });
        //提交修改结果
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!name.getText().toString().isEmpty()) {
                    task.setName(name.getText().toString());
                    task.setL_name(label.getText().toString());
                    task.setRemark(remark.getText().toString());
                    task.save();
                    finish();
                }
            }
        });
    }

    /**
     * initialize
     * 初始化
     * 将获取到的记录展示于界面中
     */
    void initialize() {
        name.setText(task.getName());
        String s = "";
        for (int x = 0; x < task.getDifficulty(); x++) {
            s = s + "☆";
        }
        difficulty.setText(s);
        label.setText(task.getL_name());
        number.setText(task.getNumber()+"次");
        duration.setText(task.getDuration()/60000+"min");
        remark.setText(task.getRemark());
        timestamp.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(task.getTimestamp()));
    }
}