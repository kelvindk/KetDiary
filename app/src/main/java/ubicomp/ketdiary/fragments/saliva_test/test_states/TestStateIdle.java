package ubicomp.ketdiary.fragments.saliva_test.test_states;

import android.util.Log;
import android.view.View;

import ubicomp.ketdiary.R;
import ubicomp.ketdiary.fragments.saliva_test.SalivaTestAdapter;
import ubicomp.ketdiary.utility.test.bluetoothle.BluetoothLE;

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
                // Get device ID from database.
                String deviceId = getSalivaTestAdapter().getTestDB().getDeviceId();

                BluetoothLE ble = new BluetoothLE(getSalivaTestAdapter(), deviceId, 0);
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
                getSalivaTestAdapter().setToIdleState(R.string.test_null);

                newState = this;
                break;
            default:
                newState = this;
                break;
        }
        return newState;
    }
}