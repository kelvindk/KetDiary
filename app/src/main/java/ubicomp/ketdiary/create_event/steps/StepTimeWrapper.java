package ubicomp.ketdiary.create_event.steps;

import android.widget.ArrayAdapter;
import android.widget.Spinner;

import ubicomp.ketdiary.R;
import ubicomp.ketdiary.create_event.SpinnerDayListener;
import ubicomp.ketdiary.create_event.SpinnerTimePeriodListener;
import ubicomp.ketdiary.fragments.event.EventLogStructure;
import ubicomp.ketdiary.ui.CreateEventActivity;

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

        // Setup spinner for step 1: day.
        spinner_day = (Spinner) createEventActivity.findViewById(R.id.spinner_day);
        // Set click listener.
        spinner_day.setOnItemSelectedListener(new SpinnerDayListener(createEventActivity, eventLogStructure));
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> spinner_day_adapter = ArrayAdapter.createFromResource(
                createEventActivity, R.array.spinner_day, android.R.layout.simple_spinner_item);
        spinner_day_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner_day.setAdapter(spinner_day_adapter);

        // Setup spinner for step 1: time period.
        spinner_time_period = (Spinner) createEventActivity.findViewById(R.id.spinner_time_period);
        // Set click listener.
        spinner_time_period.setOnItemSelectedListener(new SpinnerTimePeriodListener(createEventActivity, eventLogStructure));
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> spinner_time_period_adapter = ArrayAdapter.createFromResource(
                createEventActivity, R.array.spinner_time_period, android.R.layout.simple_spinner_item);
        spinner_time_period_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner_time_period.setAdapter(spinner_time_period_adapter);
    }
}
