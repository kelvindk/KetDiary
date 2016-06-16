package ubicomp.ketdiary.fragments.create_event;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.Calendar;

import ubicomp.ketdiary.R;
import ubicomp.ketdiary.fragments.create_event.steps.*;
import ubicomp.ketdiary.fragments.create_event.steps.StepTimeWrapper;
import ubicomp.ketdiary.fragments.event.EventLogStructure;

/**
 * A standalone activity for create(add) new event.
 *
 * Created by kelvindk on 16/6/7.
 */

public class CreateEventActivity extends AppCompatActivity {

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

        /*** Handle filling content of event ***/


        /*** Step 0, add a edit timestamp to eventLogStructure ***/
        // Add current time to ediTime & eventTime.
        eventLogStructure.editTime.add((Calendar)Calendar.getInstance().clone());
        eventLogStructure.eventTime = (Calendar)Calendar.getInstance().clone();

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
        // Inflate the icon on the top right of toolbar in CreateEventActivity.
        getMenuInflater().inflate(R.menu.menu_create_event, menu);

        MenuItem actionSaveEvent = menu.findItem(R.id.action_save);
        MenuItemCompat.getActionView(actionSaveEvent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

        MenuItem actionCancelEvent = menu.findItem(R.id.action_cancel);
        MenuItemCompat.getActionView(actionCancelEvent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Ket", "actionCancelEvent");
                // Listener of action Cancel button, back previous page after confirm.
                onBackPressed();

            }
        });

        return true;
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
}
