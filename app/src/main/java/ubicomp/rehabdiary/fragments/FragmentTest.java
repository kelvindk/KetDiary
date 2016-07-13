package ubicomp.rehabdiary.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ubicomp.rehabdiary.MainActivity;
import ubicomp.rehabdiary.R;
import ubicomp.rehabdiary.fragments.saliva_test.SalivaTestAdapter;
import ubicomp.rehabdiary.main_activity.FragmentSwitcher;
import ubicomp.rehabdiary.utility.system.PreferenceControl;
import ubicomp.rehabdiary.utility.test.bluetoothle.BluetoothLE;

/**
 * A placeholder fragment containing a simple view for Test fragment.
 */
public class FragmentTest extends Fragment {
    private MainActivity mainActivity = null;
    private FragmentSwitcher fragmentSwitcher = null;

    private SalivaTestAdapter salivaTestAdapter = null;


    private TextView textviewToolbar = null;

    public FragmentTest() {

    }

    @SuppressLint("ValidFragment")
    public FragmentTest(FragmentSwitcher fragmentSwitcher, MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        this.fragmentSwitcher = fragmentSwitcher;

        textviewToolbar = (TextView) mainActivity.findViewById(R.id.textview_toolbar);

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Ket", "onCreate fragment_test");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Ket", "onCreateView fragment_test");

        if(fragmentSwitcher != null)
            fragmentSwitcher.setFragmentOnlyDowndropTab(FragmentSwitcher.FRAGMENT_TEST);


        View fragmentTestView = inflater.inflate(R.layout.fragment_test, container, false);

        return fragmentTestView;
    }

    @Override
    public void onStart() {
        Log.d("Ket", "FragmentTest onStart "+PreferenceControl.isResultServiceRunning());

        // New SalivaTestAdapter for handling the saliva test process.
        if(salivaTestAdapter == null)
            salivaTestAdapter = new SalivaTestAdapter(mainActivity);
        else if(salivaTestAdapter.getCurrentState().getClass().getSimpleName().
                equals("TestStateIdle") ){
            salivaTestAdapter.setEnableUiComponents(true);
        }

        // Set fragment to FRAGMENT_TEST_PENDING if saliva test is running.
        if(PreferenceControl.isResultServiceRunning()) {
            if(fragmentSwitcher.getCurrentFragment() == FragmentSwitcher.FRAGMENT_TEST)
                fragmentSwitcher.setFragmentTestPending();
        }

        super.onStart();
    }

//    @Override
//    public void onResume() {
//        Log.d("Ket", "FragmentTest onResume");
//
//
//        super.onResume();
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case BluetoothLE.REQUEST_ENABLE_BT:
                if (resultCode == Activity.RESULT_OK) {
                    Log.d("Ket", "FragmentTest onActivityResult RESULT_OK "+requestCode+" "+resultCode);
                    salivaTestAdapter.bleEnableUserPressConfirm();
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    Log.d("Ket", "FragmentTest onActivityResult RESULT_CANCELED "+requestCode+" "+resultCode);
                    // Forward the result of BLE enabling.
                    salivaTestAdapter.bleEnableUserPressCancel();
                }
        }

    }

//    @Override
//    public void onStop() {
//        Log.d("Ket", "FragmentTest onStop");
//
//
//
//        super.onStop();
//    }

    @Override
    public void onDestroy() {
        Log.d("Ket", "FragmentTest onDestroy");

//        // Close voltage recording and pause cameraRecorder.
//        VoltageFileHandler voltageFileHandler = salivaTestAdapter.getVoltageFileHandler();
//        if (voltageFileHandler != null)
//            voltageFileHandler.close();
//        CameraRecorder cameraRecorder = salivaTestAdapter.getCameraRecorder();
//        if (cameraRecorder != null)
//            cameraRecorder.close();

        // Request disconnect and disable notification to device.
        BluetoothLE ble = salivaTestAdapter.getBle();
        if(ble != null) {
//            ble.bleUnlockDevice();
//            ble.bleDerequestSalivaVoltage();
//            ble.bleCancelCassetteInfo();
//            ble.bleSelfDisconnection();
            ble.bleHardTermination();
        }
//
//        // Enable related phone components that can affect saliva test.
//        salivaTestAdapter.setEnableUiComponents(true);

        salivaTestAdapter.setToIdleState(R.string.test_null);

        // Cancel all countdown timer in currentState of TestStateTransition.
        salivaTestAdapter.cancelTestStateCountdownTimer();


        super.onDestroy();
    }



}