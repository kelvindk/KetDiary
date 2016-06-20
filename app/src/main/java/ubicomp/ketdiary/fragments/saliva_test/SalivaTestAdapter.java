package ubicomp.ketdiary.fragments.saliva_test;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import ubicomp.ketdiary.MainActivity;
import ubicomp.ketdiary.R;
import ubicomp.ketdiary.fragments.saliva_test.test_states.TestStateIdle;
import ubicomp.ketdiary.fragments.saliva_test.test_states.TestStateTransition;
import ubicomp.ketdiary.utility.test.bluetoothle.BluetoothLE;
import ubicomp.ketdiary.utility.test.bluetoothle.BluetoothListener;

/**
 *  Handle the process fo saliva test.
 *
 * Created by kelvindk on 16/6/16.
 */
public class SalivaTestAdapter implements BluetoothListener {

    private MainActivity mainActivity = null;
    private SalivaTestAdapter salivaTestAdapter = null;
    private BluetoothLE ble = null;

    private TextView textviewTestButton = null;
    private ImageButton testButton = null;
    private TextView textviewTestInstructionTop = null;
    private TextView textviewTestInstructionDown = null;
    private ProgressBar progressbar = null;

    private TestStateTransition currentState = null;

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



        // Set listener to image button: testButton.
        testButton.setOnClickListener(test_button_click);

        // Init the currentState as TestStateIdle.
        currentState = new TestStateIdle(this);

    }


    /*
    * Click listener of the button on the center of FragmentTest.
    * */
    View.OnClickListener test_button_click = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            currentState = currentState.transit(TestStateTransition.TEST_BUTTON_CLICK);
//            switch (currentState) {
//                case IDLE:
//                    if(ble == null) {
////                ble = new BluetoothLE(salivaTestAdapter , PreferenceControl.getDeviceId(), 0);
//                        ble = new BluetoothLE(salivaTestAdapter , "ket_49", 0); //
//                    }
//                    // Try to connect saliva device.
//                    ble.bleConnect();
//
//                    // Set nothing to the text on this button.
//                    textviewTestButton.setText("");
//
//                    currentState = CONNECTING;
//                    break;
//                case CONNECTING:
//
//                    break;
//            }

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
    }

    @Override
    public void bleDisconnected() {
        Log.d("BLE", "bleDisconnected");
    }

    @Override
    public void bleWriteCharacteristic1Success() {
        Log.d("BLE", "bleWriteCharacteristic1Success");
    }

    @Override
    public void bleWriteStateFail() {
        Log.d("BLE", "bleWriteStateFail");
    }

    @Override
    public void bleNoPlugDetected() {
        Log.d("BLE", "bleNoPlugDetected");
    }

    @Override
    public void blePlugInserted(int cassetteId) {
        Log.d("BLE", "blePlugInserted "+cassetteId);
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
        Log.d("BLE", "bleUpdateSalivaVolt "+salivaVolt);
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


}
