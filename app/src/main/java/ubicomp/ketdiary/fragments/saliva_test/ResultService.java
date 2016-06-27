package ubicomp.ketdiary.fragments.saliva_test;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;

import ubicomp.ketdiary.MainActivity;
import ubicomp.ketdiary.R;
import ubicomp.ketdiary.utility.system.PreferenceControl;
import ubicomp.ketdiary.utility.test.bluetoothle.BluetoothLE;
import ubicomp.ketdiary.utility.test.bluetoothle.BluetoothListener;

/**
 * Created by kelvindk on 16/6/24.
 */
public class ResultService extends Service implements BluetoothListener{

    public static final String TAG = "ResultServiceBLE";

    public static final int MSG_REGISTER_CLIENT = 1;
    public static final int MSG_UNREGISTER_CLIENT = 2;
    public static final int MSG_CURRENT_COUNTDOWN = 3;
    public static final int MSG_START_RESULT_SERVICE_COUNTDOWN = 4;
    public static final int MSG_REQUEST_SERVICE_FINISH = 5;
    public static final int MSG_IS_RUNNING = 6;
    public static final int MSG_BLE_CONNECT = 7;

    public static final int MSG_SERVICE_NOT_RUNNING = -1;
    public static final int MSG_SERVICE_FINISH = -2;
    public static final int MSG_SERVICE_FAIL_NO_PLUG = -3;
    public static final int MSG_SERVICE_FAIL_CONNECT_TIMEOUT = -4;

    public static final int WAIT_RESULT_COUNTDOWN = 130000;
    public static final int WAIT_RESULT_PERIOD = 1000;

    public static final int LAST_TWO_MINUTES = 120; //120 second
    public static final int LAST_ONE_MINUTES = 60; //60 second


    private ResultService resultService = null;

    private BluetoothLE ble = null;

    private boolean testFail = false;

    private boolean testResultIsOut = false;
    private int testResult = 0;

    private int pictureCount = 0;


    /** For showing and hiding our notification. */
    private NotificationManager mNM;
    // Visible of notification
    private boolean enableNotification = false;

    /** Keeps current registered client. */
    private Messenger mClient = null;


    /** Countdown timer for saliva test result */
    private CountDownTimer resultServiceCountdown = null;
    // Current value of countdown.
    private int currentCountdown = 0;

    @Override
    public void onCreate() {
        Log.d("ResultService", "onCreate");
        this.resultService = this;
        mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

    }


    @Override
    public void onDestroy() {
        Log.d("ResultService", "onDestroy");

        // Cancel the persistent notification.
        mNM.cancel(R.string.remote_service_started);

        if(resultServiceCountdown != null)
            resultServiceCountdown.cancel();

    }

    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }


    /**
     * Handler of incoming messages from clients.
     */
    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Log.d("ResultService", "handleMessage "+msg.what);
            switch (msg.what) {
                case MSG_REGISTER_CLIENT:
                    // Reply testFail if error occurs during testing.
                    if(testFail) {
                        try {
                            msg.replyTo.send(Message.obtain(null, MSG_CURRENT_COUNTDOWN,
                                    MSG_SERVICE_FAIL_NO_PLUG, 0));
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }

                    // Reply testFail if error occurs during testing.
                    if(testResultIsOut) {
                        try {
                            msg.replyTo.send(Message.obtain(null, MSG_CURRENT_COUNTDOWN,
                                    MSG_SERVICE_FINISH, 0));
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        testResultIsOut = false;
                    }

                    enableNotification = false;
                    mClient = msg.replyTo;
                    mNM.cancel(R.string.remote_service_started);
                    break;
                case MSG_UNREGISTER_CLIENT:
                    mClient = null;
                    enableNotification = true;
                    break;
                case MSG_START_RESULT_SERVICE_COUNTDOWN:
                    startResultServiceCountdown();
                    break;
                case MSG_IS_RUNNING:
                    // If the service is not started, then reply 0.
                    if(resultServiceCountdown == null) {
                        // Reply MainActivity this service is running or not.
                        try {
                            msg.replyTo.send(Message.obtain(null, MSG_CURRENT_COUNTDOWN,
                                    MSG_SERVICE_NOT_RUNNING, 0));
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        PreferenceControl.setResultServiceIsRunning(true);
                    }
                    break;
                case MSG_BLE_CONNECT:
                    //
                    ble = new BluetoothLE(resultService, "ket_049", PreferenceControl.getUpdateDetectionTimestamp());
                    ble.bleConnect();

                    break;
                case MSG_REQUEST_SERVICE_FINISH:
//                    onDestroy();
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    /**
     * Target we publish for clients to send messages to IncomingHandler.
     */
    final Messenger mMessenger = new Messenger(new IncomingHandler());

    // Start countdown for waiting saliva test result and send message to mainActivity every 1 second.
    public void startResultServiceCountdown() {
        resultServiceCountdown = new CountDownTimer(WAIT_RESULT_COUNTDOWN, WAIT_RESULT_PERIOD){
            @Override
            public void onFinish() {
                Log.d("ResultService", "resultServiceCountdown onFinish");
                // Cancel notification.
                mNM.cancel(R.string.remote_service_started);

                // Send MSG_CURRENT_COUNTDOWN message to foreground activity.
                currentCountdown = MSG_SERVICE_FINISH;
                if(mClient != null) {
                    try {
                        mClient.send(Message.obtain(null,
                                MSG_CURRENT_COUNTDOWN, currentCountdown, 0));
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    if(enableNotification) {
                        testResultIsOut = true;
                        showNotification(getString(R.string.test_result_is_out));
                    }

                }

                startBleDisconnect();
            }

            @Override
            public void onTick(long millisUntilFinished) {
                Log.d("ResultService", "resultServiceCountdown onTick "+millisUntilFinished/WAIT_RESULT_PERIOD);
                // Update currentCountdown to remaining time of this countdown timer.
                currentCountdown = (int) millisUntilFinished/WAIT_RESULT_PERIOD;

                // Display a notification about us starting.  We put an icon in the status bar.
                if(enableNotification)
                    showNotification(getString(R.string.test_notification_countdown)+
                            currentCountdown/60+"分"+currentCountdown%60+"秒");

                // Send MSG_CURRENT_COUNTDOWN message to foreground activity.
                if(mClient != null) {
                    try {
                        mClient.send(Message.obtain(null,
                                        MSG_CURRENT_COUNTDOWN, currentCountdown, 0));
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }

                // Take pictures at last 2min and 1min.
                if((currentCountdown == LAST_TWO_MINUTES)||(currentCountdown == LAST_ONE_MINUTES)) {
                    // Take picture for test.
                    Log.d("BLE", "take Pic! " + currentCountdown);
                    ble.bleTakePicture();
                }

            }
        }.start();
    }


    /**
     * Show a notification while this service is running.
     */
    private void showNotification(String notificationString) {
        // In this sample, we'll use the same text for the ticker and the expanded notification
        CharSequence text = getText(R.string.remote_service_started);

        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);

        // Set the info for the views that show in the notification panel.
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.drawable.k_logo)  // the status icon
                .setTicker(text)  // the status text
                .setWhen(System.currentTimeMillis())  // the time stamp
                .setContentTitle("RehabDiary")  // the label of the entry
                .setContentText(notificationString)  // the contents of the entry
                .setContentIntent(contentIntent)  // The intent to send when the entry is clicked
                .build();

        // Send the notification.
        // We use a string id because it is a unique number.  We use it later to cancel.
        mNM.notify(R.string.remote_service_started, notification);
    }


    @Override
    public Context getContext() {
        return null;
    }

    @Override
    public void bleNotSupported() {

    }

    @Override
    public void bleConnectionTimeout() {
        Log.d(TAG, "bleConnectionTimeout");
        // Cancel resultServiceCountdown due to bleNoPlugDetected make test fail.
        if(resultServiceCountdown != null)
            resultServiceCountdown.cancel();

        // Disconnect BLE.
        startBleDisconnect();

        // Display a notification about us starting.  We put an icon in the status bar.
        if(enableNotification)
            showNotification(getString(R.string.test_instruction_top3));

        testFail = true;

        // Send MSG_SERVICE_FAIL_CONNECT_TIMEOUT through MSG_CURRENT_COUNTDOWN message to foreground activity.
        if(mClient != null) {
            try {
                mClient.send(Message.obtain(null,
                        MSG_CURRENT_COUNTDOWN, MSG_SERVICE_FAIL_CONNECT_TIMEOUT, 0));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void bleConnected() {
        Log.d(TAG, "bleConnected");
    }

    @Override
    public void bleDisconnected() {
        Log.d(TAG, "bleDisconnected");
        // Auto reconnect to device after 1 sec.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(ble != null)
                    ble.bleConnect();
            }
        }, 1000);

    }

    @Override
    public void bleWriteCharacteristic1Success() {
        Log.d(TAG, "bleWriteCharacteristic1Success");
    }

    @Override
    public void bleWriteStateFail() {
        Log.d(TAG, "bleWriteStateFail");
    }

    @Override
    public void bleNoPlugDetected() {
        Log.d(TAG, "bleNoPlugDetected");

        // Cancel resultServiceCountdown due to bleNoPlugDetected make test fail.
        if(resultServiceCountdown != null)
            resultServiceCountdown.cancel();

        // Disconnect BLE.
        startBleDisconnect();

        // Display a notification about us starting.  We put an icon in the status bar.
        if(enableNotification)
            showNotification(getString(R.string.test_instruction_top4));

        testFail = true;

        // Send MSG_SERVICE_FAIL_NO_PLUG through MSG_CURRENT_COUNTDOWN message to foreground activity.
        if(mClient != null) {
            try {
                mClient.send(Message.obtain(null,
                        MSG_CURRENT_COUNTDOWN, MSG_SERVICE_FAIL_NO_PLUG, 0));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void blePlugInserted(int cassetteId) {
        Log.d(TAG, "blePlugInserted "+cassetteId);
    }

    @Override
    public void bleUpdateBattLevel(int battVolt) {
        Log.d(TAG, "bleUpdateBattLevel "+battVolt);
    }

    @Override
    public void notifyDeviceVersion(int version) {
        Log.d(TAG, "notifyDeviceVersion "+version);
    }

    @Override
    public void bleUpdateSalivaVolt(int salivaVolt) {
        Log.d(TAG, "bleUpdateSalivaVolt "+salivaVolt);
    }

    @Override
    public void bleGetImageSuccess(Bitmap bitmap) {
        Log.d(TAG, "bleGetImageSuccess");

    }

    @Override
    public void bleGetImageFailure(float dropoutRate) {
        Log.d(TAG, "bleGetImageFailure "+dropoutRate);
    }

    @Override
    public void bleNotifyDetectionResult(double score) {
        // pictureCount++ to memory how many pictures were taken.
        pictureCount++;

        // Copy from legacy codes, need confirm.
        if(score == 1)
            testResult = 1;
        else if(score == -1)
            testResult = 0;

        PreferenceControl.setTestResult(testResult);

        Log.d(TAG, "bleNotifyDetectionResult "+score+ " pictureCount "+pictureCount);
    }

    @Override
    public void bleReturnDeviceVersion(int version) {

    }

    public void startBleDisconnect()
    {
        ble.bleUnlockDevice();
        ble.bleCancelCassetteInfo();
        ble.bleSelfDisconnection();
        ble = null;
    }
}
