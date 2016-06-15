package ubicomp.ketdiary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.WindowManager;

import java.util.Calendar;

import ubicomp.ketdiary.fragments.event.EventLogStructure;
import ubicomp.ketdiary.ui.CreateEventActivity;
import ubicomp.ketdiary.ui.FragmentSwitcher;
import ubicomp.ketdiary.ui.TabLayoutWrapper;
import ubicomp.ketdiary.ui.ToolbarMenuItemWrapper;

public class MainActivity extends AppCompatActivity {


    // The wrapper to handle toolbar.
    private ToolbarMenuItemWrapper toolbarMenuItemWrapper = null;
    // The wrapper to handle tab layout.
    private TabLayoutWrapper tabLayoutWrapper = null;
    // The class to manipulate fragment switch, all switching should use this class.
    private FragmentSwitcher fragmentSwitcher = null;

    private static MainActivity mainActivity = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = this;
        setContentView(R.layout.activity_main);

        // Set full screen.
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN );

        // New the wrapper of toolbar UI.
        toolbarMenuItemWrapper = new ToolbarMenuItemWrapper(this);
        // New the wrapper of tabs UI.
        tabLayoutWrapper = new TabLayoutWrapper(this);
        // New the object of handling fragment switch.
        fragmentSwitcher = new FragmentSwitcher(this, toolbarMenuItemWrapper, tabLayoutWrapper);

        // For developing
        EventLogStructure event = new EventLogStructure();
//        Calendar yesterday = (Calendar) Calendar.getInstance().clone();
//        Calendar now = (Calendar) Calendar.getInstance().clone();
//        yesterday.add(Calendar.DATE, -1);
//        event.editTime.add(yesterday);
//        event.editTime.add(now);
        event.scenarioType = EventLogStructure.ScenarioTypeEnum.BODY;
        Intent createEventIntent = new Intent (this, CreateEventActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(EventLogStructure.EVENT_LOG_STRUCUTRE_KEY, event);

        createEventIntent.putExtras(bundle);
        startActivity(createEventIntent);

    }

    @Override
    public void onBackPressed() {
//        /*** Bug-fix 1 ****/
//        FragmentManager fragmentManager = getSupportFragmentManager();
//
//        Log.d("Ket", "  "+fragmentManager.getBackStackEntryCount()+" "+getFragmentManager().getBackStackEntryCount());
//        if (fragmentManager.getBackStackEntryCount() > 0 ){
//            fragmentManager.popBackStack();
//        } else {
//            super.onBackPressed();
//        }
        super.onBackPressed();
    }

    public static MainActivity getMainActivity() {
        return mainActivity;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Pass the reference of Menu object to ToolbarMenuItemWrapper when ready
        toolbarMenuItemWrapper.setMenu(menu);

        return true;
    }

    // Pass the method call to FragmentSwitcher.
    public void setFragment(int fragmentToSwitch) {
        fragmentSwitcher.setFragment(fragmentToSwitch);
    }


}
