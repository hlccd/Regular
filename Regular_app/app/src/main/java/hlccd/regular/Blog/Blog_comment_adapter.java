package hlccd.regular.Blog;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;

import hlccd.regular.R;


public class Blog_comment_adapter extends RecyclerView.Adapter<Blog_comment_adapter.ViewHolder> {
    private Context            context;
    private List<Blog_comment> comments;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView    portrait;
        TextView     name;
        TextView     timestamp;
        ImageView    endorse;
        TextView     endorse_text;
        TextView     message;
        RecyclerView RV;

        public ViewHolder(View view) {
            super(view);
            portrait     = view.findViewById(R.id.portrait);
            name         = view.findViewById(R.id.name);
            timestamp    = view.findViewById(R.id.timestamp);
            endorse      = view.findViewById(R.id.endorse);
            endorse_text = view.findViewById(R.id.endorse_text);
            message      = view.findViewById(R.id.message);
            RV           = view.findViewById(R.id.RV);
        }
    }

    public Blog_comment_adapter(Context context, List<Blog_comment> comments) {
        this.context  = context;
        this.comments = comments;
    }

    @Override
    public Blog_comment_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blog_comment, parent, false);
        return new Blog_comment_adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Blog_comment_adapter.ViewHolder holder, int position) {
        Blog_comment comment = comments.get(position);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bmp = getURLimage(comment.getPortrait());
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

        holder.name.setText(comment.getName());
        holder.timestamp.setText((new SimpleDateFormat("yyyy-MM-dd").format(comment.getTimestamp())));
        holder.endorse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "点赞", Toast.LENGTH_SHORT).show();
            }
        });
        holder.endorse_text.setText("    " + comment.getEndorse() + "   ");
        holder.message.setText(comment.getMessage());
        LinearLayoutManager LM      = new LinearLayoutManager(context);
        Blog_reply_adapter  adapter = new Blog_reply_adapter(context, comment.getReplies());
        holder.RV.setLayoutManager(LM);
        holder.RV.setNestedScrollingEnabled(false);
        holder.RV.setAdapter(adapter);
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
        return comments.size();
    }
}
