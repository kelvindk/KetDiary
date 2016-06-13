package ubicomp.ketdiary.ui;

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

import ubicomp.ketdiary.R;
import ubicomp.ketdiary.create_event.steps.*;
import ubicomp.ketdiary.create_event.ScrollViewAdapter;
import ubicomp.ketdiary.create_event.steps.StepTimeWrapper;

/**
 * A standalone activity for create(add) new event.
 *
 * Created by kelvindk on 16/6/7.
 */

public class CreateEventActivity extends AppCompatActivity {

    private ScrollViewAdapter scrollViewAdapter = null;
    private StepTimeWrapper step1Adapter = null;
    private StepScenarioWrapper step2Adapter = null;
    private StepRelapseProbabilityWrapper step3Adapter = null;
    private StepBehaviorWrapper step4Adapter = null;
    private StepEmotionWrapper step5Adapter = null;
    private StepThoughtWrapper step6Adapter = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

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
//                // Listener of action Save button
                scrollViewAdapter.scrollToTop();

            }
        });

        MenuItem actionCancelEvent = menu.findItem(R.id.action_cancel);
        MenuItemCompat.getActionView(actionCancelEvent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Ket", "actionCancelEvent");
                // Listener of action Cancel button, back previous page after click.
//                onBackPressed();
                scrollViewAdapter.scrollToBottom();


            }
        });

        return true;
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
        }
    }
}
