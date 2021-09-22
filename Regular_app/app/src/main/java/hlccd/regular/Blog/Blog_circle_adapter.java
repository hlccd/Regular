package hlccd.regular.Blog;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hlccd.regular.R;

public class Blog_circle_adapter extends RecyclerView.Adapter<Blog_circle_adapter.ViewHolder> {
    public  List<String> circles;
    private boolean      canSkip;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView circle;

        public ViewHolder(View view) {
            super(view);
            circle = view.findViewById(R.id.circle);
        }
    }

    public Blog_circle_adapter(List<String> circles, boolean canSkip) {
        this.circles = circles;
        this.canSkip = canSkip;
    }

    @Override
    public Blog_circle_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blog_circles, parent, false);
        return new Blog_circle_adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Blog_circle_adapter.ViewHolder holder, int position) {
        String circle = circles.get(position);
        holder.circle.setText("#" + circle);
        if (canSkip){
            holder.circle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Blog_circle.theme = circle;
                    v.getContext().startActivity(new Intent(v.getContext(), Blog_circle.class));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return circles.size();
    }
}
