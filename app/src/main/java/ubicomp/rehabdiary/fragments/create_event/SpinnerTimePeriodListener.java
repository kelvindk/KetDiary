package ubicomp.rehabdiary.fragments.create_event;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;

import ubicomp.rehabdiary.R;
import ubicomp.rehabdiary.fragments.event.EventLogStructure;

/**
 * On create event page, the click listener of spinner time period.
 *
 * Created by kelvindk on 16/6/9.
 */
public class SpinnerTimePeriodListener implements AdapterView.OnItemSelectedListener {

    public final static int MIDNIGHT = 0;
    public final static int MORNING = 1;
    public final static int AFTERNOON = 2;
    public final static int NIGHT = 3;

    private CreateEventActivity createEventActivity = null;
    private EventLogStructure eventLogStructure = null;
    private boolean isInit = false;

    public SpinnerTimePeriodListener(CreateEventActivity createEventActivity, EventLogStructure eventLogStructure) {
        this.createEventActivity = createEventActivity;
        this.eventLogStructure = eventLogStructure;

    }

    /*
    * Set time period to the TextView.
    * */
    public void setTimePeriodText(int time) {
        TextView timePeriod = (TextView) createEventActivity.findViewById(R.id.create_event_time_period);
        switch(time/6) {
            case MIDNIGHT:
                timePeriod.setText(R.string.midnight);
                break;
            case MORNING:
                timePeriod.setText(R.string.morning);
                break;
            case AFTERNOON:
                timePeriod.setText(R.string.afternoon);
                break;
            case NIGHT:
                timePeriod.setText(R.string.night);
                break;
        }
    }


    /*
    * Set time period to the spinner timer period.
    * */
    public void setSpinnerTimePeriod(int time) {
        Spinner spinnerTimerPeriod = (Spinner) createEventActivity.findViewById(R.id.spinner_time_period);

        /* Set default to current time period.
           NOTE: The time period's order is different between define in this class and on spinner.
        */
        switch(time/6) {
            case MIDNIGHT:
                spinnerTimerPeriod.setSelection(3);
                break;
            case MORNING:
                spinnerTimerPeriod.setSelection(0);
                break;
            case AFTERNOON:
                spinnerTimerPeriod.setSelection(1);
                break;
            case NIGHT:
                spinnerTimerPeriod.setSelection(2);
                break;
        }
    }

    /*
    * Get time period in hour along to spinner select.
    * */
    private int getHourBySelect(int pos) {

        /* Set default to current time period.
        */
        switch(pos) {
            case 0:
                return MORNING*6;
            case 1:
                return AFTERNOON*6;
            case 2:
                return NIGHT*6;
            case 3:
                return MIDNIGHT*6;
        }

        return 0;
    }



    /*
    * Spinner time period click listener.
    * */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        // Set default to current time period when the spinner is initialized.
        if(!isInit) {
            int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            setSpinnerTimePeriod(hour);

            isInit = true;
            return;
        }


        // Set time period to selected one.
        TextView timePeriodText = (TextView) createEventActivity.findViewById(R.id.create_event_time_period);
        timePeriodText.setText(parent.getItemAtPosition(position).toString());

        /*** Log hour to eventTime in eventLogStructure. ***/
        eventLogStructure.eventTime.set(Calendar.MINUTE, 0);
        eventLogStructure.eventTime.set(Calendar.SECOND, 0);
        eventLogStructure.eventTime.set(Calendar.MILLISECOND, 0);
        eventLogStructure.eventTime.set(Calendar.HOUR_OF_DAY, getHourBySelect(position));

        Log.d("Ket", "spinner_time_period click");
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
