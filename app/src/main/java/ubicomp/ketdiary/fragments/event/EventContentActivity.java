package ubicomp.ketdiary.fragments.event;

import android.media.Rating;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.Calendar;

import ubicomp.ketdiary.R;

/**
 * Created by kelvindk on 16/6/10.
 */
public class EventContentActivity extends AppCompatActivity {

    public final static int MIDNIGHT = 0;
    public final static int MORNING = 1;
    public final static int AFTERNOON = 2;
    public final static int NIGHT = 3;

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

    private EventLogStructure eventLog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_content);

        // Set full screen.
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN );

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


        // Retrieve EventLogStructure.
        Bundle bundle = getIntent().getExtras();
        eventLog = (EventLogStructure) bundle.getSerializable(EventLogStructure.EVENT_LOG_STRUCUTRE_KEY);


        // Fill content on UI components.
        Calendar calendar = eventLog.createTime;
        String week = "";
        switch(calendar.get(Calendar.DAY_OF_WEEK)){
            case Calendar.SUNDAY:
                week = "(日)";
                break;
            case Calendar.MONDAY:
                week = "(一)";
                break;
            case Calendar.TUESDAY:
                week = "(二)";
                break;
            case Calendar.WEDNESDAY:
                week = "(三)";
                break;
            case Calendar.THURSDAY:
                week = "(四)";
                break;
            case Calendar.FRIDAY:
                week = "(五)";
                break;
            case Calendar.SATURDAY:
                week = "(六)";
        }

        String timePeriod = "";
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        switch(hour/6) {
            case MIDNIGHT:
                timePeriod = getString(R.string.midnight);
                break;
            case MORNING:
                timePeriod = getString(R.string.morning);
                break;
            case AFTERNOON:
                timePeriod = getString(R.string.afternoon);
                break;
            case NIGHT:
                timePeriod = getString(R.string.night);
                break;
        }
        String date = (calendar.get(Calendar.MONTH)+1)+"/"+calendar.get(Calendar.DAY_OF_MONTH)+ " "
                +week+" "+timePeriod;

        event_content_container_event_date.setText(date);

        /***/

        int iconId = 0;
        switch (eventLog.scenarioType) {
            case SLACKNESS:
                iconId = R.drawable.type_icon1;
                break;
            case BODY:
                iconId = R.drawable.type_icon2;
                break;
            case CONTROL:
                iconId = R.drawable.type_icon3;
                break;
            case IMPULSE:
                iconId = R.drawable.type_icon4;
                break;
            case EMOTION:
                iconId = R.drawable.type_icon5;
                break;
            case GET_ALONG:
                iconId = R.drawable.type_icon6;
                break;
            case SOCIAL:
                iconId = R.drawable.type_icon7;
                break;
            case ENTERTAIN:
                iconId = R.drawable.type_icon8;
                break;
        }
        event_content_container_scenario_type.setImageResource(iconId);

        /***/
        event_content_container_scenario.setText(eventLog.scenario);

        event_content_container_expected_thought.setText(eventLog.expectedThought);
        event_content_container_expected_emotion.setText(eventLog.expectedEmotion);
        event_content_container_expected_behavior.setText(eventLog.expectedBehavior);

        event_content_container_original_thought.setText(eventLog.originalThought);
        event_content_container_original_emotion.setText(eventLog.originalEmotion);
        event_content_container_original_behavior.setText(eventLog.originalBehavior);

        /***/
        event_content_container_drug_use_risk.setRating(eventLog.drugUseRiskLevel);


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

}
