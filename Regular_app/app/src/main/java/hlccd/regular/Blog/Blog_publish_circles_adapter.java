package hlccd.regular.Blog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import hlccd.regular.R;

public class Blog_publish_circles_adapter extends RecyclerView.Adapter<Blog_publish_circles_adapter.ViewHolder> {
    private Context       context;
    private List<String>  circles;
    private List<Boolean> select = new ArrayList<>();

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView circle;

        public ViewHolder(View view) {
            super(view);
            circle = view.findViewById(R.id.circle);
        }
    }

    public Blog_publish_circles_adapter(Context context, List<String> circles) {
        this.context = context;
        this.circles = circles;
        for (int i = 0; i < circles.size(); i++) {
            select.add(false);
        }
    }

    @Override
    public Blog_publish_circles_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blog_circles, parent, false);
        return new Blog_publish_circles_adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Blog_publish_circles_adapter.ViewHolder holder, int position) {
        String circle = circles.get(position);
        holder.circle.setText("#" + circle);
        if (select.get(position)) {
            holder.circle.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.secondary_circle_bead));
        } else {
            holder.circle.setBackgroundDrawable(context.getResources().getDrawable(R.color.background));
        }
        holder.circle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (select.get(position)) {
                    select.set(position, false);
                } else {
                    select.set(position, true);
                }
                notifyItemChanged(position);
            }
        });
    }

    public List<String> getSelect() {
        List<String> ss = new ArrayList<>();
        for (int i = 0; i < select.size(); i++) {
            if (select.get(i)) {
                ss.add(circles.get(i));
            }
        }
        return ss;
    }

    @Override
    public int getItemCount() {
        return circles.size();
    }
}

