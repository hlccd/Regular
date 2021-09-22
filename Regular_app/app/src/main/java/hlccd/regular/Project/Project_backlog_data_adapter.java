package hlccd.regular.Project;
/**
 * 待办事项记录适配器
 * 该适配器主要为了适配每一条待办事项记录并展示
 *
 * @author hlccd 2020.6.19
 * @version 1.0
 */

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;

import hlccd.regular.Homepage;
import hlccd.regular.R;
import hlccd.regular.Regular.Regular;

public class Project_backlog_data_adapter extends RecyclerView.Adapter<Project_backlog_data_adapter.ViewHolder> {
    private Homepage                   homepage;
    private List<Project_backlog_data> backlogs;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout layout;
        TextView         name;
        TextView         difficulty;
        TextView         deadline;
        ImageView        finish;

        public ViewHolder(View view) {
            super(view);
            layout     = view.findViewById(R.id.project_backlog_data_layout);
            name       = view.findViewById(R.id.project_backlog_data_name);
            difficulty = view.findViewById(R.id.project_backlog_data_difficulty);
            deadline   = view.findViewById(R.id.project_backlog_data_deadline);
            finish     = view.findViewById(R.id.project_backlog_data_finish);
        }
    }

    public Project_backlog_data_adapter(Homepage homepage, List<Project_backlog_data> backlogs) {
        this.homepage = homepage;
        this.backlogs = backlogs;
    }

    @Override
    public Project_backlog_data_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.project_backlog_data, parent, false);
        return new Project_backlog_data_adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Project_backlog_data_adapter.ViewHolder holder, int position) {
        //获取其中一条待办事项
        Project_backlog_data backlog = backlogs.get(position);
        holder.name.setText(backlog.getName());
        String tmp = "";
        for (int x = 0; x < backlog.getDifficulty(); x++) {
            tmp += "☆";
        }
        holder.difficulty.setText(tmp);
        holder.deadline.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(backlog.getDeadline()));
        //完成该待办事项
        holder.finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Regular.timestamp=String.valueOf(backlog.getTimestamp());
                Regular.type="backlog";
                homepage.toRegular();
            }
        });
        //跳转至待办事项详情编辑界面
        //同时传递该待办事项数据
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(view.getContext(), Project_backlog_edit.class);
                intent.putExtra("Project_backlog_edit", String.valueOf(backlog.getTimestamp()));
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return backlogs.size();
    }
}
