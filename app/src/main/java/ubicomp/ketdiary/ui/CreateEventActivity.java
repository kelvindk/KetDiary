package ubicomp.ketdiary.ui;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import ubicomp.ketdiary.R;
import ubicomp.ketdiary.create_event.ScenarioIconClickListener;
import ubicomp.ketdiary.create_event.ScrollViewAdapter;
import ubicomp.ketdiary.create_event.SpinnerDayListener;
import ubicomp.ketdiary.create_event.SpinnerTimePeriodListener;

/**
 * A standalone activity for create(add) new event.
 *
 * Created by kelvindk on 16/6/7.
 */

public class CreateEventActivity extends AppCompatActivity {


    private Spinner spinner_day = null;
    private Spinner spinner_time_period = null;
    private ScrollViewAdapter scrollViewAdapter = null;
    private ScenarioIconClickListener scenarioIconClickListener = null;


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


        /*** Step1 ***/
        // Setup spinner for step 1: day.
        spinner_day = (Spinner) findViewById(R.id.spinner_day);
        // Set click listener.
        spinner_day.setOnItemSelectedListener(new SpinnerDayListener(this));
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> spinner_day_adapter = ArrayAdapter.createFromResource(
                this, R.array.spinner_day, android.R.layout.simple_spinner_item);
        spinner_day_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner_day.setAdapter(spinner_day_adapter);

        // Setup spinner for step 1: time period.
        spinner_time_period = (Spinner) findViewById(R.id.spinner_time_period);
        // Set click listener.
        spinner_time_period.setOnItemSelectedListener(new SpinnerTimePeriodListener(this));
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> spinner_time_period_adapter = ArrayAdapter.createFromResource(
                this, R.array.spinner_time_period, android.R.layout.simple_spinner_item);
        spinner_time_period_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner_time_period.setAdapter(spinner_time_period_adapter);


        /*** Step2 ***/
        scenarioIconClickListener = new ScenarioIconClickListener(this);

//        ListView listview_question_step1 = (ListView) findViewById(R.id.listview_question_step1);
//        ArrayAdapter<String> listview_question_step1_adapter =
//                new ArrayAdapter(this,android.R.layout.simple_list_item_1,
//                        getResources().getStringArray(R.array.spinner_step1));;
//        listview_question_step1.setAdapter(listview_question_step1_adapter);

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
                scrollViewAdapter.setScrollDisable(true);
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
                scrollViewAdapter.setScrollDisable(false);
                scrollViewAdapter.scrollToBottom();


            }
        });

        return true;
    }
}
