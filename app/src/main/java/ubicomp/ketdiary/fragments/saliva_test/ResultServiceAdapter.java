package ubicomp.ketdiary.fragments.saliva_test;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import ubicomp.ketdiary.MainActivity;
import ubicomp.ketdiary.R;
import ubicomp.ketdiary.main_activity.CustomToast;
import ubicomp.ketdiary.main_activity.FragmentSwitcher;
import ubicomp.ketdiary.utility.data.db.FirstPageDataBase;
import ubicomp.ketdiary.utility.data.structure.TestResult;
import ubicomp.ketdiary.utility.system.PreferenceControl;

/**
 * Created by kelvindk on 16/6/25.
 */
public class ResultServiceAdapter {

    private MainActivity mainActivity = null;
    private SalivaTestAdapter salivaTestAdapter = null;


    /** Messenger for communicating with service. */
    private Messenger mService = null;
    /** Flag indicating whether we have called bind on the service. */
    private boolean mIsBound;


    public ResultServiceAdapter(MainActivity mainActivity, SalivaTestAdapter salivaTestAdapter) {
        this.mainActivity = mainActivity;
        this.salivaTestAdapter = salivaTestAdapter;

    }

    public ResultServiceAdapter(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }



    /**
     * Handler of incoming messages from service.
     */
    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {

            if (msg.what == ResultService.MSG_CURRENT_COUNTDOWN) {
                Log.d("Ket", "resultServiceCountdown " + msg.arg1);
                int receivedMsg = msg.arg1;

                Intent intent = new Intent(mainActivity, ResultService.class);

                switch (receivedMsg) {
                    case ResultService.MSG_SERVICE_FAIL_NO_PLUG:
                        PreferenceControl.setResultServiceIsRunning(false);

                        // Cancel countdown timer of current stage.
                        if(salivaTestAdapter != null)
                            salivaTestAdapter.cancelTestStateCountdownTimer();

                        // Show CustomToast.
                        CustomToast.generateToast(R.string.test_instruction_top4, -1);

                        // Transit to StateIdle and show error message.
                        if(salivaTestAdapter != null)
                            salivaTestAdapter.setToIdleState(R.string.test_instruction_top4);
                        mainActivity.getFragmentSwitcher().setFragment(FragmentSwitcher.FRAGMENT_TEST);

                        doUnbindService();

                        mainActivity.stopService(intent);

                        break;

                    case ResultService.MSG_SERVICE_FAIL_CONNECT_TIMEOUT:
                        // Show CustomToast.
                        CustomToast.generateToast(R.string.test_instruction_top3, -1);

                        // Transit to StateIdle and show error message.
                        if(salivaTestAdapter != null)
                            salivaTestAdapter.setToIdleState(R.string.test_instruction_top3);
                        mainActivity.getFragmentSwitcher().setFragment(FragmentSwitcher.FRAGMENT_TEST);

                        doUnbindService();

                        mainActivity.stopService(intent);
                        break;

                    case ResultService.MSG_SERVICE_FINISH:
                        int result = PreferenceControl.getTestResult();
                        String cassetteId = PreferenceControl.getCassetteId()+"";

                        /** Write test result to database */
                        TestResult testResult = new TestResult(result,
                                PreferenceControl.getSalivaTestTimestamp(),
                                cassetteId,
                                1, 0, 0, 0);

                        FirstPageDataBase firstPageDataBase = new FirstPageDataBase();
                        firstPageDataBase.setCassetteUsed(cassetteId);
                        firstPageDataBase.addTestResult(testResult);

                        // Show CustomToast.
                        if(result == 1) { // Saliva test results positive.
                            CustomToast.generateToast(R.string.salivaResultPositive, -1);
                        }
                        else {
                            CustomToast.generateToast(R.string.salivaResultNegative, 0);
                        }

                        PreferenceControl.setResultServiceIsRunning(false);

                        doUnbindService();

                        mainActivity.stopService(intent);
                        break;
                }


                mainActivity.resultServiceRunning(receivedMsg);
            }
            else
                super.handleMessage(msg);
        }
    }

    /**
     * Target we publish for clients to send messages to IncomingHandler.
     */
    final Messenger mMessenger = new Messenger(new IncomingHandler());

    /**
     * Class for interacting with the main interface of the service.
     */
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // This is called when the connection with the service has been
            // established, giving us the service object we can use to
            // interact with the service.  We are communicating with our
            // service through an IDL interface, so get a client-side
            // representation of that from the raw service object.
            mService = new Messenger(service);
            Log.d("Ket", "ServiceConnection onServiceConnected Attached");

            // We want to monitor the service for as long as we are
            // connected to it.
            try {
                Message msg = Message.obtain(null,
                        ResultService.MSG_REGISTER_CLIENT);
                msg.replyTo = mMessenger;
                mService.send(msg);

            } catch (RemoteException e) {
                // In this case the service has crashed before we could even
                // do anything with it; we can count on soon being
                // disconnected (and then reconnected if it can be restarted)
                // so there is no need to do anything here.
            }

            // Send message according which class new this class.
            // if new by MainActivity, is to
            if(salivaTestAdapter == null)
                isResultServiceRunning();
            else { // if new by SalivaTestAdapter
                startBleConnection();
                startResultService();
            }
            Log.d("Ket", "ServiceConnection onServiceConnected remote_service_connected");
        }

        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            mService = null;
            Log.d("Ket", "ServiceConnection onServiceDisconnected Disconnected");

        }
    };

    public void doBindService() {
        // Establish a connection with the service.
        Intent intent = new Intent(mainActivity, ResultService.class);
        // With a trick to keep service alive after MainActivity is done.
        mainActivity.startService(intent);
        mainActivity.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;

        Log.d("Ket", "doBindService Binding");
    }

    public void doUnbindService() {
        if (mIsBound) {
            // If we have received the service, and hence registered with
            // it, then now is the time to unregister.
            if (mService != null) {
                try {
                    Message msg = Message.obtain(null,
                            ResultService.MSG_UNREGISTER_CLIENT);
                    msg.replyTo = mMessenger;
                    mService.send(msg);
                } catch (RemoteException e) {
                    // There is nothing special we need to do if the service
                    // has crashed.
                }
            }

            // Detach our existing connection.
            mainActivity.unbindService(mConnection);
            mIsBound = false;
            Log.d("Ket", "doUnbindService Unbinding");
        }
    }

    public void isResultServiceRunning() {
        if (mService != null) {
            try {
                Message msg = Message.obtain(null,
                        ResultService.MSG_IS_RUNNING);
                msg.replyTo = mMessenger;
                mService.send(msg);
            } catch (RemoteException e) {
            }
        }
    }

    public void startResultService() {
        if (mService != null) {
            try {
                Message msg = Message.obtain(null,
                        ResultService.MSG_START_RESULT_SERVICE_COUNTDOWN);
                msg.replyTo = mMessenger;
                mService.send(msg);
            } catch (RemoteException e) {
            }
        }
    }


    public void startBleConnection() {
        if (mService != null) {
            try {
                Message msg = Message.obtain(null,
                        ResultService.MSG_BLE_CONNECT);
                msg.replyTo = mMessenger;
                mService.send(msg);
            } catch (RemoteException e) {
            }
        }
    }
}
