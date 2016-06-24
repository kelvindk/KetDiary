package ubicomp.ketdiary.fragments.saliva_test.test_states;

import android.os.Handler;
import android.util.Log;
import android.view.View;

import ubicomp.ketdiary.R;
import ubicomp.ketdiary.fragments.saliva_test.CustomToastCassette;
import ubicomp.ketdiary.fragments.saliva_test.SalivaTestAdapter;

/**
 * Created by kelvindk on 16/6/19.
 */
public class TestStateSalivaStage3 extends TestStateTransition {

    public TestStateSalivaStage3(SalivaTestAdapter salivaTestAdapter) {
        super(salivaTestAdapter);
    }

    @Override
    public TestStateTransition transit(int trigger) {
        TestStateTransition newState = null;
        switch (trigger) {
            case BLE_NO_CASSETTE_PLUGGED:
                Log.d("TestState", "TestStateSalivaStage3 BLE_NO_CASSETTE_PLUGGED");
                // Show CustomToastCassette.
                CustomToastCassette.generateToast();

                // Cancel Stage3TestCountdown .
                getSalivaTestAdapter().getStage3TestCountdown().cancel();

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
                getSalivaTestAdapter().setEnableBlockedForTest(true);

                break;
            case BLE_DEVICE_DISCONNECTED:
                Log.d("TestState", "TestStateSalivaStage3 BLE_DEVICE_DISCONNECTED");
                // Try to reconnect saliva device.
                getSalivaTestAdapter().getBle().bleConnect();
                newState = this;
                break;

            case BLE_CASSETTE_PLUGGED:
                Log.d("TestState", "TestStateSalivaStage3 "+trigger);
                newState = this;
                break;
            case BLE_WRITE_CHAR_FAIL:
                // Send saliva voltage request to device.
                getSalivaTestAdapter().sendRequestSalivaVoltage();
                newState = this;
                break;
            case BLE_UPDATE_SALIVA_VOLTAGE:
                Log.d("TestState", "TestStateSalivaStage3 BLE_UPDATE_SALIVA_VOLTAGE "+getSalivaTestAdapter().getSalivaVoltageQueueSum());
                // Determine saliva voltage whether dropping to low threshold.
                if(getSalivaTestAdapter().getSalivaVoltageQueueSum()
                        < SalivaTestAdapter.SECOND_VOLTAGE_THRESHOLD) {
                    Log.d("TestState", "TestStateSalivaStage3 SECOND_VOLTAGE_THRESHOLD");

                    // Finish saliva spit process, transit to TestStateFinish.
                    newState = new TestStateFinish(getSalivaTestAdapter());
                }
                else
                    newState = this;
                break;
            case TEST_TRANSIT_STAGE3_RESPIT:
                // Play supply audio feedback. (Don't know why called "supply")
                getSalivaTestAdapter().playSupplyAudio();

                // Disable progress bar.
                getSalivaTestAdapter().getProgressbar().setVisibility(View.GONE);

                // Visible getImageGuideCassette.
                getSalivaTestAdapter().getImageGuideCassette().setBackgroundResource(R.drawable.spit_to_cassette);
                getSalivaTestAdapter().getImageGuideCassette().setVisibility(View.VISIBLE);

                // Set corresponding text on test screen.
                getSalivaTestAdapter().getTextviewTestInstructionTop().setText(R.string.test_instruction_top9);
                getSalivaTestAdapter().getTextviewTestInstructionDown().setText("");

                // Transit to TestStateSalivaStage3Respit and start startStage3RespitCountdown.
                getSalivaTestAdapter().startStage3RespitCountdown();
                newState = new TestStateSalivaStage3Respit(getSalivaTestAdapter());
                break;
            default:
                Log.d("TestState", "TestStateSalivaStage3 default "+trigger);
                newState = this;
                break;
        }
        return newState;
    }
}