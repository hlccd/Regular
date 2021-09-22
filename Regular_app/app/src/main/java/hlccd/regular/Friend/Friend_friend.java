package hlccd.regular.Friend;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import hlccd.regular.R;
import hlccd.regular.User.User;

public class Friend_friend extends Fragment {
    private View                       view;
    private SwipeRefreshLayout         SR;
    private RecyclerView               RV;
    private List<Friend_friend_data>   friends = new ArrayList<>();
    private LinearLayoutManager        layoutManager;
    private Friend_friend_data_adapter adapter;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        this.view = view;
        SR        = view.findViewById(R.id.SR);
        RV        = view.findViewById(R.id.RV);
        SR.setColorSchemeResources(R.color.mainColor);
        SR.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Friend_friend_data().refresh();
                new Handler().postDelayed(new Runnable(){
                    public void run(){
                        initialize();
                        SR.setRefreshing(false);
                    }
                },1000);
            }
        });
        initialize();
    }

    private void initialize() {
        friends = LitePal.where("master=?", User.getUserId() + "").find(Friend_friend_data.class);
        Collections.sort(friends);
        layoutManager = new LinearLayoutManager(getContext());
        RV.setLayoutManager(layoutManager);
        adapter = new Friend_friend_data_adapter(friends);
        RV.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        initialize();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.friend_friend, container, false);
    }
}