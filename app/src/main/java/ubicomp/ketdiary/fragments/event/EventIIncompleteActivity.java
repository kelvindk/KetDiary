package ubicomp.ketdiary.fragments.event;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import ubicomp.ketdiary.R;
import ubicomp.ketdiary.utility.data.db.ThirdPageDataBase;

/**
 * Created by kelvindk on 16/7/7.
 */
public class EventIIncompleteActivity extends AppCompatActivity {

    public static final String TAG = "EventIIncompleteActivity";

    private EventIIncompleteActivity eventIIncompleteActivity = null;

    private ListView eventListView = null;

    private EventListAdapter eventListAdapter = null;

    ThirdPageDataBase thirdPageDataBase = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_incomplete);

        eventIIncompleteActivity = this;

        // Enable toolbar on create event activity with back button on the top left.
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_event_incomplete_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        thirdPageDataBase = new ThirdPageDataBase();
        EventLogStructure[] eventLogStructures = thirdPageDataBase.getNotCompleteEventLog();

        // Set adapter of custom ListView to this fragment
        eventListView = (ListView) findViewById(R.id.fragment_event_list_view);

        eventListAdapter = new EventListAdapter(this, eventListView);
        eventListView.setAdapter(eventListAdapter);

        eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("Ket", "eventListView onItemClick " + i);

                // Invoke EventContentActivity.
                Intent eventContentIntent = new Intent (eventIIncompleteActivity, EventContentActivity.class);
                // Put the serializable object into eventContentActivityIntent through a Bundle.
                Bundle bundle = new Bundle();
                bundle.putSerializable(EventLogStructure.EVENT_LOG_STRUCUTRE_KEY, eventListAdapter.getEventListItem(i));
                eventContentIntent.putExtras(bundle);
                startActivity(eventContentIntent);
            }
        });
    }

    @Override
    public void onResume() {
        Log.d("Ket", "EventIIncompleteActivity onResume");
        eventListAdapter.refreshListViewContent();
        super.onResume();
    }

}
