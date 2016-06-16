package ubicomp.ketdiary.fragments.event;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ubicomp.ketdiary.MainActivity;
import ubicomp.ketdiary.R;

/**
 * A custom adapter for event list.
 *
 * Created by kelvindk on 16/6/7.
 */
public class EventListAdapter extends BaseAdapter {

    private MainActivity mainActivity = null;
    private ListView eventListView = null;
    private LayoutInflater layoutInflater = null;

    // the list of items' object.
    List<EventListItem> eventListItems = new ArrayList<EventListItem>();

    public EventListAdapter(MainActivity mainActivity, ListView eventListView) {
        this.mainActivity = mainActivity;
        this.eventListView = eventListView;
        Context context = mainActivity;

        for(int i=0; i<100; i++) {
            eventListItems.add(new EventListItem());
        }


        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }



    @Override
    public int getCount() {
        return eventListItems.size();
    }

    @Override
    public Object getItem(int i) {
        return eventListItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        Log.d("Ket", "eventListView onItemClick " + i);
        return eventListItems.indexOf(getItem(i));
    }

    public class EventListItemHolder
    {
        TextView fragment_event_list_date = null;
        TextView fragment_event_list_clic_inputk = null;
        ImageView fragment_event_list_category_icon = null;
        ImageView fragment_event_list_danger = null;
        TextView fragment_event_list_description = null;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {


        View eventItemView = layoutInflater.inflate(R.layout.fragment_event_list_item, null);

        EventListItemHolder eventListItemHolder = new EventListItemHolder();

        eventListItemHolder.fragment_event_list_description
                = (TextView) eventItemView.findViewById(R.id.fragment_event_list_description);
        eventListItemHolder.fragment_event_list_description.setText(" --- "+i+" ---");


        return eventItemView;
    }

    // Add item to ListView in FragmentEvent.
    public void addItem() {
        eventListItems.add(new EventListItem());
        notifyDataSetChanged();

    }

    // Set height of ListView to 0, this is a trick to avoid crash while switch fragment.
    public void invisibleList() {
        eventListView.getLayoutParams().height = 0;
        notifyDataSetChanged();
    }

}
