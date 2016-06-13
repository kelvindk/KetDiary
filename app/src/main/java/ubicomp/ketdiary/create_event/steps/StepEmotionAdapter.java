package ubicomp.ketdiary.create_event.steps;


import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import ubicomp.ketdiary.R;
import ubicomp.ketdiary.ui.CreateEventActivity;

/**
 * Related actions in step 5 of create event.
 *
 * Created by kelvindk on 16/6/13.
 */
public class StepEmotionAdapter {

    private CreateEventActivity createEventActivity = null;

    Spinner spinner_step5_question = null;

    public StepEmotionAdapter(CreateEventActivity createEventActivity) {
        this.createEventActivity = createEventActivity;

        spinner_step5_question = (Spinner) createEventActivity.findViewById(R.id.spinner_step2_question);
        spinner_step5_question.setOnItemSelectedListener(spinnerListener);

    }

    /*
    *  Select listener for spinner of scenario.
    * */
    AdapterView.OnItemSelectedListener spinnerListener = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Log.d("Ket", " "+position);

            // Forward fill up to next step.
            /* No yet implement*/
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

}
