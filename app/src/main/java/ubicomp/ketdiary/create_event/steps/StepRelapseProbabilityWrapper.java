package ubicomp.ketdiary.create_event.steps;


import android.util.Log;
import android.widget.RadioGroup;

import ubicomp.ketdiary.R;
import ubicomp.ketdiary.fragments.event.EventLogStructure;
import ubicomp.ketdiary.create_event.CreateEventActivity;

/**
 * Related actions in step 3 of create event.
 *
 * Created by kelvindk on 16/6/13.
 */
public class StepRelapseProbabilityWrapper implements RadioGroup.OnCheckedChangeListener {
    public static final int DRUG_USE_RISK_LEVEL_DEFAULT = 3;

    private CreateEventActivity createEventActivity = null;
    private EventLogStructure eventLogStructure = null;

    public StepRelapseProbabilityWrapper(CreateEventActivity createEventActivity) {
        this.createEventActivity = createEventActivity;
        this.eventLogStructure = createEventActivity.getEventLogStructure();

        RadioGroup radioGroup = (RadioGroup) createEventActivity.findViewById(R.id.radioGroupStep3);
        radioGroup.setOnCheckedChangeListener(this);

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        // Drug use risk level: 1~5.
        int drugUseRiskLevel = DRUG_USE_RISK_LEVEL_DEFAULT;

        switch (checkedId) {
            case R.id.radioButton1:
                Log.d("Ket", "radioButton1");
                drugUseRiskLevel = 1;
                break;
            case R.id.radioButton2:
                Log.d("Ket", "radioButton2");
                drugUseRiskLevel = 2;
                break;
            case R.id.radioButton3:
                Log.d("Ket", "radioButton3");
                drugUseRiskLevel = 3;
                break;
            case R.id.radioButton4:
                Log.d("Ket", "radioButton4");
                drugUseRiskLevel = 4;
                break;
            case R.id.radioButton5:
                Log.d("Ket", "radioButton5");
                drugUseRiskLevel = 5;
                break;
        }

        /*** Log event drugUseRiskLevel selected by user. ***/
        eventLogStructure.drugUseRiskLevel = drugUseRiskLevel;
    }
}
