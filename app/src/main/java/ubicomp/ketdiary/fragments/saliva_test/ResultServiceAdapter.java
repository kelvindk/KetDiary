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

    /**
     * Handler of incoming messages from service.
     */
    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Log.d("Ket", "IncomingHandler handleMessage");

            switch (msg.what) {
                case ResultService.MSG_CURRENT_COUNTDOWN:
                    Log.d("Ket", "resultServiceCountdown received from ResultService: " + (int) msg.arg1);
                default:
                    super.handleMessage(msg);
            }
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

//                // Give it some value as an example.
//                msg = Message.obtain(null,
//                        ResultService.MSG_SET_VALUE, 54088, 0);
//                mService.send(msg);
            } catch (RemoteException e) {
                // In this case the service has crashed before we could even
                // do anything with it; we can count on soon being
                // disconnected (and then reconnected if it can be restarted)
                // so there is no need to do anything here.
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

    void doBindService() {
        // Establish a connection with the service.
        Intent intent = new Intent(mainActivity, ResultService.class);
        // With a trick to keep service alive after MainActivity is done.
        mainActivity.startService(intent);
        mainActivity.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;

        Log.d("Ket", "doBindService Binding");
    }

    void doUnbindService() {
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
}
