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
public class TestStateSalivaStage2 extends TestStateTransition {

    public TestStateSalivaStage2(SalivaTestAdapter salivaTestAdapter) {
        super(salivaTestAdapter);
    }

    @Override
    public TestStateTransition transit(int trigger) {
        TestStateTransition newState = null;
        switch (trigger) {
            case BLE_NO_CASSETTE_PLUGGED:
                Log.d("TestState", "TestStateSalivaStage2 BLE_NO_CASSETTE_PLUGGED");
                // Show CustomToastCassette.
                CustomToastCassette.generateToast();

                // Cancel Stage2TestCountdown.
                getSalivaTestAdapter().getStage2TestCountdown().cancel();

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
                Log.d("TestState", "TestStateSalivaStage2 BLE_DEVICE_DISCONNECTED");
                // Try to reconnect saliva device.
                getSalivaTestAdapter().getBle().bleConnect();
                newState = this;
                break;

            case BLE_CASSETTE_PLUGGED:
                Log.d("TestState", "TestStateSalivaStage2 "+trigger);
                newState = this;
                break;
            case BLE_WRITE_CHAR_FAIL:
                // Send saliva voltage request to device.
                getSalivaTestAdapter().sendRequestSalivaVoltage();
                newState = this;
                break;
            case BLE_UPDATE_SALIVA_VOLTAGE:
                Log.d("TestState", "TestStateSalivaStage2 BLE_UPDATE_SALIVA_VOLTAGE "+getSalivaTestAdapter().getSalivaVoltageQueueSum());
//                if(getSalivaTestAdapter().getSalivaVoltageQueueSum()
//                        < SalivaTestAdapter.SECOND_VOLTAGE_THRESHOLD) {
//                    Log.d("TestState", "SECOND_VOLTAGE_THRESHOLD");
//
//                    // Transit to
//                    newState = this;
//                }
//                else
                    newState = this;
                break;
            case TEST_TRANSIT_STAGE3:
                // Set corresponding text on test screen.
                getSalivaTestAdapter().getTextviewTestInstructionTop().setText("");
                getSalivaTestAdapter().getTextviewTestInstructionDown().setText(R.string.test_instruction_down4);

                // Invisible getImageGuideCassette.
                getSalivaTestAdapter().getImageGuideCassette().setVisibility(View.GONE);

                // Enable progress bar.
                getSalivaTestAdapter().getProgressbar().setVisibility(View.VISIBLE);

                // Transit to TestStateSalivaStage3 and start startStage3Countdown.
                getSalivaTestAdapter().startStage3Countdown();
                newState = new TestStateSalivaStage3(getSalivaTestAdapter());
                break;
            default:
                Log.d("TestState", "TestStateSalivaStage2 default "+trigger);
                newState = this;
                break;
        }
        return newState;
    }
}