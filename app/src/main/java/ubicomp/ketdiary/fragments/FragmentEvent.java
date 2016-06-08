package ubicomp.ketdiary.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import ubicomp.ketdiary.MainActivity;
import ubicomp.ketdiary.R;
import ubicomp.ketdiary.fragments.event.EventListAdapter;
import ubicomp.ketdiary.ui.FragmentSwitcher;

/**
 * A placeholder fragment containing a simple view for Event fragment.
 */
public class FragmentEvent extends Fragment {

    private MainActivity mainActivity = null;
    private FragmentSwitcher fragmentSwitcher = null;

    private ListView eventListView = null;

    private EventListAdapter eventListAdapter;

    public FragmentEvent(FragmentSwitcher fragmentSwitcher, MainActivity mainActivity) {
        this.fragmentSwitcher = fragmentSwitcher;
        this.mainActivity = mainActivity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Ket", "onCreate fragment_event");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Ket", "onCreateView fragment_event");


        fragmentSwitcher.setFragmentOnlyDowndropTab(FragmentSwitcher.FRAGMENT_EVENT);

        View fragmentEventView = inflater.inflate(R.layout.fragment_event, container, false);

        // Set adapter of custom ListView to this fragment
        eventListView = (ListView) fragmentEventView.findViewById(R.id.fragment_event_list_view);

        eventListAdapter = new EventListAdapter(this.mainActivity, eventListView);
        eventListView.setAdapter(eventListAdapter);

        eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("Ket", "eventListView onItemClick " + i);
                eventListAdapter.addItem();

            }
        });

        return fragmentEventView;
    }
}
