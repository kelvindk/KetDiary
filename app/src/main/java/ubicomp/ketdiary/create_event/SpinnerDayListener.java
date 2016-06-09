package ubicomp.ketdiary.create_event;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.Calendar;

import ubicomp.ketdiary.R;
import ubicomp.ketdiary.ui.CreateEventActivity;

/**
 * On create event page, the click listener of spinner day.
 *
 * Created by kelvindk on 16/6/9.
 */
public class SpinnerDayListener implements AdapterView.OnItemSelectedListener {

    public final static int TODAY = 0;
    public final static int YESTERDAY = 1;
    public final static int BEFORE_YESTERDAY = 2;


    private CreateEventActivity createEventActivity = null;

    public SpinnerDayListener(CreateEventActivity createEventActivity) {
        this.createEventActivity = createEventActivity;
    }

    /*
    * Spinner time period click listener.
    * */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.d("Ket", "spinner_day click");
        Calendar calendar = Calendar.getInstance();


        switch(position) {
            case TODAY:

                break;
            case YESTERDAY:
                calendar.add(Calendar.DATE, -1);
                break;
            case BEFORE_YESTERDAY:
                calendar.add(Calendar.DATE, -2);
                break;
        }

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Set time period to selected one.
        TextView dayText = (TextView) this.createEventActivity.findViewById(R.id.create_event_day);
        dayText.setText(year+"."+month+"."+day);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
