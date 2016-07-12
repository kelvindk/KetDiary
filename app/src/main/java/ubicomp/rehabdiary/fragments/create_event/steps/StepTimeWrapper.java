package ubicomp.rehabdiary.fragments.create_event.steps;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.Calendar;

import ubicomp.rehabdiary.R;
import ubicomp.rehabdiary.fragments.create_event.SpinnerDayListener;
import ubicomp.rehabdiary.fragments.create_event.SpinnerTimePeriodListener;
import ubicomp.rehabdiary.fragments.event.EventLogStructure;
import ubicomp.rehabdiary.fragments.create_event.CreateEventActivity;

/**
 * Related actions in step 1 of create event.
 *
 * Created by kelvindk on 16/6/13.
 */
public class StepTimeWrapper {

    private CreateEventActivity createEventActivity = null;
    private EventLogStructure eventLogStructure = null;

    private Spinner spinner_day = null;
    private Spinner spinner_time_period = null;

    public StepTimeWrapper(CreateEventActivity createEventActivity) {
        this.createEventActivity = createEventActivity;

        // Get the EvenLogStructure for filling the field.
        eventLogStructure = createEventActivity.getEventLogStructure();

        // Setup spinner for step 1: time period.
        spinner_time_period = (Spinner) createEventActivity.findViewById(R.id.spinner_time_period);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> spinner_time_period_adapter = ArrayAdapter.createFromResource(
                createEventActivity, R.array.spinner_time_period, android.R.layout.simple_spinner_item);
        spinner_time_period_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner_time_period.setAdapter(spinner_time_period_adapter);

        // Setup spinner for step 1: day.
        spinner_day = (Spinner) createEventActivity.findViewById(R.id.spinner_day);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> spinner_day_adapter = ArrayAdapter.createFromResource(
                createEventActivity, R.array.spinner_day, android.R.layout.simple_spinner_item);
        spinner_day_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner_day.setAdapter(spinner_day_adapter);


        // New listeners.
        SpinnerTimePeriodListener spinnerTimePeriodListener =
                new SpinnerTimePeriodListener(createEventActivity, eventLogStructure);

        SpinnerDayListener spinnerDayListener =
                new SpinnerDayListener(createEventActivity, eventLogStructure);

        // Load existed data.
        if(createEventActivity.getInitStep() != 0) {
            // Load to time spinner.
            // Disable clickable.
            spinner_time_period.setEnabled(false);
            int hour = eventLogStructure.eventTime.get(Calendar.HOUR_OF_DAY);
            spinner_time_period.setVisibility(View.GONE);
            spinnerTimePeriodListener.setTimePeriodText(hour);

            // Load to day spinner.
            spinner_day.setEnabled(false);
            spinner_day.setVisibility(View.GONE);
            spinnerDayListener.setDateText(eventLogStructure.eventTime);
        }
        else {
            // Set click listeners.
            spinner_day.setOnItemSelectedListener(spinnerDayListener);
            spinner_time_period.setOnItemSelectedListener(spinnerTimePeriodListener);
        }

    }
}
