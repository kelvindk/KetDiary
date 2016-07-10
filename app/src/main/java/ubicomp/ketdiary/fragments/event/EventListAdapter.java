package ubicomp.ketdiary.fragments.event;


import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import ubicomp.ketdiary.R;
import ubicomp.ketdiary.utility.data.db.DatabaseControl;
import ubicomp.ketdiary.utility.data.db.ThirdPageDataBase;
import ubicomp.ketdiary.utility.data.structure.TestResult;

/**
 * A custom adapter for event list.
 *
 * Created by kelvindk on 16/6/7.
 */
public class EventListAdapter extends BaseAdapter {

    private Activity mainActivity = null;
    private ListView eventListView = null;
    private LayoutInflater layoutInflater = null;

    ThirdPageDataBase thirdPageDataBase = null;

    // the list of items' object.
    List<EventLogStructure> eventListItems = new ArrayList<>();

    public EventListAdapter(Activity mainActivity, ListView eventListView) {
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

        LinearLayout event_list_incomplete_status_layout = null;
        LinearLayout event_list_therapy_status_layout = null;
        ImageView event_list_therapy_status_icon = null;
        TextView event_list_therapy_status_text = null;
        ImageView event_list_saliva_status_icon = null;
        TextView event_list_saliva_status_text = null;

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

        // Set scenario icon.
        eventListItemHolder.fragment_event_list_category_icon
                = (ImageView) eventItemView.findViewById(R.id.fragment_event_list_category_icon);

        eventListItemHolder.fragment_event_list_category_icon.
                setImageResource(eventListItems.get(position).scenarioTypeToIconId());

        // Set scenario.
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
                = (RatingBar) eventItemView.findViewById(R.id.fragment_event_list_drug_use_risk_level);
        eventListItemHolder.fragment_event_list_item_drug_use_risk_level.
                setRating(eventListItems.get(position).drugUseRiskLevel);

        /** Three status of event */
        //
        eventListItemHolder.event_list_incomplete_status_layout
                = (LinearLayout) eventItemView.findViewById(R.id.event_list_incomplete_status_layout);
        if(eventListItems.get(position).isComplete)
            eventListItemHolder.event_list_incomplete_status_layout.setVisibility(View.GONE);
        else
            eventListItemHolder.event_list_incomplete_status_layout.setVisibility(View.VISIBLE);

        //
        eventListItemHolder.event_list_therapy_status_layout
                = (LinearLayout) eventItemView.findViewById(R.id.event_list_therapy_status_layout);
        eventListItemHolder.event_list_therapy_status_layout.setVisibility(View.VISIBLE);
        eventListItemHolder.event_list_therapy_status_icon
                = (ImageView) eventItemView.findViewById(R.id.event_list_therapy_status_icon);
        eventListItemHolder.event_list_therapy_status_text
                = (TextView) eventItemView.findViewById(R.id.event_list_therapy_status_text);
        switch (eventListItems.get(position).therapyStatus) {
            case NULL:
            case NOT_YET:
                eventListItemHolder.event_list_therapy_status_layout.setVisibility(View.GONE);
                break;
            case GOOD:
                eventListItemHolder.event_list_therapy_status_icon.setBackgroundResource(R.drawable.circle3);
                eventListItemHolder.event_list_therapy_status_text.setText(R.string.therapist_not_yet);
                break;
            case BAD:
                eventListItemHolder.event_list_therapy_status_icon.setBackgroundResource(R.drawable.tri3);
                eventListItemHolder.event_list_therapy_status_text.setText(R.string.need_revise);
                break;
            case DISCUSSED:
                eventListItemHolder.event_list_therapy_status_icon.setBackgroundResource(R.drawable.star3);
                eventListItemHolder.event_list_therapy_status_text.setText(R.string.therapist_discussed);
                break;
        }

        //
        // Get saliva test result on event's day.
        DatabaseControl db = new DatabaseControl();
        Calendar eventTime = eventListItems.get(position).eventTime;
        TestResult testResult = db.getDayTestResult(eventTime.get(Calendar.YEAR),
                eventTime.get(Calendar.MONTH), eventTime.get(Calendar.DAY_OF_MONTH));

        eventListItemHolder.event_list_saliva_status_icon
                = (ImageView) eventItemView.findViewById(R.id.event_list_saliva_status_icon);
        eventListItemHolder.event_list_saliva_status_text
                = (TextView) eventItemView.findViewById(R.id.event_list_saliva_status_text);


        switch (testResult.getResult()) {
            case -1:
                eventListItemHolder.event_list_saliva_status_icon.setBackgroundResource(R.drawable.notdetect);
                eventListItemHolder.event_list_saliva_status_text.setText(R.string.no_saliva_test);
                break;
            case 0: // Pass
                eventListItemHolder.event_list_saliva_status_icon.setBackgroundResource(R.drawable.pass);
                eventListItemHolder.event_list_saliva_status_text.setText(R.string.test_pass);
                break;
            case 1: // Fail
                eventListItemHolder.event_list_saliva_status_icon.setBackgroundResource(R.drawable.notpass);
                eventListItemHolder.event_list_saliva_status_text.setText(R.string.test_fail);
                break;
        }

        return eventItemView;
    }

    // Refresh item to ListView in FragmentEvent.
    public void refreshListViewContent() {
//        Calendar oneWeekAgo = (Calendar) Calendar.getInstance().clone();
//        oneWeekAgo.add(Calendar.DATE, -7);
//        EventLogStructure[] eventLogStructures = thirdPageDataBase.getLaterEventLog(oneWeekAgo);

        EventLogStructure[] eventLogStructures = null;
        // Load incomplete events if invoked by EventIIncompleteActivity, otherwise get all events.
        if(mainActivity.getClass().getSimpleName().equals("EventIIncompleteActivity"))
            eventLogStructures = thirdPageDataBase.getNotCompleteEventLog();
        else
            eventLogStructures = thirdPageDataBase.getAllEventLog();

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
