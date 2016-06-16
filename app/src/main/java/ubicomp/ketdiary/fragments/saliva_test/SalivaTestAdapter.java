package ubicomp.ketdiary.fragments.saliva_test;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import ubicomp.ketdiary.MainActivity;
import ubicomp.ketdiary.R;
import ubicomp.ketdiary.utility.system.PreferenceControl;
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

    public enum TestStateEnum {
        IDLE,
        CONNECTING,
        CONNECT_FAIL,
    };

    interface StateTransition {
        void transit;
    }

    private TestStateEnum currentState = TestStateEnum.IDLE;

    public SalivaTestAdapter(MainActivity mainActivity) {
        this.mainActivity = mainActivity;

        salivaTestAdapter = this;

        // Get related UI components in FragmentTest.
        textviewTestButton = (TextView) mainActivity.findViewById(R.id.textview_test_button);
        testButton = (ImageButton) mainActivity.findViewById(R.id.button_test);

        // Set listener to image button: testButton.
        testButton.setOnClickListener(test_button_click);

    }

    private void stateTransition() {

    }

    /*
    * Click listener of the button on the center of FragmentTest.
    * */
    View.OnClickListener test_button_click = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (currentState) {
                case IDLE:
                    if(ble == null) {
//                ble = new BluetoothLE(salivaTestAdapter , PreferenceControl.getDeviceId(), 0);
                        ble = new BluetoothLE(salivaTestAdapter , "ket_49", 0); //
                    }
                    // Try to connect saliva device.
                    ble.bleConnect();

                    // Set nothing to the text on this button.
                    textviewTestButton.setText("");

                    currentState = TestStateEnum.CONNECTING;
                    break;
                case CONNECTING:

                    break;
            }

        }
    };

    @Override
    public Context getContext() {
        return null;
    }

    @Override
    public void bleNotSupported() {

    }

    @Override
    public void bleConnectionTimeout() {
        Log.d("BLE", "bleConnectionTimeout");
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

    }

    @Override
    public void bleWriteStateFail() {

    }

    @Override
    public void bleNoPlugDetected() {

    }

    @Override
    public void blePlugInserted(int cassetteId) {

    }

    @Override
    public void bleUpdateBattLevel(int battVolt) {

    }

    @Override
    public void notifyDeviceVersion(int version) {

    }

    @Override
    public void bleUpdateSalivaVolt(int salivaVolt) {

    }

    @Override
    public void bleGetImageSuccess(Bitmap bitmap) {

    }

    @Override
    public void bleGetImageFailure(float dropoutRate) {

    }

    @Override
    public void bleNotifyDetectionResult(double score) {

    }

    @Override
    public void bleReturnDeviceVersion(int version) {

    }
}
