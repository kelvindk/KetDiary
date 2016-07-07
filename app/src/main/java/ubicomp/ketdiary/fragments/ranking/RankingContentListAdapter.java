package ubicomp.ketdiary.fragments.ranking;

import android.app.Activity;
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

import ubicomp.ketdiary.R;
import ubicomp.ketdiary.fragments.event.EventLogStructure;
import ubicomp.ketdiary.utility.data.structure.TriggerRanking;

/**
 * Created by kelvindk on 16/7/2.
 */
public class RankingContentListAdapter extends BaseAdapter {

    private RankingContentActivity activity = null;
    private ListView rankingListView = null;
    private LayoutInflater layoutInflater = null;

    private TriggerRanking triggerRanking = null;

    // the list of items' object.
    List<EventLogStructure> rankingListItems = new ArrayList<>();

    public RankingContentListAdapter(RankingContentActivity activity, ListView rankingListView) {
        this.activity = activity;
        this.rankingListView = rankingListView;

        this.triggerRanking = activity.getTriggerRanking();

        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        refreshListViewContent();
    }


    @Override
    public int getCount() {
        return rankingListItems.size();
    }

    @Override
    public Object getItem(int position) {
        return rankingListItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return rankingListItems.indexOf(getItem(position));
    }

    // UI components of item.
    public class RankingListContentItemHolder
    {
        TextView fragment_ranking_content_list_date = null;
        ImageView fragment_ranking_content_list_therapist_icon = null;
        TextView fragment_ranking_content_list_therapist_text = null;
        TextView fragment_ranking_content_list_expected_though = null;
        TextView fragment_ranking_content_list_expected_behavior = null;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rankingItemView = layoutInflater.inflate(R.layout.fragment_ranking_content_list_item, null);

        RankingListContentItemHolder rankingListContentItemHolder = new RankingListContentItemHolder();

        // Set event date.
        rankingListContentItemHolder.fragment_ranking_content_list_date
                = (TextView) rankingItemView.findViewById(R.id.fragment_ranking_content_list_date);
        rankingListContentItemHolder.fragment_ranking_content_list_date.
                setText(rankingListItems.get(position).eventTimeToString());

        // Set therapist status icon & text.
        rankingListContentItemHolder.fragment_ranking_content_list_therapist_icon
                = (ImageView) rankingItemView.findViewById(R.id.fragment_ranking_content_list_therapist_icon);
        rankingListContentItemHolder.fragment_ranking_content_list_therapist_icon.
                setImageResource(rankingListItems.get(position).therapyStatusToIconId());

        rankingListContentItemHolder.fragment_ranking_content_list_therapist_text
                = (TextView) rankingItemView.findViewById(R.id.fragment_ranking_content_list_therapist_text);
        rankingListContentItemHolder.fragment_ranking_content_list_therapist_text.
                setText(rankingListItems.get(position).therapyStatusToIconString());

        // Set expected thought.
        rankingListContentItemHolder.fragment_ranking_content_list_expected_though
                = (TextView) rankingItemView.findViewById(R.id.fragment_ranking_content_list_expected_though);
        rankingListContentItemHolder.fragment_ranking_content_list_expected_though.
                setText(rankingListItems.get(position).expectedThought);

        // Set expected behavior.
        rankingListContentItemHolder.fragment_ranking_content_list_expected_behavior
                = (TextView) rankingItemView.findViewById(R.id.fragment_ranking_content_list_expected_behavior);
        rankingListContentItemHolder.fragment_ranking_content_list_expected_behavior.
                setText(rankingListItems.get(position).expectedBehavior);


        return rankingItemView;
    }

    // Refresh item to ListView in FragmentEvent.
    public void refreshListViewContent() {
        Log.d("Ket", "triggerRanking.eventLogList "+triggerRanking.eventLogList.size());
        rankingListItems = triggerRanking.eventLogList;

        notifyDataSetChanged();

    }

    // Set height of ListView to 0, this is a trick to avoid crash while switch fragment.
    public void invisibleList() {
        rankingListView.getLayoutParams().height = 0;
        notifyDataSetChanged();
    }
}
