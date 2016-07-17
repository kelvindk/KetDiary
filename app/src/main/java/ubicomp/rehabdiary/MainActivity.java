package ubicomp.rehabdiary;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ubicomp.rehabdiary.fragments.FragmentTest;
import ubicomp.rehabdiary.fragments.saliva_test.ResultService;
import ubicomp.rehabdiary.fragments.saliva_test.ResultServiceAdapter;
import ubicomp.rehabdiary.fragments.saliva_test.SalivaTestAdapter;
import ubicomp.rehabdiary.fragments.saliva_test.test_states.TestStateWaitResult;
import ubicomp.rehabdiary.main_activity.FragmentSwitcher;
import ubicomp.rehabdiary.main_activity.TabLayoutWrapper;
import ubicomp.rehabdiary.main_activity.ToolbarMenuItemWrapper;
import ubicomp.rehabdiary.utility.data.db.AddScoreDataBase;
import ubicomp.rehabdiary.utility.data.download.Downloader;
import ubicomp.rehabdiary.utility.data.upload.UploadService;
import ubicomp.rehabdiary.utility.dialog.PasswordLockDialogActivity;
import ubicomp.rehabdiary.utility.dialog.QuestionIdentityDialog;
import ubicomp.rehabdiary.utility.system.PreferenceControl;
import ubicomp.rehabdiary.utility.test.bluetoothle.BluetoothLE;


public class MainActivity extends AppCompatActivity {

    public static final int BROWSING_COUNTDOWN = 10000; // 10000

    // The wrapper to handle toolbar.
    private ToolbarMenuItemWrapper toolbarMenuItemWrapper = null;
    // The wrapper to handle tab layout.
    private TabLayoutWrapper tabLayoutWrapper = null;

    // The class to manipulate fragment switch, all switching should use this class.
    private FragmentSwitcher fragmentSwitcher = null;

    private static MainActivity mainActivity = null;

    // Countdown timer for checking user's browsing time for event page and ranking page.
    private CountDownTimer eventBrowsingCountdown = null;
    private CountDownTimer rankingBrowsingCountdown = null;

    // wake lock is set by SalivaTestAdapter to block backpress by user.
    private boolean wakeLocked = false;


    public void setWakeLocked(boolean wakeLocked) {
        this.wakeLocked = wakeLocked;
    }

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

    // Getter of FragmentSwitcher.
    public FragmentSwitcher getFragmentSwitcher() {
        return fragmentSwitcher;
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


        // Download data from server.
        Downloader downloader = new Downloader();
        downloader.updateSVM();
        downloader.updateTrigger();
        downloader.updateCassetteTask();

        // Show password lock.
        showPasswordLock();



        // For developing
//        Calendar calendar = (Calendar) Calendar.getInstance().clone();
//        calendar.add(Calendar.DATE, -1);
//        TestResult testResult = new TestResult(1,
//                System.currentTimeMillis(),
//                "11",
//                1, 0, 0, 0);
//
//        FirstPageDataBase firstPageDataBase = new FirstPageDataBase();
//        boolean cs = firstPageDataBase.checkCassetteUsed("CT_99839");
//        Cassette[] cassettes = firstPageDataBase.getAllCassette();
//        firstPageDataBase.addTestResult(testResult);

//        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
//        LayoutInflater layoutInflater = LayoutInflater.from(this);
//        View dialogView = layoutInflater.inflate(R.layout.dialog_reflection_acceptance, null);
//        dialog.setView(dialogView);
//        dialog.setTitle("反思認同度");
//        dialog.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int item) {
//                /* User clicked "Confirm"*/
//                Log.d("Ket", "Dialog OK");
//
//            }
//        });
//        dialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int item) {
//                /* User clicked "Cancel"*/
//                Log.d("Ket", "Dialog Cancel");
//            }
//        });
//        dialog.show();


//        TestResult testResult = new TestResult(0,
//                System.currentTimeMillis()-30000,
//                "1",
//                1, 0, 0, 0);
//
//        FirstPageDataBase firstPageDataBase = new FirstPageDataBase();
////        firstPageDataBase.setCassetteUsed(cassetteId);
//        firstPageDataBase.addTestResult(testResult);

//        DatabaseControl db = new DatabaseControl();
//        TestResult testResult = db.getLatestTestResult();

//        ThirdPageDataBase thirdPageDataBase = new ThirdPageDataBase();
//        thirdPageDataBase.insertDummyTrigger();
//        EventLogStructure[] eventLogStructures = thirdPageDataBase.getAllEventLog();

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
//        Intent eventContentActivityIntent = new Intent(this, CreateEventActivity.class);
//        // Put the serializable object into eventContentActivityIntent through a Bundle.
//        Bundle bundle = new Bundle();
//        bundle.putSerializable(EventLogStructure.EVENT_LOG_STRUCUTRE_KEY, event);
//        eventContentActivityIntent.putExtras(bundle);
//        // Start the activity.
//        startActivity(eventContentActivityIntent);

        Log.d("Ket", "MainActivity onCreate");
    }

    @Override
    public void onBackPressed() {
        if(!wakeLocked)
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
                PreferenceControl.setResultServiceIsRunning(false);
                // With a trick to stop service.
                resultServiceAdapter.doUnbindService();
                Intent intent = new Intent(this, ResultService.class);
                stopService(intent);
                break;
            case ResultService.MSG_SERVICE_FINISH:
                int result = PreferenceControl.getTestResult();
                Log.d("KetResult", "Test result is: "+result);
                PreferenceControl.setResultServiceIsRunning(false);


                if(result == 1) { // Saliva test results positive.
                    textviewToolbar.setText(R.string.salivaResultPositive);
                }
                else {
                    textviewToolbar.setText(R.string.salivaResultNegative);
                }

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fragmentSwitcher.getFragmentStatistics().onResume();
                        fragmentSwitcher.setFragment(FragmentSwitcher.FRAGMENT_STATISTICS);
                    }
                }, 500);

                handler = new Handler();
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

    public void startBrowsingCountdown(int fragment) {
        switch (fragment) {
            case FragmentSwitcher.FRAGMENT_TEST:
            case FragmentSwitcher.FRAGMENT_STATISTICS:
            case FragmentSwitcher.FRAGMENT_TEST_PENDING:

                // Cancel browsing countdown when leaving this page.
                if(eventBrowsingCountdown != null)
                    eventBrowsingCountdown.cancel();
                if(rankingBrowsingCountdown != null)
                    rankingBrowsingCountdown.cancel();

                break;
            case FragmentSwitcher.FRAGMENT_EVENT:
                if(rankingBrowsingCountdown != null)
                    rankingBrowsingCountdown.cancel();

                // Start countdown timer to check whether user's browsing is over 10 sec.
                eventBrowsingCountdown = new CountDownTimer(BROWSING_COUNTDOWN, BROWSING_COUNTDOWN){
                    @Override
                    public void onFinish() {
                        // Invoke add score.
                        Log.d("AddScore", "addScoreViewPage3List");
                        AddScoreDataBase addScoreDataBase = new AddScoreDataBase();
                        addScoreDataBase.addScoreViewPage3List();
                    }

                    @Override
                    public void onTick(long millisUntilFinished) {
                        // No op.
//                        Log.d("AddScore", "Page 3 - 1  Tick "+ millisUntilFinished/1000);
                    }
                }.start();

                break;
            case FragmentSwitcher.FRAGMENT_RANKING:
                if(eventBrowsingCountdown != null)
                    eventBrowsingCountdown.cancel();

                // Start countdown timer to check whether user's browsing is over 10 sec.
                rankingBrowsingCountdown = new CountDownTimer(BROWSING_COUNTDOWN, BROWSING_COUNTDOWN){
                    @Override
                    public void onFinish() {
                        // Invoke add score.
                        Log.d("AddScore", "addScoreViewPage4List");
                        AddScoreDataBase addScoreDataBase = new AddScoreDataBase();
                        addScoreDataBase.addScoreViewPage4List();
                    }

                    @Override
                    public void onTick(long millisUntilFinished) {
                        // No op.
                    }
                }.start();

                break;
        }
    }


    private void showPasswordLock() {
        // Show password lock.
        if(PreferenceControl.getAppPasswordAble() == 1) {
            Intent password_intent = new Intent(this, PasswordLockDialogActivity.class);
            startActivityForResult(password_intent, PasswordLockDialogActivity.PASSWORD_LOCK_INT_KEY);
        }
    }


    @Override
    public void onResume() {
        Log.d("Ket", "MainActivity onResume ");

        startBrowsingCountdown(fragmentSwitcher.getCurrentFragment());

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

        // Cancel browsing countdown when leaving this page.
        if(eventBrowsingCountdown != null)
            eventBrowsingCountdown.cancel();
        if(rankingBrowsingCountdown != null)
            rankingBrowsingCountdown.cancel();

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

        // Upload local data to server.
        UploadService.startUploadService(this);

        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
//        Log.d("Ket", "MainActivity onActivityResult "+requestCode+" "+resultCode);

        // Forward onActivityResult to FragmentTest.
        if (requestCode == BluetoothLE.REQUEST_ENABLE_BT) {
            fragmentSwitcher.getFragmentTest().onActivityResult(requestCode, resultCode, intent);
        }


        if (requestCode == TestStateWaitResult.SALIVA_TEST_INT_KEY) {
            Log.d("Ket", "MainActivity onActivityResult SALIVA_TEST_INT_KEY");

            /** Show dialog to ask reflection acceptance */
            // Show question identity dialog.
            QuestionIdentityDialog questionIdentityDialog =
                    new QuestionIdentityDialog((RelativeLayout)findViewById(R.id.question_identity_dialog_layout));
            questionIdentityDialog.initialize();
            questionIdentityDialog.show();

        }

        if (requestCode == PasswordLockDialogActivity.PASSWORD_LOCK_INT_KEY) {

            if(resultCode == RESULT_OK) {
                Log.d("Ket", "MainActivity onActivityResult PASSWORD_LOCK_INT_KEY OK");
            }

            if(resultCode == RESULT_CANCELED) {
                Log.d("Ket", "MainActivity onActivityResult PASSWORD_LOCK_INT_KEY CANCELED");
                finish();
            }

        }
    }

}
