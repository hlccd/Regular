package hlccd.regular;
/**
 * 软件主页
 * 软件主页包含两个子界面
 * 分别是记账界面、规划子界面(默认)和个人详情界面,后续将会继续添加
 * 不同界面分别和RadioButton分别绑定,滑动界面或点击按钮会进行同步修改
 * 当前界面关闭ViewPager滑动,避免影响子界面中的ViewPager滑动从而导致异常
 *
 * @author hlccd
 * @version 1.0 2020.6.5
 * @version 2.0 2020.6.10 新增project规划子界面并设为默认,存在异常闪退问题，groupButton按照1012点击会闪退
 * @version 3.0 2021.7.14 新增regular执行子界面并设为默认,添加friend好友自界面,关闭ViewPager2滑动后闪退消失
 */

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import hlccd.regular.Bill.Bill;
import hlccd.regular.My.My;
import hlccd.regular.Project.*;
import hlccd.regular.Friend.Friend;
import hlccd.regular.Regular.Regular;
import hlccd.regular.util.VP_adapter;


public class Homepage extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
    public ViewPager2     VP;
    public RadioGroup     RG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);
        VP = findViewById(R.id.VP);
        RG = findViewById(R.id.RG);
        VP.setUserInputEnabled(false);//关闭父控件滑动，避免影响Fragment滑动

        VP_adapter adapter = new VP_adapter(this);
        adapter.addFragment(new Bill());//添加记账子界面碎片
        adapter.addFragment(new Project());//添加规划子界面碎片
        adapter.addFragment(new Regular());//添加执行子界面碎片
        adapter.addFragment(new Friend());//添加好友子界面碎片
        adapter.addFragment(new My());//添加个人详情子界面碎片
//        adapter.addFragment(new Bazaar());
        VP.setAdapter(adapter);
        VP.setCurrentItem(2);//默认第0个添加的即规划子界面碎片为该活动启动时展示的首界面
        RG.setOnCheckedChangeListener(this);
//        VP.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
//            @Override
//            public void onPageSelected(int position) {
//                super.onPageSelected(position);
//                /**
//                 * 将RadioGroup的各个按钮与ViewPager2中的碎片进行相互绑定
//                 * 0号按钮绑定记账字界面碎片
//                 * 1号按钮绑定规划记录界面碎片
//                 * 2号按钮绑定个人详情界面碎片
//                 * 滑动更换碎片的时同时切换处于点击状态的RadioButton
//                 * @deprecated 由于关闭了该界面的滑动更换, 故该功能暂时搁置
//                 */
//                switch (position) {
//                    case 0:
//                        ((RadioButton) findViewById(R.id.bill)).setChecked(true);
//                        break;
//                    case 1:
//                        ((RadioButton) findViewById(R.id.project)).setChecked(true);
//                        break;
//                    case 2:
//                        ((RadioButton) findViewById(R.id.regular)).setChecked(true);
//                        break;
//                    case 3:
//                        ((RadioButton) findViewById(R.id.friend)).setChecked(true);
//                        break;
//                    case 4:
//                        ((RadioButton) findViewById(R.id.my)).setChecked(true);
//                        break;
//                }
//            }
//        });
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        /**
         * 将RadioGroup的各个按钮与ViewPager2中的碎片进行相互绑定
         * 记账字界面碎片绑定0号按钮
         * 规划记录界面碎片绑定1号按钮
         * 执行计划界面碎片绑定2号按钮
         * 好友群组界面碎片绑定3号按钮
         * 个人详情界面碎片绑定4号按钮
         * 点击RadioButton时切换当前展示Fragment
         */
        switch (checkedId) {
            case R.id.bill:
                VP.setCurrentItem(0);
                break;
            case R.id.project:
                VP.setCurrentItem(1);
                break;
            case R.id.regular:
                VP.setCurrentItem(2);
                break;
            case R.id.friend:
                VP.setCurrentItem(3);
                break;
            case R.id.my:
                VP.setCurrentItem(4);
                break;
        }
    }
    public void toRegular(){
        VP.setCurrentItem(2);
        ((RadioButton) findViewById(R.id.regular)).setChecked(true);
    }
    @Override
    public void onResume() {
        super.onResume();
    }
}
