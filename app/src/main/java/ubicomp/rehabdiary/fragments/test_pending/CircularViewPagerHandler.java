package ubicomp.rehabdiary.fragments.test_pending;

import android.support.v4.view.ViewPager;

import ubicomp.rehabdiary.utility.system.PreferenceControl;
import ubicomp.rehabdiary.utility.system.clicklog.ClickLog;
import ubicomp.rehabdiary.utility.system.clicklog.ClickLogId;

/**
 * Created by kelvindk on 16/7/11.
 */

public class CircularViewPagerHandler implements ViewPager.OnPageChangeListener {
    private ViewPager mViewPager;
    private int mCurrentPosition;
    private int mScrollState;

    public CircularViewPagerHandler(final ViewPager viewPager) {
        mViewPager = viewPager;
    }

    @Override
    public void onPageSelected(final int position) {
        ClickLog.Log(ClickLogId.PAGE1_COPING_TOPS_SWEEP);
        mCurrentPosition = position;

        // Store last coping tip on view page.
        PreferenceControl.setLastCopingTip(position);
    }

    @Override
    public void onPageScrollStateChanged(final int state) {
        handleScrollState(state);
        mScrollState = state;
    }

    private void handleScrollState(final int state) {
        if (state == ViewPager.SCROLL_STATE_IDLE) {
            setNextItemIfNeeded();
        }
    }

    private void setNextItemIfNeeded() {
        if (!isScrollStateSettling()) {
            handleSetNextItem();
        }
    }

    private boolean isScrollStateSettling() {
        return mScrollState == ViewPager.SCROLL_STATE_SETTLING;
    }

    private void handleSetNextItem() {
        final int lastPosition = mViewPager.getAdapter().getCount() - 1;
        if(mCurrentPosition == 0) {
            mViewPager.setCurrentItem(lastPosition, false);
        } else if(mCurrentPosition == lastPosition) {
            mViewPager.setCurrentItem(0, false);
        }
    }

    @Override
    public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {
    }
}

