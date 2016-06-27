package ubicomp.ketdiary.fragments.create_event;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import ubicomp.ketdiary.R;
import ubicomp.ketdiary.fragments.create_event.steps.*;
import ubicomp.ketdiary.fragments.create_event.steps.StepTimeWrapper;
import ubicomp.ketdiary.fragments.event.EventLogStructure;
import ubicomp.ketdiary.fragments.saliva_test.ResultService;
import ubicomp.ketdiary.utility.system.PreferenceControl;

/**
 * A standalone activity for create(add) new event.
 *
 * Created by kelvindk on 16/6/7.
 */

public class CreateEventActivity extends AppCompatActivity {

    public static final String TAG = "CreateEventActivity";

    private CreateEventActivity createEventActivity = null;
    private Activity superActivity = null;
    private EventLogStructure eventLogStructure = null;
    private CoordinatorLayout coordinatorLayout = null;

    private ScrollViewAdapter scrollViewAdapter = null;

    private StepTimeWrapper step1Adapter = null;
    private StepScenarioWrapper step2Adapter = null;
    private StepRelapseProbabilityWrapper step3Adapter = null;
    private StepBehaviorWrapper step4Adapter = null;
    private StepEmotionWrapper step5Adapter = null;
    private StepThoughtWrapper step6Adapter = null;
    private StepExpectedBehaviorWrapper step7Adapter = null;
    private StepExpectedEmotionWrapper step8Adapter = null;
    private StepExpectedThoughtWrapper step9Adapter = null;

    private TextView textviewToolbar = null;

    private Menu menu = null;


    /** Messenger for communicating with service. */
    private Messenger mService = null;
    /** Flag indicating whether we have called bind on the service. */
    private boolean mIsBound;

    public Menu getMenu() {
        return menu;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        createEventActivity = this;
        superActivity = (Activity) this.getParent();

        // Get CoordinatorLayout for showing Snackbar.
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.create_event_coordinatorLayout);

        // Get event data structure through Intent.
        Intent intent = this.getIntent();
        EventLogStructure receivedEventLogStructure =
                (EventLogStructure) intent.getSerializableExtra(EventLogStructure.EVENT_LOG_STRUCUTRE_KEY);

        // For creating a new event, eventLogStructure will be null. Then new an object here.
        if(receivedEventLogStructure == null) {
            Log.d("CreateEvent", "eventLogStructure null");
            eventLogStructure = new EventLogStructure();
        }
        else {
            Log.d("CreateEvent", "existed eventLogStructure, for editing with this page");
            eventLogStructure = receivedEventLogStructure;
        }

        // Set full screen.
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN );

        // Enable toolbar on create event activity with back button on the top left.
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_create_event_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // New ScrollViewAdapter to handle scrolling in this activity.
        scrollViewAdapter = new ScrollViewAdapter(this);
        // Disable scrolling on ScrollView.
        scrollViewAdapter.setScrollDisable(true);

        // UI component.
        textviewToolbar = (TextView) findViewById(R.id.textview_create_toolbar);

        /*** Handle filling content of event ***/


        /*** Step 0, add a edit timestamp to eventLogStructure ***/
        // Add current time to ediTime, eventTime & createTime.
        eventLogStructure.editTime = (Calendar) Calendar.getInstance().clone();
        eventLogStructure.eventTime = (Calendar) Calendar.getInstance().clone();
        eventLogStructure.createTime = (Calendar) Calendar.getInstance().clone();

        /*** Step 1 ***/
        step1Adapter = new StepTimeWrapper(this);

        /*** Step 2 ***/
        step2Adapter = new StepScenarioWrapper(this);

        /*** Step 3 ***/
        step3Adapter = new StepRelapseProbabilityWrapper(this);

        /*** Step 4 ***/
        step4Adapter = new StepBehaviorWrapper(this);

        /*** Step 5 ***/
        step5Adapter = new StepEmotionWrapper(this);

        /*** Step 6 ***/
        step6Adapter = new StepThoughtWrapper(this);

        /*** Step 7 ***/
        step7Adapter = new StepExpectedBehaviorWrapper(this);

        /*** Step 8 ***/
        step8Adapter = new StepExpectedEmotionWrapper(this);

        /*** Step 9 ***/
        step9Adapter = new StepExpectedThoughtWrapper(this);


//        Bundle bundle = getIntent().getExtras();
//        EventLogStructure eventLog =
//                (EventLogStructure) bundle.getSerializable(EventLogStructure.EVENT_LOG_STRUCUTRE_KEY);
//        Log.d("Ket", "eventLog.scenarioType "+eventLog.scenarioType);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        // Inflate the icon on the top right of toolbar in CreateEventActivity.
        getMenuInflater().inflate(R.menu.menu_create_event, menu);

        MenuItem actionSaveEvent = menu.findItem(R.id.action_save);
        MenuItemCompat.getActionView(actionSaveEvent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Return if the button is not clickable due to current steps.
                if(!scrollViewAdapter.isActionSaveButtonClickable())
                    return;

                Log.d("Ket", "actionSaveEvent");
                // Listener of action Save button
//                Snackbar snackbar = Snackbar.make(coordinatorLayout, R.string.create_event_saved,  Snackbar.LENGTH_LONG);
//                TextView textViewSnackbar = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
//                textViewSnackbar.setTextColor(getResources().getColor(R.color.colorAccent));
//                snackbar.setAction("Action", null);
//                snackbar.show();

                Toast toast = Toast.makeText(CreateEventActivity.this, R.string.create_event_saved, Toast.LENGTH_LONG);
                toast.show();

                /*** Need save event data to storage ***/

            }
        });

//        MenuItem actionCancelEvent = menu.findItem(R.id.action_cancel);
//        MenuItemCompat.getActionView(actionCancelEvent).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("Ket", "actionCancelEvent");
//                // Listener of action Cancel button, back previous page after confirm.
//                onBackPressed();
//
//            }
//        });

        return true;
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume");

        // Receive message from ResultService if necessary.
        if( PreferenceControl.isResultServiceIsRunning()) {
            doBindService();
        }
        else {
            doUnbindService();
        }

        super.onResume();
    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop");

        doUnbindService();
        super.onStop();
    }

    /*
    *  Handle back pressed.
    * */
    @Override
    public void onBackPressed() {
        // New dialog.
        AlertDialog.Builder dialog = new AlertDialog.Builder(createEventActivity);
        dialog.setTitle(R.string.confirm_cancel_click);
        dialog.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                /* User clicked "Confirm"*/
                // Finish activity.
                createEventActivity.finish();
            }
        });
        dialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                /* User clicked "Confirm"*/
                // no op.
            }
        });
        dialog.show();
    }



    public EventLogStructure getEventLogStructure() {
        return eventLogStructure;
    }


    /*
    *  Pass method call to ScrollViewAdapter.
    * */
    public void scrollViewScrollToBottom() {
        scrollViewAdapter.scrollToBottom();
    }


    /*
    *  onActivityResult, to receive message from other Activity.
    * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        super.onActivityResult(requestCode, resultCode, result);

        if (resultCode != RESULT_OK || result == null) {
            return;
        }

        switch (requestCode) {
            case StepBehaviorWrapper.INTENT_SPEECH_INPUT_RESULT:
                step4Adapter.onSpeechToTextActivityResult(result);
                break;
            case StepThoughtWrapper.INTENT_SPEECH_INPUT_RESULT:
                step6Adapter.onSpeechToTextActivityResult(result);
                break;
            case StepExpectedBehaviorWrapper.INTENT_SPEECH_INPUT_RESULT:
                step7Adapter.onSpeechToTextActivityResult(result);
                break;
            case StepExpectedThoughtWrapper.INTENT_SPEECH_INPUT_RESULT:
                step9Adapter.onSpeechToTextActivityResult(result);
                break;
        }
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
                switch (receivedMsg) {
                    case ResultService.MSG_SERVICE_FAIL_NO_PLUG:
                        //
                        break;
                    case ResultService.MSG_SERVICE_FAIL_CONNECT_TIMEOUT:
                        //
                        break;
                    case ResultService.MSG_SERVICE_FINISH:
                        //
                        textviewToolbar.setText(R.string.toolbar_test_result_is_out);
                        doUnbindService();
                        break;
                    default:
                        textviewToolbar.setVisibility(View.VISIBLE);
                        textviewToolbar.setText(getString(R.string.test_notification_countdown)
                                +"\n"+msg.arg1/60+"分"+msg.arg1%60+"秒");
                        break;
                }

            }

            Log.d(TAG, "handleMessage "+msg.what);
            switch (msg.what) {
                case ResultService.MSG_SERVICE_FAIL_NO_PLUG:
                    Log.d(TAG, "MSG_SERVICE_FAIL_NO_PLUG");
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
                        ResultService.MSG_REGISTER_CREATE_EVENT_CLIENT);
                msg.replyTo = mMessenger;
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

    public void doBindService() {
        // Establish a connection with the service.
        Intent intent = new Intent(this, ResultService.class);
        // With a trick to keep service alive after MainActivity is done.
        startService(intent);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;

        Log.d("Ket", "CreateEvent doBindService Binding");
    }

    public void doUnbindService() {
        if (mIsBound) {
            // If we have received the service, and hence registered with
            // it, then now is the time to unregister.
            if (mService != null) {
                try {
                    Message msg = Message.obtain(null,
                            ResultService.MSG_UNREGISTER_CREATE_EVENT_CLIENT);
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
