package ubicomp.ketdiary.create_event;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

import ubicomp.ketdiary.R;
import ubicomp.ketdiary.ui.CreateEventActivity;

/**
 * This class handle ScrollView's access.
 *
 * Created by kelvindk on 16/6/10.
 */
public class ScrollViewAdapter implements View.OnTouchListener {

    private CreateEventActivity createEventActivity = null;
    private ScrollView scrollView = null;
    private boolean isScrollDisable = false;

    public ScrollViewAdapter(CreateEventActivity createEventActivity) {
        this.createEventActivity = createEventActivity;
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
        }
    };

    /*
    *   Click listener of next step button.
    * */
    View.OnClickListener nextButtonOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Log.d("Ket", "NextButtonOnclick");
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
    public void scrollToNext() {
        scrollView.fullScroll(ScrollView.FOCUS_FORWARD);
    }

    /*
    *   Scroll view to bottom.
    * */
    public void scrollToBottom() {
        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
    }

    public int getRelativeTop(View myView) {
        if (myView.getParent() == myView.getRootView())
            return myView.getTop();
        else
            return myView.getTop() + getRelativeTop((View) myView.getParent());
    }


    /*
    *   Trick for enable/disable scrolling.
    * */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return isScrollDisable;
    }
}
