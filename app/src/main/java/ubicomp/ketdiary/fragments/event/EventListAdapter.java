package ubicomp.ketdiary.fragments.event;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import ubicomp.ketdiary.MainActivity;
import ubicomp.ketdiary.R;
import ubicomp.ketdiary.utility.data.db.ThirdPageDataBase;

/**
 * A custom adapter for event list.
 *
 * Created by kelvindk on 16/6/7.
 */
public class EventListAdapter extends BaseAdapter {

    private MainActivity mainActivity = null;
    private ListView eventListView = null;
    private LayoutInflater layoutInflater = null;

    ThirdPageDataBase thirdPageDataBase = null;

    // the list of items' object.
    List<EventLogStructure> eventListItems = new ArrayList<>();

    public EventListAdapter(MainActivity mainActivity, ListView eventListView) {
        this.mainActivity = mainActivity;
        this.eventListView = eventListView;
        Context context = mainActivity;

        thirdPageDataBase = new ThirdPageDataBase();

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public EventLogStructure getEventListItem(int pos) {
        return eventListItems.get(pos);
    }


    @Override
    public int getCount() {
        return eventListItems.size();
    }

    @Override
    public Object getItem(int position) {
        return eventListItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return eventListItems.indexOf(getItem(position));
    }

    // UI components of item.
    public class EventListItemHolder
    {
        TextView fragment_event_list_date = null;
        ImageView fragment_event_list_category_icon = null;
        TextView fragment_event_list_description = null;
        TextView fragment_event_list_expected_behavior = null;
        RatingBar fragment_event_list_item_drug_use_risk_level = null;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        View eventItemView = layoutInflater.inflate(R.layout.fragment_event_list_item, null);

        EventListItemHolder eventListItemHolder = new EventListItemHolder();

        // Set event date.
        eventListItemHolder.fragment_event_list_date
                = (TextView)  eventItemView.findViewById(R.id.fragment_event_list_date);

        eventListItemHolder.fragment_event_list_date.
                setText(eventListItems.get(position).eventTimeToString());

        // Set event icon.
        eventListItemHolder.fragment_event_list_category_icon
                = (ImageView) eventItemView.findViewById(R.id.fragment_event_list_category_icon);

        eventListItemHolder.fragment_event_list_category_icon.
                setImageResource(eventListItems.get(position).scenarioTypeToIconId());

        // Set event scenario.
        eventListItemHolder.fragment_event_list_description
                = (TextView) eventItemView.findViewById(R.id.fragment_event_list_description);
        eventListItemHolder.fragment_event_list_description.setText(eventListItems.get(position).scenario);

        // Set expected behavior.
        String expectedBehavior = mainActivity.getString(R.string.expected_behavior) + "： "+
                eventListItems.get(position).expectedBehavior;
        eventListItemHolder.fragment_event_list_expected_behavior
                = (TextView) eventItemView.findViewById(R.id.fragment_event_list_expected_behavior);
        eventListItemHolder.fragment_event_list_expected_behavior.setText(expectedBehavior);

        // Set risk level.
        eventListItemHolder.fragment_event_list_item_drug_use_risk_level
                = (RatingBar) eventItemView.findViewById(R.id.fragment_event_list_item_drug_use_risk_level);
        eventListItemHolder.fragment_event_list_item_drug_use_risk_level.setRating(eventListItems.get(position).drugUseRiskLevel);

        return eventItemView;
    }

    // Refresh item to ListView in FragmentEvent.
    public void refreshListViewContent() {
        Calendar oneWeekAgo = (Calendar) Calendar.getInstance().clone();
        oneWeekAgo.add(Calendar.DATE, -7);
//        EventLogStructure[] eventLogStructures = thirdPageDataBase.getLaterEventLog(oneWeekAgo);
        EventLogStructure[] eventLogStructures = thirdPageDataBase.getAllEventLog();

        if(eventLogStructures == null)
            return;

        eventListItems = new ArrayList<>(Arrays.asList(eventLogStructures));

        notifyDataSetChanged();

    }

    // Set height of ListView to 0, this is a trick to avoid crash while switch fragment.
    public void invisibleList() {
        eventListView.getLayoutParams().height = 0;
        notifyDataSetChanged();
    }

}
