package ubicomp.ketdiary.fragments.saliva_test;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import ubicomp.ketdiary.MainActivity;
import ubicomp.ketdiary.R;

/**
 * Created by kelvindk on 16/6/24.
 */
public class ResultService extends Service {

    public static final int MSG_REGISTER_CLIENT = 1;
    public static final int MSG_UNREGISTER_CLIENT = 2;
    public static final int MSG_CURRENT_COUNTDOWN = 3;


    /** For showing and hiding our notification. */
    private NotificationManager mNM;
    // Visible of notification
    private boolean enableNotificaiton = false;

    /** Keeps track of all current registered clients. */
    private ArrayList<Messenger> mClients = new ArrayList<Messenger>();

    /** Holds last value set by a client. */
    private int mValue = 0;

    /** Countdown timer for saliva test result */
    private CountDownTimer resultServiceCountdown = null;
    // Current value of countdown.
    private int currentCountdown = 0;

    /**
     * Handler of incoming messages from clients.
     */
    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_REGISTER_CLIENT:
                    mClients.add(msg.replyTo);
                    enableNotificaiton = false;
                    mNM.cancel(R.string.remote_service_started);
                    break;
                case MSG_UNREGISTER_CLIENT:
                    mClients.remove(msg.replyTo);
                    enableNotificaiton = true;
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
        resultServiceCountdown = new CountDownTimer(100000, 1000){
            @Override
            public void onFinish() {
                Log.d("ResultService", "resultServiceCountdown onFinish");
                mNM.cancel(R.string.remote_service_started);
            }

            @Override
            public void onTick(long millisUntilFinished) {
                Log.d("ResultService", "resultServiceCountdown onTick "+millisUntilFinished/1000+" mClients.size() "+mClients.size());
                // Update currentCountdown to remaining time of this countdown timer.
                currentCountdown = (int) millisUntilFinished/1000;

                // Display a notification about us starting.  We put an icon in the status bar.
                if(enableNotificaiton)
                    showNotification(getString(R.string.test_notification_countdown)+
                            currentCountdown/60+"分"+currentCountdown%60+"秒");

                // Send MSG_CURRENT_COUNTDOWN message to foreground activity.
                if(mClients.size()>0) {
                    for (int i=mClients.size()-1; i>=0; i--) {
                        try {
                            mClients.get(i).send(Message.obtain(null,
                                    MSG_CURRENT_COUNTDOWN, currentCountdown, 0));
                        } catch (RemoteException e) {
                            // The client is dead.  Remove it from the list;
                            // we are going through the list from back to front
                            // so this is safe to do inside the loop.
                            mClients.remove(i);
                        }
                    }
                }

            }
        }.start();
    }

    @Override
    public void onCreate() {
        mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        // Start countdown to 12min for waiting saliva test result.
        startResultServiceCountdown();

    }


    @Override
    public void onDestroy() {
        // Cancel the persistent notification.
        mNM.cancel(R.string.remote_service_started);

        resultServiceCountdown.cancel();

        Log.d("KetService", "KetService onDestroy");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
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
}
