package ubicomp.ketdiary;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Lock screen to portrait.
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // New the wrapper of toolbar UI.
        toolbarMenuItemWrapper = new ToolbarMenuItemWrapper(this);
        // New the wrapper of tabs UI.
        tabLayoutWrapper = new TabLayoutWrapper(this);
        // New the object of handling fragment switch.
        fragmentSwitcher = new FragmentSwitcher(this, toolbarMenuItemWrapper, tabLayoutWrapper);


    }

    @Override
    public void onBackPressed() {
        /*** Bug-fix B1 ****/
        FragmentManager fragmentManager = getSupportFragmentManager();

        Log.d("Ket", "  "+fragmentManager.getBackStackEntryCount()+" "+getFragmentManager().getBackStackEntryCount());
        if (fragmentManager.getBackStackEntryCount() > 0 ){
            fragmentManager.popBackStack();
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Pass the reference of Menu object to ToolbarMenuItemWrapper when ready
        this.toolbarMenuItemWrapper.setMenu(menu);

        return true;
    }

    // To pass the call for fragment switch to FragmentSwitcher.
    public void setFragment(int fragmentToSwitch) {
        this.fragmentSwitcher.setFragment(fragmentToSwitch);
    }

}
