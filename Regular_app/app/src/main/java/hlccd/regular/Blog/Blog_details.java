package hlccd.regular.Blog;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import hlccd.regular.R;

import static org.litepal.LitePalApplication.getContext;

public class Blog_details extends AppCompatActivity {
    public static Blog_data          blog     = new Blog_data();
    private       List<Blog_comment> comments = new ArrayList<>();

    private ImageView          portrait;
    private TextView           name;
    private RecyclerView       img;
    private TextView           timestamp;
    private ImageView          close;
    private TextView           message;
    private RecyclerView       circles;
    private TextView           endorse;
    private TextView           comment;
    private SwipeRefreshLayout SR;
    private RecyclerView       commentRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blog_details);
        portrait  = findViewById(R.id.portrait);
        name      = findViewById(R.id.name);
        img       = findViewById(R.id.img);
        timestamp = findViewById(R.id.timestamp);
        close     = findViewById(R.id.close);
        message   = findViewById(R.id.message);
        circles   = findViewById(R.id.circles);
        endorse   = findViewById(R.id.endorse);
        comment   = findViewById(R.id.comment);
        SR        = findViewById(R.id.SR);
        commentRV = findViewById(R.id.commentRV);

        initBlog();
        getComments();

    }

    private void initBlog() {
        name.setText(blog.getName());
        timestamp.setText((new SimpleDateFormat("yyyy-MM-dd").format(blog.getTimestamp())));
        close.setVisibility(View.GONE);
        message.setText("        " + blog.getMessage());

        LinearLayoutManager circleLM      = new LinearLayoutManager(getContext());
        Blog_circle_adapter CircleAdapter = new Blog_circle_adapter(blog.getCircles(), false);
        circleLM.setOrientation(LinearLayoutManager.HORIZONTAL);
        circles.setLayoutManager(circleLM);
        circles.setAdapter(CircleAdapter);

        endorse.setText("    " + blog.getEndorse() + "   ");
        endorse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Blog_details.this, "点赞", Toast.LENGTH_SHORT).show();
            }
        });
        comment.setText("    " + blog.getComment() + "   ");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bmp = getURLimage(blog.getPortrait());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (bmp == null) {
                            portrait.setImageResource(R.mipmap.my_falls_blue);
                        } else {
                            portrait.setImageBitmap(bmp);
                        }
                    }
                });
            }
        }).start();

        LinearLayoutManager LM         = new LinearLayoutManager(getContext());
        Blog_img_adapter    ImgAdapter = new Blog_img_adapter(Blog_details.this, blog.getImg());
        LM.setOrientation(LinearLayoutManager.HORIZONTAL);//设置为横向排列
        img.setLayoutManager(LM);
        img.setAdapter(ImgAdapter);
    }

    private void getComments() {
        List<Blog_comment> comments = new ArrayList<>();
        List<Blog_reply>   replies  = new ArrayList<>();
        for (Long i = 1L; i < 10; i++) {
            replies.add(new Blog_reply(blog.getPortrait(), "hlccd", i, "hlccd", i, System.currentTimeMillis(), "message", i));
            comments.add(new Blog_comment(blog.getPortrait(), "hlccd", i, System.currentTimeMillis(), "message", i, replies));
        }
        LinearLayoutManager  LM      = new LinearLayoutManager(getContext());
        Blog_comment_adapter adapter = new Blog_comment_adapter(Blog_details.this, comments);
        commentRV.setLayoutManager(LM);
        commentRV.setAdapter(adapter);
        SR.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        SR.setRefreshing(false);
                    }
                }, 1000);
            }
        });
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
}