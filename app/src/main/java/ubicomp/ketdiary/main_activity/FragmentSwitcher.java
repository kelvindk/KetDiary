package ubicomp.ketdiary.main_activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import ubicomp.ketdiary.MainActivity;
import ubicomp.ketdiary.R;
import ubicomp.ketdiary.fragments.FragmentEvent;
import ubicomp.ketdiary.fragments.FragmentRanking;
import ubicomp.ketdiary.fragments.FragmentStatistics;
import ubicomp.ketdiary.fragments.FragmentTest;
import ubicomp.ketdiary.fragments.FragmentTestPending;
import ubicomp.ketdiary.utility.system.PreferenceControl;

/**
 * This class aggregates all manipulation of fragment switch.
 *
 * Created by kelvindk on 16/6/6.
 */

public class FragmentSwitcher {
    public final static int FRAGMENT_TEST = 0;
    public final static int FRAGMENT_STATISTICS = 1;
    public final static int FRAGMENT_EVENT = 2;
    public final static int FRAGMENT_RANKING = 3;
    public final static int FRAGMENT_TEST_PENDING = 4;

    private MainActivity mainActivity = null;
    private TabLayoutWrapper tabLayoutWrapper = null;

    public ToolbarMenuItemWrapper getToolbarMenuItemWrapper() {
        return toolbarMenuItemWrapper;
    }

    private ToolbarMenuItemWrapper toolbarMenuItemWrapper = null;

    // FragmentManager is used to manage fragment.
    private FragmentManager fragmentManager = null;
    private Fragment[] fragments = new Fragment[5];

    private int currentFragment = 0;

    // Constructor received the references for fragment switch.
    public FragmentSwitcher(MainActivity mainActivity,
                            ToolbarMenuItemWrapper toolbarMenuItemWrapper,
                            TabLayoutWrapper tabLayoutWrapper) {
        this.mainActivity = mainActivity;
        this.tabLayoutWrapper = tabLayoutWrapper;
        this.toolbarMenuItemWrapper = toolbarMenuItemWrapper;

        // Create four fragments.
        fragments[0] = new FragmentTest(this, mainActivity);
        fragments[1] = new FragmentStatistics(this, mainActivity);
        fragments[2] = new FragmentEvent(this, mainActivity);
        fragments[3] = new FragmentRanking(this, mainActivity);
        fragments[4] = new FragmentTestPending(this, mainActivity);

        fragmentManager = this.mainActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

//        // Add four fragments to view.
        fragmentTransaction.add(R.id.fragment_main_container, fragments[0], fragments[0].getClass().getName());
        fragmentTransaction.add(R.id.fragment_main_container, fragments[1], fragments[1].getClass().getName());
        fragmentTransaction.add(R.id.fragment_main_container, fragments[2], fragments[2].getClass().getName());
        fragmentTransaction.add(R.id.fragment_main_container, fragments[3], fragments[3].getClass().getName());
        fragmentTransaction.add(R.id.fragment_main_container, fragments[4], fragments[4].getClass().getName());

        fragmentTransaction.hide(fragments[1]);
        fragmentTransaction.hide(fragments[2]);
        fragmentTransaction.hide(fragments[3]);
        fragmentTransaction.hide(fragments[4]);

        fragmentTransaction.commit();


    }

    public int getCurrentFragment() {
        return currentFragment;
    }

    public void setFragmentOnlyDowndropTab(int fragmentToSwitch) {
        tabLayoutWrapper.setTabSelected(fragmentToSwitch);
        toolbarMenuItemWrapper.setSpinnerSelection(fragmentToSwitch);
    }

    // Switch to FragmentTestPending.
    public void setFragmentTestPending() {
        Log.d("Ket", "setFragmentTestPending");
        toolbarMenuItemWrapper.inflate(this.mainActivity, R.menu.menu_test);

        // Switch fragment to selected page.
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.hide(fragments[currentFragment]);
        fragmentTransaction.show(fragments[FRAGMENT_TEST_PENDING]);
        fragmentTransaction.commitAllowingStateLoss();
//        fragmentTransaction.commit();

        currentFragment = FRAGMENT_TEST_PENDING;
    }


    // Switch the fragment.
    public void setFragment(int fragmentToSwitch) {

        /*
        * Bind tab and downdrop selection,
        * i.e., they will change together when one of them is selected.
        * */
        tabLayoutWrapper.setTabSelected(fragmentToSwitch);
        toolbarMenuItemWrapper.setSpinnerSelection(fragmentToSwitch);

        // Switch toolbar according to selected fragment.
        switch (fragmentToSwitch) {
            case FRAGMENT_TEST:
                toolbarMenuItemWrapper.inflate(this.mainActivity, R.menu.menu_test);
                break;
            case FRAGMENT_STATISTICS:
                toolbarMenuItemWrapper.inflate(this.mainActivity, R.menu.menu_statistics);
                break;
            case FRAGMENT_EVENT:
                toolbarMenuItemWrapper.inflate(this.mainActivity, R.menu.menu_event);
                // Refresh badge count of menu item on toolbar.
                toolbarMenuItemWrapper.refreshRemindBadgeCount();
                break;
            case FRAGMENT_RANKING:
                toolbarMenuItemWrapper.inflate(this.mainActivity, R.menu.menu_ranking);
                break;
        }

        // Hide current fragment.
//        setHideFragment(currentFragment);


        // Switch fragment to selected page.
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Log.d("PreferenceControl", "isResultServiceRunning "+PreferenceControl.isResultServiceRunning()+" currentFragment "+currentFragment);


        if(PreferenceControl.isResultServiceRunning()) {
            if(fragmentToSwitch == FRAGMENT_TEST) {
                fragmentToSwitch = FRAGMENT_TEST_PENDING;
            }
        }

        fragmentTransaction.hide(fragments[currentFragment]);
        fragmentTransaction.show(fragments[fragmentToSwitch]);

        fragmentTransaction.commit();

        // Update which fragment we stay.
        currentFragment = fragmentToSwitch;

    }

    public void setHideFragment(int fragmentPos) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.hide(fragments[fragmentPos]);
        fragmentTransaction.commit();
    }

    /*
    *  Getter of FragmentTest
    * */
    public FragmentTest getFragmentTest() {
        return (FragmentTest) fragments[FRAGMENT_TEST];
    }

    /*
    *  Getter of FragmentTest
    * */
    public FragmentStatistics getFragmentStatistics() {
        return (FragmentStatistics) fragments[FRAGMENT_STATISTICS];
    }
}
