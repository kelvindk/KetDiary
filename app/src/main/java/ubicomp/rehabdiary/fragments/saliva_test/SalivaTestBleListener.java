package ubicomp.rehabdiary.fragments.saliva_test;

import android.content.Context;
import android.graphics.Bitmap;

import ubicomp.rehabdiary.utility.test.bluetoothle.BluetoothListener;

/**
 * Implement the listener for receive callback from BluetoothLE
 *
 * Created by kelvindk on 16/6/16.
 */
public class SalivaTestBleListener implements BluetoothListener {
    @Override
    public Context getContext() {
        return null;
    }

    @Override
    public void bleNotSupported() {

    }

    @Override
    public void bleConnectionTimeout() {

    }

    @Override
    public void bleConnected() {

    }

    @Override
    public void bleDisconnected() {

    }

    @Override
    public void bleWriteCharacteristic1Success() {

    }

    @Override
    public void bleWriteStateFail() {

    }

    @Override
    public void bleNoPlugDetected() {

    }

    @Override
    public void blePlugInserted(int cassetteId) {

    }

    @Override
    public void bleUpdateBattLevel(int battVolt) {

    }

    @Override
    public void notifyDeviceVersion(int version) {

    }

    @Override
    public void bleUpdateSalivaVolt(int salivaVolt) {

    }

    @Override
    public void bleGetImageSuccess(Bitmap bitmap) {

    }

    @Override
    public void bleGetImageFailure(float dropoutRate) {

    }

    @Override
    public void bleNotifyDetectionResult(double score) {

    }

    @Override
    public void bleReturnDeviceVersion(int version) {

    }
}
