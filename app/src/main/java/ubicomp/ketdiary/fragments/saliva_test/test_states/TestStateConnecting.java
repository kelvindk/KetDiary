package ubicomp.ketdiary.fragments.saliva_test.test_states;

import android.os.Handler;
import android.util.Log;
import android.view.View;

import ubicomp.ketdiary.R;
import ubicomp.ketdiary.fragments.saliva_test.SalivaTestAdapter;


/**
 * Created by kelvindk on 16/6/19.
 */
public class TestStateConnecting extends TestStateTransition {

    public TestStateConnecting(SalivaTestAdapter salivaTestAdapter) {
        super(salivaTestAdapter);
    }

    @Override
    public TestStateTransition transit(int trigger) {
        TestStateTransition newState = null;
        switch (trigger) {
            case BLE_ENABLE_USER_PRESS_CANCEL:
                Log.d("TestState", "BLE_ENABLE_USER_PRESS_CANCEL");
                // Set corresponding text on test screen.
                getSalivaTestAdapter().getTextviewTestButton().setText(R.string.test_start);
                getSalivaTestAdapter().getTextviewTestInstructionTop().setText(R.string.test_instruction_top1);
                getSalivaTestAdapter().getTextviewTestInstructionDown().setText(R.string.test_instruction_down1);
                // Disable progress bar.
                getSalivaTestAdapter().getProgressbar().setVisibility(View.GONE);
                // Enable center button.
                getSalivaTestAdapter().getImagebuttonTestButton().setClickable(true);
                // Transit to TestStateIdle.
                newState = new TestStateIdle(getSalivaTestAdapter());
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
                getSalivaTestAdapter().setEnableBlockedForTest(true);
                // Transit to TestStateIdle.
                newState = new TestStateIdle(getSalivaTestAdapter());
                break;
            case BLE_DEVICE_CONNECTED:
                Log.d("TestState", "BLE_DEVICE_CONNECTED");
                // Request cassette info from device with delay 500ms.
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getSalivaTestAdapter().getBle().bleRequestCassetteInfo();
                    }
                }, 1000);

                // Transit to TestStateConnected.
                newState = new TestStateConnected(getSalivaTestAdapter());

        }
        return newState;
    }
}