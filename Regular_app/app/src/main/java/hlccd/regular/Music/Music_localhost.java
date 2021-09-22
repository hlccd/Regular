package hlccd.regular.Music;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import java.util.ArrayList;
import java.util.List;

import hlccd.regular.Friend.Friend_friend_data;
import hlccd.regular.R;

public class Music_localhost extends Fragment {
    private SwipeRefreshLayout SR;
    private RecyclerView       RV;
    private List<Song>          songs = new ArrayList<>();
    private LinearLayoutManager layoutManager;
    private SongAdapter         adapter;

    @RequiresApi (api = Build.VERSION_CODES.R)
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        SR        = view.findViewById(R.id.SR);
        RV        = view.findViewById(R.id.RV);
        SR.setColorSchemeResources(R.color.mainColor);
        SR.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initialize();
                SR.setRefreshing(false);
            }
        });
        initialize();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.music_localhost, container, false);
        return view;
    }

    @RequiresApi (api = Build.VERSION_CODES.R)
    @Override
    public void onResume() {
        super.onResume();
        initialize();
    }

    @RequiresApi (api = Build.VERSION_CODES.R)
    private void initialize() {
        songs         = MusicUtils.getMusicData(getContext());
        layoutManager = new LinearLayoutManager(getContext());
        RV.setLayoutManager(layoutManager);
        adapter = new SongAdapter(getActivity(), songs);
        RV.setAdapter(adapter);
    }
}
