package hlccd.regular.Blog;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import hlccd.regular.R;

public class Blog_search extends AppCompatActivity {
    private EditText            search;
    private TextView            send;
    private RecyclerView        RV;
    private List<Blog_data>     blogs = new ArrayList<>();
    private LinearLayoutManager layoutManager;
    private Blog_data_adapter   adapter;
    private List<String>        ss    = new Blog_data().getCircleList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blog_search);
        search = findViewById(R.id.search);
        send   = findViewById(R.id.send);
        RV     = findViewById(R.id.RV);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = search.getText().toString();
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
                    blog.setName("search");
                    blog.setImg(img);
                    blog.setTimestamp(System.currentTimeMillis());
                    blog.setMessage(message);
                    blog.setComment(Long.valueOf(i));
                    blog.setEndorse(Long.valueOf(i));
                    blog.setCircles(ss);
                    blogs.add(blog);
                }
                layoutManager = new LinearLayoutManager(Blog_search.this);
                RV.setLayoutManager(layoutManager);
                adapter = new Blog_data_adapter(Blog_search.this, blogs, true);
                RV.setAdapter(adapter);
            }
        });
    }
}