package ubicomp.ketdiary;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import ubicomp.ketdiary.fragments.FragmentTest;
import ubicomp.ketdiary.fragments.saliva_test.ResultService;
import ubicomp.ketdiary.fragments.saliva_test.ResultServiceAdapter;
import ubicomp.ketdiary.fragments.saliva_test.SalivaTestAdapter;
import ubicomp.ketdiary.main_activity.FragmentSwitcher;
import ubicomp.ketdiary.main_activity.TabLayoutWrapper;
import ubicomp.ketdiary.main_activity.ToolbarMenuItemWrapper;
import ubicomp.ketdiary.utility.system.PreferenceControl;


public class MainActivity extends AppCompatActivity {

    // The wrapper to handle toolbar.
    private ToolbarMenuItemWrapper toolbarMenuItemWrapper = null;
    // The wrapper to handle tab layout.
    private TabLayoutWrapper tabLayoutWrapper = null;
    // The class to manipulate fragment switch, all switching should use this class.
    private FragmentSwitcher fragmentSwitcher = null;

    private static MainActivity mainActivity = null;

    // ResultServiceRunning is running or not, which is used to adapt FragmentTest's view.
    private boolean isResultServiceRunning = false;

    public ResultServiceAdapter getResultServiceAdapter(SalivaTestAdapter salivaTestAdapter) {
        resultServiceAdapter = new ResultServiceAdapter(this, salivaTestAdapter);
        return resultServiceAdapter;
    }

    // Handle ResultService message.
    private ResultServiceAdapter resultServiceAdapter = null;
    private TextView textviewToolbar = null;

    static {
        System.loadLibrary("opencv_java3");
    }


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

////        Calendar yesterday = (Calendar) Calendar.getInstance().clone();
////        Calendar now = (Calendar) Calendar.getInstance().clone();
////        yesterday.add(Calendar.DATE, -1);
////        event.editTime.add(yesterday);
////        event.editTime.add(now);
//        event.scenarioType = EventLogStructure.ScenarioTypeEnum.BODY;
         //Data structure to store event.
//        EventLogStructure event = new EventLogStructure();
//        // Set one field as example. scenarioType is an enum.
//        event.scenarioType = EventLogStructure.ScenarioTypeEnum.BODY;
//        Intent eventContentActivityIntent = new Intent(CreateEventActivity.class);
//        // Put the serializable object into eventContentActivityIntent through a Bundle.
//        Bundle bundle = new Bundle();
//        bundle.putSerializable(EventLogStructure.EVENT_LOG_STRUCUTRE_KEY, event);
//        eventContentActivityIntent.putExtras(bundle);
//        // Start the activity.
//        startActivity(eventContentActivityIntent);

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


    // Getter of FragmentTest.
    public FragmentTest getFragmentTest() {
        return fragmentSwitcher.getFragmentTest();
    }

    // Getter of TabLayoutWrapper.
    public TabLayoutWrapper getTabLayoutWrapper() {
        return tabLayoutWrapper;
    }

    // Getter of ToolbarMenuItemWrapper.
    public ToolbarMenuItemWrapper getToolbarMenuItemWrapper() {
        return toolbarMenuItemWrapper;
    }

    public boolean isResultServiceRunning() {
        return isResultServiceRunning;
    }

    public void setResultServiceRunning(boolean resultServiceRunning) {
        isResultServiceRunning = resultServiceRunning;
    }

    // Pass the method call to FragmentSwitcher.
    public void setFragment(int fragmentToSwitch) {
        fragmentSwitcher.setFragment(fragmentToSwitch);
    }

    // Pass the method call to FragmentSwitcher.
    public void setFragmentTest() {
        fragmentSwitcher.setFragmentTestWaitResult();
    }


    /*
    * Listener of message from ResultService.
    * Received countdown will show on toolbar.
    * */
    public void resultServiceRunning(int countdown) {
        Log.d("Ket", "MainActivity resultServiceRunning countdown="+countdown);

        Handler handler = new Handler();
        switch (countdown){
            case ResultService.MSG_SERVICE_NOT_RUNNING:
                textviewToolbar.setVisibility(View.GONE);
                // With a trick to stop service.
                resultServiceAdapter.doUnbindService();
                Intent intent = new Intent(this, ResultService.class);
                stopService(intent);
                break;
            case ResultService.MSG_SERVICE_FINISH:
                int result = PreferenceControl.getTestResult();
                Log.d("KetResult", "Test result is: "+result);
                isResultServiceRunning = false;
                fragmentSwitcher.setFragment(FragmentSwitcher.FRAGMENT_TEST);
                /*** What else need to store? ***/
                if(result == 1) { // Saliva test results positive.
                    textviewToolbar.setText(R.string.salivaResultPositive);
                }
                else {
                    textviewToolbar.setText(R.string.salivaResultNegative);
                }

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        textviewToolbar.setVisibility(View.GONE);
                    }
                }, 20000);

                break;
            case ResultService.MSG_SERVICE_FAIL_NO_PLUG:
                /*** Saliva test process is fail!  ***/
                textviewToolbar.setText(getString(R.string.test_instruction_top4));

                // Invisible textviewToolbar after 10 sec.
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        textviewToolbar.setVisibility(View.GONE);
                    }
                }, 20000);
                break;
            case ResultService.MSG_SERVICE_FAIL_CONNECT_TIMEOUT:
                /*** Saliva test process is fail!  ***/
                textviewToolbar.setText(getString(R.string.test_instruction_top3));

                // Invisible textviewToolbar after 10 sec.
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        textviewToolbar.setVisibility(View.GONE);
                    }
                }, 20000);
                break;
            default:
                textviewToolbar.setVisibility(View.VISIBLE);
                textviewToolbar.setText(getString(R.string.test_notification_countdown)
                        +"\n"+countdown/60+"分"+countdown%60+"秒");
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Pass the reference of Menu object to ToolbarMenuItemWrapper when ready
        toolbarMenuItemWrapper.setMenu(menu);
        // Show FragmentTest's action button due to it's first page when app start.
        toolbarMenuItemWrapper.inflate(this.mainActivity, R.menu.menu_test);

        return true;
    }

    @Override
    public void onResume() {
        Log.d("Ket", "MainActivity onResume");
        /*** Not sure following codes for OpenCV, currently work fine ***/
//        if (!OpenCVLoader.initDebug()) {
//            Log.d("Ket", "Internal OpenCV library not found. Using OpenCV Manager for initialization");
//            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
//        } else {
//            Log.d("Ket", "OpenCV library found inside package. Using it!");
//            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
//        }

        // Check ResultService is running or not.
        textviewToolbar = (TextView) findViewById(R.id.textview_toolbar);
        resultServiceAdapter = new ResultServiceAdapter(this);
        resultServiceAdapter.doBindService();

        super.onResume();
    }

    @Override
    public void onStart() {
        Log.d("Ket", "MainActivity onStart");

        super.onStart();
    }

    @Override
    public void onRestart() {
        Log.d("Ket", "MainActivity onRestart");


        super.onRestart();
    }

    @Override
    public void onPause() {
        Log.d("Ket", "MainActivity onPause");
        // Unbind the connection with ResultService.
        // With a trick to stop service.
        if(resultServiceAdapter != null)
            resultServiceAdapter.doUnbindService();

        super.onPause();
    }

    @Override
    public void onStop() {
        Log.d("Ket", "MainActivity onStop");

        super.onStop();
    }

    @Override
    public void onDestroy() {
        Log.d("Ket", "MainActivity onDestroy");

        super.onDestroy();
    }


//    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
//        @Override
//        public void onManagerConnected(int status) {
//            switch (status) {
//                case LoaderCallbackInterface.SUCCESS:{
//                    Log.i("Ket", "OpenCV loaded successfully");
//                } break;
//                default:{
//                    super.onManagerConnected(status);
//                } break;
//            }
//        }
//    };


}
