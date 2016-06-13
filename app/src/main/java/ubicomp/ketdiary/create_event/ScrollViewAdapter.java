package ubicomp.ketdiary.create_event;

import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import ubicomp.ketdiary.R;
import ubicomp.ketdiary.ui.CreateEventActivity;

/**
 * This class handle ScrollView's access in create event pgg.
 *
 * Created by kelvindk on 16/6/10.
 */
public class ScrollViewAdapter implements View.OnTouchListener {

    public static final int NUMBER_OF_TOTAL_STEPS = 5;

    private CreateEventActivity createEventActivity = null;
    private ScrollView scrollView = null;

    // Indicate scrollability of the ListView. A trick to disable scrolling.
    private boolean isScrollDisable = false;

    // Current step of create event.
    private int currentStep = 0;
    // RelativeLayouts of steps, this array is used to set visibility of step's content.
    private RelativeLayout[] stepRelativeLayouts = new RelativeLayout[NUMBER_OF_TOTAL_STEPS];
    // Buttons of steps as a trick for setting clickable and transparent of of each step
    private Button[] stepCoverButtons = new Button[NUMBER_OF_TOTAL_STEPS];

    public ScrollViewAdapter(CreateEventActivity createEventActivity) {
        this.createEventActivity = createEventActivity;

        // Get RelativeLayouts and Buttons of steps.
        stepRelativeLayouts[0] = (RelativeLayout) createEventActivity.findViewById(R.id.create_event_step1);
        stepRelativeLayouts[1] = (RelativeLayout) createEventActivity.findViewById(R.id.create_event_step2);
        stepRelativeLayouts[2] = (RelativeLayout) createEventActivity.findViewById(R.id.create_event_step3);
        stepRelativeLayouts[3] = (RelativeLayout) createEventActivity.findViewById(R.id.create_event_step4);
        stepRelativeLayouts[4] = (RelativeLayout) createEventActivity.findViewById(R.id.create_event_step5);

        stepCoverButtons[0] = (Button) createEventActivity.findViewById(R.id.create_event_cover_step1);
        stepCoverButtons[1] = (Button) createEventActivity.findViewById(R.id.create_event_cover_step2);
        stepCoverButtons[2] = (Button) createEventActivity.findViewById(R.id.create_event_cover_step3);
        stepCoverButtons[3] = (Button) createEventActivity.findViewById(R.id.create_event_cover_step4);
        stepCoverButtons[4] = (Button) createEventActivity.findViewById(R.id.create_event_cover_step5);

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

            // Set Gone to the part of current step.
            stepRelativeLayouts[currentStep].setVisibility(View.GONE);

            // Set Visible to "next step" button if current is last step.
            if(currentStep == (NUMBER_OF_TOTAL_STEPS-1)) {
                ((Button) createEventActivity.findViewById(R.id.create_event_next)).
                        setVisibility(View.VISIBLE);
            }

            // Reduce the count to previous step.
            currentStep--;

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

        }
    };

    /*
    *   Click listener of next step button.
    * */
    View.OnClickListener nextButtonOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Log.d("Ket", "NextButtonOnclick");
            // Enable the button cover of current step.
            stepCoverButtons[currentStep].setVisibility(View.VISIBLE);

            // Set Visible to previous button if current is step 1.
            if(currentStep == 0) {
                ((Button) createEventActivity.findViewById(R.id.create_event_previous)).
                        setVisibility(View.VISIBLE);
            }

            // Advance the count to next step.
            currentStep++;

            // Invisible the "next step" button if reach the last step.
            if(currentStep >= (NUMBER_OF_TOTAL_STEPS-1)) {
                currentStep = NUMBER_OF_TOTAL_STEPS-1;
                ((Button) createEventActivity.findViewById(R.id.create_event_next)).
                        setVisibility(View.INVISIBLE);
            }

            // Set Visible of the part of next step.
            stepRelativeLayouts[currentStep].setVisibility(View.VISIBLE);

            // Scroll screen to button after a micro delay.
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Scroll screen to button.
                    scrollToBottom();
                }
            }, 50);





        }
    };


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
