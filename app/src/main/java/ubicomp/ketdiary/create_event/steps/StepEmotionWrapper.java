package ubicomp.ketdiary.create_event.steps;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import ubicomp.ketdiary.R;
import ubicomp.ketdiary.fragments.event.EventLogStructure;
import ubicomp.ketdiary.ui.CreateEventActivity;

/**
 * Related actions in step 5 of create event.
 *
 * Created by kelvindk on 16/6/13.
 */
public class StepEmotionWrapper {

    private CreateEventActivity createEventActivity = null;
    private EventLogStructure eventLogStructure = null;


    private EditText editText_emotion_step5 = null;

    public StepEmotionWrapper(CreateEventActivity createEventActivity) {
        this.createEventActivity = createEventActivity;
        this.eventLogStructure = createEventActivity.getEventLogStructure();


        ((ImageButton) createEventActivity.findViewById(R.id.recent_emotion_step5)).setOnClickListener(behavior_step5);
        editText_emotion_step5 = ((EditText) createEventActivity.findViewById(R.id.editText_emotion_step5));
        editText_emotion_step5.setOnClickListener(behavior_step5);


    }

    // Popup dialog to show recently use behavior.
    View.OnClickListener behavior_step5 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d("Ket", "recent_behavior_step4");

            final String[] emotionStrings =
                    createEventActivity.getApplicationContext().getResources().getStringArray(R.array.then_emotions);

            AlertDialog.Builder dialog = new AlertDialog.Builder(createEventActivity);
            dialog.setTitle(R.string.then_emotion);
            dialog.setItems(emotionStrings, new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int pos) {
                    // TODO Auto-generated method stub
                    Log.d("Ket", emotionStrings[pos]);
                    editText_emotion_step5.setText(emotionStrings[pos]);
                }
            });

            dialog.show();
        }
    };


}
