package hlccd.regular.Project;
/**
 * 日常任务记录适配器
 * 该适配器主要为了适配每一条日常任务记录并展示
 *
 * @author hlccd 2020.6.19
 * @version 1.0
 */

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hlccd.regular.Homepage;
import hlccd.regular.R;
import hlccd.regular.Regular.Regular;

public class Project_task_data_adapter extends RecyclerView.Adapter<Project_task_data_adapter.ViewHolder> {
    public Homepage                homepage;
    public List<Project_task_data> tasks;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout layout;
        TextView         name;
        TextView         difficulty;
        TextView         average;
        TextView         begin;

        public ViewHolder(View view) {
            super(view);
            layout     = view.findViewById(R.id.project_task_data_layout);
            name       = view.findViewById(R.id.project_task_data_name);
            difficulty = view.findViewById(R.id.project_task_data_difficulty);
            average    = view.findViewById(R.id.project_task_data_average);
            begin      = view.findViewById(R.id.project_task_data_begin);
        }
    }

    public Project_task_data_adapter(Homepage homepage, List<Project_task_data> tasks) {
        this.homepage = homepage;
        this.tasks    = tasks;
    }

    @Override
    public Project_task_data_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.project_task_data, parent, false);
        return new Project_task_data_adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Project_task_data_adapter.ViewHolder holder, int position) {
        //获取其中一个日常任务记录
        Project_task_data task = tasks.get(position);
        holder.name.setText(task.getName());
        String tmp = "";
        for (int x = 0; x < task.getDifficulty(); x++) {
            tmp += "☆";
        }
        holder.difficulty.setText(tmp);
        //若从未执行则展示0min平均执行时间
        //否则展示对应的平均执行时间（min）
        if (task.getNumber() == 0) {
            holder.average.setText("0min");
        } else {
            holder.average.setText((task.getDuration() / 60000) / task.getNumber() + "min");
        }
        //跳转到计时活动
        holder.begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Regular.timestamp = String.valueOf(task.getTimestamp());
                Regular.type      = "task";
                homepage.toRegular();
            }
        });
        //跳转到日常任务详情编辑活动
        //同时将该记录数据传送过去
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(view.getContext(), Project_task_edit.class);
                intent.putExtra("Project_task_edit", String.valueOf(task.getTimestamp()));
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }
}

