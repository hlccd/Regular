package hlccd.regular.util;
/**
 * VP_adapter
 * ViewPager2适配器, 用于适配ViewPager2控件
 * 可以通过添加碎片的方式新增子界面
 *
 * @author hlccd 2021.6.5
 * @version 1.0
 */

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

public class VP_adapter extends FragmentStateAdapter {
    private List<Class> fragments;

    /**
     * VP_adapter构建函数
     * 用于初始化该适配器, 将放入的活动环境进行上传
     *
     * @param fragmentActivity 活动环境
     * @author hlccd 2021.6.5
     */
    public VP_adapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        if (fragments == null) {
            fragments = new ArrayList<>();
        }
    }

    /**
     * addFragment
     * 添加碎片进入该适配器中用以新增子界面
     *
     * @param fragment 待添加碎片
     * @author hlccd 2021.6.5
     */
    public void addFragment(Fragment fragment) {
        if (fragments != null) {
            fragments.add(fragment.getClass());
        }
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        try {
            return (Fragment) fragments.get(position).newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return fragments.size();
    }
}
