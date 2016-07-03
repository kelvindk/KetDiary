package ubicomp.ketdiary.fragments.event;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

import ubicomp.ketdiary.R;
import ubicomp.ketdiary.fragments.create_event.CreateEventActivity;

/**
 * Created by kelvindk on 16/6/10.
 */
public class EventContentActivity extends AppCompatActivity {

    // Key of this object for deliver between activities through Intent.
    public static final String EVENT_CONTENT_ACTIVITY_KEY = "EventContentActivity";

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

    private TextView event_content_container_therapy_status = null;
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

        event_content_container_therapy_status = (TextView) findViewById(R.id.event_content_container_therapy_status);
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
//        if(eventLog.reviseExpectedThought) {
            event_content_container_expected_thought_revise.setVisibility(View.VISIBLE);
            event_content_container_expected_thought_layout.setBackgroundResource(R.drawable.layout_red_frame);
            event_content_container_expected_thought_layout.setOnClickListener(eventContentClickListener);
//        }
//        if(eventLog.reviseExpectedEmotion) {
            event_content_container_expected_emotion_revise.setVisibility(View.VISIBLE);
            event_content_container_expected_emotion_layout.setBackgroundResource(R.drawable.layout_red_frame);
            event_content_container_expected_emotion_layout.setOnClickListener(eventContentClickListener);
//        }
//        if(eventLog.reviseExpectedBehavior) {
            event_content_container_expected_behavior_revise.setVisibility(View.VISIBLE);
            event_content_container_expected_behavior_layout.setBackgroundResource(R.drawable.layout_red_frame);
            event_content_container_expected_behavior_layout.setOnClickListener(eventContentClickListener);
//        }
//        if(eventLog.reviseOriginalThought) {
            event_content_container_original_thought_revise.setVisibility(View.VISIBLE);
            event_content_container_original_thought_layout.setBackgroundResource(R.drawable.layout_red_frame);
            event_content_container_original_thought_layout.setOnClickListener(eventContentClickListener);
//        }
//        if(eventLog.reviseOriginalEmotion) {
            event_content_container_original_emotion_revise.setVisibility(View.VISIBLE);
            event_content_container_original_emotion_layout.setBackgroundResource(R.drawable.layout_red_frame);
            event_content_container_original_emotion_layout.setOnClickListener(eventContentClickListener);
//        }
        if(eventLog.reviseOriginalBehavior) {
            event_content_container_original_behavior_revise.setVisibility(View.VISIBLE);
            event_content_container_original_behavior_layout.setBackgroundResource(R.drawable.layout_red_frame);
            event_content_container_original_behavior_layout.setOnClickListener(eventContentClickListener);
        }

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
                        /* User clicked "Confirm"*/
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

            // Start startCreateEventActivity.
            startCreateEventActivity(step);
        }
    };

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
        startActivity(intent);
    }

}
