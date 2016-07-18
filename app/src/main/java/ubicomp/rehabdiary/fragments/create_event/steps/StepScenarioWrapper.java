package ubicomp.rehabdiary.fragments.create_event.steps;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import ubicomp.rehabdiary.R;
import ubicomp.rehabdiary.fragments.event.EventLogStructure;
import ubicomp.rehabdiary.fragments.create_event.CreateEventActivity;
import ubicomp.rehabdiary.utility.data.db.ThirdPageDataBase;
import ubicomp.rehabdiary.utility.data.structure.TriggerItem;

/**
 * Click listener of scenario icons in step2 of create event.
 *
 * Created by kelvindk on 16/6/11.
 */
public class StepScenarioWrapper implements View.OnClickListener {

    private CreateEventActivity createEventActivity = null;
    private EventLogStructure eventLogStructure = null;

    EditText editText_scenario_step2 = null;

    private ImageButton[] step2Icons = new ImageButton[9];
    private int previousSelectedIcon = 0;

    private String dialogPrompt = null;

    TriggerItem[] triggerItems = null;
    String[] frequentInputString = null;


    public StepScenarioWrapper(CreateEventActivity createEventActivity) {
        this.createEventActivity = createEventActivity;
        this.eventLogStructure = createEventActivity.getEventLogStructure();

        // Set listener to image button: recent_emotion.
//        ((ImageButton) createEventActivity.findViewById(R.id.scenario_step2)).setOnClickListener(scenario_step2);
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
        (step2Icons[8] = (ImageButton) createEventActivity.findViewById(R.id.scenario_button9)).
                setOnClickListener(this);

        // Load existed data.
        if(createEventActivity.getInitStep() != 0) {
            setScenarioSelection(eventLogStructure.scenarioType);
        }

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
                    Log.d("Ket", "scenario_step2 onClick "+pos+" "+triggerItems[pos].getItem());
                    // Store selected scenario to EventLogStructure and show on the screen.
                    logToEventLogStructure(frequentInputString[pos]);
                    editText_scenario_step2.setText(frequentInputString[pos]);

                    if(previousSelectedIcon == 8) {
                        // Unselected previous icon.
                        setIconToUnselected();
                        // Select icon according to type of scenario.
                        setScenarioSelection(EventLogStructure.
                                scenarioTypeIntToEnum(triggerItems[pos].getItem()/100));
                    }


                    // In edit mode, set clickable to "save" action button when selected.
                    if(createEventActivity.getInitStep() != 0)
                        createEventActivity.getScrollViewAdapter().setSaveEventButtonClickable(true);
                }
            });
            if(frequentInputString != null)
                dialog.setCancelable(false);


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
            case 8:
                previousSelectedIconId = R.id.scenario_button9;
                previousSelectedDrawableId = R.drawable.create_event_step2_selector_icon9;
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
            case R.id.scenario_button9:
                Log.d("Ket", "StepScenarioWrapper "+view.getId()+" 9");
                iconSelectedStringId = R.string.all_scenario;
                iconSelectedDrawableId = R.drawable.type_icon9_pressed;
                previousSelectedIcon = 8;
                selectedScenarioTypeEnum = EventLogStructure.ScenarioTypeEnum.ALL;
                break;
        }



        // Get content of selected scenario type from database.
        ThirdPageDataBase thirdPageDataBase = new ThirdPageDataBase();

        // If previousSelectedIcon is 8, load all scenarios.
        // Ans also set the prompt of popup dialog along with selected type of scenario.
        if(previousSelectedIcon == 8) {
            triggerItems = thirdPageDataBase.getAllTrigger();
            dialogPrompt = createEventActivity.getResources().getString(R.string.step2_all_scenario_question);
        }
        else {
            triggerItems = thirdPageDataBase.getTypeTrigger(previousSelectedIcon+1);
            dialogPrompt = createEventActivity.getResources().getString(R.string.step2_question_right);
            dialogPrompt = "「"+createEventActivity.getResources().getString(iconSelectedStringId)+dialogPrompt;
        }


        if(triggerItems != null) {
            frequentInputString = new String[triggerItems.length];

            for (int i = 0; i < triggerItems.length; i++) {
                /** Need to have a condition for determine show or not*/
                frequentInputString[i] = triggerItems[i].getContent();
            }
        }
        else {
            frequentInputString = null;
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

    /*
    *  Method to load data from eventLogStructure.
    * */
    private void setScenarioSelection(EventLogStructure.ScenarioTypeEnum scenarioType) {
        int iconSelectedStringId = 0;
        switch (scenarioType) {
            case SLACKNESS:
                //
                previousSelectedIcon = 0;
                iconSelectedStringId = R.string.slackness;
                step2Icons[0].setBackgroundResource(R.drawable.type_icon1_pressed);
                break;
            case BODY:
                //
                previousSelectedIcon = 1;
                iconSelectedStringId = R.string.body;
                step2Icons[1].setBackgroundResource(R.drawable.type_icon2_pressed);
                break;
            case CONTROL:
                //
                previousSelectedIcon = 2;
                iconSelectedStringId = R.string.control;
                step2Icons[2].setBackgroundResource(R.drawable.type_icon3_pressed);
                break;
            case IMPULSE:
                //
                previousSelectedIcon = 3;
                iconSelectedStringId = R.string.impulse;
                step2Icons[3].setBackgroundResource(R.drawable.type_icon4_pressed);
                break;
            case EMOTION:
                //
                previousSelectedIcon = 4;
                iconSelectedStringId = R.string.emotion;
                step2Icons[4].setBackgroundResource(R.drawable.type_icon5_pressed);
                break;
            case GET_ALONG:
                //
                previousSelectedIcon = 5;
                iconSelectedStringId = R.string.get_along;
                step2Icons[5].setBackgroundResource(R.drawable.type_icon6_pressed);
                break;
            case SOCIAL:
                //
                previousSelectedIcon = 6;
                iconSelectedStringId = R.string.social;
                step2Icons[6].setBackgroundResource(R.drawable.type_icon7_pressed);
                break;
            case ENTERTAIN:
                //
                previousSelectedIcon = 7;
                iconSelectedStringId = R.string.entertain;
                step2Icons[7].setBackgroundResource(R.drawable.type_icon8_pressed);
                break;
            case ALL:
                //
                previousSelectedIcon = 8;
                iconSelectedStringId = R.string.all_scenario;
                step2Icons[8].setBackgroundResource(R.drawable.type_icon9_pressed);
                break;
        }

        // iconSelectedStringId can be 0 only in developing phase, won't happen in regular case.
        if(iconSelectedStringId == 0)
            return;

        // Set the prompt of popup dialog along with selected type of scenario.
        dialogPrompt = createEventActivity.getResources().getString(R.string.step2_question_right);
        dialogPrompt = "「"+createEventActivity.getResources().getString(iconSelectedStringId)+dialogPrompt;

        ((TextView) createEventActivity.findViewById(R.id.textview_step2_question)).setText(dialogPrompt);
        ((LinearLayout) createEventActivity.findViewById(R.id.layout_step2_question)).setVisibility(View.VISIBLE);
        editText_scenario_step2.setText(eventLogStructure.scenario);
        editText_scenario_step2.requestFocus();

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
        else {
            frequentInputString = null;
        }
    }
}
