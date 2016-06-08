package ubicomp.ketdiary.ui;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import ubicomp.ketdiary.R;

/**
 * A standalone activity for create(add) new event.
 *
 * Created by kelvindk on 16/6/7.
 */

public class CreateEventActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        // Set full screen.
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN );

        // Enable toolbar on create event activity with back button on the top left.
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_create_event_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the icon on the top right of toolbar in CreateEventActivity.
        getMenuInflater().inflate(R.menu.menu_create_event, menu);

        MenuItem actionSaveEvent = menu.findItem(R.id.action_save);
        MenuItemCompat.getActionView(actionSaveEvent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Ket", "actionSaveEvent");
                // Listener of action Save button

            }
        });

        MenuItem actionCancelEvent = menu.findItem(R.id.action_cancel);
        MenuItemCompat.getActionView(actionCancelEvent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Ket", "actionCancelEvent");
                // Listener of action Cancel button
                onBackPressed();
            }
        });

        return true;
    }

}
