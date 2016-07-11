package ubicomp.ketdiary.fragments.create_event;

import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import ubicomp.ketdiary.R;

/**
 * This class handle ScrollView's access in create event pgg.
 *
 * Created by kelvindk on 16/6/10.
 */
public class ScrollViewAdapter implements View.OnTouchListener {

    public static final int NUMBER_OF_TOTAL_STEPS = 9;
    public static final int STEPS_ALLOW_SAVE = 3;

    public static final int STEP4_COVER_BASE_DP_HEIGHT = 50;
    public static final int STEP5_COVER_BASE_DP_HEIGHT = 65;
    public static final int STEP6_COVER_BASE_DP_HEIGHT = 65;
    public static final int STEP7_COVER_BASE_DP_HEIGHT = 75;
    public static final int STEP8_COVER_BASE_DP_HEIGHT = 65;

    private CreateEventActivity createEventActivity = null;
    private ScrollView scrollView = null;

    private float displayDensity = 0;

    // Indicate scrollability of the ListView. A trick to disable scrolling.
    private boolean isScrollDisable = false;

    // Current step of create event.
    private int currentStep = 0;
    public int getCurrentStep() {
        return currentStep;
    }

    // Current most advanced step had reach. Will only be added.
    private int currentMaxStep = 0;

    public int getCurrentMaxStep() {
        return currentMaxStep;
    }

    // RelativeLayouts of steps, this array is used to set visibility of step's content.
    private RelativeLayout[] stepRelativeLayouts = new RelativeLayout[NUMBER_OF_TOTAL_STEPS];
    // Buttons of steps as a trick for setting clickable and transparent of of each step
    private Button[] stepCoverButtons = new Button[NUMBER_OF_TOTAL_STEPS];
    // Progress bar & text.
    private RatingBar progressBar = null;
    private TextView progressText = null;

    // Toast for warring.
    private Toast[] toasts = new Toast[NUMBER_OF_TOTAL_STEPS];


    // Boolean to store action save button state.
    private boolean isActionSaveButtonClickable = false;

    public boolean isActionSaveButtonClickable() {
        return isActionSaveButtonClickable;
    }

    public ScrollViewAdapter(CreateEventActivity createEventActivity) {
        this.createEventActivity = createEventActivity;

        // Get display density.
        displayDensity = createEventActivity.getResources().getDisplayMetrics().density;

        // Get RelativeLayouts and Buttons of steps.
        stepRelativeLayouts[0] = (RelativeLayout) createEventActivity.findViewById(R.id.create_event_step1);
        stepRelativeLayouts[1] = (RelativeLayout) createEventActivity.findViewById(R.id.create_event_step2);
        stepRelativeLayouts[2] = (RelativeLayout) createEventActivity.findViewById(R.id.create_event_step3);
        stepRelativeLayouts[3] = (RelativeLayout) createEventActivity.findViewById(R.id.create_event_step4);
        stepRelativeLayouts[4] = (RelativeLayout) createEventActivity.findViewById(R.id.create_event_step5);
        stepRelativeLayouts[5] = (RelativeLayout) createEventActivity.findViewById(R.id.create_event_step6);
        stepRelativeLayouts[6] = (RelativeLayout) createEventActivity.findViewById(R.id.create_event_step7);
        stepRelativeLayouts[7] = (RelativeLayout) createEventActivity.findViewById(R.id.create_event_step8);
        stepRelativeLayouts[8] = (RelativeLayout) createEventActivity.findViewById(R.id.create_event_step9);

        stepCoverButtons[0] = (Button) createEventActivity.findViewById(R.id.create_event_cover_step1);
        stepCoverButtons[1] = (Button) createEventActivity.findViewById(R.id.create_event_cover_step2);
        stepCoverButtons[2] = (Button) createEventActivity.findViewById(R.id.create_event_cover_step3);
        stepCoverButtons[3] = (Button) createEventActivity.findViewById(R.id.create_event_cover_step4);
        stepCoverButtons[4] = (Button) createEventActivity.findViewById(R.id.create_event_cover_step5);
        stepCoverButtons[5] = (Button) createEventActivity.findViewById(R.id.create_event_cover_step6);
        stepCoverButtons[6] = (Button) createEventActivity.findViewById(R.id.create_event_cover_step7);
        stepCoverButtons[7] = (Button) createEventActivity.findViewById(R.id.create_event_cover_step8);
        stepCoverButtons[8] = (Button) createEventActivity.findViewById(R.id.create_event_cover_step9);

        //
        toasts[0] = Toast.makeText(createEventActivity, "", Toast.LENGTH_SHORT);
        toasts[1] = Toast.makeText(createEventActivity, R.string.warning_empty_step2, Toast.LENGTH_SHORT);
        toasts[2] = Toast.makeText(createEventActivity, R.string.warning_empty_step3, Toast.LENGTH_SHORT);
        toasts[3] = Toast.makeText(createEventActivity, R.string.warning_empty_step4, Toast.LENGTH_SHORT);
        toasts[4] = Toast.makeText(createEventActivity, R.string.warning_empty_step5, Toast.LENGTH_SHORT);
        toasts[5] = Toast.makeText(createEventActivity, R.string.warning_empty_step6, Toast.LENGTH_SHORT);
        toasts[6] = Toast.makeText(createEventActivity, R.string.warning_empty_step7, Toast.LENGTH_SHORT);
        toasts[7] = Toast.makeText(createEventActivity, R.string.warning_empty_step8, Toast.LENGTH_SHORT);
        toasts[8] = Toast.makeText(createEventActivity, R.string.warning_empty_step9, Toast.LENGTH_SHORT);

        // Progress bar in toolbar.
        progressBar = (RatingBar) createEventActivity.findViewById(R.id.create_event_progress_bar);
        progressText = (TextView) createEventActivity.findViewById(R.id.create_event_progress_text);

        // Get ScrollView of create event page and set a touch listener.
        scrollView = (ScrollView) createEventActivity.
                findViewById(R.id.activity_create_event_scrollview);
        scrollView.setOnTouchListener(this);

        // Set listener to previous & next step button .
        createEventActivity.findViewById(R.id.create_event_previous).
                setOnClickListener(previousButtonOnClickListener);
        createEventActivity.findViewById(R.id.create_event_next).
                setOnClickListener(nextButtonOnClickListener);

    }



    /*
    *   Click listener of previous step button.
    * */
    View.OnClickListener previousButtonOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Log.d("Ket", "PreviousButtonOnclick");
            if((currentStep < currentMaxStep) && isCurrentStepContentEmpty()) {

            }
            else {
//                toasts[currentStep].cancel();
                performPreviousStep();
            }
        }
    };

    /*
    *   Click listener of next step button.
    * */
    View.OnClickListener nextButtonOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Log.d("Ket", "NextButtonOnclick");
            if(isCurrentStepContentEmpty()) {
//                setSaveEventButtonClickable(false);
            }
            else {
//                toasts[currentStep].cancel();
                performNextStep();
            }

        }
    };

    /*
    *  Check current step is not empty.
    * */
    private boolean isCurrentStepContentEmpty() {

        switch (currentStep) {
            case 1:
                if(((EditText)createEventActivity.
                        findViewById(R.id.editText_scenario_step2)).getText().length() == 0) {
                    toasts[1].show();
                    return true;
                }
                break;
            case 2:
                RadioGroup radioGroup = (RadioGroup) createEventActivity.findViewById(R.id.radioGroupStep3);
                int id = radioGroup.getCheckedRadioButtonId();
                if(id < 0) {
                    toasts[2].show();
                    return true;
                }
                break;
            case 3:
                if(((EditText)createEventActivity.
                        findViewById(R.id.editText_behavior_step4)).getText().length() == 0) {
                    toasts[3].show();
                    return true;
                }
                break;
            case 4:
                if(((EditText)createEventActivity.
                        findViewById(R.id.editText_emotion_step5)).getText().length() == 0) {
                    toasts[4].show();
                    return true;
                }
                break;
            case 5:
                if(((EditText)createEventActivity.
                        findViewById(R.id.editText_thought_step6)).getText().length() == 0) {
                    toasts[5].show();
                    return true;
                }
                break;
            case 6:
                if(((EditText)createEventActivity.
                        findViewById(R.id.editText_behavior_step7)).getText().length() == 0) {
                    toasts[6].show();
                    return true;
                }
                break;
            case 7:
                if(((EditText)createEventActivity.
                        findViewById(R.id.editText_emotion_step8)).getText().length() == 0) {
                    toasts[7].show();
                    return true;
                }
                break;
            case 8:
                if(((EditText)createEventActivity.
                        findViewById(R.id.editText_thought_step9)).getText().length() == 0) {
                    toasts[8].show();
                    return true;
                }
                break;

        }

        return false;
    }

    public void performPreviousStep() {
        // Set Gone to the part of current step.
        stepRelativeLayouts[currentStep].setVisibility(View.GONE);

        // Set "next step"  to next step button if current is last step.
        if(currentStep == (NUMBER_OF_TOTAL_STEPS-1)) {
            ((Button) createEventActivity.findViewById(R.id.create_event_next)).
                    setText(R.string.next_step);
        }

        // Reduce the count to previous step.
        currentStep--;


        switch(currentStep) {
            case 6:
                // Show text of scenario & original behavior.
                createEventActivity.findViewById(R.id.textview_step7_scenario).setVisibility(View.VISIBLE);

                break;
            case 7:
                // Show text of scenario & original emotion.
                createEventActivity.findViewById(R.id.textview_step8_scenario).setVisibility(View.VISIBLE);

                break;

            case 8:
                // Show text of scenario & original thought.
                createEventActivity.findViewById(R.id.textview_step9_scenario).setVisibility(View.VISIBLE);

                break;
        }


        // Update progress bar.
        progressBar.setRating(currentStep+1);
        progressText.setText((currentStep+1)+"/9");

        // Invisible the "previous step" button if reach the first step.
        if(currentStep <= 0) {
            currentStep = 0;
            ((Button) createEventActivity.findViewById(R.id.create_event_previous)).
                    setVisibility(View.INVISIBLE);
        }

        // Disable the button cover of previous step.
        stepCoverButtons[currentStep].setVisibility(View.GONE);

        // Scroll screen to button after a micro delay.
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Scroll screen to button.
                scrollToBottom();
            }
        }, 50);

        // Disable "Save" action button, if currentStep < 2.
        if(currentMaxStep < STEPS_ALLOW_SAVE) {
            setSaveEventButtonClickable(false);
        }

        // Do specific UI actions for particular step.
        setUiActions();
    }



    public void performNextStep() {



        if(currentStep == (NUMBER_OF_TOTAL_STEPS-1)) {
            Log.d("Ket", "NextButtonOnclick complete");
            // Trigger action save button to perform save as well as user click.
            MenuItem actionSaveEvent = createEventActivity.getMenu().findItem(R.id.action_save);
            MenuItemCompat.getActionView(actionSaveEvent).performClick();
            // Also press onBackPressed() of createEventActivity to exit.
            createEventActivity.onBackPressed();
            return;
        }


        // Enable the button cover of current step.
        stepCoverButtons[currentStep].setVisibility(View.VISIBLE);

        // Set Visible to previous button if current is going to step 1.
        if(currentStep == 0) {
            ((Button) createEventActivity.findViewById(R.id.create_event_previous)).
                    setVisibility(View.VISIBLE);
        }

        // Advance the count to next step.
        currentStep++;

        // Handle question for reflection steps.
        setReflectionText();


        // Update progress bar.
        progressBar.setRating(currentStep+1);
        progressText.setText((currentStep+1)+"/9");

        // Keep most advanced step had reach.
        if(currentStep > currentMaxStep)
            currentMaxStep = currentStep;

        // Set "complete" to next step button if reach the last step.
        if(currentStep >= (NUMBER_OF_TOTAL_STEPS-1)) {
            currentStep = NUMBER_OF_TOTAL_STEPS-1;
            ((Button) createEventActivity.findViewById(R.id.create_event_next)).
                    setText(R.string.complete);
        }

        // Set Visible of the part of next step.
        stepRelativeLayouts[currentStep].setVisibility(View.VISIBLE);

        // Scroll screen to button after a micro delay.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Scroll screen to button.
                scrollToBottom();
            }
        }, 50);

        // Enable "Save" action button, if currentStep >= 2. And won't enable in create mode.
        if((currentMaxStep >= STEPS_ALLOW_SAVE) && (createEventActivity.getInitStep() == 0)) {
            setSaveEventButtonClickable(true);
        }

        // Do specific UI actions for particular step.
        setUiActions();
    }

    public void setSaveEventButtonClickable(boolean clickable) {
        if(((LinearLayout) createEventActivity.
                findViewById(R.id.menu_item_save_event_layout)) == null) {
            return;
        }

        if(clickable) {
            ((LinearLayout) createEventActivity.
                    findViewById(R.id.menu_item_save_event_layout)).setAlpha(1);
//            // Update currentMaxStep.
//            currentMaxStep = STEPS_ALLOW_SAVE;

        }
        else {
            ((LinearLayout) createEventActivity.
                    findViewById(R.id.menu_item_save_event_layout)).setAlpha((float)0.1);
        }

        isActionSaveButtonClickable = clickable;

    }


    public void setFocusStep(int step) {
        currentStep = step-1;
        currentMaxStep = currentStep;


        // Update progress bar.
        progressBar.setRating(currentStep+1);
        progressText.setText((currentStep+1)+"/9");


        for(int i=0; i<currentStep; i++) {
            // Enable the button cover of current step.
            stepCoverButtons[i].setVisibility(View.VISIBLE);
            // Set Visible of the part of next step.
            stepRelativeLayouts[i].setVisibility(View.VISIBLE);
        }
        stepRelativeLayouts[currentStep].setVisibility(View.VISIBLE);


        // Set Visible to previous button if current is going to step 1.
        if(currentStep > 0) {
            ((Button) createEventActivity.findViewById(R.id.create_event_previous)).
                    setVisibility(View.VISIBLE);
        }

        // Set "complete" to next step button if reach the last step.
        if(currentStep >= (NUMBER_OF_TOTAL_STEPS-1)) {
            currentStep = NUMBER_OF_TOTAL_STEPS-1;
            ((Button) createEventActivity.findViewById(R.id.create_event_next)).
                    setText(R.string.complete);
        }





    }

    public void setFocusQuestionText() {
        /** */
        String tempString = "";
        // Step4
        tempString = createEventActivity.getString(R.string.step4_question_left)+
                createEventActivity.getEventLogStructure().scenario +
                createEventActivity.getString(R.string.step4_question_right);
        ((TextView) createEventActivity.findViewById(R.id.textview_step4_question))
                .setText(tempString);

        // Step5
        tempString = createEventActivity.getString(R.string.step5_question_left)+
                ((EditText) createEventActivity.findViewById(R.id.editText_behavior_step4)).getText() +
                createEventActivity.getString(R.string.step5_question_right);
        ((TextView) createEventActivity.findViewById(R.id.textview_step5_question))
                .setText(tempString);

        // Step6
        tempString = createEventActivity.getString(R.string.step6_question_left)+
                createEventActivity.getEventLogStructure().scenario +
                createEventActivity.getString(R.string.step6_question_mid)+
                ((EditText) createEventActivity.findViewById(R.id.editText_emotion_step5)).getText() +
                createEventActivity.getString(R.string.step6_question_right);
        ((TextView) createEventActivity.findViewById(R.id.textview_step6_question))
                .setText(tempString);

        // Step7
        TextView tempText = null;
        String ifScenarioString = "\n\n"+createEventActivity.getString(R.string.if_scenario_again_left)+
                createEventActivity.getEventLogStructure().scenario+
                createEventActivity.getString(R.string.if_scenario_again_right);

        // Show text of scenario & original behavior.
        tempText = (TextView) createEventActivity.findViewById(R.id.textview_step7_scenario);
        tempString = createEventActivity.getString(R.string.original_behavior_colon) +"\n"+
                createEventActivity.getEventLogStructure().originalBehavior + ifScenarioString;
        tempText.setText(tempString);
        tempText.setVisibility(View.VISIBLE);

        // Step8
        // Disable text of scenario & original behavior in previous step.
        createEventActivity.findViewById(R.id.textview_step7_scenario).setVisibility(View.GONE);

        // Show text of scenario & original emotion.
        tempText = (TextView) createEventActivity.findViewById(R.id.textview_step8_scenario);
        tempString = createEventActivity.getString(R.string.original_emotion_colon) +"\n"+
                createEventActivity.getEventLogStructure().originalEmotion + ifScenarioString;
        tempText.setText(tempString);
        tempText.setVisibility(View.VISIBLE);

        // Set text of question in step8.
        tempText = (TextView) createEventActivity.findViewById(R.id.textview_step8_question);
        tempString = createEventActivity.getString(R.string.step8_question_left)+
                createEventActivity.getEventLogStructure().expectedBehavior+
                createEventActivity.getString(R.string.step8_question_right);
        tempText.setText(tempString);


        // Handle question for reflection steps.
        setReflectionText();
    }

    public void setFocusCoverHeight() {

        for(int i=0; i<currentStep; i++) {
            // Enable the button cover of current step.
            stepCoverButtons[i].setVisibility(View.GONE);
        }

        /** Load question test of each step on the screen. */
        int coverHeight = 0;

        // Step5
        coverHeight = (int)(STEP4_COVER_BASE_DP_HEIGHT*displayDensity) +
                createEventActivity.findViewById(R.id.textview_step4_question).getHeight() +
                createEventActivity.findViewById(R.id.editText_behavior_step4).getHeight();

        createEventActivity.findViewById(R.id.create_event_cover_step4).
                getLayoutParams().height = coverHeight;

        Log.d("Ket", "H S Step5 "+coverHeight);

        // Step6
        coverHeight = (int)(STEP5_COVER_BASE_DP_HEIGHT*displayDensity) +
                createEventActivity.findViewById(R.id.textview_step5_question).getHeight() +
                createEventActivity.findViewById(R.id.editText_emotion_step5).getHeight();

        createEventActivity.findViewById(R.id.create_event_cover_step5).
                getLayoutParams().height = coverHeight;

        int c = createEventActivity.findViewById(R.id.textview_step5_question).getHeight();
        int d = createEventActivity.findViewById(R.id.editText_emotion_step5).getHeight();
        Log.d("Ket", "H S Step6 "+coverHeight+" "+c+" "+d);

        // Step7
        coverHeight = (int)(STEP6_COVER_BASE_DP_HEIGHT*displayDensity) +
                createEventActivity.findViewById(R.id.textview_step6_question).getHeight() +
                createEventActivity.findViewById(R.id.editText_thought_step6).getHeight();

        createEventActivity.findViewById(R.id.create_event_cover_step6).
                getLayoutParams().height = coverHeight;

        Log.d("Ket", "H S Step7 "+coverHeight);

        // Step8
        coverHeight = (int)(STEP7_COVER_BASE_DP_HEIGHT*displayDensity) +
                createEventActivity.findViewById(R.id.textview_step7_question).getHeight() +
                createEventActivity.findViewById(R.id.editText_behavior_step7).getHeight();

        createEventActivity.findViewById(R.id.create_event_cover_step7).
                getLayoutParams().height = coverHeight;

        Log.d("Ket", "H S Step8 "+coverHeight);

        // Step9
        // Adapt cover height.
        coverHeight = (int)(STEP8_COVER_BASE_DP_HEIGHT*displayDensity) +
                createEventActivity.findViewById(R.id.textview_step8_question).getHeight() +
                createEventActivity.findViewById(R.id.editText_emotion_step8).getHeight();

        createEventActivity.findViewById(R.id.create_event_cover_step8).
                getLayoutParams().height = coverHeight;

        Log.d("Ket", "H S Step9 "+coverHeight);

        for(int i=0; i<currentStep; i++) {
            // Enable the button cover of current step.
            stepCoverButtons[i].setVisibility(View.VISIBLE);
        }

        // Scroll screen to button after a delay.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do specific UI actions for particular step.
                setUiActions();
                // Scroll screen to button.
                scrollToBottom();
            }
        }, 500);

    }

    /*
    *  Set specific UI actions for particular steps.
    * */
    /*** If no extra specific action for each step, can rewrite this function to be more simple. ***/
    private void setUiActions() {
        String tempText = "";
        int coverHeight = 0;
        switch (currentStep) {
            case 0:
                break;
            case 3:
                // Set focus to EditText after a micro delay.
                final Handler handlerStep4 = new Handler();
                handlerStep4.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Scroll screen to button.
                        ((EditText) createEventActivity.findViewById(R.id.editText_behavior_step4)).requestFocus();
                    }
                }, 50);

                // Load scenario on screen.
                tempText = createEventActivity.getString(R.string.step4_question_left)+
                        createEventActivity.getEventLogStructure().scenario +
                        createEventActivity.getString(R.string.step4_question_right);
                ((TextView) createEventActivity.findViewById(R.id.textview_step4_question))
                        .setText(tempText);
                break;
            case 4:
                // Set focus to EditText after a micro delay.
                final Handler handlerStep5 = new Handler();
                handlerStep5.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Scroll screen to button.
                        ((EditText) createEventActivity.findViewById(R.id.editText_emotion_step5)).requestFocus();
                    }
                }, 50);

                // Load scenario on screen.
                tempText = createEventActivity.getString(R.string.step5_question_left)+
                        ((EditText) createEventActivity.findViewById(R.id.editText_behavior_step4)).getText() +
                        createEventActivity.getString(R.string.step5_question_right);
                ((TextView) createEventActivity.findViewById(R.id.textview_step5_question))
                        .setText(tempText);

                // Adapt cover height.
                coverHeight = (int)(STEP4_COVER_BASE_DP_HEIGHT*displayDensity) +
                        createEventActivity.findViewById(R.id.textview_step4_question).getHeight() +
                        createEventActivity.findViewById(R.id.editText_behavior_step4).getHeight();

                createEventActivity.findViewById(R.id.create_event_cover_step4).
                        getLayoutParams().height = coverHeight;

                int a = createEventActivity.findViewById(R.id.textview_step4_question).getHeight();
                int b = createEventActivity.findViewById(R.id.editText_behavior_step4).getHeight();
                Log.d("Ket", "H Step5 "+coverHeight+" "+a+" "+b);
                break;
            case 5:
                // Set focus to EditText after a micro delay.
                final Handler handlerStep6 = new Handler();
                handlerStep6.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Scroll screen to button.
                        ((EditText) createEventActivity.findViewById(R.id.editText_thought_step6)).requestFocus();
                    }
                }, 50);

                // Load scenario on screen.
                tempText = createEventActivity.getString(R.string.step6_question_left)+
                        createEventActivity.getEventLogStructure().scenario +
                        createEventActivity.getString(R.string.step6_question_mid)+
                        ((EditText) createEventActivity.findViewById(R.id.editText_emotion_step5)).getText() +
                        createEventActivity.getString(R.string.step6_question_right);
                ((TextView) createEventActivity.findViewById(R.id.textview_step6_question))
                        .setText(tempText);

                // Adapt cover height.
                coverHeight = (int)(STEP5_COVER_BASE_DP_HEIGHT*displayDensity) +
                        createEventActivity.findViewById(R.id.textview_step5_question).getHeight() +
                        createEventActivity.findViewById(R.id.editText_emotion_step5).getHeight();

                createEventActivity.findViewById(R.id.create_event_cover_step5).
                        getLayoutParams().height = coverHeight;

                int c = createEventActivity.findViewById(R.id.textview_step5_question).getHeight();
                int d = createEventActivity.findViewById(R.id.editText_emotion_step5).getHeight();
                Log.d("Ket", "H Step6 "+coverHeight+" "+c+" "+d);
                break;
            case 6:
                // Set focus to EditText after a micro delay.
                final Handler handlerStep7 = new Handler();
                handlerStep7.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Scroll screen to button.
                        ((EditText) createEventActivity.findViewById(R.id.editText_behavior_step7)).requestFocus();
                    }
                }, 50);


                // Adapt cover height.
                coverHeight = (int)(STEP6_COVER_BASE_DP_HEIGHT*displayDensity) +
                        createEventActivity.findViewById(R.id.textview_step6_question).getHeight() +
                        createEventActivity.findViewById(R.id.editText_thought_step6).getHeight();

                createEventActivity.findViewById(R.id.create_event_cover_step6).
                        getLayoutParams().height = coverHeight;

                Log.d("Ket", "step 7 "+coverHeight);
                break;
            case 7:
                // Set focus to EditText after a micro delay.
                final Handler handlerStep8 = new Handler();
                handlerStep8.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Scroll screen to button.
                        ((EditText) createEventActivity.findViewById(R.id.editText_emotion_step8)).requestFocus();
                    }
                }, 50);

                // Adapt cover height.
                coverHeight = (int)(STEP7_COVER_BASE_DP_HEIGHT*displayDensity) +
                        createEventActivity.findViewById(R.id.textview_step7_question).getHeight() +
                        createEventActivity.findViewById(R.id.editText_behavior_step7).getHeight();

                createEventActivity.findViewById(R.id.create_event_cover_step7).
                        getLayoutParams().height = coverHeight;

                Log.d("Ket", "step 8 "+coverHeight);
                break;
            case 8:
                // Set focus to EditText after a micro delay.
                final Handler handlerStep9 = new Handler();
                handlerStep9.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Scroll screen to button.
                        ((EditText) createEventActivity.findViewById(R.id.editText_thought_step9)).requestFocus();
                    }
                }, 50);

                // Adapt cover height.
                coverHeight = (int)(STEP8_COVER_BASE_DP_HEIGHT*displayDensity) +
                        createEventActivity.findViewById(R.id.textview_step8_question).getHeight() +
                        createEventActivity.findViewById(R.id.editText_emotion_step8).getHeight();

                createEventActivity.findViewById(R.id.create_event_cover_step8).
                        getLayoutParams().height = coverHeight;

                Log.d("Ket", "step 9 "+coverHeight);
                break;
        }

    }


    private void setReflectionText() {
        // Following codes must be performed earlier than setUiActions().
        String tempString = "";
        String ifScenarioString = "\n\n"+createEventActivity.getString(R.string.if_scenario_again_left)+
                createEventActivity.getEventLogStructure().scenario+
                createEventActivity.getString(R.string.if_scenario_again_right);
        TextView tempText = null;
        switch(currentStep) {
            case 6:
                // Show text of scenario & original behavior.
                tempText = (TextView) createEventActivity.findViewById(R.id.textview_step7_scenario);
                tempString = createEventActivity.getString(R.string.original_behavior_colon) +"\n"+
                        createEventActivity.getEventLogStructure().originalBehavior + ifScenarioString;
                tempText.setText(tempString);
                tempText.setVisibility(View.VISIBLE);

                break;
            case 7:
                // Disable text of scenario & original behavior in previous step.
                createEventActivity.findViewById(R.id.textview_step7_scenario).setVisibility(View.GONE);

                // Show text of scenario & original emotion.
                tempText = (TextView) createEventActivity.findViewById(R.id.textview_step8_scenario);
                tempString = createEventActivity.getString(R.string.original_emotion_colon) +"\n"+
                        createEventActivity.getEventLogStructure().originalEmotion + ifScenarioString;
                tempText.setText(tempString);
                tempText.setVisibility(View.VISIBLE);

                // Set text of question in step8.
                tempText = (TextView) createEventActivity.findViewById(R.id.textview_step8_question);
                tempString = createEventActivity.getString(R.string.step8_question_left)+
                        createEventActivity.getEventLogStructure().expectedBehavior+
                        createEventActivity.getString(R.string.step8_question_right);
                tempText.setText(tempString);
                break;

            case 8:
                // Disable text of scenario & original behavior in previous step.
                createEventActivity.findViewById(R.id.textview_step8_scenario).setVisibility(View.GONE);

                // Show text of scenario & original thought.
                tempText = (TextView) createEventActivity.findViewById(R.id.textview_step9_scenario);
                tempString = createEventActivity.getString(R.string.original_thought_colon) +"\n"+
                        createEventActivity.getEventLogStructure().originalThought + ifScenarioString;
                tempText.setText(tempString);
                tempText.setVisibility(View.VISIBLE);

                // Set text of question in step9.
                tempText = (TextView) createEventActivity.findViewById(R.id.textview_step9_question);
                tempString = createEventActivity.getString(R.string.step9_question_left)+
                        createEventActivity.getEventLogStructure().expectedEmotion+
                        createEventActivity.getString(R.string.step9_question_right);
                tempText.setText(tempString);
                break;
        }
    }


    /*
    *   Enable/disable scrolling by touch.
    *   true: disable, false: enable
    * */
    public void setScrollDisable(boolean enable) {
        isScrollDisable = enable;
    }

    /*
    *   Scroll view to bottom.
    * */
    public void scrollToBottom() {
        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
    }


    /*
    *   Scroll view to top.
    * */
    public void scrollToTop() {
        scrollView.fullScroll(ScrollView.FOCUS_UP);
    }


    /*
    *   Trick for enable/disable scrolling.
    * */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return isScrollDisable;
    }
}
