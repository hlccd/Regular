package hlccd.regular.Blog;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import hlccd.regular.Friend.Friend_friend_data;
import hlccd.regular.R;

public class Blog_circle extends AppCompatActivity {
    public static String theme = "";

    private TextView           title;
    private ConstraintLayout   attention;
    private RadioGroup         RG;
    private SwipeRefreshLayout SR;

    private RecyclerView        RV;
    private List<Blog_data>     blogs   = new ArrayList<>();
    private LinearLayoutManager layoutManager;
    private Blog_data_adapter   adapter;
    private List<String>        ss      = new Blog_data().getCircleList();
    private String              message = "fakjndaktsdblnkasftkgbdfml;ktgbemllskj,kfcmladykwb,aslaufk,sflk,g";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blog_circle);
        title     = findViewById(R.id.title);
        attention = findViewById(R.id.attention);
        RG        = findViewById(R.id.RG);
        SR        = findViewById(R.id.SR);
        RV        = findViewById(R.id.RV);

        SR.setColorSchemeResources(R.color.mainColor);
        SR.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Friend_friend_data().refresh();
                new Handler().postDelayed(new Runnable(){
                    public void run(){
                        blogs.clear();
                        String       url = "https://img1.baidu.com/it/u=1617410225,2888947431&fm=26&fmt=auto&gp=0.jpg";
                        List<String> img = new ArrayList<>();
                        img.add(url);
                        img.add(url);
                        img.add(url);
                        img.add(url);
                        img.add(url);
                        for (int i = 0; i < 10; i++) {
                            Blog_data blog = new Blog_data();
                            blog.setPortrait(url);
                            blog.setName("new");
                            blog.setImg(img);
                            blog.setTimestamp(System.currentTimeMillis());
                            blog.setMessage(message);
                            blog.setComment(Long.valueOf(i));
                            blog.setEndorse(Long.valueOf(i));
                            blog.setCircles(ss);
                            blogs.add(blog);
                        }

                        layoutManager = new LinearLayoutManager(Blog_circle.this);
                        RV.setLayoutManager(layoutManager);
                        adapter = new Blog_data_adapter(Blog_circle.this, blogs, false);
                        RV.setAdapter(adapter);
                        SR.setRefreshing(false);
                    }
                },1000);
            }
        });

        blogs.clear();
        String       url = "https://img1.baidu.com/it/u=1617410225,2888947431&fm=26&fmt=auto&gp=0.jpg";
        List<String> img = new ArrayList<>();
        img.add(url);
        img.add(url);
        img.add(url);
        img.add(url);
        img.add(url);
        for (int i = 0; i < 10; i++) {
            Blog_data blog = new Blog_data();
            blog.setPortrait(url);
            blog.setName("hlccd");
            blog.setImg(img);
            blog.setTimestamp(System.currentTimeMillis());
            blog.setMessage(message);
            blog.setComment(Long.valueOf(i));
            blog.setEndorse(Long.valueOf(i));
            blog.setCircles(ss);
            blogs.add(blog);
        }

        layoutManager = new LinearLayoutManager(Blog_circle.this);
        RV.setLayoutManager(layoutManager);
        adapter = new Blog_data_adapter(Blog_circle.this, blogs, false);
        RV.setAdapter(adapter);

        title.setText(theme);
        attention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Blog_circle.this, "关注该圈子", Toast.LENGTH_SHORT).show();
            }
        });
        RG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.newest:
                        Toast.makeText(Blog_circle.this, "最新", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.hot:
                        Toast.makeText(Blog_circle.this, "热门", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }
}