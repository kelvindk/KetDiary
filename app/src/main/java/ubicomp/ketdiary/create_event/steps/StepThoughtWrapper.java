package ubicomp.ketdiary.create_event.steps;


import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.ArrayList;

import ubicomp.ketdiary.R;
import ubicomp.ketdiary.ui.CreateEventActivity;

/**
 * Related actions in step 6 of create event.
 *
 * Created by kelvindk on 16/6/13.
 */
public class StepThoughtWrapper {

    public static final int INTENT_SPEECH_INPUT_RESULT = 200;

    private CreateEventActivity createEventActivity = null;

    EditText editText = null;

    public StepThoughtWrapper(CreateEventActivity createEventActivity) {
        this.createEventActivity = createEventActivity;


        // Set text changed listener.
        editText = (EditText) createEventActivity.findViewById(R.id.editText_thought_step6);
        editText.addTextChangedListener(textWatcher);

        ((ImageButton) createEventActivity.findViewById(R.id.voice_input_step6)).setOnClickListener(voice_input_listener);
        ((ImageButton) createEventActivity.findViewById(R.id.recent_behavior_step6)).setOnClickListener(recent_behavior_listener);


    }

    /*
    *  Receive the result of Google speech to text activity.
    * */
    public void onSpeechToTextActivityResult(Intent result) {
        ArrayList<String> resultStrings = result
                .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
        Log.d("Ket", resultStrings.get(0));
        editText.append(resultStrings.get(0));
        editText.requestFocus();
    }

    // Enable voice input.
    View.OnClickListener voice_input_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d("Ket", "voice_input_step4");
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "zh-TW");
            try {
                createEventActivity.startActivityForResult(intent, INTENT_SPEECH_INPUT_RESULT);
            } catch (ActivityNotFoundException a) {
                Log.d("Ket", "Speech recognition fail!");

            }
        }
    };

    // Popup dialog to show recently use behavior.
    View.OnClickListener recent_behavior_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d("Ket", "recent_behavior_step6");

            final String[] frequentInputString = {"1","2","3","4","5","6"};

            AlertDialog.Builder dialog = new AlertDialog.Builder(createEventActivity);
            dialog.setTitle(R.string.frequent_input_step6);
            dialog.setItems(frequentInputString, new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int pos) {
                    // TODO Auto-generated method stub
                    Log.d("Ket", frequentInputString[pos]);
                }
            });

            dialog.show();
        }
    };

    /*
    * Override TextWatcher to catch any input change in EditText.
    * */
    TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void afterTextChanged(Editable input) {
            Log.d("Ket", "afterTextChanged "+input.toString());
            createEventActivity.scrollViewScrollToBottom();
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start,
                                      int count, int after) {
//            Log.d("Ket", "beforeTextChanged");
        }

        @Override
        public void onTextChanged(CharSequence s, int start,
                                  int before, int count) {
//            Log.d("Ket", "onTextChanged");
        }
    };



}
