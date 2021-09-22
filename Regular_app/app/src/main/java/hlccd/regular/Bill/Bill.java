package hlccd.regular.Bill;
/**
 * 记账子界面碎片
 * 该界面包含两个子界面
 * 分别是记录界面(默认)和预算界面
 * 不同界面分别和RadioButton分别绑定,滑动界面或点击按钮会进行同步修改
 *
 * @author hlccd 2020.6.5
 * @version 1.0
 */

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import hlccd.regular.R;
import hlccd.regular.util.VP_adapter;

public class Bill extends Fragment implements RadioGroup.OnCheckedChangeListener{
    private ViewPager2 VP;
    private RadioGroup RG;
    private View view;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        this.view=view;
        VP = (ViewPager2)view.findViewById(R.id.VP);
        RG = (RadioGroup)view.findViewById(R.id.RG);
        VP_adapter adapter=new VP_adapter(getActivity());
        adapter.addFragment(new Bill_record());//添加记录子界面碎片
        adapter.addFragment(new Bill_budget());//添加预算子界面碎片
        VP.setAdapter(adapter);
        VP.setCurrentItem(0);//默认第0个添加的即记录子界面碎片为该界面启动时展示的首界面
        RG.setOnCheckedChangeListener(this);
        VP.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                /**
                 * 将RadioGroup的各个按钮与ViewPager2中的碎片进行相互绑定
                 * 0号按钮绑定记录字界面碎片
                 * 1号按钮绑定预算界面碎片
                 * 滑动更换碎片的时同时切换处于点击状态的RadioButton
                 */
                switch (position) {
                    case 0:
                        ((RadioButton) view.findViewById(R.id.record)).setChecked(true);
                        break;
                    case 1:
                        ((RadioButton) view.findViewById(R.id.budget)).setChecked(true);
                        break;
                }
            }
        });
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view =inflater.inflate(R.layout.bill,container,false);
        return view;
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
            case R.id.record:
                VP.setCurrentItem(0);
                break;
            case R.id.budget:
                VP.setCurrentItem(1);
                break;
        }
    }
}
