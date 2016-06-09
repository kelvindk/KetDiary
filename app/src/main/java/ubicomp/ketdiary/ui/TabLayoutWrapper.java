package ubicomp.ketdiary.ui;

import android.support.design.widget.TabLayout;
import android.util.Log;

import ubicomp.ketdiary.MainActivity;
import ubicomp.ketdiary.R;

/**
 * This class is used to process the creation, manipulation of Tablayout.
 * The click listener of each button on toolbar is also in this class.
 * Created by kelvindk on 16/6/6.
 */
public class TabLayoutWrapper implements TabLayout.OnTabSelectedListener {

    public final static int NUMBER_OF_TABS = 4;

    private MainActivity mainActivity = null;

    // Four tabs' object.
    private TabLayout.Tab[] tabLayoutTabs = new TabLayout.Tab[NUMBER_OF_TABS];


    public TabLayoutWrapper(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        // Create a TabLayout
        TabLayout tabLayout = (TabLayout) mainActivity.findViewById(R.id.activity_main_tabs);

        // Create four tabs.
        tabLayoutTabs[0] = tabLayout.newTab().setCustomView(R.layout.tab_icon_text_test);
        tabLayout.addTab(tabLayoutTabs[0]);
        tabLayoutTabs[1] = tabLayout.newTab().setCustomView(R.layout.tab_icon_text_result);
        tabLayout.addTab(tabLayoutTabs[1]);
        tabLayoutTabs[2] = tabLayout.newTab().setCustomView(R.layout.tab_icon_text_event);
        tabLayout.addTab(tabLayoutTabs[2]);
        tabLayoutTabs[3] = tabLayout.newTab().setCustomView(R.layout.tab_icon_text_ranking);
        tabLayout.addTab(tabLayoutTabs[3]);

        // Set the tab selected listener.
        tabLayout.setOnTabSelectedListener(this);
    }

    public void setTabSelected(int targetTab) {
        tabLayoutTabs[targetTab].select();
    }

    // Switch fragment when select another tab.
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        Log.d("Ket", "onTabSelected " + tab.getPosition());
        mainActivity.setFragment(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        // no op
//        Log.d("Ket", "onTabUnselected " + tab.getPosition());
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        // no op
//        Log.d("Ket", "onTabReselected " + tab.getPosition());
    }
}
