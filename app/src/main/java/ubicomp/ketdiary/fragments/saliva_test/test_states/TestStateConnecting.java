package ubicomp.ketdiary.fragments.saliva_test.test_states;

import android.os.Handler;
import android.util.Log;
import android.view.View;

import ubicomp.ketdiary.R;
import ubicomp.ketdiary.fragments.saliva_test.SalivaTestAdapter;
import ubicomp.ketdiary.utility.data.structure.TestDetail;
import ubicomp.ketdiary.utility.system.PreferenceControl;


/**
 * Created by kelvindk on 16/6/19.
 */
public class TestStateConnecting extends TestStateTransition {

    public TestStateConnecting(SalivaTestAdapter salivaTestAdapter) {
        super(salivaTestAdapter);
    }

    @Override
    public TestStateTransition transit(int trigger) {
        TestDetail testDetail = null;

        TestStateTransition newState = null;
        switch (trigger) {
            case BLE_ENABLE_USER_PRESS_CANCEL:
                Log.d("TestState", "BLE_ENABLE_USER_PRESS_CANCEL");

                // Transit to StateIdle and show error message.
                newState = getSalivaTestAdapter().setToIdleState(R.string.test_instruction_top1);

                break;
            case BLE_ENABLE_USER_PRESS_CONFIRM:
                Log.d("TestState", "BLE_ENABLE_USER_PRESS_CONFIRM");
                // Set corresponding text on test screen.
                getSalivaTestAdapter().getTextviewTestButton().setText("");
                getSalivaTestAdapter().getTextviewTestInstructionTop().setText(R.string.test_instruction_top2);
                // Disable progress bar.
                getSalivaTestAdapter().getProgressbar().setVisibility(View.VISIBLE);
                // Try to connect saliva device.
                getSalivaTestAdapter().getBle().bleConnect();
                // Transit to same state
                newState = this;
                break;
            case BLE_CONNECTION_TIMEOUT:
                Log.d("TestState", "BLE_CONNECTION_TIMEOUT");
                // Set corresponding text on test screen.
                getSalivaTestAdapter().getTextviewTestButton().setText(R.string.test_start);
                getSalivaTestAdapter().getTextviewTestInstructionTop().setText(R.string.test_instruction_top3);
                getSalivaTestAdapter().getTextviewTestInstructionDown().setText(R.string.test_instruction_down1);
                // Disable progress bar.
                getSalivaTestAdapter().getProgressbar().setVisibility(View.GONE);
                // Enable center button.
                getSalivaTestAdapter().getImagebuttonTestButton().setClickable(true);
                getSalivaTestAdapter().setEnableUiComponents(true);
                // Transit to TestStateIdle.
                newState = new TestStateIdle(getSalivaTestAdapter());

                testDetail = new TestDetail(PreferenceControl.getCassetteId()+"",
                        PreferenceControl.getUpdateDetectionTimestamp(),
                        TestDetail.TEST_CONNECTING,
                        PreferenceControl.getPassVoltage1(),
                        PreferenceControl.getPassVoltage2(),
                        PreferenceControl.getBatteryLevel(),
                        0, 0,
                        "CON_TIMEOUT",
                        "" );

                getSalivaTestAdapter().getTestDB().addTestDetail(testDetail);

                break;
            case BLE_DEVICE_CONNECTED:
                Log.d("TestState", "BLE_DEVICE_CONNECTED");

                // Request cassette ID from device.
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(getSalivaTestAdapter().getBle() != null)
                            getSalivaTestAdapter().getBle().bleRequestCassetteInfo();
                    }
                }, 200);

                // Transit to TestStateConnected.
                newState = new TestStateConnected(getSalivaTestAdapter());
                break;
            case BLE_UPDATE_SALIVA_VOLTAGE:
                newState = this;
                break;
            case BLE_NO_CASSETTE_PLUGGED:
                newState = this;
                break;
            default:
                newState = this;
                break;
        }
        return newState;
    }
}