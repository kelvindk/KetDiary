package ubicomp.ketdiary.fragments.saliva_test;

import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;

import ubicomp.ketdiary.MainActivity;
import ubicomp.ketdiary.R;
import ubicomp.ketdiary.fragments.saliva_test.test_states.TestStateIdle;
import ubicomp.ketdiary.fragments.saliva_test.test_states.TestStateTransition;
import ubicomp.ketdiary.utility.data.file.ImageFileHandler;
import ubicomp.ketdiary.utility.data.file.MainStorage;
import ubicomp.ketdiary.utility.data.file.VoltageFileHandler;
import ubicomp.ketdiary.utility.system.PreferenceControl;
import ubicomp.ketdiary.utility.test.bluetoothle.BluetoothLE;
import ubicomp.ketdiary.utility.test.bluetoothle.BluetoothListener;
import ubicomp.ketdiary.utility.test.camera.CameraCaller;
import ubicomp.ketdiary.utility.test.camera.CameraInitHandler;
import ubicomp.ketdiary.utility.test.camera.CameraRecorder;
import ubicomp.ketdiary.utility.test.camera.CameraRunHandler;

/**
 *  Handle the process fo saliva test.
 *
 * Created by kelvindk on 16/6/16.
 */
public class SalivaTestAdapter implements BluetoothListener, CameraCaller {

    public static int FIRST_VOLTAGE_THRESHOLD = 200;//PreferenceControl.getVoltag1();
    public static int SECOND_VOLTAGE_THRESHOLD= 100;//PreferenceControl.getVoltag2();

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
    private ImageFileHandler imgFileHandler;
    private VoltageFileHandler voltageFileHandler;

    // Cassette saliva voltage.
    private int salivaVoltage;

    // Wakelock avoid phone to sleep.
    private PowerManager.WakeLock wakeLock = null;

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


        // Set listener to image button: testButton.
        testButton.setOnClickListener(test_button_click);

        // Init the currentState as TestStateIdle.
        currentState = new TestStateIdle(this);

        // Load sound into sound pool
        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 5);
        shortBeepAudioId = soundPool.load(mainActivity, R.raw.short_beep, 1);
        dinDingAudioId = soundPool.load(mainActivity, R.raw.din_ding, 1);
        supplyAudioId = soundPool.load(mainActivity, R.raw.supply, 1);

    }


    /*
    * Click listener of the button on the center of FragmentTest.
    * */
    View.OnClickListener test_button_click = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            currentState = currentState.transit(TestStateTransition.TEST_BUTTON_CLICK);

        }
    };


    public MainActivity getMainActivity() {
        return mainActivity;
    }

    public BluetoothLE getBle() {
        return ble;
    }

    public void setBle(BluetoothLE ble) {
        this.ble = ble;
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


    // Getter of plugged cassette ID.
    public int getPluggedCassetteId() {
        return pluggedCassetteId;
    }

    // Getter of salivaVoltage.
    public int getSalivaVoltage() {
        return salivaVoltage;
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

    // Getter of cameraRecorder.
    public CameraRecorder getCameraRecorder() {
        return cameraRecorder;
    }

    // When start saliva test process, block all related components that can affect testing.
    public void setEnableBlockedForTest(boolean enable) {
        // Enable clickable of Toolbar Spinner & Tabs.
        mainActivity.getTabLayoutWrapper().enableTabs(enable);
        mainActivity.getToolbarMenuItemWrapper().enableToolbarSpinner(enable);

        // Release WakeLock to enable phone sleep and other resources.
        if(enable) {
            /*** Will crash if do not close CameraRecorder. *Legacy codes, need rewrite.... ***/
            imageFaceAnchor.setVisibility(View.INVISIBLE);
            if(cameraRecorder != null) {
                cameraRecorder.pause();
                cameraRecorder.close();
            }
            releaseWakeLock();
        }
        else{
            acquireWakeLock();
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
        byte[] command = new byte[]{BluetoothLE.BLE_REQUEST_SALIVA_VOLTAGE};
        ble.mAppStateTypeDef = BluetoothLE.AppStateTypeDef.APP_FETCH_INFO;
        ble.bleWriteCharacteristic1(command);
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
        pluggedCassetteId = cassetteId;
        currentState = currentState.transit(TestStateTransition.BLE_CASSETTE_PLUGGED);
    }

    @Override
    public void bleUpdateBattLevel(int battVolt) {
        Log.d("BLE", "bleUpdateBattLevel "+battVolt);
    }

    @Override
    public void notifyDeviceVersion(int version) {
        Log.d("BLE", "notifyDeviceVersion "+version);
    }

    @Override
    public void bleUpdateSalivaVolt(int salivaVolt) {
        Log.d("BLE", currentState.getClass().getSimpleName()+" bleUpdateSalivaVolt "+salivaVolt);
        salivaVoltage = salivaVolt;
        currentState = currentState.transit(TestStateTransition.BLE_UPDATE_SALIVA_VOLTAGE);
    }

    @Override
    public void bleGetImageSuccess(Bitmap bitmap) {
        Log.d("BLE", "bleGetImageSuccess");
    }

    @Override
    public void bleGetImageFailure(float dropoutRate) {
        Log.d("BLE", "bleGetImageFailure "+dropoutRate);
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
