package ubicomp.ketdiary.create_event.steps;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import ubicomp.ketdiary.R;
import ubicomp.ketdiary.ui.CreateEventActivity;

/**
 * Click listener of scenario icons in step2 of create event.
 *
 * Created by kelvindk on 16/6/11.
 */
public class StepScenarioWrapper implements View.OnClickListener {

    private CreateEventActivity createEventActivity = null;

    Spinner spinner_step2_question = null;

    private ImageButton[] step2Icons = new ImageButton[8];
    private int previousSelectedIcon = 0;


    public StepScenarioWrapper(CreateEventActivity createEventActivity) {
        this.createEventActivity = createEventActivity;

        spinner_step2_question = (Spinner) createEventActivity.findViewById(R.id.spinner_step2_question);
        spinner_step2_question.setOnItemSelectedListener(spinnerListener);

        (step2Icons[0] = (ImageButton) createEventActivity.findViewById(R.id.scenario_button1)).
                setOnClickListener(this);
        (step2Icons[1] = (ImageButton) createEventActivity.findViewById(R.id.scenario_button2)).
                setOnClickListener(this);
        (step2Icons[2] = (ImageButton) createEventActivity.findViewById(R.id.scenario_button3)).
                setOnClickListener(this);
        (step2Icons[3] = (ImageButton) createEventActivity.findViewById(R.id.scenario_button4)).
                setOnClickListener(this);
        (step2Icons[4] = (ImageButton) createEventActivity.findViewById(R.id.scenario_button5)).
                setOnClickListener(this);
        (step2Icons[5] = (ImageButton) createEventActivity.findViewById(R.id.scenario_button6)).
                setOnClickListener(this);
        (step2Icons[6] = (ImageButton) createEventActivity.findViewById(R.id.scenario_button7)).
                setOnClickListener(this);
        (step2Icons[7] = (ImageButton) createEventActivity.findViewById(R.id.scenario_button8)).
                setOnClickListener(this);

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


    /*
    * Recover previous pressed icon to unpressed.
    * */
    private void setIconToUnselected() {
        int previousSelectedIconId = 0;
        int previousSelectedDrawableId = 0;

        switch (previousSelectedIcon) {
            case 0:
                previousSelectedIconId = R.id.scenario_button1;
                previousSelectedDrawableId = R.drawable.create_event_step2_selector_icon1;
                break;
            case 1:
                previousSelectedIconId = R.id.scenario_button2;
                previousSelectedDrawableId = R.drawable.create_event_step2_selector_icon2;
                break;
            case 2:
                previousSelectedIconId = R.id.scenario_button3;
                previousSelectedDrawableId = R.drawable.create_event_step2_selector_icon3;
                break;
            case 3:
                previousSelectedIconId = R.id.scenario_button4;
                previousSelectedDrawableId = R.drawable.create_event_step2_selector_icon4;
                break;
            case 4:
                previousSelectedIconId = R.id.scenario_button5;
                previousSelectedDrawableId = R.drawable.create_event_step2_selector_icon5;
                break;
            case 5:
                previousSelectedIconId = R.id.scenario_button6;
                previousSelectedDrawableId = R.drawable.create_event_step2_selector_icon6;
                break;
            case 6:
                previousSelectedIconId = R.id.scenario_button7;
                previousSelectedDrawableId = R.drawable.create_event_step2_selector_icon7;
                break;
            case 7:
                previousSelectedIconId = R.id.scenario_button8;
                previousSelectedDrawableId = R.drawable.create_event_step2_selector_icon8;
                break;

        }

        ((ImageButton) createEventActivity.findViewById(previousSelectedIconId)).setBackgroundResource(previousSelectedDrawableId);
    }


    @Override
    public void onClick(View view) {

        // Recover previous selected icon to unselected.
        setIconToUnselected();


        // Acquire the string and pressed icon ID along with selected icons.
        int iconSelectedStringId = 0;
        int iconSelectedDrawableId = 0;
        switch(view.getId()) {
            case R.id.scenario_button1:
                Log.d("Ket", "StepScenarioWrapper "+view.getId()+" 1");
                iconSelectedStringId = R.string.slackness;
                iconSelectedDrawableId = R.drawable.type_icon1_pressed;
                previousSelectedIcon = 0;
                break;
            case R.id.scenario_button2:
                Log.d("Ket", "StepScenarioWrapper "+view.getId()+" 2");
                iconSelectedStringId = R.string.body;
                iconSelectedDrawableId = R.drawable.type_icon2_pressed;
                previousSelectedIcon = 1;
                break;
            case R.id.scenario_button3:
                Log.d("Ket", "StepScenarioWrapper "+view.getId()+" 3");
                iconSelectedStringId = R.string.control;
                iconSelectedDrawableId = R.drawable.type_icon3_pressed;
                previousSelectedIcon = 2;
                break;
            case R.id.scenario_button4:
                Log.d("Ket", "StepScenarioWrapper "+view.getId()+" 4");
                iconSelectedStringId = R.string.impulse;
                iconSelectedDrawableId = R.drawable.type_icon4_pressed;
                previousSelectedIcon = 3;
                break;
            case R.id.scenario_button5:
                Log.d("Ket", "StepScenarioWrapper "+view.getId()+" 5");
                iconSelectedStringId = R.string.emotion;
                iconSelectedDrawableId = R.drawable.type_icon5_pressed;
                previousSelectedIcon = 4;
                break;
            case R.id.scenario_button6:
                Log.d("Ket", "StepScenarioWrapper "+view.getId()+" 6");
                iconSelectedStringId = R.string.get_along;
                iconSelectedDrawableId = R.drawable.type_icon6_pressed;
                previousSelectedIcon = 5;
                break;
            case R.id.scenario_button7:
                Log.d("Ket", "StepScenarioWrapper "+view.getId()+" 7");
                iconSelectedStringId = R.string.social;
                iconSelectedDrawableId = R.drawable.type_icon7_pressed;
                previousSelectedIcon = 6;
                break;
            case R.id.scenario_button8:
                Log.d("Ket", "StepScenarioWrapper "+view.getId()+" 8");
                iconSelectedStringId = R.string.entertain;
                iconSelectedDrawableId = R.drawable.type_icon8_pressed;
                previousSelectedIcon = 7;
                break;
        }

        // Set the prompt of the spinner's popup along with selected type of scenario.
        String spinnerPrompt = createEventActivity.getResources().getString(R.string.step2_question_right);

        spinnerPrompt = "「"+createEventActivity.getResources().getString(iconSelectedStringId)+spinnerPrompt;
        spinner_step2_question.setPrompt(spinnerPrompt);


        // Enable popup of spinner.
        spinner_step2_question.performClick();
        ((TextView) createEventActivity.findViewById(R.id.textview_step2_question)).setText(spinnerPrompt);

        ((LinearLayout) createEventActivity.findViewById(R.id.layout_step2_question)).setVisibility(View.VISIBLE);

        view.setBackgroundResource(iconSelectedDrawableId);
    }
}
