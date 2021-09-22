package hlccd.regular.Blog;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hlccd.regular.R;

public class Blog_join_adapter extends RecyclerView.Adapter<Blog_join_adapter.ViewHolder> {
    public List<String> circles;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView         name;
        ConstraintLayout attention;

        public ViewHolder(View view) {
            super(view);
            name      = view.findViewById(R.id.name);
            attention = view.findViewById(R.id.attention);
        }
    }

    public Blog_join_adapter(List<String> circles) {
        this.circles = circles;
    }

    @Override
    public Blog_join_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blog_join, parent, false);
        return new Blog_join_adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Blog_join_adapter.ViewHolder holder, int position) {
        String circle = circles.get(position);
        holder.name.setText(circle);
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Blog_circle.theme=circle;
                v.getContext().startActivity(new Intent(v.getContext(),Blog_circle.class));
            }
        });
        holder.attention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "关注该圈子", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return circles.size();
    }
}