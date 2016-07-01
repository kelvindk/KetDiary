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

    public final static int MIDNIGHT = 0;
    public final static int MORNING = 1;
    public final static int AFTERNOON = 2;
    public final static int NIGHT = 3;

    private MainActivity mainActivity = null;
    private ListView eventListView = null;
    private LayoutInflater layoutInflater = null;

    ThirdPageDataBase thirdPageDataBase = null;

    // the list of items' object.
    List<EventLogStructure> eventListItems = new ArrayList<EventLogStructure>();

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
        ImageView fragment_event_list_category_icon = null;
        TextView fragment_event_list_description = null;
        TextView fragment_event_list_expected_behavior = null;
        RatingBar fragment_event_list_item_drug_use_risk_level = null;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {


        View eventItemView = layoutInflater.inflate(R.layout.fragment_event_list_item, null);

        EventListItemHolder eventListItemHolder = new EventListItemHolder();

        // Set event date.
        eventListItemHolder.fragment_event_list_date
                = (TextView)  eventItemView.findViewById(R.id.fragment_event_list_date);
        Calendar calendar = eventListItems.get(i).createTime;
        String week = "";
        switch(calendar.get(Calendar.DAY_OF_WEEK)){
            case Calendar.SUNDAY:
                week = "(日)";
                break;
            case Calendar.MONDAY:
                week = "(一)";
                break;
            case Calendar.TUESDAY:
                week = "(二)";
                break;
            case Calendar.WEDNESDAY:
                week = "(三)";
                break;
            case Calendar.THURSDAY:
                week = "(四)";
                break;
            case Calendar.FRIDAY:
                week = "(五)";
                break;
            case Calendar.SATURDAY:
                week = "(六)";
        }

        String timePeriod = "";
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        switch(hour/6) {
            case MIDNIGHT:
                timePeriod = mainActivity.getString(R.string.midnight);
                break;
            case MORNING:
                timePeriod = mainActivity.getString(R.string.morning);
                break;
            case AFTERNOON:
                timePeriod = mainActivity.getString(R.string.afternoon);
                break;
            case NIGHT:
                timePeriod = mainActivity.getString(R.string.night);
                break;
        }
        String date = (calendar.get(Calendar.MONTH)+1)+"/"+calendar.get(Calendar.DAY_OF_MONTH)+ " "
                +week+" "+timePeriod;
        eventListItemHolder.fragment_event_list_date.setText(date);

        // Set event icon.
        eventListItemHolder.fragment_event_list_category_icon
                = (ImageView) eventItemView.findViewById(R.id.fragment_event_list_category_icon);
        int iconId = 0;
        switch (eventListItems.get(i).scenarioType) {
            case SLACKNESS:
                iconId = R.drawable.type_icon1;
                break;
            case BODY:
                iconId = R.drawable.type_icon2;
                break;
            case CONTROL:
                iconId = R.drawable.type_icon3;
                break;
            case IMPULSE:
                iconId = R.drawable.type_icon4;
                break;
            case EMOTION:
                iconId = R.drawable.type_icon5;
                break;
            case GET_ALONG:
                iconId = R.drawable.type_icon6;
                break;
            case SOCIAL:
                iconId = R.drawable.type_icon7;
                break;
            case ENTERTAIN:
                iconId = R.drawable.type_icon8;
                break;
        }
        eventListItemHolder.fragment_event_list_category_icon.setImageResource(iconId);

        // Set event scenario.
        eventListItemHolder.fragment_event_list_description
                = (TextView) eventItemView.findViewById(R.id.fragment_event_list_description);
        eventListItemHolder.fragment_event_list_description.setText(eventListItems.get(i).scenario);

        // Set expected behavior.
        String expectedBehavior = mainActivity.getString(R.string.expected_behavior) + "： "+
                eventListItems.get(i).expectedBehavior;
        eventListItemHolder.fragment_event_list_expected_behavior
                = (TextView) eventItemView.findViewById(R.id.fragment_event_list_expected_behavior);
        eventListItemHolder.fragment_event_list_expected_behavior.setText(expectedBehavior);

        // Set risk level.
        eventListItemHolder.fragment_event_list_item_drug_use_risk_level
                = (RatingBar) eventItemView.findViewById(R.id.fragment_event_list_item_drug_use_risk_level);
        eventListItemHolder.fragment_event_list_item_drug_use_risk_level.setRating(eventListItems.get(i).drugUseRiskLevel);

        return eventItemView;
    }

    // Refresh item to ListView in FragmentEvent.
    public void refreshDataSet() {
        Calendar oneWeekAgo = (Calendar) Calendar.getInstance().clone();
        oneWeekAgo.add(Calendar.DATE, -7);
//        EventLogStructure[] eventLogStructures = thirdPageDataBase.getLaterEventLog(oneWeekAgo);
        EventLogStructure[] eventLogStructures = thirdPageDataBase.getAllEventLog();

        for(int i=0; i<eventLogStructures.length; i++) {
            eventListItems.add(eventLogStructures[i]);
        }

        notifyDataSetChanged();

    }

    // Set height of ListView to 0, this is a trick to avoid crash while switch fragment.
    public void invisibleList() {
        eventListView.getLayoutParams().height = 0;
        notifyDataSetChanged();
    }

}
