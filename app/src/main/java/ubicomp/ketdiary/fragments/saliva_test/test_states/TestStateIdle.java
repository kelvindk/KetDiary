package ubicomp.ketdiary.fragments.saliva_test.test_states;

import android.view.View;

import ubicomp.ketdiary.R;
import ubicomp.ketdiary.fragments.saliva_test.SalivaTestAdapter;
import ubicomp.ketdiary.utility.test.bluetoothle.BluetoothLE;

/**
 * Created by kelvindk on 16/6/19.
 */
public class TestStateIdle extends TestStateTransition {

    public TestStateIdle(SalivaTestAdapter salivaTestAdapter) {
        super(salivaTestAdapter);
    }

    @Override
    public TestStateTransition transit(int trigger) {
        TestStateTransition newState = null;
        switch (trigger) {
            case TEST_BUTTON_CLICK:
                BluetoothLE ble = new BluetoothLE(getSalivaTestAdapter(), "ket_49", 0);
                getSalivaTestAdapter().setBle(ble);
                // Try to connect saliva device.
                ble.bleConnect();

                // Set corresponding text on test screen.
                getSalivaTestAdapter().getTextviewTestButton().setText("");
                getSalivaTestAdapter().getTextviewTestInstructionTop().setText(R.string.test_instruction_top2);
                getSalivaTestAdapter().getTextviewTestInstructionDown().setText("");
                // Enable progress bar.
                getSalivaTestAdapter().getProgressbar().setVisibility(View.VISIBLE);
                // Disable center button.
                getSalivaTestAdapter().getImagebuttonTestButton().setClickable(false);

                // Transit to TestStateConnecting.
                newState = new TestStateConnecting(getSalivaTestAdapter());
                break;
        }
        return newState;
    }
}