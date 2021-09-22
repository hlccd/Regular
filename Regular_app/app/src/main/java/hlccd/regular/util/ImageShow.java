package hlccd.regular.util;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import hlccd.regular.Blog.Blog_img_adapter;
import hlccd.regular.R;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.litepal.LitePalApplication.getContext;

public class ImageShow extends AppCompatActivity {
    public static List<File>   files = new ArrayList<>();
    public static List<String> urls  = new ArrayList<>();
    public static int          idx   = 0;
    private       RecyclerView RV;
    private       TextView     text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_show);
        RV   = findViewById(R.id.RV);
        text = findViewById(R.id.text);

        LinearLayoutManager LM;
        ImageViewAdapter    adapter;
        LM = new LinearLayoutManager(getContext());
        LM.setOrientation(LinearLayoutManager.HORIZONTAL);//设置为横向排列
        RV.setLayoutManager(LM);
        adapter = new ImageViewAdapter(this, files, urls);
        RV.setAdapter(adapter);
        RV.scrollToPosition(idx);

        RV.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager l = (LinearLayoutManager) recyclerView.getLayoutManager();
                idx = l.findFirstVisibleItemPosition();
                text.setText((idx + 1) + "/" + adapter.getItemCount());
            }
        });

        if (adapter.getItemCount() > 0) {
            text.setText((idx + 1) + "/" + adapter.getItemCount());
        } else{
            text.setVisibility(View.GONE);
        }
    }
}