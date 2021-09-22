package hlccd.regular.Project;
/**
 * 规划日常任务界面碎片
 * 该界面中输入名称后点击添加即可新增日常任务
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

public class Project_task extends Fragment {
    private EditText     edit;
    private ImageView    add;
    private RecyclerView RV;
    private View         view;

    private List<Project_task_data>   tasks = new ArrayList<>();
    private LinearLayoutManager       layoutManager;
    private Project_task_data_adapter adapter;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        this.view = view;
        edit      = view.findViewById(R.id.project_task_edit);
        add       = view.findViewById(R.id.project_task_add);
        RV        = view.findViewById(R.id.project_task_RV);
        initialize();
        //新增日常任务
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edit.getText().toString();
                if (!name.isEmpty()) {
                    Project_task_data task = new Project_task_data();
                    task.setName(name);
                    task.setU_id(User.getUserId());
                    task.setG_id(0L);
                    task.setL_name("");
                    task.setDifficulty(1);
                    task.setDuration(0L);
                    task.setNumber(0L);
                    task.setRemark("");
                    task.setTimestamp(System.currentTimeMillis());
                    task.save();
                    edit.setText("");
                    onResume();
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.project_task, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initialize();
    }

    /**
     * initialize
     * 初始化数据
     * 展示该用户的全部的日常任务数据
     * 同时绑定适配器
     */
    private void initialize() {
        List<Project_task_data> all = LitePal.findAll(Project_task_data.class);
        tasks = new ArrayList<>();
        for (int x = 0; x < all.size(); x++) {
            if (Objects.equals(all.get(x).getU_id(), User.getUserId())) {
                tasks.add(all.get(x));
            }
        }
        Collections.sort(tasks);
        layoutManager = new LinearLayoutManager(getContext());
        RV.setLayoutManager(layoutManager);
        adapter = new Project_task_data_adapter((Homepage) getActivity(), tasks);
        RV.setAdapter(adapter);
    }
}
