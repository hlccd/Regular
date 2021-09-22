package hlccd.regular.Friend;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import org.litepal.LitePal;

import java.util.List;

import hlccd.regular.R;
import hlccd.regular.User.User;
import hlccd.regular.util.VP_adapter;

public class Friend extends Fragment implements RadioGroup.OnCheckedChangeListener {

    private View       view;
    private TextView   search;
    private TextView   inform;
    private ImageView  notice;
    private RadioGroup RG;
    private ViewPager2 VP;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        this.view   = view;
        search      = view.findViewById(R.id.search);
        inform      = view.findViewById(R.id.inform);
        notice      = view.findViewById(R.id.notice);
        RG          = view.findViewById(R.id.RG);
        VP          = view.findViewById(R.id.VP);
        IntentFilter intentFilter =new IntentFilter();
        intentFilter.addAction("info");
        getActivity().registerReceiver( new broadcastReceiver(),intentFilter);
        notice();
        inform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Friend_info.class));
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Friend_search.class));
            }
        });

        VP_adapter adapter = new VP_adapter(getActivity());
        adapter.addFragment(new Friend_message());//添加消息子界面碎片
        adapter.addFragment(new Friend_friend());//添加好友子界面碎片
        adapter.addFragment(new Friend_group());//添加群组子界面碎片
        VP.setAdapter(adapter);
        VP.setCurrentItem(0);//默认第0个添加的即消息子界面碎片为该界面启动时展示的首界面
        RG.setOnCheckedChangeListener(this);
        VP.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                /**
                 * 将RadioGroup的各个按钮与ViewPager2中的碎片进行相互绑定
                 * 0号按钮绑定消息字界面碎片
                 * 1号按钮绑定好友界面碎片
                 * 2号按钮绑定群组界面碎片
                 * 滑动更换碎片的时同时切换处于点击状态的RadioButton
                 */
                switch (position) {
                    case 0:
                        ((RadioButton) view.findViewById(R.id.message)).setChecked(true);
                        break;
                    case 1:
                        ((RadioButton) view.findViewById(R.id.friend)).setChecked(true);
                        break;
                    case 2:
                        ((RadioButton) view.findViewById(R.id.group)).setChecked(true);
                        break;
                }
            }
        });
    }
    private void notice(){
        List<Friend_info_data> infos= LitePal.where("master=?", User.getUserId() + "").find(Friend_info_data.class);
        notice.setVisibility(View.GONE);
        for (int i=0;i<infos.size();i++){
            if (!infos.get(i).getDispose()){
                notice.setVisibility(View.VISIBLE);
            }
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        notice();
    }
    private class  broadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            notice();
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.friend, container, false);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        /**
         * 将RadioGroup的各个按钮与ViewPager2中的碎片进行相互绑定
         * 记录界面碎片绑定0号按钮
         * 预算界面碎片绑定1号按钮
         * 点击RadioButton时切换当前展示Fragment
         */
        switch (checkedId) {
            case R.id.message:
                VP.setCurrentItem(0);
                break;
            case R.id.friend:
                VP.setCurrentItem(1);
                break;
            case R.id.group:
                VP.setCurrentItem(2);
                break;
        }
    }
}
