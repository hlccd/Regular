package hlccd.regular.Regular;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hlccd.regular.Homepage;
import hlccd.regular.Project.Project_task_data;
import hlccd.regular.Project.Project_task_data_adapter;
import hlccd.regular.Project.Project_task_edit;
import hlccd.regular.R;

import static org.litepal.LitePalApplication.getContext;

public class Regular_data_adapter extends RecyclerView.Adapter<Regular_data_adapter.ViewHolder> {
    private String[]                 ss;
    private List<List<Regular_data>> regularss;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView     time;
        TextView     duration;
        RecyclerView RV;

        public ViewHolder(View view) {
            super(view);
            time     = view.findViewById(R.id.time);
            duration = view.findViewById(R.id.duration);
            RV       = view.findViewById(R.id.RV);
        }
    }

    public Regular_data_adapter(String[] ss, List<List<Regular_data>> regularss) {
        this.ss        = ss;
        this.regularss = regularss;
    }

    @Override
    public Regular_data_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.regular_data_adapter, parent, false);
        return new Regular_data_adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Regular_data_adapter.ViewHolder holder, int position) {
        //获取其中一个日常任务记录
        List<Regular_data>           regulars = regularss.get(position);
        holder.time.setText(ss[position]);
        Long duration=0L;
        for (int x=0;x<regulars.size();x++){
            duration+=regulars.get(x).getDuration();
        }
        holder.duration.setText(duration/(60*1000)+"min");

        LinearLayoutManager          layoutManager;
        Regular_data_adapter_adapter adapter;
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);//设置为横向排列
        holder.RV.setLayoutManager(layoutManager);
        adapter = new Regular_data_adapter_adapter(regulars);
        holder.RV.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return regularss.size();
    }
}