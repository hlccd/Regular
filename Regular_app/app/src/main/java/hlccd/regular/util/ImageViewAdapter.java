package hlccd.regular.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import hlccd.regular.R;


public class ImageViewAdapter extends RecyclerView.Adapter<ImageViewAdapter.ViewHolder> {
    private Context      context;
    private List<File>   files;
    private List<String> urls;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;

        public ViewHolder(View view) {
            super(view);
            img = view.findViewById(R.id.img);
        }
    }

    public ImageViewAdapter(Context context, List<File> files, List<String> urls) {
        this.context = context;
        this.files   = files;
        this.urls    = urls;
    }

    @Override
    public ImageViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_show_data, parent, false);
        return new ImageViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageViewAdapter.ViewHolder holder, int position) {
        if (files.size() > 0) {
            File file = files.get(position);
            holder.img.setImageURI(Uri.fromFile(file));
        } else if (urls.size() > 0) {
            String url = urls.get(position);
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
        }

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
        if (files.size() > 0) {
            return files.size();
        }
        if (urls.size() > 0) {
            return urls.size();
        }
        return 0;
    }
}

