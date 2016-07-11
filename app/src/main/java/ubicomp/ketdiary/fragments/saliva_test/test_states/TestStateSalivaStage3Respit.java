package ubicomp.ketdiary.fragments.saliva_test.test_states;

import android.os.Handler;
import android.util.Log;
import android.view.View;

import ubicomp.ketdiary.R;
import ubicomp.ketdiary.fragments.saliva_test.CustomToastCassette;
import ubicomp.ketdiary.fragments.saliva_test.SalivaTestAdapter;
import ubicomp.ketdiary.utility.data.structure.TestDetail;
import ubicomp.ketdiary.utility.system.PreferenceControl;

/**
 * Created by kelvindk on 16/6/19.
 */
public class TestStateSalivaStage3Respit extends TestStateTransition {

    public TestStateSalivaStage3Respit(SalivaTestAdapter salivaTestAdapter) {
        super(salivaTestAdapter);
    }

    @Override
    public TestStateTransition transit(int trigger) {
        TestDetail testDetail = null;

        TestStateTransition newState = null;
        switch (trigger) {
            case BLE_NO_CASSETTE_PLUGGED:
                Log.d("TestState", "TestStateSalivaStage3Respit BLE_NO_CASSETTE_PLUGGED");
                // Show CustomToastCassette.
                CustomToastCassette.generateToast();

                // Cancel Stage3RespitTestCountdown .
                getSalivaTestAdapter().getStage3RespitTestCountdown().cancel();

                // Set corresponding text on test screen.
                getSalivaTestAdapter().getTextviewTestButton().setText(R.string.test_start);
                getSalivaTestAdapter().getTextviewTestInstructionTop().setText(R.string.test_instruction_top4);
                getSalivaTestAdapter().getTextviewTestInstructionDown().setText(R.string.test_instruction_down1);

                // Disable progress bar.
                getSalivaTestAdapter().getProgressbar().setVisibility(View.GONE);

                // Invisible getImageGuideCassette.
                getSalivaTestAdapter().getImageGuideCassette().setVisibility(View.GONE);

                // Enable center button after 2.5s.
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getSalivaTestAdapter().getImagebuttonTestButton().setClickable(true);
                    }
                }, 2500);

                // Transit to TestStateIdle.
                newState = new TestStateIdle(getSalivaTestAdapter());

                // Disconnect BLE connection with device.
                getSalivaTestAdapter().getBle().bleSelfDisconnection();

                // Enable related phone components that can affect saliva test.
                getSalivaTestAdapter().setEnableUiComponents(true);

                testDetail = new TestDetail(PreferenceControl.getCassetteId()+"",
                        PreferenceControl.getUpdateDetectionTimestamp(),
                        TestDetail.TEST_SALIVA_STAGE3_RESPIT,
                        PreferenceControl.getPassVoltage1(),
                        PreferenceControl.getPassVoltage2(),
                        PreferenceControl.getBatteryLevel(),
                        0, 0,
                        "NO_PLUG",
                        "" );

                getSalivaTestAdapter().getTestDB().addTestDetail(testDetail);

                break;
            case BLE_DEVICE_DISCONNECTED:
                Log.d("TestState", "TestStateSalivaStage3Respit BLE_DEVICE_DISCONNECTED");
                // Try to reconnect saliva device.
                getSalivaTestAdapter().getBle().bleConnect();

                testDetail = new TestDetail(PreferenceControl.getCassetteId()+"",
                        PreferenceControl.getUpdateDetectionTimestamp(),
                        TestDetail.TEST_SALIVA_STAGE3_RESPIT,
                        PreferenceControl.getPassVoltage1(),
                        PreferenceControl.getPassVoltage2(),
                        PreferenceControl.getBatteryLevel(),
                        0, 0,
                        "BLE_DISC_RE",
                        "" );

                getSalivaTestAdapter().getTestDB().addTestDetail(testDetail);

                newState = this;
                break;

            case BLE_CASSETTE_PLUGGED:
                Log.d("TestState", "TestStateSalivaStage3Respit "+trigger);
                newState = this;
                break;
            case BLE_WRITE_CHAR_FAIL:
                // Send saliva voltage request to device.
                getSalivaTestAdapter().sendRequestSalivaVoltage();
                newState = this;
                break;
            case BLE_UPDATE_SALIVA_VOLTAGE:
                Log.d("TestState", "TestStateSalivaStage3Respit BLE_UPDATE_SALIVA_VOLTAGE "+getSalivaTestAdapter().getSalivaVoltageAverage());
                // Determine saliva voltage whether dropping to low threshold.
                if(getSalivaTestAdapter().getSalivaVoltageAverage()
                        < SalivaTestAdapter.SECOND_VOLTAGE_THRESHOLD) {
                    Log.d("TestState", "TestStateSalivaStage3Respit SECOND_VOLTAGE_THRESHOLD");

                    PreferenceControl.setPassVoltage2(getSalivaTestAdapter().getSalivaVoltageAverage());

                    // Finish saliva spit process, transit to TestStateWaitResult.
                    newState = new TestStateWaitResult(getSalivaTestAdapter());
                }
                else
                    newState = this;
                break;
            case TEST_FINISH:
                // Transit to TestStateWaitResult.
                newState = new TestStateWaitResult(getSalivaTestAdapter());
                break;
            default:
                Log.d("TestState", "TestStateSalivaStage3Respit default "+trigger);
                newState = this;
                break;
        }
        return newState;
    }
}