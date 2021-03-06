package ubicomp.ketdiary.fragments.create_event.steps;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import ubicomp.ketdiary.R;
import ubicomp.ketdiary.fragments.event.EventLogStructure;
import ubicomp.ketdiary.fragments.create_event.CreateEventActivity;
import ubicomp.ketdiary.utility.data.db.ThirdPageDataBase;
import ubicomp.ketdiary.utility.data.structure.TriggerItem;

/**
 * Click listener of scenario icons in step2 of create event.
 *
 * Created by kelvindk on 16/6/11.
 */
public class StepScenarioWrapper implements View.OnClickListener {

    private CreateEventActivity createEventActivity = null;
    private EventLogStructure eventLogStructure = null;

    Spinner spinner_step2_question = null;
    EditText editText_scenario_step2 = null;

    private ImageButton[] step2Icons = new ImageButton[8];
    private int previousSelectedIcon = 0;

    private String dialogPrompt = null;

    String[] frequentInputString = null;


    public StepScenarioWrapper(CreateEventActivity createEventActivity) {
        this.createEventActivity = createEventActivity;
        this.eventLogStructure = createEventActivity.getEventLogStructure();

        // Set listener to image button: recent_emotion.
        ((ImageButton) createEventActivity.findViewById(R.id.scenario_step2)).setOnClickListener(scenario_step2);
        editText_scenario_step2 = ((EditText) createEventActivity.findViewById(R.id.editText_scenario_step2));
        editText_scenario_step2.setOnClickListener(scenario_step2);


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


    // Popup dialog to show recently use behavior.
    View.OnClickListener scenario_step2 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d("Ket", "scenario_step2");

            AlertDialog.Builder dialog = new AlertDialog.Builder(createEventActivity);
            dialog.setTitle(Html.fromHtml("<b>"+dialogPrompt+"</b>"));
            dialog.setItems(frequentInputString, new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int pos) {
                    // TODO Auto-generated method stub
                    Log.d("Ket", "scenario_step2 onClick");
                    logToEventLogStructure(frequentInputString[pos]);
                    editText_scenario_step2.setText(frequentInputString[pos]);
                }
            });

            dialog.show();
        }
    };

    /*** Log event originalBehavior. ***/
    private void logToEventLogStructure(String input) {
        eventLogStructure.scenario = input;
    }


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

        EventLogStructure.ScenarioTypeEnum selectedScenarioTypeEnum = null;

        switch(view.getId()) {
            case R.id.scenario_button1:
                Log.d("Ket", "StepScenarioWrapper "+view.getId()+" 1");
                iconSelectedStringId = R.string.slackness;
                iconSelectedDrawableId = R.drawable.type_icon1_pressed;
                previousSelectedIcon = 0;
                selectedScenarioTypeEnum = EventLogStructure.ScenarioTypeEnum.SLACKNESS;
                break;
            case R.id.scenario_button2:
                Log.d("Ket", "StepScenarioWrapper "+view.getId()+" 2");
                iconSelectedStringId = R.string.body;
                iconSelectedDrawableId = R.drawable.type_icon2_pressed;
                previousSelectedIcon = 1;
                selectedScenarioTypeEnum = EventLogStructure.ScenarioTypeEnum.BODY;
                break;
            case R.id.scenario_button3:
                Log.d("Ket", "StepScenarioWrapper "+view.getId()+" 3");
                iconSelectedStringId = R.string.control;
                iconSelectedDrawableId = R.drawable.type_icon3_pressed;
                previousSelectedIcon = 2;
                selectedScenarioTypeEnum = EventLogStructure.ScenarioTypeEnum.CONTROL;
                break;
            case R.id.scenario_button4:
                Log.d("Ket", "StepScenarioWrapper "+view.getId()+" 4");
                iconSelectedStringId = R.string.impulse;
                iconSelectedDrawableId = R.drawable.type_icon4_pressed;
                previousSelectedIcon = 3;
                selectedScenarioTypeEnum = EventLogStructure.ScenarioTypeEnum.IMPULSE;
                break;
            case R.id.scenario_button5:
                Log.d("Ket", "StepScenarioWrapper "+view.getId()+" 5");
                iconSelectedStringId = R.string.emotion;
                iconSelectedDrawableId = R.drawable.type_icon5_pressed;
                previousSelectedIcon = 4;
                selectedScenarioTypeEnum = EventLogStructure.ScenarioTypeEnum.EMOTION;
                break;
            case R.id.scenario_button6:
                Log.d("Ket", "StepScenarioWrapper "+view.getId()+" 6");
                iconSelectedStringId = R.string.get_along;
                iconSelectedDrawableId = R.drawable.type_icon6_pressed;
                previousSelectedIcon = 5;
                selectedScenarioTypeEnum = EventLogStructure.ScenarioTypeEnum.GET_ALONG;
                break;
            case R.id.scenario_button7:
                Log.d("Ket", "StepScenarioWrapper "+view.getId()+" 7");
                iconSelectedStringId = R.string.social;
                iconSelectedDrawableId = R.drawable.type_icon7_pressed;
                previousSelectedIcon = 6;
                selectedScenarioTypeEnum = EventLogStructure.ScenarioTypeEnum.SOCIAL;
                break;
            case R.id.scenario_button8:
                Log.d("Ket", "StepScenarioWrapper "+view.getId()+" 8");
                iconSelectedStringId = R.string.entertain;
                iconSelectedDrawableId = R.drawable.type_icon8_pressed;
                previousSelectedIcon = 7;
                selectedScenarioTypeEnum = EventLogStructure.ScenarioTypeEnum.ENTERTAIN;
                break;
        }

        // Set the prompt of popup dialog along with selected type of scenario.
        dialogPrompt = createEventActivity.getResources().getString(R.string.step2_question_right);
        dialogPrompt = "「"+createEventActivity.getResources().getString(iconSelectedStringId)+dialogPrompt;

        // Get content of selected scenario type from database.
        ThirdPageDataBase thirdPageDataBase = new ThirdPageDataBase();
        TriggerItem[] triggerItems = thirdPageDataBase.getTypeTrigger(previousSelectedIcon+1);
        if(triggerItems != null) {
            frequentInputString = new String[triggerItems.length];
            for(int i=0; i<triggerItems.length; i++) {
                /** Need to have a condition for determine show or not*/
                frequentInputString[i] = triggerItems[i].getContent();
            }
        }

        // Enable popup of dialog.
        editText_scenario_step2.performClick();
        ((TextView) createEventActivity.findViewById(R.id.textview_step2_question)).setText(dialogPrompt);
        ((LinearLayout) createEventActivity.findViewById(R.id.layout_step2_question)).setVisibility(View.VISIBLE);
        editText_scenario_step2.requestFocus();


        // Switch clicked icon to "selected" icon.
        view.setBackgroundResource(iconSelectedDrawableId);

        /*** Log event scenario type inputted by user. ***/
        eventLogStructure.scenarioType = selectedScenarioTypeEnum;
    }
}
