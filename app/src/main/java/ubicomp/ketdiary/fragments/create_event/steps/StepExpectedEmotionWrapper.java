package ubicomp.ketdiary.fragments.create_event.steps;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.Arrays;
import java.util.StringTokenizer;

import ubicomp.ketdiary.R;
import ubicomp.ketdiary.fragments.event.EventLogStructure;
import ubicomp.ketdiary.fragments.create_event.CreateEventActivity;

/**
 * Related actions in step 8 of create event.
 *
 * Created by kelvindk on 16/6/13.
 */
public class StepExpectedEmotionWrapper {

    private CreateEventActivity createEventActivity = null;
    private EventLogStructure eventLogStructure = null;

    private EditText editText_emotion_step8 = null;

    // Emotion string list.
    private String[] emotionStrings = null;
    // new boolean list to store item checked in dialog list.
    private boolean[] dialogCheckList = null;

    public StepExpectedEmotionWrapper(CreateEventActivity createEventActivity) {
        this.createEventActivity = createEventActivity;
        this.eventLogStructure = createEventActivity.getEventLogStructure();

        ((ImageButton) createEventActivity.findViewById(R.id.recent_emotion_step8)).setOnClickListener(behavior_step8);
        editText_emotion_step8 = ((EditText) createEventActivity.findViewById(R.id.editText_emotion_step8));
        editText_emotion_step8.setOnClickListener(behavior_step8);

        // Get emotion string list.
        emotionStrings = createEventActivity.
                getApplicationContext().getResources().getStringArray(R.array.expected_emotions);
        // New a check list of dialog.
        dialogCheckList =  new boolean[emotionStrings.length];
        Arrays.fill(dialogCheckList, Boolean.FALSE);

        // Load existed data.
        if(createEventActivity.getInitStep() != 0) {
            editText_emotion_step8.setText(eventLogStructure.expectedEmotion);
            StringTokenizer tokens = new StringTokenizer(eventLogStructure.expectedEmotion, " ");
            int i=0; // O(nlogn) comparison.
            while(tokens.hasMoreTokens()) {
                String token = tokens.nextToken();
                for(; i<dialogCheckList.length;i++) {
                    if(emotionStrings[i].equals(token)) {
                        dialogCheckList[i] = true;
                        break;
                    }
                }
            }
        }
    }

    // Popup dialog to show recently use behavior.
    /*** Tow layer inner listener... ***/
    View.OnClickListener behavior_step8 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d("Ket", "recent_behavior_step8");

            // New dialog.
            AlertDialog.Builder dialog = new AlertDialog.Builder(createEventActivity);
            dialog.setTitle(R.string.expected_emotion);

//            dialog.setMultiChoiceItems(emotionStrings, dialogCheckList,
//                    new DialogInterface.OnMultiChoiceClickListener() {
//                        public void onClick(DialogInterface dialog, int item,
//                                            boolean isChecked) {
//                        /* User clicked on a check box */
//                            Log.d("Emotion", item+" "+isChecked);
//                            // no op.
//                        }
//                    });

            dialog.setSingleChoiceItems(emotionStrings, 0, null);

            dialog.setPositiveButton(R.string.confirm,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {
                        /* User clicked "Confirm"*/
                            Log.d("Emotion", "Confirm ");

                            // Get the string includes selected emotions.
                            String emotionsString = "";
                            dialog.dismiss();
                            int selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
                            emotionsString += emotionStrings[selectedPosition];

                            // In edit mode, set clickable to "save" action button when selected if emotionsString is not null.
                            if((createEventActivity.getInitStep() != 0) && (!emotionsString.toString().equals("")))
                                createEventActivity.getScrollViewAdapter().setSaveEventButtonClickable(true);

                            // Set selected emotions on the EditText. Can be multiple select.
                            editText_emotion_step8.setText(emotionsString);

                            /*** Log event expected emotions selected by user. ***/
                            eventLogStructure.expectedEmotion = emotionsString;

                            // Any editing can set "revise" flag to false.
                            eventLogStructure.reviseExpectedEmotion = false;
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
