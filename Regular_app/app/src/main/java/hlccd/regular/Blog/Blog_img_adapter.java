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
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import hlccd.regular.R;
import hlccd.regular.util.ImageShow;

public class Blog_img_adapter extends RecyclerView.Adapter<Blog_img_adapter.ViewHolder> {
    private Context      context;
    private List<String> urls;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        ImageView delete;

        public ViewHolder(View view) {
            super(view);
            img    = view.findViewById(R.id.img);
            delete = view.findViewById(R.id.delete);
        }
    }

    public Blog_img_adapter(Context context, List<String> urls) {
        this.context = context;
        this.urls    = urls;
    }

    @Override
    public Blog_img_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blog_img, parent, false);
        return new Blog_img_adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Blog_img_adapter.ViewHolder holder, int position) {
        String url = urls.get(position);
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageShow.files.clear();
                ImageShow.urls = urls;
                ImageShow.idx  = position;
                ((Activity) context).startActivity(new Intent(context, ImageShow.class));
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bmp = getURLimage(url);
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (bmp == null) {
                            holder.img.setImageResource(R.drawable.app_icon);
                        } else {
                            holder.img.setImageBitmap(bmp);
                        }
                    }
                });
            }
        }).start();
        holder.delete.setVisibility(View.GONE);
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
        return urls.size();
    }
}

