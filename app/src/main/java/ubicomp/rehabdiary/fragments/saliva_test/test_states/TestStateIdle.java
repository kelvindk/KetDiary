package ubicomp.rehabdiary.fragments.saliva_test.test_states;

import android.util.Log;
import android.view.View;

import ubicomp.rehabdiary.R;
import ubicomp.rehabdiary.fragments.saliva_test.SalivaTestAdapter;
import ubicomp.rehabdiary.utility.test.bluetoothle.BluetoothLE;

/**
 * Created by kelvindk on 16/6/19.
 */
public class TestStateIdle extends TestStateTransition {

    public TestStateIdle(SalivaTestAdapter salivaTestAdapter) {
        super(salivaTestAdapter);
    }

    @Override
    public TestStateTransition transit(int trigger) {
        TestStateTransition newState = null;
        switch (trigger) {
            case TEST_BUTTON_CLICK:
                // Disable related phone components that can affect saliva test.
                getSalivaTestAdapter().setEnableUiComponents(false);

                // Get device ID from database.
                String deviceId = getSalivaTestAdapter().getTestDB().getDeviceId();

                BluetoothLE ble = new BluetoothLE(getSalivaTestAdapter(), deviceId, 1);
                getSalivaTestAdapter().setBle(ble);
                // Try to connect saliva device.
                ble.bleConnect();

                // Set corresponding text on test screen.
                getSalivaTestAdapter().getTextviewTestButton().setText("");
                getSalivaTestAdapter().getTextviewTestInstructionTop().setText(R.string.test_instruction_top2);
                getSalivaTestAdapter().getTextviewTestInstructionDown().setText("");
                // Enable progress bar.
                getSalivaTestAdapter().getProgressbar().setVisibility(View.VISIBLE);
                // Disable center button.
                getSalivaTestAdapter().getImagebuttonTestButton().setClickable(false);

                // Enable corresponding component to store test results.
                getSalivaTestAdapter().initTestLogComponents();

                // Transit to TestStateConnecting.
                newState = new TestStateConnecting(getSalivaTestAdapter());
                break;
            case BLE_NO_CASSETTE_PLUGGED:
                Log.d("TestState", "BLE_NO_CASSETTE_PLUGGED Idle");
                // Do nothing.
                newState = this;
                break;
            case BLE_DEVICE_CONNECTED:
                // Transit to TestStateConnecting.
                newState = new TestStateConnecting(getSalivaTestAdapter());
                newState.transit(BLE_DEVICE_CONNECTED);
                break;
            case BLE_UPDATE_SALIVA_VOLTAGE:
                newState = getSalivaTestAdapter().setToIdleState(R.string.test_null);
                break;
            default:
                newState = this;
                break;
        }
        return newState;
    }
}