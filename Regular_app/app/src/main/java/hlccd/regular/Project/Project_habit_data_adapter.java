package hlccd.regular.Project;
/**
 * 个人习惯记录适配器
 * 该适配器主要为了适配每一条个人习惯记录并展示
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

import hlccd.regular.Homepage;
import hlccd.regular.R;
import hlccd.regular.Regular.Regular;

import java.util.List;

public class Project_habit_data_adapter extends RecyclerView.Adapter<Project_habit_data_adapter.ViewHolder> {
    public Homepage                 homepage;
    public List<Project_habit_data> habits;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout layout;
        ImageView        positive;
        ImageView        negative;
        TextView         name;
        TextView         difficulty;
        TextView         point;

        public ViewHolder(View view) {
            super(view);
            layout     = view.findViewById(R.id.project_habit_data_layout);
            positive   = view.findViewById(R.id.project_habit_data_positive);
            negative   = view.findViewById(R.id.project_habit_data_negative);
            name       = view.findViewById(R.id.project_habit_data_name);
            difficulty = view.findViewById(R.id.project_habit_data_difficulty);
            point      = view.findViewById(R.id.project_habit_data_point);
        }
    }

    public Project_habit_data_adapter(Homepage homepage, List<Project_habit_data> habits) {
        this.homepage = homepage;
        this.habits  = habits;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.project_habit_data, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //获取其中一条个人习惯记录
        Project_habit_data habit = habits.get(position);
        holder.name.setText(habit.getName());
        String tmp = "";
        for (int x = 0; x < habit.getDifficulty(); x++) {
            tmp += "☆";
        }
        holder.difficulty.setText(tmp);
        if (habit.getPositive() < 0) {
            holder.point.setText("-/" + habit.getNegative() / 60000 + "min");
        } else if (habit.getNegative() < 0) {
            holder.point.setText(habit.getPositive() / 60000 + "min/-");
        } else {
            holder.point.setText(habit.getPositive() / 60000 + "min/" + habit.getNegative() / 60000 + "min");
        }
        //新增积极习惯完成记录
        holder.positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //当且仅当拥有积极性时才增加
                if (habit.getPositive() >= 0) {
                    Regular.timestamp =String.valueOf(habit.getTimestamp());
                    Regular.type      ="habit_positive";
                    homepage.toRegular();
                }
            }
        });
        //新增消极习惯完成记录
        holder.negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //当且仅当拥有消极性时才增加
                if (habit.getNegative() >= 0) {
                    Regular.timestamp =String.valueOf(habit.getTimestamp());
                    Regular.type      ="habit_negative";
                    homepage.toRegular();
                }
            }
        });
        //跳转到个人习惯详情修改页面
        //同时将该习惯信息传送过去
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(view.getContext(), Project_habit_edit.class);
                intent.putExtra("Project_habit_edit", String.valueOf(habit.getTimestamp()));
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return habits.size();
    }
}

