package ubicomp.ketdiary.create_event;

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
    }

    public void setScrollDisable(boolean enable) {
        isScrollDisable = enable;
    }

    public void scrollToBottom() {
        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return isScrollDisable;
    }
}
