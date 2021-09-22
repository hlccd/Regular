package hlccd.regular.My;
/**
 * 反馈意见活动
 * 该活动用于给用户提交意见建议
 *
 * 暂未实现对后端的连接，无法提交反馈（后续添加）
 *
 * @author hlccd 2020.6.19
 * @version 1.0
 */
import androidx.appcompat.app.AppCompatActivity;

import hlccd.regular.R;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class My_opinion extends AppCompatActivity {
    private EditText title;
    private TextView type;
    private Spinner  spinner;
    private EditText content;
    private EditText relation;
    private Button   submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_opinion);
        title    = findViewById(R.id.my_opinion_title);
        type     = findViewById(R.id.my_opinion_type);
        spinner  = findViewById(R.id.my_opinion_spinner);
        content  = findViewById(R.id.my_opinion_content);
        relation = findViewById(R.id.my_opinion_relation);
        submit   = findViewById(R.id.my_opinion_submit);
        String ss[] = {"投诉", "建议"};
        //spinner的适配器
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.spinner_item, ss);
        spinner.setAdapter(adapter);
        //对spinner进行监听,监听其中的每一个item
        //点击将会修改反馈类型
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type.setText(ss[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //提交反馈，由于未连接后端暂时存在问题
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}