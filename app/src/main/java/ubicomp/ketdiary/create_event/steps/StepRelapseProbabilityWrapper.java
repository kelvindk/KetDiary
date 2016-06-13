package ubicomp.ketdiary.create_event.steps;


import android.util.Log;
import android.widget.RadioGroup;

import ubicomp.ketdiary.R;
import ubicomp.ketdiary.ui.CreateEventActivity;

/**
 * Related actions in step 3 of create event.
 *
 * Created by kelvindk on 16/6/13.
 */
public class StepRelapseProbabilityWrapper implements RadioGroup.OnCheckedChangeListener {

    private CreateEventActivity createEventActivity = null;

    public StepRelapseProbabilityWrapper(CreateEventActivity createEventActivity) {
        this.createEventActivity = createEventActivity;

        RadioGroup radioGroup = (RadioGroup) createEventActivity.findViewById(R.id.radioGroupStep3);
        radioGroup.setOnCheckedChangeListener(this);

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        switch (checkedId) {
            case R.id.radioButton1:
                Log.d("Ket", "radioButton1");
                break;
            case R.id.radioButton2:
                Log.d("Ket", "radioButton2");
                break;
            case R.id.radioButton3:
                Log.d("Ket", "radioButton3");
                break;
            case R.id.radioButton4:
                Log.d("Ket", "radioButton4");
                break;
            case R.id.radioButton5:
                Log.d("Ket", "radioButton5");
                break;
        }
    }
}
