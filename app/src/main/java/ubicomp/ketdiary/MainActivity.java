package ubicomp.ketdiary;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.WindowManager;

import ubicomp.ketdiary.fragments.FragmentTest;
import ubicomp.ketdiary.fragments.saliva_test.ResultService;
import ubicomp.ketdiary.main_activity.FragmentSwitcher;
import ubicomp.ketdiary.main_activity.TabLayoutWrapper;
import ubicomp.ketdiary.main_activity.ToolbarMenuItemWrapper;

public class MainActivity extends AppCompatActivity {


    // The wrapper to handle toolbar.
    private ToolbarMenuItemWrapper toolbarMenuItemWrapper = null;
    // The wrapper to handle tab layout.
    private TabLayoutWrapper tabLayoutWrapper = null;
    // The class to manipulate fragment switch, all switching should use this class.
    private FragmentSwitcher fragmentSwitcher = null;

    private static MainActivity mainActivity = null;

    /** Messenger for communicating with service. */
    Messenger mService = null;
    /** Flag indicating whether we have called bind on the service. */
    boolean mIsBound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = this;
        setContentView(R.layout.activity_main);

        // Set full screen.
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN );

        // New the wrapper of toolbar UI.
        toolbarMenuItemWrapper = new ToolbarMenuItemWrapper(this);
        // New the wrapper of tabs UI.
        tabLayoutWrapper = new TabLayoutWrapper(this);
        // New the object of handling fragment switch.
        fragmentSwitcher = new FragmentSwitcher(this, toolbarMenuItemWrapper, tabLayoutWrapper);


        // For developing
        doUnbindService();
        doBindService();

////        Calendar yesterday = (Calendar) Calendar.getInstance().clone();
////        Calendar now = (Calendar) Calendar.getInstance().clone();
////        yesterday.add(Calendar.DATE, -1);
////        event.editTime.add(yesterday);
////        event.editTime.add(now);
//        event.scenarioType = EventLogStructure.ScenarioTypeEnum.BODY;
         //Data structure to store event.
//        EventLogStructure event = new EventLogStructure();
//        // Set one field as example. scenarioType is an enum.
//        event.scenarioType = EventLogStructure.ScenarioTypeEnum.BODY;
//        Intent eventContentActivityIntent = new Intent CreateEventActivityctivity.class);
//        // Put the serializable object into eventContentActivityIntent through a Bundle.
//        Bundle bundle = new Bundle();
//        bundle.putSerializable(EventLogStructure.EVENT_LOG_STRUCUTRE_KEY, event);
//        eventContentActivityIntent.putExtras(bundle);
//        // Start the activity.
//        startActivity(eventContentActivityIntent);

    }

    @Override
    public void onBackPressed() {
//        /*** Bug-fix 1 ****/
//        FragmentManager fragmentManager = getSupportFragmentManager();
//
//        Log.d("Ket", "  "+fragmentManager.getBackStackEntryCount()+" "+getFragmentManager().getBackStackEntryCount());
//        if (fragmentManager.getBackStackEntryCount() > 0 ){
//            fragmentManager.popBackStack();
//        } else {
//            super.onBackPressed();
//        }
        super.onBackPressed();
    }

    public static MainActivity getMainActivity() {
        return mainActivity;
    }


    // Getter of FragmentTest.
    public FragmentTest getFragmentTest() {
        return fragmentSwitcher.getFragmentTest();
    }

    // Getter of TabLayoutWrapper.
    public TabLayoutWrapper getTabLayoutWrapper() {
        return tabLayoutWrapper;
    }

    // Getter of ToolbarMenuItemWrapper.
    public ToolbarMenuItemWrapper getToolbarMenuItemWrapper() {
        return toolbarMenuItemWrapper;
    }

    // Pass the method call to FragmentSwitcher.
    public void setFragment(int fragmentToSwitch) {
        fragmentSwitcher.setFragment(fragmentToSwitch);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Pass the reference of Menu object to ToolbarMenuItemWrapper when ready
        toolbarMenuItemWrapper.setMenu(menu);
        // Show FragmentTest's action button due to it's first page when app start.
        toolbarMenuItemWrapper.inflate(this.mainActivity, R.menu.menu_test);

        return true;
    }

    @Override
    public void onStart() {
        Log.d("Ket", "MainActivity onStart");


        super.onStart();
    }

    @Override
    public void onRestart() {
        Log.d("Ket", "MainActivity onRestart");


        super.onRestart();
    }

    @Override
    public void onPause() {
        Log.d("Ket", "MainActivity onPause");


        super.onPause();
    }

    @Override
    public void onStop() {
        Log.d("Ket", "MainActivity onStop");

        super.onStop();
    }

    @Override
    public void onDestroy() {
        Log.d("Ket", "MainActivity onDestroy");


        super.onDestroy();
    }

    /**
     * Handler of incoming messages from service.
     */
    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Log.d("Ket", "IncomingHandler handleMessage");

            switch (msg.what) {
                case ResultService.MSG_SET_VALUE:
                    Log.d("Ket", "IncomingHandler Received from service: " + (int) msg.arg1);
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

                // Give it some value as an example.
                msg = Message.obtain(null,
                        ResultService.MSG_SET_VALUE, 54088, 0);
                mService.send(msg);
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
        // Establish a connection with the service.  We use an explicit
        // class name because there is no reason to be able to let other
        // applications replace our component.
        bindService(new Intent(this,
                ResultService.class), mConnection, Context.BIND_AUTO_CREATE);
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
            unbindService(mConnection);
            mIsBound = false;
            Log.d("Ket", "doUnbindService Unbinding");
        }
    }

}
