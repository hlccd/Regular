package hlccd.regular.Blog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;

import hlccd.regular.R;

import static org.litepal.LitePalApplication.getContext;

public class Blog_data_adapter extends RecyclerView.Adapter<Blog_data_adapter.ViewHolder> {
    private Context         context;
    private List<Blog_data> blogs;
    private boolean         canSkip;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout layout;
        ImageView        portrait;
        TextView         name;
        RecyclerView     img;
        TextView         timestamp;
        ImageView        close;
        TextView         message;
        RecyclerView     circles;
        TextView         endorse;
        TextView         comment;

        public ViewHolder(View view) {
            super(view);
            layout    = view.findViewById(R.id.layout);
            portrait  = view.findViewById(R.id.portrait);
            name      = view.findViewById(R.id.name);
            img       = view.findViewById(R.id.img);
            timestamp = view.findViewById(R.id.timestamp);
            close     = view.findViewById(R.id.close);
            message   = view.findViewById(R.id.message);
            circles   = view.findViewById(R.id.circles);
            endorse   = view.findViewById(R.id.endorse);
            comment   = view.findViewById(R.id.comment);
        }
    }

    public Blog_data_adapter(Context context, List<Blog_data> blogs, boolean canSkip) {
        this.context = context;
        this.blogs   = blogs;
        this.canSkip = canSkip;
    }

    @Override
    public Blog_data_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blog_data, parent, false);
        return new Blog_data_adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Blog_data_adapter.ViewHolder holder, int position) {
        Blog_data blog = blogs.get(position);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Blog_details.blog=blog;
                v.getContext().startActivity(new Intent(v.getContext(),Blog_details.class));
            }
        });
        holder.name.setText(blog.getName());
        holder.timestamp.setText((new SimpleDateFormat("yyyy-MM-dd").format(blog.getTimestamp())));
        holder.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blogs.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, getItemCount());
            }
        });
        holder.message.setText("        " + blog.getMessage());

        LinearLayoutManager layoutManager;
        Blog_circle_adapter adapter;
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);//设置为横向排列
        holder.circles.setLayoutManager(layoutManager);
        adapter = new Blog_circle_adapter(blog.getCircles(), canSkip);
        holder.circles.setAdapter(adapter);

        holder.endorse.setText("    " + blog.getEndorse() + "   ");
        holder.comment.setText("    " + blog.getComment() + "   ");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bmp = getURLimage(blog.getPortrait());
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (bmp == null) {
                            holder.portrait.setImageResource(R.mipmap.my_falls_blue);
                        } else {
                            holder.portrait.setImageBitmap(bmp);
                        }
                    }
                });
            }
        }).start();

        LinearLayoutManager LM;
        Blog_img_adapter    ImgAdapter;
        LM = new LinearLayoutManager(getContext());
        LM.setOrientation(LinearLayoutManager.HORIZONTAL);//设置为横向排列
        holder.img.setLayoutManager(LM);
        ImgAdapter = new Blog_img_adapter(context, blog.getImg());
        holder.img.setAdapter(ImgAdapter);

    }

    public Bitmap getURLimage(String url) {
        Bitmap bmp = null;
        try {
            URL myurl = new URL(url);
            // 获得连接
            HttpURLConnection conn = (HttpURLConnection) myurl.openConnection();
            conn.setConnectTimeout(6000);//设置超时
            conn.setDoInput(true);
            conn.setUseCaches(false);//不缓存
            conn.connect();
            InputStream is = conn.getInputStream();//获得图片的数据流
            bmp = BitmapFactory.decodeStream(is);//读取图像数据
            //读取文本数据
            //byte[] buffer = new byte[100];
            //inputStream.read(buffer);
            //text = new String(buffer);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bmp;
    }

    @Override
    public int getItemCount() {
        return blogs.size();
    }
}
