package ubicomp.ketdiary.fragments.event;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import ubicomp.ketdiary.R;

/**
 * Created by kelvindk on 16/7/7.
 */
public class EventIIncompleteActivity extends AppCompatActivity {

    public static final String TAG = "EventIIncompleteActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
    }

}
