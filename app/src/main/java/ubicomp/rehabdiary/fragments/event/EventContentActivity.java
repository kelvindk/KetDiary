package ubicomp.rehabdiary.fragments.event;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import ubicomp.rehabdiary.R;
import ubicomp.rehabdiary.fragments.create_event.CreateEventActivity;
import ubicomp.rehabdiary.utility.data.db.AddScoreDataBase;
import ubicomp.rehabdiary.utility.data.db.DatabaseControl;
import ubicomp.rehabdiary.utility.data.structure.TestResult;

/**
 * Created by kelvindk on 16/6/10.
 */
public class EventContentActivity extends AppCompatActivity {

    // Key of this object for deliver between activities through Intent.
    public static final String EVENT_CONTENT_ACTIVITY_KEY = "EventContentActivity";

    public static final int EVENT_CONTENT_ACTIVITY_INT_KEY = 46;

    public static final int BROWSING_COUNTDOWN = 10000; // 10000

    private EventContentActivity eventContentActivity = null;

    private TextView event_content_container_event_date = null;
    private ImageView event_content_container_scenario_type = null;
    private TextView event_content_container_scenario = null;

    private TextView event_content_container_expected_thought = null;
    private TextView event_content_container_expected_emotion = null;
    private TextView event_content_container_expected_behavior = null;

    private TextView event_content_container_original_thought = null;
    private TextView event_content_container_original_emotion = null;
    private TextView event_content_container_original_behavior = null;

    private RatingBar event_content_container_drug_use_risk = null;

    private TextView event_content_container_expected_thought_revise = null;
    private TextView event_content_container_expected_emotion_revise = null;
    private TextView event_content_container_expected_behavior_revise = null;
    private TextView event_content_container_original_thought_revise = null;
    private TextView event_content_container_original_emotion_revise = null;
    private TextView event_content_container_original_behavior_revise = null;

    private LinearLayout event_content_container_expected_thought_layout = null;
    private LinearLayout event_content_container_expected_emotion_layout = null;
    private LinearLayout event_content_container_expected_behavior_layout = null;
    private LinearLayout event_content_container_original_thought_layout = null;
    private LinearLayout event_content_container_original_emotion_layout = null;
    private LinearLayout event_content_container_original_behavior_layout = null;

    private LinearLayout event_content_incomplete_status_layout = null;
    private LinearLayout event_content_therapy_status_layout = null;
    private ImageView event_content_therapy_status_icon = null;
    private TextView event_content_therapy_status_text = null;
    private ImageView event_content_saliva_status_icon = null;
    private TextView event_content_saliva_status_text = null;

    private Drawable expected_thought_original_drawable = null;
    private Drawable expected_emotion_original_drawable = null;
    private Drawable expected_behavior_original_drawable = null;
    private Drawable original_thought_original_drawable = null;
    private Drawable original_emotion_original_drawable = null;
    private Drawable original_behavior_original_drawable = null;

    private Toast emptyStep4Toast = null;
    private Toast emptyStep5Toast = null;
    private Toast emptyStep6Toast = null;
    private Toast emptyStep7Toast = null;
    private Toast emptyStep8Toast = null;

    // Countdown timer for checking user's browsing time in this page.
    private CountDownTimer browsingCountdown = null;


    private EventLogStructure eventLog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_content);

        // Set full screen.
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN );

        eventContentActivity = this;

        // Retrieve EventLogStructure.
        Bundle bundle = getIntent().getExtras();
        eventLog = (EventLogStructure) bundle.getSerializable(EventLogStructure.EVENT_LOG_STRUCUTRE_KEY);

        // Enable toolbar on create event activity with back button on the top left.
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_event_content_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // Initialize UI components.
        initUiComponents();
    }

    private void initUiComponents() {

        // Get UI components.
        event_content_container_event_date = (TextView) findViewById(R.id.event_content_container_event_date);
        event_content_container_scenario_type = (ImageView) findViewById(R.id.event_content_container_scenario_type);
        event_content_container_scenario = (TextView) findViewById(R.id.event_content_container_scenario);

        event_content_container_expected_thought = (TextView) findViewById(R.id.event_content_container_expected_thought);
        event_content_container_expected_emotion = (TextView) findViewById(R.id.event_content_container_expected_emotion);
        event_content_container_expected_behavior = (TextView) findViewById(R.id.event_content_container_expected_behavior);

        event_content_container_original_thought = (TextView) findViewById(R.id.event_content_container_original_thought);
        event_content_container_original_emotion = (TextView) findViewById(R.id.event_content_container_original_emotion);
        event_content_container_original_behavior = (TextView) findViewById(R.id.event_content_container_original_behavior);

        event_content_container_drug_use_risk = (RatingBar) findViewById(R.id.event_content_container_drug_use_risk);

        event_content_container_expected_thought_revise = (TextView) findViewById(R.id.event_content_container_expected_thought_revise);
        event_content_container_expected_emotion_revise = (TextView) findViewById(R.id.event_content_container_expected_emotion_revise);
        event_content_container_expected_behavior_revise = (TextView) findViewById(R.id.event_content_container_expected_behavior_revise);
        event_content_container_original_thought_revise = (TextView) findViewById(R.id.event_content_container_original_thought_revise);
        event_content_container_original_emotion_revise = (TextView) findViewById(R.id.event_content_container_original_emotion_revise);
        event_content_container_original_behavior_revise = (TextView) findViewById(R.id.event_content_container_original_behavior_revise);

        event_content_container_expected_thought_layout = (LinearLayout) findViewById(R.id.event_content_container_expected_thought_layout);
        event_content_container_expected_emotion_layout = (LinearLayout) findViewById(R.id.event_content_container_expected_emotion_layout);
        event_content_container_expected_behavior_layout = (LinearLayout) findViewById(R.id.event_content_container_expected_behavior_layout);
        event_content_container_original_thought_layout = (LinearLayout) findViewById(R.id.event_content_container_original_thought_layout);
        event_content_container_original_emotion_layout = (LinearLayout) findViewById(R.id.event_content_container_original_emotion_layout);
        event_content_container_original_behavior_layout = (LinearLayout) findViewById(R.id.event_content_container_original_behavior_layout);

        expected_thought_original_drawable = event_content_container_expected_thought_layout.getBackground();
        expected_emotion_original_drawable = event_content_container_expected_emotion_layout.getBackground();
        expected_behavior_original_drawable = event_content_container_expected_behavior_layout.getBackground();
        original_thought_original_drawable = event_content_container_original_thought_layout.getBackground();
        original_emotion_original_drawable = event_content_container_original_emotion_layout.getBackground();
        original_behavior_original_drawable = event_content_container_original_behavior_layout.getBackground();

        event_content_incomplete_status_layout = (LinearLayout) findViewById(R.id.event_content_incomplete_status_layout);
        event_content_therapy_status_layout = (LinearLayout) findViewById(R.id.event_content_therapy_status_layout);
        event_content_therapy_status_icon = (ImageView) findViewById(R.id.event_content_therapy_status_icon);
        event_content_therapy_status_text = (TextView) findViewById(R.id.event_content_therapy_status_text);
        event_content_saliva_status_icon = (ImageView) findViewById(R.id.event_content_saliva_status_icon);
        event_content_saliva_status_text = (TextView) findViewById(R.id.event_content_saliva_status_text);

        emptyStep4Toast = Toast.makeText(this, R.string.empty_step4_toast, Toast.LENGTH_SHORT);
        emptyStep5Toast = Toast.makeText(this, R.string.empty_step5_toast, Toast.LENGTH_SHORT);
        emptyStep6Toast = Toast.makeText(this, R.string.empty_step6_toast, Toast.LENGTH_SHORT);
        emptyStep7Toast = Toast.makeText(this, R.string.empty_step7_toast, Toast.LENGTH_SHORT);
        emptyStep8Toast = Toast.makeText(this, R.string.empty_step8_toast, Toast.LENGTH_SHORT);

        // Fill content on UI components.
        loadEventLogDataToUi();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the icon on the top right of toolbar in CreateEventActivity.
        getMenuInflater().inflate(R.menu.menu_event_content, menu);

        MenuItem actionDeleteEvent = menu.findItem(R.id.action_delete);
        MenuItemCompat.getActionView(actionDeleteEvent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Ket", "actionDeleteEvent");
                //New dialog.
                AlertDialog.Builder dialog = new AlertDialog.Builder(eventContentActivity);
                dialog.setTitle(R.string.confirm_delete_event_click);
                dialog.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        /* User clicked "Confirm"*/

                        /*** Need to delete event data from database ***/

                        // Finish activity.
                        eventContentActivity.finish();
                    }
                });
                dialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        /* User clicked "Cancel"*/
                        // no op.
                    }
                });
                dialog.show();

//                Toast toast = Toast.makeText(EventContentActivity.this, R.string.create_event_saved, Toast.LENGTH_LONG);
//                toast.show();


            }
        });

        MenuItem actionEditEvent = menu.findItem(R.id.action_edit);
        MenuItemCompat.getActionView(actionEditEvent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Ket", "actionEditEvent");

                // Start CreateEventActivity to edit event.
                startCreateEventActivity(2);
            }
        });

        return true;
    }


    View.OnClickListener eventContentClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            int step = 0;
            switch (view.getId()) {
                case R.id.event_content_container_expected_thought_layout:
                    Log.d("Ket", "event_content_container_expected_thought_layout");
                    step = 9;
                    break;
                case R.id.event_content_container_expected_emotion_layout:
                    Log.d("Ket", "event_content_container_expected_emotion_layout");
                    step = 8;
                    break;
                case R.id.event_content_container_expected_behavior_layout:
                    Log.d("Ket", "event_content_container_expected_behavior_layout");
                    step = 7;
                    break;
                case R.id.event_content_container_original_thought_layout:
                    Log.d("Ket", "event_content_container_original_thought_layout");
                    step = 6;
                    break;
                case R.id.event_content_container_original_emotion_layout:
                    Log.d("Ket", "event_content_container_original_emotion_layout");
                    step = 5;
                    break;
                case R.id.event_content_container_original_behavior_layout:
                    Log.d("Ket", "event_content_container_original_behavior_layout");
                    step = 4;
                    break;
            }

            //
            int firstEmptyStep = getFirstEmptyFieldStep();
            if(firstEmptyStep < step) {
                Log.d("Ket", "firstEmptyStep "+firstEmptyStep+" step"+ step);
                switch (firstEmptyStep) {
                    case 4:
                        emptyStep4Toast.show();
                        break;
                    case 5:
                        emptyStep5Toast.show();
                        break;
                    case 6:
                        emptyStep6Toast.show();
                        break;
                    case 7:
                        emptyStep7Toast.show();
                        break;
                    case 8:
                        emptyStep8Toast.show();
                        break;
                }
            }
            else {
                // Start startCreateEventActivity.
                startCreateEventActivity(step);
            }

        }
    };


    private int getFirstEmptyFieldStep() {

        if(eventLog.originalBehavior.equals(""))
            return 4;

        if(eventLog.originalEmotion.equals(""))
            return 5;

        if(eventLog.originalThought.equals(""))
            return 6;

        if(eventLog.expectedBehavior.equals(""))
            return 7;

        if(eventLog.expectedEmotion.equals(""))
            return 8;

        if(eventLog.expectedThought.equals(""))
            return 9;


        return -1;
    }

    /*
    *  Start CreateEventActivity and send an existed EventLogStructure with focus step.
    * */
    private void startCreateEventActivity(int step) {
        Intent intent =
                new Intent(eventContentActivity, CreateEventActivity.class);
        // Put the serializable object into eventContentActivityIntent through a Bundle.
        Bundle bundle = new Bundle();
        bundle.putInt(EVENT_CONTENT_ACTIVITY_KEY, step);
        bundle.putSerializable(EventLogStructure.EVENT_LOG_STRUCUTRE_KEY, eventLog);
        intent.putExtras(bundle);
        // Start the activity.
        startActivityForResult(intent, EVENT_CONTENT_ACTIVITY_INT_KEY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == EVENT_CONTENT_ACTIVITY_INT_KEY) {
            if(resultCode == Activity.RESULT_OK){
                Bundle bundle = intent.getExtras();
                eventLog =
                        (EventLogStructure) bundle.getSerializable(EventLogStructure.EVENT_LOG_STRUCUTRE_KEY);

                Log.d("Ket", "EventContentActivity onActivityResult OK "+eventLog.drugUseRiskLevel);

                /** Refresh edited data to UI components on this page.*/
                loadEventLogDataToUi();


            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }



    private void loadEventLogDataToUi() {
        // Fill content on UI components.
        event_content_container_event_date.setText(eventLog.eventTimeToString());

        event_content_container_scenario_type.setImageResource(eventLog.scenarioTypeToIconId());

        event_content_container_scenario.setText(eventLog.scenario);

        event_content_container_expected_thought.setText(eventLog.expectedThought);
        event_content_container_expected_emotion.setText(eventLog.expectedEmotion);
        event_content_container_expected_behavior.setText(eventLog.expectedBehavior);

        event_content_container_original_thought.setText(eventLog.originalThought);
        event_content_container_original_emotion.setText(eventLog.originalEmotion);
        event_content_container_original_behavior.setText(eventLog.originalBehavior);

        event_content_container_drug_use_risk.setRating(eventLog.drugUseRiskLevel);

        // Show red frame on the fields that need revision.
        if((eventLog.reviseExpectedThought) || (eventLog.expectedThought.equals(""))) {
            event_content_container_expected_thought_revise.setVisibility(View.VISIBLE);
            event_content_container_expected_thought_layout.setBackgroundResource(R.drawable.layout_red_frame);
            event_content_container_expected_thought_layout.setOnClickListener(eventContentClickListener);
        }
        else {
            event_content_container_expected_thought_revise.setVisibility(View.GONE);
            event_content_container_expected_thought_layout.setBackground(expected_thought_original_drawable);
            event_content_container_expected_thought_layout.setOnClickListener(null);
        }

        if((eventLog.reviseExpectedEmotion) || (eventLog.expectedEmotion.equals(""))) {
            event_content_container_expected_emotion_revise.setVisibility(View.VISIBLE);
            event_content_container_expected_emotion_layout.setBackgroundResource(R.drawable.layout_red_frame);
            event_content_container_expected_emotion_layout.setOnClickListener(eventContentClickListener);
        }
        else {
            event_content_container_expected_emotion_revise.setVisibility(View.GONE);
            event_content_container_expected_emotion_layout.setBackground(expected_emotion_original_drawable);
            event_content_container_expected_emotion_layout.setOnClickListener(null);
        }

        if((eventLog.reviseExpectedBehavior) || (eventLog.expectedBehavior.equals(""))) {
            event_content_container_expected_behavior_revise.setVisibility(View.VISIBLE);
            event_content_container_expected_behavior_layout.setBackgroundResource(R.drawable.layout_red_frame);
            event_content_container_expected_behavior_layout.setOnClickListener(eventContentClickListener);
        }
        else {
            event_content_container_expected_behavior_revise.setVisibility(View.GONE);
            event_content_container_expected_behavior_layout.setBackground(expected_behavior_original_drawable);
            event_content_container_expected_behavior_layout.setOnClickListener(null);
        }

        if((eventLog.reviseOriginalThought) || (eventLog.originalThought.equals(""))) {
            event_content_container_original_thought_revise.setVisibility(View.VISIBLE);
            event_content_container_original_thought_layout.setBackgroundResource(R.drawable.layout_red_frame);
            event_content_container_original_thought_layout.setOnClickListener(eventContentClickListener);
        }
        else {
            event_content_container_original_thought_revise.setVisibility(View.GONE);
            event_content_container_original_thought_layout.setBackground(original_thought_original_drawable);
            event_content_container_original_thought_layout.setOnClickListener(null);
        }

        if((eventLog.reviseOriginalEmotion) || (eventLog.originalEmotion.equals(""))) {
            event_content_container_original_emotion_revise.setVisibility(View.VISIBLE);
            event_content_container_original_emotion_layout.setBackgroundResource(R.drawable.layout_red_frame);
            event_content_container_original_emotion_layout.setOnClickListener(eventContentClickListener);
        }
        else {
            event_content_container_original_emotion_revise.setVisibility(View.GONE);
            event_content_container_original_emotion_layout.setBackground(original_emotion_original_drawable);
            event_content_container_original_emotion_layout.setOnClickListener(eventContentClickListener);
        }


        if((eventLog.reviseOriginalBehavior) || (eventLog.originalBehavior.equals(""))) {
            event_content_container_original_behavior_revise.setVisibility(View.VISIBLE);
            event_content_container_original_behavior_layout.setBackgroundResource(R.drawable.layout_red_frame);
            event_content_container_original_behavior_layout.setOnClickListener(eventContentClickListener);
        }
        else {
            event_content_container_original_behavior_revise.setVisibility(View.GONE);
            event_content_container_original_behavior_layout.setBackground(original_behavior_original_drawable);
            event_content_container_original_behavior_layout.setOnClickListener(null);
        }


        /** Three status of event */
        //
        event_content_incomplete_status_layout
                = (LinearLayout) findViewById(R.id.event_content_incomplete_status_layout);
        if(eventLog.isComplete)
            event_content_incomplete_status_layout.setVisibility(View.GONE);
        else
            event_content_incomplete_status_layout.setVisibility(View.VISIBLE);

        //
        event_content_therapy_status_layout
                = (LinearLayout) findViewById(R.id.event_content_therapy_status_layout);
        event_content_therapy_status_layout.setVisibility(View.VISIBLE);
        event_content_therapy_status_icon
                = (ImageView) findViewById(R.id.event_content_therapy_status_icon);
        event_content_therapy_status_text
                = (TextView) findViewById(R.id.event_content_therapy_status_text);
        switch (eventLog.therapyStatus) {
            case NULL:
            case NOT_YET:
                event_content_therapy_status_layout.setVisibility(View.GONE);
                break;
            case GOOD:
                event_content_therapy_status_icon.setBackgroundResource(R.drawable.circle3);
                event_content_therapy_status_text.setText(R.string.therapist_not_yet);
                break;
            case BAD:
                event_content_therapy_status_icon.setBackgroundResource(R.drawable.tri3);
                event_content_therapy_status_text.setText(R.string.need_revise);
                break;
            case DISCUSSED:
                event_content_therapy_status_icon.setBackgroundResource(R.drawable.star3);
                event_content_therapy_status_text.setText(R.string.therapist_discussed);
                break;
        }

        //
        // Get saliva test result on event's day.
        DatabaseControl db = new DatabaseControl();
        Calendar eventTime = eventLog.eventTime;
        TestResult testResult = db.getDayTestResult(eventTime.get(Calendar.YEAR),
                eventTime.get(Calendar.MONTH), eventTime.get(Calendar.DAY_OF_MONTH));

        event_content_saliva_status_icon
                = (ImageView) findViewById(R.id.event_content_saliva_status_icon);
        event_content_saliva_status_text
                = (TextView) findViewById(R.id.event_content_saliva_status_text);


        switch (testResult.getResult()) {
            case -1:
                event_content_saliva_status_icon.setBackgroundResource(R.drawable.notdetect);
                event_content_saliva_status_text.setText(R.string.no_saliva_test);
                break;
            case 0: // Pass
                event_content_saliva_status_icon.setBackgroundResource(R.drawable.pass);
                event_content_saliva_status_text.setText(R.string.test_pass);
                break;
            case 1: // Fail
                event_content_saliva_status_icon.setBackgroundResource(R.drawable.notpass);
                event_content_saliva_status_text.setText(R.string.test_fail);
                break;
        }
    }


    @Override
    public void onResume() {
        Log.d("Ket", "EventContentActivity onStart");

        // Start countdown timer to check whether user's browsing is over 10 sec.
        browsingCountdown = new CountDownTimer(BROWSING_COUNTDOWN, BROWSING_COUNTDOWN){
            @Override
            public void onFinish() {
                // Invoke add score.
                Log.d("AddScore", "addScoreViewPage3Detail");
                AddScoreDataBase addScoreDataBase = new AddScoreDataBase();
                addScoreDataBase.addScoreViewPage3Detail();
            }

            @Override
            public void onTick(long millisUntilFinished) {
                // No op.
//                Log.d("AddScore", "Page3 - 2  Tick "+ millisUntilFinished/1000);
            }
        }.start();

        super.onResume();
    }


    @Override
    public void onPause() {
        Log.d("Ket", "EventContentActivity onPause");

        // Cancel browsing countdown when leaving this page.
        browsingCountdown.cancel();

        super.onPause();
    }


}
