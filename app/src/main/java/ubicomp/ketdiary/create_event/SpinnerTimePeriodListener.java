package ubicomp.ketdiary.create_event;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Calendar;

import ubicomp.ketdiary.R;
import ubicomp.ketdiary.ui.CreateEventActivity;

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
    private boolean isInit = false;

    public SpinnerTimePeriodListener(CreateEventActivity createEventActivity) {
        this.createEventActivity = createEventActivity;


    }

    /*
    * Set time period to the TextView.
    * */
    private void setTimePeriodText(int time) {
        TextView timePeriod = (TextView) this.createEventActivity.findViewById(R.id.create_event_time_period);
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
    private void setSpinnerTimePeriod(int time) {
        Spinner spinnerTimerPeriod = (Spinner) this.createEventActivity.findViewById(R.id.spinner_time_period);

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
    * Spinner time period click listener.
    * */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.d("Ket", "spinner_time_period click");

        // Set default to current time period when the spinner is initialized.
        if(!isInit) {
            int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            setTimePeriodText(hour);
            setSpinnerTimePeriod(hour);
            isInit = true;
            return;
        }

        // Set time period to selected one.
        TextView timePeriodText = (TextView) this.createEventActivity.findViewById(R.id.create_event_time_period);
        timePeriodText.setText(parent.getItemAtPosition(position).toString());


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
