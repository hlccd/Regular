package hlccd.regular.Friend;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import hlccd.regular.Homepage;
import hlccd.regular.R;
import hlccd.regular.User.User;

import android.content.Intent;
import android.os.Bundle;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Friend_info extends AppCompatActivity {
    private RecyclerView             RV;
    private List<Friend_info_data>   infos = new ArrayList<>();
    private LinearLayoutManager      layoutManager;
    private Friend_info_data_adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_info);
        RV = findViewById(R.id.RV);
        initialize();
    }

    private void initialize() {
        infos = LitePal.where("master=?", User.getUserId() + "").find(Friend_info_data.class);
        Collections.sort(infos);
        layoutManager = new LinearLayoutManager(Friend_info.this);
        RV.setLayoutManager(layoutManager);
        adapter = new Friend_info_data_adapter(infos);
        RV.setAdapter(adapter);
    }
}