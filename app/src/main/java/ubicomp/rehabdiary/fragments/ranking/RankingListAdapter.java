package ubicomp.rehabdiary.fragments.ranking;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ubicomp.rehabdiary.MainActivity;
import ubicomp.rehabdiary.R;
import ubicomp.rehabdiary.utility.data.db.FourthPageDataBase;
import ubicomp.rehabdiary.utility.data.structure.TriggerRanking;

/**
 * Created by kelvindk on 16/7/2.
 */
public class RankingListAdapter extends BaseAdapter {

    private Activity mainActivity = null;
    private ListView rankingListView = null;
    private LayoutInflater layoutInflater = null;

    private FourthPageDataBase fourthPageDataBase = null;

    // the list of items' object.
    List<TriggerRanking> rankingListItems = new ArrayList<>();

    public RankingListAdapter(Fragment fragment, ListView rankingListView) {
        this.mainActivity = fragment.getActivity();
        this.rankingListView = rankingListView;
        Context context = mainActivity;

        fourthPageDataBase = new FourthPageDataBase();

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


    }

    public TriggerRanking getRankingListItem(int pos) {
        return rankingListItems.get(pos);
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
    public class RankingListItemHolder
    {
        TextView fragment_ranking_list_rank = null;
        RatingBar fragment_ranking_list_drug_use_risk_level = null;
        ImageView fragment_ranking_list_category_icon = null;
        TextView fragment_ranking_list_description = null;
        TextView fragment_ranking_list_frequency_text = null;
        TextView fragment_ranking_list_test_fail_text = null;
        TextView ranking_list_reflection_progress_text = null;
        ProgressBar ranking_list_reflection_progress_bar = null;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rankingItemView = layoutInflater.inflate(R.layout.fragment_ranking_list_item, null);

        RankingListItemHolder rankingListItemHolder = new RankingListItemHolder();

        // Set ranking number.
        rankingListItemHolder.fragment_ranking_list_rank
                = (TextView)  rankingItemView.findViewById(R.id.fragment_ranking_list_rank);
        rankingListItemHolder.fragment_ranking_list_rank.setText(""+(position+1));

        // Set average risk level.
        rankingListItemHolder.fragment_ranking_list_drug_use_risk_level
                = (RatingBar)  rankingItemView.findViewById(R.id.fragment_ranking_list_drug_use_risk_level);
        rankingListItemHolder.fragment_ranking_list_drug_use_risk_level.
                setRating(rankingListItems.get(position).averageDrugUseRisk);

        // Set scenario icon.
        rankingListItemHolder.fragment_ranking_list_category_icon
                = (ImageView)  rankingItemView.findViewById(R.id.fragment_ranking_list_category_icon);
        rankingListItemHolder.fragment_ranking_list_category_icon.
                setImageResource(rankingListItems.get(position).scenarioTypeToIconId());

        // Set scenario.
        rankingListItemHolder.fragment_ranking_list_description
                = (TextView) rankingItemView.findViewById(R.id.fragment_ranking_list_description);
        rankingListItemHolder.fragment_ranking_list_description.setText(rankingListItems.get(position).scenario);

        // Set number of event for this scenario type.
        rankingListItemHolder.fragment_ranking_list_frequency_text
                = (TextView)  rankingItemView.findViewById(R.id.fragment_ranking_list_frequency_text);
        rankingListItemHolder.fragment_ranking_list_frequency_text.
                setText(rankingListItems.get(position).eventLogNum+"");

        // Set number of saliva fails.
        rankingListItemHolder.fragment_ranking_list_test_fail_text
                = (TextView)  rankingItemView.findViewById(R.id.fragment_ranking_list_test_fail_text);
        rankingListItemHolder.fragment_ranking_list_test_fail_text.
                setText(rankingListItems.get(position).noPassDay+"");

        // Set percentage of reflection progress.
        int percentage = (int)(rankingListItems.get(position).completePercentage*100);
        rankingListItemHolder.ranking_list_reflection_progress_text
                = (TextView) rankingItemView.findViewById(R.id.ranking_list_reflection_progress_text);
        rankingListItemHolder.ranking_list_reflection_progress_text.
                setText(percentage+"%");

        rankingListItemHolder.ranking_list_reflection_progress_bar
                = (ProgressBar) rankingItemView.findViewById(R.id.ranking_list_reflection_progress_bar);
        rankingListItemHolder.ranking_list_reflection_progress_bar.setProgress(percentage);

        return rankingItemView;
    }

    // Refresh item to ListView in FragmentEvent.
    public void refreshListViewContent() {

        TriggerRanking[] triggerRankings = fourthPageDataBase.getRankingList();

        if(triggerRankings == null)
            return;

        rankingListItems = new ArrayList<>(Arrays.asList(triggerRankings));

        notifyDataSetChanged();

    }

    // Set height of ListView to 0, this is a trick to avoid crash while switch fragment.
    public void invisibleList() {
        rankingListView.getLayoutParams().height = 0;
        notifyDataSetChanged();
    }
}
