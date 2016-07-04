package ubicomp.ketdiary.main_activity;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import ubicomp.ketdiary.MainActivity;
import ubicomp.ketdiary.R;
import ubicomp.ketdiary.fragments.create_event.CreateEventActivity;
import ubicomp.ketdiary.fragments.event.EventLogStructure;
import ubicomp.ketdiary.utility.back_door.HelpActivity;
import ubicomp.ketdiary.utility.data.db.ThirdPageDataBase;
import ubicomp.ketdiary.utility.data.structure.TriggerItem;
import ubicomp.ketdiary.utility.statistic.CopingActivity;
import ubicomp.ketdiary.utility.statistic.ScaleOnTouchListener;

/**
 *  ToolbarMenuItemAdapter is used to handle the page switching between test, result, event and
 *  ranking pages. The click listener of each button on toolbar is also in this class.
 *
 * Created by kelvindk on 16/6/6.
 */
public class ToolbarMenuItemWrapper implements AdapterView.OnItemSelectedListener {

    public final static int SPINNER_TEST = 0;
    public final static int SPINNER_RESULT = 1;
    public final static int SPINNER_EVENT = 2;
    public final static int SPINNER_RANKING = 3;

    private MainActivity mainActivity = null;
    private Menu menu = null;
    private Spinner spinner_toolbar = null;

    private int remindBadgeCount = 95;

    // Construct object,
    public ToolbarMenuItemWrapper(MainActivity mainActivity) {
        this.mainActivity = mainActivity;

        // Enable toolbar on main activity
        Toolbar toolbar = (Toolbar) mainActivity.findViewById(R.id.activity_main_toolbar);
        mainActivity.setSupportActionBar(toolbar);

        // Get the spinner on the toolbar
        spinner_toolbar = (Spinner) mainActivity.findViewById(R.id.spinner_toolbar);
        spinner_toolbar.setOnItemSelectedListener(this);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> spinner_content_adapter = ArrayAdapter.createFromResource(
                mainActivity, R.array.toolbar_spinner_array, R.layout.toolbar_spinner_layout);
        spinner_content_adapter.setDropDownViewResource(R.layout.toolbar_spinner_layout);
        // Apply the adapter to the spinner
        spinner_toolbar.setAdapter(spinner_content_adapter);

        // Disable the App title on toolbar
        mainActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);

    }


    /*
    *  Enable/Disable clickable of Toolbar spinner & action button.
    * */
    public void enableToolbarClickable(boolean enable) {
        spinner_toolbar.setEnabled(enable);
        if(menu == null)
            return;
        MenuItem itemActionSettings = menu.findItem(R.id.action_settings);
        if(itemActionSettings != null)
            MenuItemCompat.getActionView(itemActionSettings).setEnabled(enable);
    }

    /*
     Receive the Menu object when it is ready, the received Menu object is used to switch the icon
     when dropbown list is selected.
     */
    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public void setSpinnerSelection(int spinnerSelection) {
        this.spinner_toolbar.setSelection(spinnerSelection);
    }

    public void setRemindBadgeCount(int remindBadgeCount) {
        this.remindBadgeCount = remindBadgeCount;
    }

    public int getRemindBadgeCount() {
        return remindBadgeCount;
    }

    public void refreshRemindBadgeCount() {
        TextView remindBadgeText = (TextView) mainActivity.findViewById(R.id.menu_remind_badge);
        if(remindBadgeText == null) return;
        remindBadgeText.setText(""+remindBadgeCount);
    }

    /* Inflate the icon on the top right of toolbar according to user's selection on dropdown menu
     on the top left. Also handle button click of each icon.
     */
    public void inflate(final MainActivity mainActivity, int menuID) {

        if(menu == null)
            return;
        // Clear menu before inflate
        menu.clear();

        switch (menuID) {
            case R.menu.menu_test:

                mainActivity.getMenuInflater().inflate(R.menu.menu_test, menu);

                MenuItem itemActionSettings = menu.findItem(R.id.action_settings);
                MenuItemCompat.getActionView(itemActionSettings).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("Ket", "action_settings");
//                         Listener of action Settings button
                        Intent createEventIntent = new Intent (mainActivity, HelpActivity.class);
                        mainActivity.startActivity(createEventIntent);

                    }
                });
                break;
            case R.menu.menu_statistics:

                mainActivity.getMenuInflater().inflate(R.menu.menu_statistics, menu);

                MenuItem copingSkill = menu.findItem(R.id.coping_skill);
                MenuItemCompat.getActionView(copingSkill).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("Ket", "comping_skill");

                        Intent intent = new Intent();
                        intent.setClass(mainActivity, CopingActivity.class);
                        mainActivity.startActivity(intent);
                    }
                });
//                MenuItemCompat.getActionView(copingSkill).setOnTouchListener(new ScaleOnTouchListener());
                break;
            case R.menu.menu_event:
                mainActivity.getMenuInflater().inflate(R.menu.menu_event, menu);

                MenuItem actionAddEvent = menu.findItem(R.id.action_add_event);
                MenuItemCompat.getActionView(actionAddEvent).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("Ket", "action_add_event");
                        // Listener of action Add button
                        Intent createEventIntent = new Intent (mainActivity, CreateEventActivity.class);
                        mainActivity.startActivity(createEventIntent);

                    }
                });

                MenuItem actionRemind = menu.findItem(R.id.action_remind);
                MenuItemCompat.getActionView(actionRemind).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Listener of action Remind button
                        // Set the badge of remind button according to the number of incomplete event logging.
                        remindBadgeCount++;
                        refreshRemindBadgeCount();
                        Log.d("Ket", "action_remind "+remindBadgeCount);
                    }
                });
                break;
            case R.menu.menu_ranking:

                break;
        }


    }

    /*
        The selected listener of the downdrop list of toolbar (on the top left)
    */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

        // Inflate the menu; this adds items to the action bar according to the selection.
        mainActivity.getFragmentSwitcher().setFragment(pos);

//        switch(pos) {
//            // Inflate the menu; this adds items to the action bar according to the selection.
//            case SPINNER_TEST:
//                Log.d("Ket", "pos 0");
//                mainActivity.setFragment(FragmentSwitcher.FRAGMENT_TEST);
//                break;
//            case SPINNER_RESULT:
//                Log.d("Ket", "pos 1");
//                mainActivity.setFragment(FragmentSwitcher.FRAGMENT_RESULT);
//                break;
//            case SPINNER_EVENT:
//                Log.d("Ket", "pos 2");
//                mainActivity.setFragment(FragmentSwitcher.FRAGMENT_EVENT);
//                break;
//            case SPINNER_RANKING:
//                Log.d("Ket", "pos 3");
//                mainActivity.setFragment(FragmentSwitcher.FRAGMENT_RANKING);
//                break;
//        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
