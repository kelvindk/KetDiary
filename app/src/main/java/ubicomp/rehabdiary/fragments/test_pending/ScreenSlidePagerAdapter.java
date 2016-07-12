package ubicomp.rehabdiary.fragments.test_pending;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by kelvindk on 16/7/10.
 */
public class ScreenSlidePagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;

    public ScreenSlidePagerAdapter(FragmentManager fm, List<Fragment> fragments){
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}