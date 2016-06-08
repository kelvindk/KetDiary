package ubicomp.ketdiary.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import ubicomp.ketdiary.MainActivity;
import ubicomp.ketdiary.R;
import ubicomp.ketdiary.fragments.FragmentEvent;
import ubicomp.ketdiary.fragments.FragmentRanking;
import ubicomp.ketdiary.fragments.FragmentResult;
import ubicomp.ketdiary.fragments.FragmentTest;

/**
 * This class aggregates all manipulation of fragment switch.
 *
 * Created by kelvindk on 16/6/6.
 */

public class FragmentSwitcher {
    public final static int FRAGMENT_TEST = 0;
    public final static int FRAGMENT_RESULT = 1;
    public final static int FRAGMENT_EVENT = 2;
    public final static int FRAGMENT_RANKING = 3;

    private MainActivity mainActivity = null;
    private TabLayoutWrapper tabLayoutWrapper = null;
    ToolbarMenuItemWrapper toolbarMenuItemWrapper = null;

    // FragmentManager is used to manage fragment.
    private FragmentManager fragmentManager = null;
    private Fragment[] fragments = new Fragment[4];
    private int currentFragment = 0;

    // Constructor received the references for fragment switch.
    public FragmentSwitcher(MainActivity mainActivity,
                            ToolbarMenuItemWrapper toolbarMenuItemWrapper,
                            TabLayoutWrapper tabLayoutWrapper) {
        this.mainActivity = mainActivity;
        this.tabLayoutWrapper = tabLayoutWrapper;
        this.toolbarMenuItemWrapper = toolbarMenuItemWrapper;

        // Create four fragment.
        fragments[0] = new FragmentTest(this);
        fragments[1] = new FragmentResult(this);
        fragments[2] = new FragmentEvent(this, this.mainActivity);
        fragments[3] = new FragmentRanking(this);

        fragmentManager = this.mainActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.fragment_container, fragments[0], ""+FRAGMENT_TEST);
        fragmentTransaction.add(R.id.fragment_container, fragments[1], ""+FRAGMENT_RESULT);
        fragmentTransaction.add(R.id.fragment_container, fragments[2], ""+FRAGMENT_EVENT);
        fragmentTransaction.add(R.id.fragment_container, fragments[3], ""+FRAGMENT_RANKING);

        // Show FragmentTest a first page.
        fragmentTransaction.replace(R.id.fragment_container, fragments[0]);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void setFragmentOnlyDowndropTab(int fragmentToSwitch) {
        tabLayoutWrapper.setTabSelected(fragmentToSwitch);
        toolbarMenuItemWrapper.setSpinnerSelection(fragmentToSwitch);
    }


    // Switch the fragment.
    public void setFragment(int fragmentToSwitch) {

        // Return if attempt to go the same fragment.
        if(fragmentToSwitch == this.currentFragment)
            return;

        // Switch fragment to selected page.
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragments[fragmentToSwitch]);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        // Bind tab and downdrop selection,
        // i.e., they will change together when one of them is selected.
        tabLayoutWrapper.setTabSelected(fragmentToSwitch);
        toolbarMenuItemWrapper.setSpinnerSelection(fragmentToSwitch);


//        for(int i=0; i<fragments.length; i++) {
//            Log.d("Ket", i+" "+fragments[i].isAdded()+" "+fragments[i].isHidden()+" "+fragments[i].isInLayout());
//        }

        switch (fragmentToSwitch) {
            case FRAGMENT_TEST:
                toolbarMenuItemWrapper.inflate(this.mainActivity, R.menu.menu_test);
                break;
            case FRAGMENT_RESULT:
                toolbarMenuItemWrapper.inflate(this.mainActivity, R.menu.menu_result);
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


        // Update which fragment we stay.
        this.currentFragment = fragmentToSwitch;

    }
}
