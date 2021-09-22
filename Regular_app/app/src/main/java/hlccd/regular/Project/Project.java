package hlccd.regular.Project;
/**
 * 规划详情子界面碎片
 * 该界面包含三个子界面
 * 分别是个人习惯记录界面，日常任务记录界面，待办事项记录界面
 * 不同界面分别和RadioButton分别绑定,滑动界面或点击按钮会进行同步修改
 *
 * @author hlccd 2020.6.18
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

public class Project extends Fragment implements RadioGroup.OnCheckedChangeListener{
    private ViewPager2 VP;
    private RadioGroup RG;
    private View       view;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        this.view=view;
        VP = view.findViewById(R.id.VP);
        RG = view.findViewById(R.id.RG);

        VP_adapter adapter = new VP_adapter(getActivity());
        adapter.addFragment(new Project_habit());//添加个人习惯子界面碎片
        adapter.addFragment(new Project_task());//添加日常任务子界面碎片
        adapter.addFragment(new Project_backlog());//添加待办事项子界面碎片
        VP.setAdapter(adapter);
        VP.setCurrentItem(0);//默认第0个添加的即个人习惯子界面碎片为该活动启动时展示的首界面
        RG.setOnCheckedChangeListener(this);
        VP.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                /**
                 * 将RadioGroup的各个按钮与ViewPager2中的碎片进行相互绑定
                 * 0号按钮绑定个人习惯记录界面碎片
                 * 1号按钮绑定日常任务记录界面碎片
                 * 2号按钮绑定待办事项记录界面碎片
                 * 滑动更换碎片的时同时切换处于点击状态的RadioButton
                 */
                switch (position) {
                    case 0:
                        ((RadioButton) view.findViewById(R.id.habit)).setChecked(true);
                        break;
                    case 1:
                        ((RadioButton) view.findViewById(R.id.task)).setChecked(true);
                        break;
                    case 2:
                        ((RadioButton) view.findViewById(R.id.backlog)).setChecked(true);
                        break;
                }
            }
        });
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view =inflater.inflate(R.layout.project,container,false);
        return view;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        /**
         * 将RadioGroup的各个按钮与ViewPager2中的碎片进行相互绑定
         * 个人习惯界面碎片绑定0号按钮
         * 日常任务界面碎片绑定1号按钮
         * 待办事项记录界面碎片绑定2号按钮
         * 点击RadioButton时切换当前展示Fragment
         */
        switch (checkedId) {
            case R.id.habit:
                VP.setCurrentItem(0);
                break;
            case R.id.task:
                VP.setCurrentItem(1);
                break;
            case R.id.backlog:
                VP.setCurrentItem(2);
                break;
        }
    }
}
