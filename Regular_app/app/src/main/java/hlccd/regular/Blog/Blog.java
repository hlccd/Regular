package hlccd.regular.Blog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import hlccd.regular.Friend.Friend_friend_data;
import hlccd.regular.R;

public class Blog extends AppCompatActivity {
    private DrawerLayout      DL;
    private TextView          catalogue;
    private TextView          search;
    private TextView          publish;
    private CoordinatorLayout circle;

    private RecyclerView        join;
    private List<String>        circles = new Blog_data().getCircleList();
    private LinearLayoutManager LM;
    private Blog_join_adapter   joinAdapter;

    private SwipeRefreshLayout SR;
    private RecyclerView       RV;
    private List<Blog_data>     blogs   = new ArrayList<>();
    private LinearLayoutManager layoutManager;
    private Blog_data_adapter   adapter;
    private List<String>        ss      = new Blog_data().getCircleList();
    private String              message = "fakjndaktsdblnkasftkgbdfml;ktgbemllskj,kfcmladykwb,aslaufk,sflk,g";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blog);
        DL        = findViewById(R.id.drawer);
        join      = findViewById(R.id.join);
        catalogue = findViewById(R.id.catalogue);
        search    = findViewById(R.id.search);
        publish   = findViewById(R.id.publish);
        circle    = findViewById(R.id.circle);
        SR    = findViewById(R.id.SR);
        RV        = findViewById(R.id.RV);

        SR.setColorSchemeResources(R.color.mainColor);
        SR.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
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

                        layoutManager = new LinearLayoutManager(Blog.this);
                        RV.setLayoutManager(layoutManager);
                        adapter = new Blog_data_adapter(Blog.this, blogs, true);
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

        layoutManager = new LinearLayoutManager(Blog.this);
        RV.setLayoutManager(layoutManager);
        adapter = new Blog_data_adapter(this, blogs,true);
        RV.setAdapter(adapter);

        LM = new LinearLayoutManager(Blog.this);
        join.setLayoutManager(LM);
        joinAdapter = new Blog_join_adapter(circles);
        join.setAdapter(joinAdapter);

        catalogue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Blog.this, "目录", Toast.LENGTH_SHORT).show();
            }
        });
        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Blog.this, Blog_publish.class));
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Blog.this, Blog_search.class));
            }
        });
        circle.setOnClickListener(new View.OnClickListener() {
            @SuppressLint ("WrongConstant")
            @Override
            public void onClick(View v) {
                DL.open();
            }
        });
    }
}