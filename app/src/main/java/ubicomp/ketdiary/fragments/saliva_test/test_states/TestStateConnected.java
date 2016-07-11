package ubicomp.ketdiary.fragments.saliva_test.test_states;

import android.os.CountDownTimer;
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
public class TestStateConnected extends TestStateTransition {

    public static final int PREPARE_TO_TEST_COUNTDOWN = 13200;
    public static final int PREPARE_TO_TEST_COUNTDOWN_PERIOD = 1200;

    // Declare newSate here to allow accessing by CountDownTimer.
    private TestStateTransition newState = null;
    private CountDownTimer prepareSalivaCountdown = null;

    // Boolean of cassette ID is whether passed validation.
    boolean isCassetteIdValid = false;

    public TestStateConnected(SalivaTestAdapter salivaTestAdapter) {
        super(salivaTestAdapter);

        /*** Before start the ten second countdown timer ***/
        // Set corresponding text on test screen.
        getSalivaTestAdapter().getTextviewTestInstructionTop().setText(R.string.test_instruction_top5);
        getSalivaTestAdapter().getTextviewTestInstructionDown().setText(R.string.test_instruction_down2);
        // Disable progress bar.
        getSalivaTestAdapter().getProgressbar().setVisibility(View.GONE);


        /*** Start ten second countdown. The period is slightly longer than 1 second. ***/
        prepareSalivaCountdown = new CountDownTimer(PREPARE_TO_TEST_COUNTDOWN, PREPARE_TO_TEST_COUNTDOWN_PERIOD){
            @Override
            public void onFinish() {

                // Play sound feedback ding ding.
                getSalivaTestAdapter().playDingDingAudio();
                // Set corresponding text on test screen.
                getSalivaTestAdapter().getTextviewTestButton().setText("");
                getSalivaTestAdapter().getTextviewTestInstructionTop().setText("");
                getSalivaTestAdapter().getTextviewTestInstructionDown().setText(R.string.test_instruction_top6);
                // Visible progress_bar_test on the screen.
                getSalivaTestAdapter().getProgressbar().setVisibility(View.VISIBLE);

                // Visible getImageGuideCassette.
                getSalivaTestAdapter().getImageGuideCassette().setBackgroundResource(R.drawable.spit_to_cassette);
                getSalivaTestAdapter().getImageGuideCassette().setVisibility(View.VISIBLE);

                // Visible face anchor on test screen.
                getSalivaTestAdapter().getImageFaceAnchor().setVisibility(View.VISIBLE);

                // Send request saliva voltage to device.
                getSalivaTestAdapter().sendRequestSalivaVoltage();

                // Start CameraRecorder, stage1Countdown and taking photos periodically.
                getSalivaTestAdapter().getCameraRecorder().start();
                getSalivaTestAdapter().startStage1CountdownAndPeriodPhotoShot();

                // Transit TestStateSalivaStage1.
                getSalivaTestAdapter().
                        setCurrentState(new TestStateSalivaStage1(getSalivaTestAdapter()));
            }

            @Override
            public void onTick(long millisUntilFinished) {
                // Play sound feedback short beep every second.
                getSalivaTestAdapter().playShortBeepAudio();
                getSalivaTestAdapter().getTextviewTestButton().
                        setText(""+(millisUntilFinished/PREPARE_TO_TEST_COUNTDOWN_PERIOD));


            }
        }.start();
    }

    @Override
    public TestStateTransition transit(int trigger) {
        TestDetail testDetail = null;

        switch (trigger) {
            case BLE_NO_CASSETTE_PLUGGED:
                Log.d("TestState", "BLE_NO_CASSETTE_PLUGGED");
                // Show CustomToastCassette.
                CustomToastCassette.generateToast();

                // Set corresponding text on test screen.
                getSalivaTestAdapter().getTextviewTestButton().setText(R.string.test_start);
                getSalivaTestAdapter().getTextviewTestInstructionTop().setText(R.string.test_instruction_top4);
                getSalivaTestAdapter().getTextviewTestInstructionDown().setText(R.string.test_instruction_down1);

                // Disable progress bar.
                getSalivaTestAdapter().getProgressbar().setVisibility(View.GONE);

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

                // Finish the prepareSalivaCountdown.
                if(prepareSalivaCountdown != null)
                    prepareSalivaCountdown.cancel();

                // Disconnect BLE connection with device.
                getSalivaTestAdapter().getBle().bleSelfDisconnection();

                // Enable related phone components that can affect saliva test.
                getSalivaTestAdapter().setEnableUiComponents(true);

                testDetail = new TestDetail(PreferenceControl.getCassetteId()+"",
                        PreferenceControl.getUpdateDetectionTimestamp(),
                        TestDetail.TEST_CONNECTED,
                        PreferenceControl.getPassVoltage1(),
                        PreferenceControl.getPassVoltage2(),
                        PreferenceControl.getBatteryLevel(),
                        0, 0,
                        "NO_PLUG",
                        "" );

                getSalivaTestAdapter().getTestDB().addTestDetail(testDetail);

                break;

            case DEVICE_LOW_BATTERY:
                Log.d("TestState", "DEVICE_LOW_BATTERY");

                // Finish the prepareSalivaCountdown.
                if(prepareSalivaCountdown != null)
                    prepareSalivaCountdown.cancel();

                // Transit to StateIdle and show error message.
                newState = getSalivaTestAdapter().setToIdleState(R.string.test_instruction_top11);

                testDetail = new TestDetail(PreferenceControl.getCassetteId()+"",
                        PreferenceControl.getUpdateDetectionTimestamp(),
                        TestDetail.TEST_CONNECTED,
                        PreferenceControl.getPassVoltage1(),
                        PreferenceControl.getPassVoltage2(),
                        PreferenceControl.getBatteryLevel(),
                        0, 0,
                        "LOW_BAT",
                        "" );

                getSalivaTestAdapter().getTestDB().addTestDetail(testDetail);

                break;

            case BLE_CASSETTE_PLUGGED:
                int pluggedCassetteId = getSalivaTestAdapter().getPluggedCassetteId();
                Log.d("TestState", "BLE_CASSETTE_PLUGGED "+pluggedCassetteId);


                if(!isCassetteIdValid) {
                    isCassetteIdValid = true;
                    /*** Check cassette ID from database ***/
                    if((!getSalivaTestAdapter().getTestDB().isDeveloper()) &&
                            (getSalivaTestAdapter().getTestDB().checkCassetteUsed("CT_"+pluggedCassetteId))) {
                        // Cancel the prepareSalivaCountdown.
                        if(prepareSalivaCountdown != null)
                            prepareSalivaCountdown.cancel();

                        // Cassette is used.
                        newState = getSalivaTestAdapter().setToIdleState(R.string.test_instruction_top14);

                        testDetail = new TestDetail(pluggedCassetteId+"",
                                PreferenceControl.getUpdateDetectionTimestamp(),
                                TestDetail.TEST_CONNECTED,
                                PreferenceControl.getPassVoltage1(),
                                PreferenceControl.getPassVoltage2(),
                                PreferenceControl.getBatteryLevel(),
                                0, 0,
                                "CT_USED",
                                "" );

                        getSalivaTestAdapter().getTestDB().addTestDetail(testDetail);
                        Log.d("BLE", "Cassette USED "+pluggedCassetteId);
                        break;
                    }

                    // Test shotting function of device.
                    Log.d("BLE", "take Pic!");
                    getSalivaTestAdapter().getBle().bleTakePicture();
                }

                newState = this;
                break;
            case BLE_GET_IMAGE_SUCCESS:
                Log.d("TestState", "BLE_GET_IMAGE_SUCCESS ");

                // Enable cassette info again. MAY NOT NECESSARY, NEED CHECK!
                getSalivaTestAdapter().getBle().bleRequestCassetteInfo();

                newState = this;
                break;
            case BLE_GET_IMAGE_FAILURE:
                Log.d("TestState", "BLE_GET_IMAGE_FAILURE ");

                // Finish the prepareSalivaCountdown.
                if(prepareSalivaCountdown != null)
                    prepareSalivaCountdown.cancel();

                // Transit to StateIdle and show error message.
                newState = getSalivaTestAdapter().setToIdleState(R.string.test_instruction_top12);

                testDetail = new TestDetail(PreferenceControl.getCassetteId()+"",
                        PreferenceControl.getUpdateDetectionTimestamp(),
                        TestDetail.TEST_CONNECTED,
                        PreferenceControl.getPassVoltage1(),
                        PreferenceControl.getPassVoltage2(),
                        PreferenceControl.getBatteryLevel(),
                        0, 0,
                        "BLE_IMG_FAIL",
                        "" );

                getSalivaTestAdapter().getTestDB().addTestDetail(testDetail);

                break;
            case CANCEL_COUNTDOWN_TIMER:
                Log.d("TestState", "CANCEL_COUNTDOWN_TIMER ");
                // Cancel prepareSalivaCountdown timer.
                if(prepareSalivaCountdown != null)
                    prepareSalivaCountdown.cancel();
                break;
            default:
                newState = this;
                break;
        }
        return newState;
    }

    private void setNewState(TestStateTransition newState) {
        this.newState = newState;
    }
}