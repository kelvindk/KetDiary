package ubicomp.ketdiary.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import ubicomp.ketdiary.MainActivity;
import ubicomp.ketdiary.R;
import ubicomp.ketdiary.fragments.ranking.RankingListAdapter;
import ubicomp.ketdiary.main_activity.FragmentSwitcher;

/**
 * A placeholder fragment containing a simple view for Ranking fragment.
 */
public class FragmentRanking extends Fragment {

    private MainActivity mainActivity = null;
    private FragmentSwitcher fragmentSwitcher = null;

    private ListView rankingListView = null;

    private RankingListAdapter rankingListAdapter = null;

    public FragmentRanking(FragmentSwitcher fragmentSwitcher, MainActivity mainActivity) {
        this.fragmentSwitcher = fragmentSwitcher;
        this.mainActivity = mainActivity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Ket", "onCreate fragment_ranking");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Ket", "onCreateView fragment_ranking");
        fragmentSwitcher.setFragmentOnlyDowndropTab(FragmentSwitcher.FRAGMENT_RANKING);

        View fragmentRankingView = inflater.inflate(R.layout.fragment_ranking, container, false);

        // Set adapter of custom ListView to this fragment
        rankingListView = (ListView) fragmentRankingView.findViewById(R.id.fragment_ranking_list_view);

        rankingListAdapter = new RankingListAdapter(mainActivity, rankingListView);
        rankingListView.setAdapter(rankingListAdapter);

        return fragmentRankingView;
    }

    @Override
    public void onResume() {
        Log.d("Ket", "FragmentRanking onResume");
//        rankingListAdapter.refreshListViewContent();
        super.onResume();
    }

    // Set height of ListView to 0, this is a trick to avoid crash while switch fragment.
    public void invisibleList() {
        if(rankingListAdapter == null)
            return;
        rankingListAdapter.invisibleList();
    }
}
