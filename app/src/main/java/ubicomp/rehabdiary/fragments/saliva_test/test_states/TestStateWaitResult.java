package ubicomp.rehabdiary.fragments.saliva_test.test_states;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;

import java.util.Calendar;

import ubicomp.rehabdiary.MainActivity;
import ubicomp.rehabdiary.R;
import ubicomp.rehabdiary.fragments.create_event.CreateEventActivity;
import ubicomp.rehabdiary.fragments.event.EventLogStructure;
import ubicomp.rehabdiary.fragments.saliva_test.SalivaTestAdapter;
import ubicomp.rehabdiary.utility.system.PreferenceControl;

/**
 * Created by kelvindk on 16/6/19.
 */
public class TestStateWaitResult extends TestStateTransition {

    public static final int SALIVA_TEST_INT_KEY = 980;

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

        // Disconnect BLE connection with device.
        salivaTestAdapter.getBle().bleSelfDisconnection();

        // Close voltage recording and pause cameraRecorder.
        salivaTestAdapter.getVoltageFileHandler().close();
        salivaTestAdapter.getCameraRecorder().pause();

        // Invisible progress bar.
        salivaTestAdapter.getProgressbar().setVisibility(View.GONE);

        // Invisible face anchor on test screen.
        getSalivaTestAdapter().getImageFaceAnchor().setVisibility(View.GONE);

        // Invisible getImageGuideCassette.
        salivaTestAdapter.getImageGuideCassette().setVisibility(View.GONE);

        /*** Start TestResultService to wait result after 12min ***/
        getSalivaTestAdapter().startResultService();

        // Countdown to finish saliva test(collection) and then go to CreateEventActivity.
        finishCountdown = new CountDownTimer(STAGE_FINAL_COUNTDOWN, STAGE_FINAL_PERIOD){
            @Override
            public void onFinish() {
                Log.d("TestState", "TestStateWaitResult countdown onFinish");
                // Reset to Idle state.
                getSalivaTestAdapter().setToIdleState(R.string.test_null);

                PreferenceControl.setResultServiceIsRunning(true);

                /*** Go to waiting result fragment ***/
                getSalivaTestAdapter().getMainActivity().getFragmentSwitcher().setFragmentTestPending();

                // Enable clickable of UI components.
                getSalivaTestAdapter().setEnableUiComponents(true);

                /*** Start CreateEventActivity ***/
                EventLogStructure event = new EventLogStructure();
                event.createTime = (Calendar) Calendar.getInstance().clone();
                event.isAfterTest = true;

                Intent intent =
                        new Intent(getSalivaTestAdapter().getMainActivity(), CreateEventActivity.class);
                // Put the serializable object into eventContentActivityIntent through a Bundle.
                Bundle bundle = new Bundle();
                bundle.putSerializable(EventLogStructure.EVENT_LOG_STRUCUTRE_KEY, event);
                intent.putExtras(bundle);
                // Start the activity.
                getSalivaTestAdapter().getMainActivity().startActivityForResult(intent, SALIVA_TEST_INT_KEY);

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
            case CANCEL_COUNTDOWN_TIMER:
                Log.d("TestState", "TestStateWaitResult CANCEL_COUNTDOWN_TIMER");
                finishCountdown.cancel();
                break;
            default:
                Log.d("TestState", "TestStateWaitResult default "+trigger);
                newState = this;
                break;
        }
        return newState;
    }
}