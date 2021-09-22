package hlccd.regular.Regular;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;

import hlccd.regular.R;

public class Regular_data_adapter_adapter extends RecyclerView.Adapter<Regular_data_adapter_adapter.ViewHolder> {
    public List<Regular_data> regulars;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout layout;
        TextView         name;
        TextView         difficulty;
        TextView         duration;

        public ViewHolder(View view) {
            super(view);
            layout     = view.findViewById(R.id.regular_data_layout);
            name       = view.findViewById(R.id.regular_data_name);
            difficulty = view.findViewById(R.id.regular_data_difficulty);
            duration   = view.findViewById(R.id.regular_data_duration);
        }
    }

    public Regular_data_adapter_adapter(List<Regular_data> regulars) {
        this.regulars = regulars;
    }

    @Override
    public Regular_data_adapter_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.regular_data, parent, false);
        return new Regular_data_adapter_adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Regular_data_adapter_adapter.ViewHolder holder, int position) {
        //获取其中一个日常任务记录
        Regular_data regular = regulars.get(position);
        holder.name.setText(regular.getName());
        String tmp = "";
        for (int x = 0; x < regular.getDifficulty(); x++) {
            tmp += "☆";
        }
        holder.difficulty.setText(tmp);
        holder.duration.setText(regular.getDuration()/(60*1000)+"分钟");
    }

    @Override
    public int getItemCount() {
        return regulars.size();
    }
}
