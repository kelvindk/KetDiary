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
public class TestStateSalivaStage3Respit extends TestStateTransition {

    public TestStateSalivaStage3Respit(SalivaTestAdapter salivaTestAdapter) {
        super(salivaTestAdapter);
    }

    // 請將臉對準畫面中央，並將口水吐進管中 (三水 0)
    // 吐完足量口水後，取出口水匣後方擋片 + 圖 + 請稍候 60 秒
    // 請等待 160 秒 + 正在確認口水量 (三水 2)
    // if電壓沒降下來 => 請在 180 秒內再吐一口口水 + 口水量不足 (三水 2)
    // 口水量確認中 (三水 3)

    @Override
    public TestStateTransition transit(int trigger) {
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
                getSalivaTestAdapter().setEnableBlockedForTest(true);

                break;
            case BLE_DEVICE_DISCONNECTED:
                Log.d("TestState", "TestStateSalivaStage3Respit BLE_DEVICE_DISCONNECTED");
                // Try to reconnect saliva device.
                getSalivaTestAdapter().getBle().bleConnect();
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
                Log.d("TestState", "TestStateSalivaStage3Respit BLE_UPDATE_SALIVA_VOLTAGE "+getSalivaTestAdapter().getSalivaVoltage());
//                if(getSalivaTestAdapter().getSalivaVoltage()
//                        < SalivaTestAdapter.SECOND_VOLTAGE_THRESHOLD) {
//                    Log.d("TestState", "SECOND_VOLTAGE_THRESHOLD");
//
//                    // Transit to
//                    newState = this;
//                }
//                else
                    newState = this;
                break;
            default:
                Log.d("TestState", "TestStateSalivaStage3Respit default "+trigger);
                newState = this;
                break;
        }
        return newState;
    }
}