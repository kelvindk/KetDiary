package ubicomp.ketdiary.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ubicomp.ketdiary.MainActivity;
import ubicomp.ketdiary.R;
import ubicomp.ketdiary.fragments.test_pending.CircularViewPagerHandler;
import ubicomp.ketdiary.fragments.test_pending.FragmentTestPendingPage;
import ubicomp.ketdiary.fragments.test_pending.ScreenSlidePagerAdapter;
import ubicomp.ketdiary.main_activity.FragmentSwitcher;
import ubicomp.ketdiary.utility.system.PreferenceControl;

/**
 * A placeholder fragment containing a simple view for TestWaitResult fragment.
 */
public class FragmentTestPending extends Fragment {

    private FragmentSwitcher fragmentSwitcher = null;
    private MainActivity mainActivity = null;

    private ViewPager mPager;

    private PagerAdapter mPagerAdapter;

    public FragmentTestPending() {

    }

    @SuppressLint("ValidFragment")
    public FragmentTestPending(FragmentSwitcher fragmentSwitcher, MainActivity mainActivity) {
        this.fragmentSwitcher = fragmentSwitcher;
        this.mainActivity = mainActivity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Ket", "onCreate FragmentTestPending");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Ket", "onCreateView FragmentTestPending");
        fragmentSwitcher.setFragmentOnlyDowndropTab(FragmentSwitcher.FRAGMENT_TEST);

        View fragmentWaitResultView = inflater.inflate(R.layout.fragment_test_pending, container, false);

        return fragmentWaitResultView;
    }

    @Override
    public void onStart() {
        Log.d("Ket", "FragmentTestPending onStart ");

        String[] copingTipsString = mainActivity.getResources().getStringArray(R.array.coping_tips_array);

        List<Fragment> fragments = new ArrayList<Fragment>();
        for(int i=0; i<copingTipsString.length; i++) {
            fragments.add(FragmentTestPendingPage.newInstance(copingTipsString[i]));
        }

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) mainActivity.findViewById(R.id.fragment_test_pending_pager);
        mPager.setOnPageChangeListener(new CircularViewPagerHandler(mPager));

        mPagerAdapter = new ScreenSlidePagerAdapter(mainActivity.getSupportFragmentManager(), fragments);
        mPager.setAdapter(mPagerAdapter);

        super.onStart();
    }

    @Override
    public void onResume() {
        Log.d("Ket", "FragmentTestPending onResume ");

        // Resume last coping tip from Preference.
        mPager.setCurrentItem(PreferenceControl.getLastCopingTip());

        super.onResume();
    }

}
