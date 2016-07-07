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
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.w3c.dom.Text;

import ubicomp.ketdiary.R;

/**
 * This class handle ScrollView's access in create event pgg.
 *
 * Created by kelvindk on 16/6/10.
 */
public class ScrollViewAdapter implements View.OnTouchListener {

    public static final int NUMBER_OF_TOTAL_STEPS = 9;
    public static final int STEPS_ALLOW_SAVE = 3;

    private CreateEventActivity createEventActivity = null;
    private ScrollView scrollView = null;

    // Indicate scrollability of the ListView. A trick to disable scrolling.
    private boolean isScrollDisable = false;

    // Current step of create event.
    private int currentStep = 0;
    public int getCurrentStep() {
        return currentStep;
    }

    // Current most advanced step had reach. Will only be added.
    private int currentMaxStep = 0;
    // RelativeLayouts of steps, this array is used to set visibility of step's content.
    private RelativeLayout[] stepRelativeLayouts = new RelativeLayout[NUMBER_OF_TOTAL_STEPS];
    // Buttons of steps as a trick for setting clickable and transparent of of each step
    private Button[] stepCoverButtons = new Button[NUMBER_OF_TOTAL_STEPS];
    // Progress bar & text.
    private RatingBar progressBar = null;
    private TextView progressText = null;


    // Boolean to store action save button state.
    private boolean isActionSaveButtonClickable = false;

    public boolean isActionSaveButtonClickable() {
        return isActionSaveButtonClickable;
    }

    public ScrollViewAdapter(CreateEventActivity createEventActivity) {
        this.createEventActivity = createEventActivity;

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
    *  Set specific UI actions for particular steps.
    * */
    /*** If no extra specific action for each step, can rewrite this function to be more simple. ***/
    private void setUiActions() {
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
                break;
        }
    }


    /*
    *   Click listener of previous step button.
    * */
    View.OnClickListener previousButtonOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Log.d("Ket", "PreviousButtonOnclick");

            performPreviousStep();
        }
    };

    /*
    *   Click listener of next step button.
    * */
    View.OnClickListener nextButtonOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Log.d("Ket", "NextButtonOnclick");
            performNextStep();
        }
    };

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

        // Enable "Save" action button, if currentStep >= 2. And won't enable in save mode.
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
            // Update currentMaxStep.
            currentMaxStep = STEPS_ALLOW_SAVE;

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

        // Scroll screen to button after a delay.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Scroll screen to button.
                scrollToBottom();
                // Do specific UI actions for particular step.
                setUiActions();
            }
        }, 300);

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
