package ubicomp.rehabdiary.fragments.saliva_test;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Message;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.Queue;

import ubicomp.rehabdiary.MainActivity;
import ubicomp.rehabdiary.R;
import ubicomp.rehabdiary.fragments.saliva_test.test_states.TestStateIdle;
import ubicomp.rehabdiary.fragments.saliva_test.test_states.TestStateTransition;
import ubicomp.rehabdiary.utility.data.db.FirstPageDataBase;
import ubicomp.rehabdiary.utility.data.file.ImageFileHandler;
import ubicomp.rehabdiary.utility.data.file.MainStorage;
import ubicomp.rehabdiary.utility.data.file.VoltageFileHandler;
import ubicomp.rehabdiary.utility.data.structure.TestDetail;
import ubicomp.rehabdiary.utility.system.PreferenceControl;
import ubicomp.rehabdiary.utility.test.bluetoothle.BluetoothLE;
import ubicomp.rehabdiary.utility.test.bluetoothle.BluetoothListener;
import ubicomp.rehabdiary.utility.test.camera.CameraCaller;
import ubicomp.rehabdiary.utility.test.camera.CameraInitHandler;
import ubicomp.rehabdiary.utility.test.camera.CameraRecorder;
import ubicomp.rehabdiary.utility.test.camera.CameraRunHandler;

/**
 *  Handle the process fo saliva test.
 *
 * Created by kelvindk on 16/6/16.
 */
public class SalivaTestAdapter implements BluetoothListener, CameraCaller {

    public static int FIRST_VOLTAGE_THRESHOLD = PreferenceControl.getVoltag1();
    public static int SECOND_VOLTAGE_THRESHOLD = PreferenceControl.getVoltag2();
    public static int SALIVA_VOLTAGE_QUEUE_SIZE = 3;

    public static int DEVICE_LOW_BATTERY_THRESHOLD = 103;

    public static final int STAGE1_COUNTDOWN = 30000; // Should be 30000
    public static final int STAGE1_PERIOD = 3000;
    public static final int STAGE2_COUNTDOWN = 60000; // Should be 60000
    public static final int STAGE2_PERIOD = 1000;
    public static final int STAGE3_COUNTDOWN = 180000; // Should be 180000
    public static final int STAGE3_PERIOD = 1000;
    public static final int STAGE3_RESPIT_COUNTDOWN = 120000; // Should be 120000
    public static final int STAGE3_RESPIT_PERIOD = 1000;

    private MainActivity mainActivity = null;
    private SalivaTestAdapter salivaTestAdapter = null;
    private BluetoothLE ble = null;

    // UI components.
    private TextView textviewTestButton = null;
    private ImageButton testButton = null;
    private TextView textviewTestInstructionTop = null;
    private TextView textviewTestInstructionDown = null;
    private ProgressBar progressbar = null;
    private ImageView imageFaceAnchor = null;
    private ImageView imageGuideCassette = null;

    // Current test state.
    private TestStateTransition currentState = null;
    // Plugged cassette ID.
    private int pluggedCassetteId = 0;

    // SoundPool for playing sound feedback.
    private SoundPool soundPool = null;
    // Sound Id.
    private int shortBeepAudioId;
    private int dinDingAudioId;
    private int supplyAudioId;

    // Camera components.
    private CameraInitHandler cameraInitHandler = null;
    private CameraRecorder cameraRecorder = null;
    private CameraRunHandler cameraRunHandler = null;

    // Storage components, store test results.
    private ImageFileHandler imgFileHandler = null;
    private VoltageFileHandler voltageFileHandler = null;

    // Cassette saliva voltage, maintain a size tree queue to calculate moving average.
    private Queue salivaVoltageQueue = new LinkedList();
    private int salivaVoltageQueueSum = 0;

    // Wakelock avoid phone to sleep.
    private PowerManager.WakeLock wakeLock = null;

    // Countdown timer for stages of saliva test.
    private CountDownTimer stage1TestCountdown = null;
    private CountDownTimer stage2TestCountdown = null;
    private CountDownTimer stage3TestCountdown = null;
    private CountDownTimer stage3RespitTestCountdown = null;

    // ResultServiceAdapter is used to handle the connection with ResultService.
    private ResultServiceAdapter resultServiceAdapter = null;

    // For accessing database.
    FirstPageDataBase testDB = null;


    public SalivaTestAdapter(MainActivity mainActivity) {
        this.mainActivity = mainActivity;

        salivaTestAdapter = this;

        // Get related UI components in FragmentTest.
        textviewTestButton = (TextView) mainActivity.findViewById(R.id.textview_test_button);
        testButton = (ImageButton) mainActivity.findViewById(R.id.button_test);
        textviewTestInstructionTop =
                (TextView) mainActivity.findViewById(R.id.textview_test_instruction_top);
        textviewTestInstructionDown =
                (TextView) mainActivity.findViewById(R.id.textview_test_instruction_down);
        progressbar = (ProgressBar) mainActivity.findViewById(R.id.progress_bar_test);
        imageFaceAnchor = (ImageView) mainActivity.findViewById(R.id.test_face_anchor);
        imageGuideCassette = (ImageView) mainActivity.findViewById(R.id.image_cassette_guide);


        // Set listener to image button: testButton.
        testButton.setOnClickListener(test_button_click);

        // Init the currentState as TestStateIdle.
        currentState = new TestStateIdle(this);

        // Load sound into sound pool
        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 5);
        shortBeepAudioId = soundPool.load(mainActivity, R.raw.short_beep, 1);
        dinDingAudioId = soundPool.load(mainActivity, R.raw.din_ding, 1);
        supplyAudioId = soundPool.load(mainActivity, R.raw.supply, 1);

        // New object for accessing database.
        testDB = new FirstPageDataBase();

    }


    /*
    * Click listener of the button on the center of FragmentTest.
    * */
    View.OnClickListener test_button_click = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            // Only can do saliva test twice in 6 hours.
            if((getTestDB().isDeveloper()) ||
                (testDB.checkTestStatus(Calendar.getInstance())))
                currentState = currentState.transit(TestStateTransition.TEST_BUTTON_CLICK);
            else {
                setToIdleState(R.string.test_instruction_top13);
            }
        }
    };


    public MainActivity getMainActivity() {
        return mainActivity;
    }

    public TestStateTransition getCurrentState() {
        return currentState;
    }

    public BluetoothLE getBle() {
        return ble;
    }

    public void setBle(BluetoothLE ble) {
        this.ble = ble;
    }

    public FirstPageDataBase getTestDB() {
        return testDB;
    }

    public void setCurrentState(TestStateTransition newState) {
        currentState = newState;
    }

    /*** Getter of UI components. ***/
    public TextView getTextviewTestButton() {
        return textviewTestButton;
    }

    public ImageButton getImagebuttonTestButton() {
        return testButton;
    }

    public TextView getTextviewTestInstructionTop() {
        return textviewTestInstructionTop;
    }

    public TextView getTextviewTestInstructionDown() {
        return textviewTestInstructionDown;
    }

    public ProgressBar getProgressbar() {
        return progressbar;
    }

    public ImageView getImageFaceAnchor() {
        return imageFaceAnchor;
    }

    public ImageView getImageGuideCassette() {
        return imageGuideCassette;
    }

    public ImageFileHandler getImgFileHandler() {
        return imgFileHandler;
    }

    public VoltageFileHandler getVoltageFileHandler() {
        return voltageFileHandler;
    }


    // Getter of plugged cassette ID.
    public int getPluggedCassetteId() {
        return pluggedCassetteId;
    }

    // Getter of moving average of salivaVoltage.
    public int getSalivaVoltageAverage() {
        return salivaVoltageQueueSum/SALIVA_VOLTAGE_QUEUE_SIZE;
    }


    /*** Start ResultService through ResultServiceAdapter. ***/
    public void startResultService() {
        resultServiceAdapter = mainActivity.getResultServiceAdapter(this);
        resultServiceAdapter.doBindService();
        resultServiceAdapter.startResultService();

    }

    /*** Stop ResultService through ResultServiceAdapter. ***/
    public void stopResultService() {
        if(resultServiceAdapter != null)
            resultServiceAdapter.doUnbindService();
    }


    // Cancel countdown timer inside currentState
    public void cancelTestStateCountdownTimer() {
        if(currentState != null)
            currentState.transit(TestStateTransition.CANCEL_COUNTDOWN_TIMER);
    }


    /*** Below contains a lot of codes written for fitting legacy codes, need rewrite....  ***/
    // Play short_beep sound feedback.
    public void playShortBeepAudio() {
        soundPool.play(shortBeepAudioId, 0.6f, 0.6f, 0, 0, 1.0F);
    }
    // Play ding_ding sound feedback.
    public void playDingDingAudio() {
        soundPool.play(dinDingAudioId, 1.0F, 1.0F, 0, 0, 1.0F);
    }
    // Play supply sound feedback.
    public void playSupplyAudio() {
        soundPool.play(supplyAudioId, 1.5F, 1.5F, 0, 0, 1.0F);
    }

    // Getter of cameraRecorder.
    public CameraRecorder getCameraRecorder() {
        return cameraRecorder;
    }

    // Getter of getStage1TestCountdown.
    public CountDownTimer getStage1TestCountdown() {
        return stage1TestCountdown;
    }

    // Start countdown for stage1 test and taking photos periodically every 10 seconds.
    public void startStage1CountdownAndPeriodPhotoShot() {
        stage1TestCountdown = new CountDownTimer(STAGE1_COUNTDOWN, STAGE1_PERIOD){
            @Override
            public void onFinish() {
                // Take photo!
                cameraRunHandler.sendEmptyMessage(0);
                setToIdleState(R.string.test_instruction_top7);

                TestDetail testDetail = new TestDetail(PreferenceControl.getCassetteId()+"",
                        PreferenceControl.getUpdateDetectionTimestamp(),
                        TestDetail.TEST_SALIVA_STAGE1,
                        PreferenceControl.getPassVoltage1(),
                        PreferenceControl.getPassVoltage2(),
                        PreferenceControl.getBatteryLevel(),
                        0, 0,
                        "NOT_ENOUGH_SALIVA",
                        "" );

                testDB.addTestDetail(testDetail);
            }

            @Override
            public void onTick(long millisUntilFinished) {
                // Take photo!
                cameraRunHandler.sendEmptyMessage(0);
            }
        }.start();
    }

    // Getter of getStage2TestCountdown.
    public CountDownTimer getStage2TestCountdown() {
        return stage2TestCountdown;
    }

    // Start countdown for stage2 test.
    public void startStage2Countdown() {
        stage2TestCountdown = new CountDownTimer(STAGE2_COUNTDOWN, STAGE2_PERIOD){
            @Override
            public void onFinish() {

                // Start Stage3Countdown and transit to stage3 to wait saliva propagating.
                currentState = currentState.transit(TestStateTransition.TEST_TRANSIT_STAGE3);
            }

            @Override
            public void onTick(long millisUntilFinished) {
                String countdownString =
                        mainActivity.getResources().getString(R.string.test_instruction_top8) + " " +
                                (millisUntilFinished/ STAGE2_PERIOD) + " " +
                                mainActivity.getResources().getString(R.string.test_second);
                getTextviewTestInstructionTop().setText(countdownString);
            }
        }.start();
    }

    // Getter of getStage3TestCountdown.
    public CountDownTimer getStage3TestCountdown() {
        return stage3TestCountdown;
    }

    // Start countdown for stage3 test.
    public void startStage3Countdown() {
        stage3TestCountdown = new CountDownTimer(STAGE3_COUNTDOWN, STAGE3_PERIOD){
            @Override
            public void onFinish() {
                // If saliva voltage doesn't drop to low threshold. Ask user spit again.
                if(salivaVoltageQueueSum > SECOND_VOLTAGE_THRESHOLD) {
                    // Start Stage3Countdown and transit to stage3respit to wait saliva propagating.
                    currentState = currentState.transit(TestStateTransition.TEST_TRANSIT_STAGE3_RESPIT);
                }
            }

            @Override
            public void onTick(long millisUntilFinished) {
                String countdownString =
                        mainActivity.getResources().getString(R.string.test_instruction_top8) + " " +
                                (millisUntilFinished/ STAGE3_PERIOD) + " " +
                                mainActivity.getResources().getString(R.string.test_second);
                getTextviewTestInstructionTop().setText(countdownString);
            }
        }.start();
    }

    // Getter of getStage3RespitTestCountdown.
    public CountDownTimer getStage3RespitTestCountdown() {
        return stage3RespitTestCountdown;
    }

    // Start countdown for stage3 test.
    public void startStage3RespitCountdown() {
        stage3RespitTestCountdown = new CountDownTimer(STAGE3_RESPIT_COUNTDOWN, STAGE3_RESPIT_PERIOD){
            int secondToShot = 0;

            @Override
            public void onFinish() {
                // Take photo!
                cameraRunHandler.sendEmptyMessage(0);

                // Finish saliva spit process, transit to TestStateWaitResult.
                currentState = currentState.transit(TestStateTransition.TEST_FINISH);

            }

            @Override
            public void onTick(long millisUntilFinished) {
                // Take photo every three seconds.
                if(++secondToShot >= 3) {
                    cameraRunHandler.sendEmptyMessage(0);
                    secondToShot = 0;
                }

                String countdownString =
                        mainActivity.getResources().getString(R.string.test_instruction_down5_left) + " " +
                                (millisUntilFinished/ STAGE3_PERIOD) + " " +
                                mainActivity.getResources().getString(R.string.test_instruction_down5_right);
                getTextviewTestInstructionDown().setText(countdownString);
            }
        }.start();
    }

    // Transit to Idle state.
    public TestStateIdle setToIdleState(int failMessage) {
        // Set corresponding text on test screen.
        getTextviewTestButton().setBackgroundResource(0);
        getTextviewTestButton().setText(R.string.test_start);
        getTextviewTestInstructionTop().setText(failMessage);
        getTextviewTestInstructionDown().setText(R.string.test_instruction_down1);

        // Transit to TestStateIdle.
        currentState = new TestStateIdle(this);

        // Disconnect BLE connection with device.
        if(ble != null) {
            ble.bleUnlockDevice();
//            ble.bleDerequestSalivaVoltage();
//            ble.bleCancelCassetteInfo();
            ble.bleSelfDisconnection();
            ble = null;
        }

        // Reset two saliva voltages in testing.
        PreferenceControl.setPassVoltage1(0);
        PreferenceControl.setPassVoltage2(0);


        // Clear salivaVoltageQueue & salivaVoltageQueueSum.
        salivaVoltageQueue.clear();
        salivaVoltageQueueSum = 0;

        // Close voltage recording and pause cameraRecorder.
        if(voltageFileHandler != null)
            voltageFileHandler.close();
        if(cameraRecorder != null)
            cameraRecorder.pause();

        // Invisible progress bar.
        getProgressbar().setVisibility(View.GONE);

        // Invisible getImageGuideCassette.
        getImageGuideCassette().setVisibility(View.GONE);

        // Enable center button.
        getImagebuttonTestButton().setClickable(true);

        // Enable related phone components that can affect saliva test.
        setEnableUiComponents(true);

        return (TestStateIdle) currentState;
    }

    // When start saliva test process, block all related components that can affect testing.
    public void setEnableUiComponents(boolean enable) {

        // Enable clickable of Toolbar & Tabs.
        mainActivity.getTabLayoutWrapper().enableTabs(enable);
        mainActivity.getToolbarMenuItemWrapper().enableToolbarClickable(enable);

        // Release WakeLock to enable phone sleep and other resources.
        if(enable) {
            /*** Will crash if do not close CameraRecorder. ***/
             if(imageFaceAnchor != null)
                imageFaceAnchor.setVisibility(View.INVISIBLE);
            if(cameraRecorder != null) {
                cameraRecorder.close();
            }
            releaseWakeLock();
            mainActivity.setWakeLocked(false);
        }
        else{
            acquireWakeLock();
            mainActivity.setWakeLocked(true);
        }
    }

    // Enable WakeLock to avoid phone sleep.
    private void acquireWakeLock(){
        if (wakeLock == null){
            PowerManager pm = (PowerManager) mainActivity.getSystemService(Context.POWER_SERVICE);
            wakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK
                    | PowerManager.ON_AFTER_RELEASE, "PostLocationService");
            if (null != wakeLock){
                wakeLock.acquire();
            }
        }
    }

    // Release WakeLock, allow the phone can go sleep.
    private void releaseWakeLock(){
        if (wakeLock != null){
            wakeLock.release();
            wakeLock = null;
        }
    }

    // Init camera components. *Legacy codes, need rewrite....
    public void initTestLogComponents() {
        // This timestamp will be a unique ID as directory name to store test data.
        long timestamp = System.currentTimeMillis();
        // Write timestamp to preference as ID of this saliva test.
        PreferenceControl.setUpdateDetectionTimestamp(timestamp);

        // Root directory.
        File dir = MainStorage.getMainStorageDirectory();
        File mainDirectory = new File(dir, String.valueOf(timestamp));
        if (!mainDirectory.exists()) {
            if (!mainDirectory.mkdirs()) {
                Log.d("Ket", "Something wrong! can't mkdir!");
                return;
            }
        }

        // For store cassette voltage result.
        voltageFileHandler = new VoltageFileHandler(mainDirectory,
                String.valueOf(timestamp));

        // For store face shots.
        imgFileHandler = new ImageFileHandler(mainDirectory,
                String.valueOf(timestamp));

        // Camera components.
        cameraRecorder = new CameraRecorder(this, imgFileHandler);
        cameraRunHandler = new CameraRunHandler(cameraRecorder);

        // initialize camera task
        cameraInitHandler = new CameraInitHandler(this, cameraRecorder);
        cameraInitHandler.sendEmptyMessage(0);
    }



    /*** BLE callbacks & methods ***/

    public void sendRequestSalivaVoltage() {
        // Send request saliva voltage to device.
        ble.bleRequestSalivaNotification();
    }

    public void bleEnableUserPressConfirm() {
        Log.d("Ket", "User press confirm in enabling");
        currentState = currentState.transit(TestStateTransition.BLE_ENABLE_USER_PRESS_CONFIRM);
    }

    public void bleEnableUserPressCancel() {
        Log.d("Ket", "User press cancel in enabling");
        currentState = currentState.transit(TestStateTransition.BLE_ENABLE_USER_PRESS_CANCEL);
    }

    @Override
    public Context getContext() {
        return null;
    }

    @Override
    public void bleNotSupported() {
        Log.d("BLE", "bleNotSupported");
    }

    @Override
    public void bleConnectionTimeout() {
        Log.d("BLE", "bleConnectionTimeout");
        currentState = currentState.transit(TestStateTransition.BLE_CONNECTION_TIMEOUT);
    }

    @Override
    public void bleConnected() {
        Log.d("BLE", "bleConnected");

        currentState = currentState.transit(TestStateTransition.BLE_DEVICE_CONNECTED);
    }

    @Override
    public void bleDisconnected() {
        Log.d("BLE", currentState.getClass().getSimpleName()+" bleDisconnected");
        currentState = currentState.transit(TestStateTransition.BLE_DEVICE_DISCONNECTED);
    }

    @Override
    public void bleWriteCharacteristic1Success() {
        Log.d("BLE", "bleWriteCharacteristic1Success");
    }

    @Override
    public void bleWriteStateFail() {
        Log.d("BLE", "bleWriteStateFail");
        currentState = currentState.transit(TestStateTransition.BLE_WRITE_CHAR_FAIL);
    }

    @Override
    public void bleNoPlugDetected() {
        Log.d("BLE", currentState.getClass().getSimpleName()+" bleNoPlugDetected");
        currentState = currentState.transit(TestStateTransition.BLE_NO_CASSETTE_PLUGGED);
    }

    @Override
    public void blePlugInserted(int cassetteId) {
        Log.d("BLE", currentState.getClass().getSimpleName()+" blePlugInserted "+cassetteId);

        PreferenceControl.setCassetteId(cassetteId);
        pluggedCassetteId = cassetteId;
        currentState = currentState.transit(TestStateTransition.BLE_CASSETTE_PLUGGED);
    }

    @Override
    public void bleUpdateBattLevel(int battVolt) {
        Log.d("BLE", "bleUpdateBattLevel "+battVolt);

        PreferenceControl.setBatteryLevel(battVolt);

        if(battVolt < DEVICE_LOW_BATTERY_THRESHOLD)
            currentState = currentState.transit(TestStateTransition.DEVICE_LOW_BATTERY);
    }

    @Override
    public void notifyDeviceVersion(int version) {
        Log.d("BLE", "notifyDeviceVersion "+version);
    }

    @Override
    public void bleUpdateSalivaVolt(int salivaVolt) {
        Log.d("BLE", currentState.getClass().getSimpleName()+" bleUpdateSalivaVolt "+salivaVolt);

        salivaVoltageQueueSum += salivaVolt;
        salivaVoltageQueue.add(salivaVolt);
        if(salivaVoltageQueue.size() > SALIVA_VOLTAGE_QUEUE_SIZE) {
            salivaVoltageQueueSum -= (int) salivaVoltageQueue.poll();
        }

        currentState = currentState.transit(TestStateTransition.BLE_UPDATE_SALIVA_VOLTAGE);

        // Write saliva voltage values to file.
        Message msg = new Message();
        Bundle data = new Bundle();
        data.putString("VOLTAGE", System.currentTimeMillis()+" v="+salivaVolt+"\n");
        msg.setData(data);
        if(voltageFileHandler!=null)
            voltageFileHandler.sendMessage(msg);
    }

    @Override
    public void bleGetImageSuccess(Bitmap bitmap) {
        Log.d("BLE", "bleGetImageSuccess");

        currentState = currentState.transit(TestStateTransition.BLE_GET_IMAGE_SUCCESS);
    }

    @Override
    public void bleGetImageFailure(float dropoutRate) {
        Log.d("BLE", "bleGetImageFailure "+dropoutRate);

        if(currentState != null)
            currentState = currentState.transit(TestStateTransition.BLE_GET_IMAGE_FAILURE);
    }

    @Override
    public void bleNotifyDetectionResult(double score) {
        Log.d("BLE", "bleNotifyDetectionResult "+score);
    }

    @Override
    public void bleReturnDeviceVersion(int version) {
        Log.d("BLE", "bleReturnDeviceVersion");
    }




    /*** Callbacks of camera actions. *Legacy codes, need rewrite.... ***/
    
    @Override
    public void stopByFail(int fail) {

    }

    @Override
    public FrameLayout getPreviewFrameLayout() {
        return (FrameLayout) mainActivity.findViewById(R.id.camera_layout);
    }

    @Override
    public Point getPreviewSize() {
        FrameLayout frameLayout = (FrameLayout) mainActivity.findViewById(R.id.test_start_layout);
        int left = frameLayout.getLeft();
        int right = frameLayout.getRight();
        int top = frameLayout.getTop();
        int bottom = frameLayout.getBottom();
        return new Point(right - left, bottom - top);
    }

    @Override
    public void updateInitState(int type) {

    }

    @Override
    public void updateDoneState(int type) {

    }

}
