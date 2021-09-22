package hlccd.regular.Blog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

import hlccd.regular.R;
import hlccd.regular.util.ImageShow;

public class Blog_publish_img_adapter extends RecyclerView.Adapter<Blog_publish_img_adapter.ViewHolder> {
    Context context;
    private List<File> files;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        ImageView delete;

        public ViewHolder(View view) {
            super(view);
            img    = view.findViewById(R.id.img);
            delete = view.findViewById(R.id.delete);
        }
    }

    public Blog_publish_img_adapter(Context context, List<File> files) {
        this.context = context;
        this.files   = files;
    }

    @Override
    public Blog_publish_img_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blog_img, parent, false);
        return new Blog_publish_img_adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Blog_publish_img_adapter.ViewHolder holder, int position) {
        File file = files.get(position);
        holder.img.setImageURI(Uri.fromFile(file));
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                files.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, getItemCount());
            }
        });
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageShow.files = files;
                ImageShow.urls.clear();
                ImageShow.idx = position;
                ((Activity) context).startActivity(new Intent(context, ImageShow.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    public List<File> getFiles() {
        return files;
    }
}
