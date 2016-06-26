package ubicomp.ketdiary.fragments.saliva_test.test_states;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;

import ubicomp.ketdiary.MainActivity;
import ubicomp.ketdiary.R;
import ubicomp.ketdiary.fragments.saliva_test.SalivaTestAdapter;

/**
 * Created by kelvindk on 16/6/19.
 */
public class TestStateWaitResult extends TestStateTransition {

    public static final int STAGE_FINAL_COUNTDOWN = 6000;
    public static final int STAGE_FINAL_PERIOD = 1000;

    private  CountDownTimer finishCountdown = null;

    public TestStateWaitResult(SalivaTestAdapter salivaTestAdapter) {
        super(salivaTestAdapter);

        // Disable test countdowns of stage3 and stage3respit.
        CountDownTimer stage3TestCountdown = salivaTestAdapter.getStage3TestCountdown();
        if(stage3TestCountdown != null)
            stage3TestCountdown.cancel();
        CountDownTimer stage3RespitTestCountdown = salivaTestAdapter.getStage3RespitTestCountdown();
        if(stage3RespitTestCountdown != null)
            stage3RespitTestCountdown.cancel();

        // Set corresponding text on test screen.
        salivaTestAdapter.getTextviewTestButton().setBackgroundResource(R.drawable.check_test);
        salivaTestAdapter.getTextviewTestInstructionTop().setText(R.string.test_instruction_top10);
        salivaTestAdapter.getTextviewTestInstructionDown().setText("");
        salivaTestAdapter.setEnableUiComponents(true);

        // Disconnect BLE connection with device.
        salivaTestAdapter.getBle().bleSelfDisconnection();

        // Close voltage recording and pause cameraRecorder.
        salivaTestAdapter.getVoltageFileHandler().close();
        salivaTestAdapter.getCameraRecorder().pause();

        // Invisible progress bar.
        salivaTestAdapter.getProgressbar().setVisibility(View.GONE);

        // Invisible getImageGuideCassette.
        salivaTestAdapter.getImageGuideCassette().setVisibility(View.GONE);

        // Enable related phone components that can affect saliva test.
        salivaTestAdapter.setEnableUiComponents(true);

        // Countdown to finish saliva test(collection).
        finishCountdown = new CountDownTimer(STAGE_FINAL_COUNTDOWN, STAGE_FINAL_PERIOD){
            @Override
            public void onFinish() {
                Log.d("TestState", "TestStateWaitResult countdown onFinish");
                // Reset to Idle state.
                getSalivaTestAdapter().setToIdleState(R.string.test_null);

                /*** Start TestResultService to wait result after about 10min ***/

            }

            @Override
            public void onTick(long millisUntilFinished) {
                MainActivity mainActivity = getSalivaTestAdapter().getMainActivity();
                String countdownString =
                        mainActivity.getResources().getString(R.string.test_instruction_down6_left) + " " +
                                (millisUntilFinished/ STAGE_FINAL_PERIOD) + " " +
                                mainActivity.getResources().getString(R.string.test_instruction_down6_right);
                getSalivaTestAdapter().getTextviewTestInstructionDown().setText(countdownString);
            }
        }.start();

    }


    @Override
    public TestStateTransition transit(int trigger) {
        TestStateTransition newState = null;
        switch (trigger) {

            default:
                Log.d("TestState", "TestStateWaitResult default "+trigger);
                newState = this;
                break;
        }
        return newState;
    }
}