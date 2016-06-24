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
public class TestStateSalivaStage1 extends TestStateTransition {

    public TestStateSalivaStage1(SalivaTestAdapter salivaTestAdapter) {
        super(salivaTestAdapter);
    }

    @Override
    public TestStateTransition transit(int trigger) {
        TestStateTransition newState = null;
        switch (trigger) {
            case BLE_NO_CASSETTE_PLUGGED:
                Log.d("TestState", "TestStateTransition BLE_NO_CASSETTE_PLUGGED");
                // Show CustomToastCassette.
                CustomToastCassette.generateToast();

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
                Log.d("TestState", "TestStateSalivaStage1 BLE_DEVICE_DISCONNECTED");
                // Try to reconnect saliva device.
                getSalivaTestAdapter().getBle().bleConnect();
                newState = this;
                break;

            case BLE_CASSETTE_PLUGGED:
                Log.d("TestState", "TestStateSalivaStage1 "+trigger);
                newState = this;
                break;
            case BLE_WRITE_CHAR_FAIL:
                // Send request saliva voltage to device.
                getSalivaTestAdapter().sendRequestSalivaVoltage();
                newState = this;
                break;
            case BLE_UPDATE_SALIVA_VOLTAGE:
                Log.d("TestState", "TestStateSalivaStage1 BLE_UPDATE_SALIVA_VOLTAGE "+getSalivaTestAdapter().getSalivaVoltageQueueSum());
                // If first cassette electrode is conducted, transit to TestStateSalivaStage2.
                if(getSalivaTestAdapter().getSalivaVoltageQueueSum()
                        > SalivaTestAdapter.FIRST_VOLTAGE_THRESHOLD) {
                    Log.d("TestState", "FIRST_VOLTAGE_THRESHOLD");

                    // Set corresponding text on test screen.
                    getSalivaTestAdapter().getTextviewTestInstructionTop().setText("");
                    getSalivaTestAdapter().getTextviewTestInstructionDown().setText(R.string.test_instruction_down3);

                    // Visible getImageGuideCassette.
                    getSalivaTestAdapter().getImageGuideCassette().setBackgroundResource(R.drawable.draw_cassette);
                    getSalivaTestAdapter().getImageGuideCassette().setVisibility(View.VISIBLE);

                    // Invisible progress bar.
                    getSalivaTestAdapter().getProgressbar().setVisibility(View.GONE);


                    // Cancel stage1 countdown timer.
                    getSalivaTestAdapter().getStage1TestCountdown().cancel();

                    // Start CameraRecorder
                    getSalivaTestAdapter().getCameraRecorder().start();

                    // Enable stage2 countdown timer.
                    getSalivaTestAdapter().startStage2Countdown();

                    /*** Should label this cassette ID as used ***/

                    // Transit to TestStateSalivaStage2 (stage2).
                    newState = new TestStateSalivaStage2(getSalivaTestAdapter());
                }
                else // Stay in current state.
                    newState = this;
                break;

            default:
                Log.d("TestState", "TestStateSalivaStage1 default "+trigger);
                newState = this;
                break;
        }
        return newState;
    }
}