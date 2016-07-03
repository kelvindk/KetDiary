package ubicomp.ketdiary.fragments.ranking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ubicomp.ketdiary.MainActivity;
import ubicomp.ketdiary.R;
import ubicomp.ketdiary.fragments.event.EventLogStructure;

/**
 * Created by kelvindk on 16/7/2.
 */
public class RankingListAdapter extends BaseAdapter {

    private MainActivity mainActivity = null;
    private ListView rankingListView = null;
    private LayoutInflater layoutInflater = null;

    // the list of items' object.
    List<EventLogStructure> rankingListItems = new ArrayList<>();

    public RankingListAdapter(MainActivity mainActivity, ListView rankingListView) {
        this.mainActivity = mainActivity;
        this.rankingListView = rankingListView;
        Context context = mainActivity;

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


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
        TextView fragment_ranking_list_description = null;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rankingItemView = layoutInflater.inflate(R.layout.fragment_ranking_list_item, null);

        RankingListItemHolder rankingListItemHolder = new RankingListItemHolder();

        rankingListItemHolder.fragment_ranking_list_description
                = (TextView)  rankingItemView.findViewById(R.id.fragment_ranking_list_description);
        rankingListItemHolder.fragment_ranking_list_description.setText("---"+position+"---");

        return rankingItemView;
    }

    // Refresh item to ListView in FragmentEvent.
    public void refreshListViewContent() {

        for(int i=0; i<20; i++) {
            rankingListItems.add(new EventLogStructure());
        }
        notifyDataSetChanged();

    }

    // Set height of ListView to 0, this is a trick to avoid crash while switch fragment.
    public void invisibleList() {
        rankingListView.getLayoutParams().height = 0;
        notifyDataSetChanged();
    }
}
