package hlccd.regular.Project;
/**
 * 规划个人习惯界面碎片
 * 该界面中输入名称后点击添加即可新增个人习惯
 * 记录默认同时含有积极性和消极性
 * 若要修改可点击对应按钮取消对应属性
 *
 * @author hlccd 2020.6.19
 * @version 1.0
 */

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import hlccd.regular.Homepage;
import hlccd.regular.R;
import hlccd.regular.User.User;

public class Project_habit extends Fragment {
    private EditText     edit;
    private ImageView    add;
    private TextView     positiveText;
    private TextView     negativeText;
    private RecyclerView RV;

    private boolean                    isPositive;
    private boolean                    isNegative;
    private List<Project_habit_data>   habits = new ArrayList<>();
    private LinearLayoutManager        layoutManager;
    private Project_habit_data_adapter adapter;


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        edit         = view.findViewById(R.id.project_habit_edit);
        add          = view.findViewById(R.id.project_habit_add);
        positiveText = view.findViewById(R.id.project_habit_positive);
        negativeText = view.findViewById(R.id.project_habit_negative);
        RV           = view.findViewById(R.id.project_habit_RV);
        //设置为默认状态
        //即同时含有积极性和消极性
        isPositive   = true;
        isNegative   = true;
        positiveText.setText("☑积极习惯");
        negativeText.setText("☑消极习惯");
        initialize();
        //修改积极性的状态
        positiveText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPositive) {
                    isPositive = false;
                    positiveText.setText("▢积极习惯");
                } else {
                    isPositive = true;
                    positiveText.setText("☑积极习惯");
                }
            }
        });
        //修改积极性的状态
        negativeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNegative) {
                    isNegative = false;
                    negativeText.setText("▢消极习惯");
                } else {
                    isNegative = true;
                    negativeText.setText("☑消极习惯");
                }
            }
        });
        //添加个人习惯
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(isPositive || isNegative)){
                    Toast.makeText(getContext(),"无法添加无属性习惯",Toast.LENGTH_SHORT).show();
                    return;
                }
                String name = edit.getText().toString();
                //如果未输入名字则不予添加
                if (!name.isEmpty()) {
                    Project_habit_data habit = new Project_habit_data();
                    habit.setName(name);
                    habit.setU_id(User.getUserId());
                    habit.setL_name("");
                    habit.setDifficulty(1);
                    //若不含有某属性则将其值设为-1
                    if (isPositive) {
                        habit.setPositive(0L);
                    } else {
                        habit.setPositive(-1L);
                    }
                    if (isNegative) {
                        habit.setNegative(0L);
                    } else {
                        habit.setNegative(-1L);
                    }
                    habit.setRemark("");
                    habit.setTimestamp(System.currentTimeMillis());
                    //添加该习惯并刷新界面同时将名称编辑框制空
                    habit.save();
                    edit.setText("");
                    onResume();
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.project_habit, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initialize();
    }

    /**
     * initialize
     * 数据初始化
     * 将当前登录用户的所有个人习惯排序后进行展示
     * 同时进行适配器绑定，使得更新后界面能够同步修改
     */
    private void initialize() {
        List<Project_habit_data> all = LitePal.findAll(Project_habit_data.class);
        habits = new ArrayList<>();
        for (int x = 0; x < all.size(); x++) {
            if (Objects.equals(all.get(x).getU_id(), User.getUserId())) {
                habits.add(all.get(x));
            }
        }
        Collections.sort(habits);
        layoutManager = new LinearLayoutManager(getContext());
        RV.setLayoutManager(layoutManager);
        adapter = new Project_habit_data_adapter((Homepage) getActivity(), habits);
        RV.setAdapter(adapter);
    }
}
