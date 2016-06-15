package ubicomp.ketdiary.create_event.steps;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.Arrays;

import ubicomp.ketdiary.R;
import ubicomp.ketdiary.fragments.event.EventLogStructure;
import ubicomp.ketdiary.create_event.CreateEventActivity;

/**
 * Related actions in step 5 of create event.
 *
 * Created by kelvindk on 16/6/13.
 */
public class StepEmotionWrapper {

    private CreateEventActivity createEventActivity = null;
    private EventLogStructure eventLogStructure = null;

    private EditText editText_emotion_step5 = null;

    // Emotion string list.
    private String[] emotionStrings = null;
    // new boolean list to store item checked in dialog list.
    private boolean[] dialogCheckList = null;


    public StepEmotionWrapper(CreateEventActivity createEventActivity) {
        this.createEventActivity = createEventActivity;
        this.eventLogStructure = createEventActivity.getEventLogStructure();

        // Set listener to image button: recent_emotion.
        ((ImageButton) createEventActivity.findViewById(R.id.recent_emotion_step5)).setOnClickListener(behavior_step5);
        editText_emotion_step5 = ((EditText) createEventActivity.findViewById(R.id.editText_emotion_step5));
        editText_emotion_step5.setOnClickListener(behavior_step5);

        // Get emotion string list.
        emotionStrings = createEventActivity.
                getApplicationContext().getResources().getStringArray(R.array.then_emotions);
        // New a check list of dialog.
        dialogCheckList =  new boolean[emotionStrings.length];
        Arrays.fill(dialogCheckList, Boolean.FALSE);
    }

    // Popup dialog to show recently use behavior.
    /*** Tow layer inner listener... ***/
    View.OnClickListener behavior_step5 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d("Ket", "recent_behavior_step5");

            // New dialog.
            AlertDialog.Builder dialog = new AlertDialog.Builder(createEventActivity);
            dialog.setTitle(R.string.then_emotion);

            dialog.setMultiChoiceItems(emotionStrings, dialogCheckList,
                new DialogInterface.OnMultiChoiceClickListener() {
                    public void onClick(DialogInterface dialog, int item,
                                        boolean isChecked) {
                        /* User clicked on a check box */
                        Log.d("Emotion", item+" "+isChecked);
                        // no op.
                    }
                });

            dialog.setPositiveButton(R.string.confirm,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        /* User clicked "Confirm"*/
                        Log.d("Emotion", "Confirm ");

                        // Get the string includes selected emotions.
                        String emotionsString = "";
                        for(int i=0; i<dialogCheckList.length; i++) {
                            if(dialogCheckList[i]) {
                                emotionsString += emotionStrings[i] +" ";
                            }
                        }
                        // Set selected emotions on the EditText. Can be multiple select.
                        editText_emotion_step5.setText(emotionsString);
                        /*** Log event original emotions selected by user. ***/
                        eventLogStructure.originalEmotion = emotionsString;
                    }
                });
            dialog.setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        /* User clicked "Cancel" */
                        Log.d("Emotion", "Cancel "+item);
                        // no op.
                    }
                });

            dialog.show();
        }
    };


}
