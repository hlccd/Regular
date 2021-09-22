package hlccd.regular.Bill;
/**
 * 添加记账记录活动
 * 该界面包含两个子界面
 * 分别是添加消费记录子界面(默认)和添加收入记录子界面
 * 不同界面分别和RadioButton分别绑定,滑动界面或点击按钮会进行同步修改
 *
 * @author hlccd 2020.6.7
 * @version 1.0
 */
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import hlccd.regular.R;
import hlccd.regular.util.VP_adapter;

public class Bill_record_add extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
    private ViewPager2 VP;
    private RadioGroup RG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bill_record_add);
        VP = findViewById(R.id.bill_record_add_VP);
        RG = findViewById(R.id.bill_record_add_RG);
        VP_adapter adapter = new VP_adapter(this);
        adapter.addFragment(new Bill_record_add_consume());//添加增加消费记录子界面碎片
        adapter.addFragment(new Bill_record_add_income());//添加增加收入记录子界面碎片
        VP.setAdapter(adapter);
        VP.setCurrentItem(0);//默认第0个添加的即增加消费记录子界面碎片为该界面启动时展示的首界面
        RG.setOnCheckedChangeListener(this);
        VP.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                /**
                 * 将RadioGroup的各个按钮与ViewPager2中的碎片进行相互绑定
                 * 0号按钮绑定增加消费记录字界面碎片
                 * 1号按钮绑定增加收入记录界面碎片
                 * 滑动更换碎片的时同时切换处于点击状态的RadioButton
                 */
                switch (position) {
                    case 0:
                        ((RadioButton) findViewById(R.id.bill_record_add_consume)).setChecked(true);
                        break;
                    case 1:
                        ((RadioButton) findViewById(R.id.bill_record_add_income)).setChecked(true);
                        break;
                }
            }
        });
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        /**
         * 将RadioGroup的各个按钮与ViewPager2中的碎片进行相互绑定
         * 增加消费记录界面碎片绑定0号按钮
         * 增加收入记录界面碎片绑定1号按钮
         * 点击RadioButton时切换当前展示Fragment
         */
        switch (checkedId) {
            case R.id.bill_record_add_consume:
                VP.setCurrentItem(0);
                break;
            case R.id.bill_record_add_income:
                VP.setCurrentItem(1);
                break;
        }
    }
}