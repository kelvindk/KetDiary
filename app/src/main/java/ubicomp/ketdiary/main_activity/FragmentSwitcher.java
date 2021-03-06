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
import ubicomp.ketdiary.fragments.FragmentTestWaitResult;

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
    public final static int FRAGMENT_TEST_WAIT_RESULT = 4;

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
        fragments[1] = new FragmentStatistics(this);
        fragments[2] = new FragmentEvent(this, mainActivity);
        fragments[3] = new FragmentRanking(this);
        fragments[4] = new FragmentTestWaitResult(this, mainActivity);

        fragmentManager = this.mainActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Add four fragments to view.
        fragmentTransaction.add(R.id.fragment_main_container, fragments[0], ""+FRAGMENT_TEST);
        fragmentTransaction.add(R.id.fragment_main_container, fragments[1], ""+FRAGMENT_STATISTICS);
        fragmentTransaction.add(R.id.fragment_main_container, fragments[2], ""+FRAGMENT_EVENT);
        fragmentTransaction.add(R.id.fragment_main_container, fragments[3], ""+FRAGMENT_RANKING);
        fragmentTransaction.add(R.id.fragment_main_container, fragments[4], ""+FRAGMENT_TEST_WAIT_RESULT);


        // Show FragmentTest as first page.
        fragmentTransaction.replace(R.id.fragment_main_container, fragments[0]);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    public void setFragmentOnlyDowndropTab(int fragmentToSwitch) {
        tabLayoutWrapper.setTabSelected(fragmentToSwitch);
        toolbarMenuItemWrapper.setSpinnerSelection(fragmentToSwitch);
    }

    // Switch to FragmentTestWaitResult.
    public void setFragmentTestWaitResult() {
        Log.d("Ket", "setFragmentTestWaitResult");
        toolbarMenuItemWrapper.inflate(this.mainActivity, R.menu.menu_test);

        currentFragment = FRAGMENT_TEST_WAIT_RESULT;

        // Switch fragment to selected page.
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_main_container, fragments[FRAGMENT_TEST_WAIT_RESULT]);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    // Switch to FragmentTest.
    public void setFragmentTest() {
        Log.d("Ket", "setFragmentTest");
        toolbarMenuItemWrapper.inflate(this.mainActivity, R.menu.menu_test);

        currentFragment = FRAGMENT_TEST;

        // Switch fragment to selected page.
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_main_container, fragments[FRAGMENT_TEST]);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    // Switch the fragment.
    public void setFragment(int fragmentToSwitch) {

        // Return if attempt to go the same fragment.
        if(fragmentToSwitch == this.currentFragment)
            return;

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


//        for(int i=0; i<fragments.length; i++) {
//            Log.d("Ket", i+" "+fragments[i].isAdded()+" "+fragments[i].isHidden()+" "+fragments[i].isInLayout());
//        }


        // When switch out from FragmentEvent, do this trick to avoid crash.
        if(currentFragment == FRAGMENT_EVENT)
            ((FragmentEvent)fragments[FRAGMENT_EVENT]).invisibleList();

        // Update which fragment we stay.
        currentFragment = fragmentToSwitch;

        // Switch fragment to selected page.
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_main_container, fragments[fragmentToSwitch]);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    /*
    *  Getter of FragmentTest
    * */
    public FragmentTest getFragmentTest() {
        return (FragmentTest) fragments[FRAGMENT_TEST];
    }
}
