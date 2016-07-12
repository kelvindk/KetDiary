package ubicomp.rehabdiary.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import ubicomp.rehabdiary.MainActivity;
import ubicomp.rehabdiary.R;
import ubicomp.rehabdiary.fragments.ranking.RankingContentActivity;
import ubicomp.rehabdiary.fragments.ranking.RankingListAdapter;
import ubicomp.rehabdiary.main_activity.FragmentSwitcher;
import ubicomp.rehabdiary.utility.data.structure.TriggerRanking;

/**
 * A placeholder fragment containing a simple view for Ranking fragment.
 */
public class FragmentRanking extends Fragment {

    // Key of this object for deliver between activities through Intent.
    public static final String FRAGMENT_RANKING_KEY = "FragmentRanking";

    private MainActivity mainActivity = null;
    private FragmentSwitcher fragmentSwitcher = null;

    private ListView rankingListView = null;

    private RankingListAdapter rankingListAdapter = null;

    public FragmentRanking() {

    }

    @SuppressLint("ValidFragment")
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

        rankingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("Ket", "rankingListView onItemClick " + i);

                // Invoke EventContentActivity.
                Intent rankingContentIntent = new Intent (mainActivity, RankingContentActivity.class);
                // Put the serializable object into eventContentActivityIntent through a Bundle.
                Bundle bundle = new Bundle();
                bundle.putSerializable(TriggerRanking.TRIGGER_RANKING_STRUCUTRE_KEY, rankingListAdapter.getRankingListItem(i));
                bundle.putInt(FRAGMENT_RANKING_KEY, i);
                rankingContentIntent.putExtras(bundle);
                startActivity(rankingContentIntent);
            }
        });

        return fragmentRankingView;
    }

    @Override
    public void onResume() {
        Log.d("Ket", "FragmentRanking onResume");
        rankingListAdapter.refreshListViewContent();
        super.onResume();
    }

    // Set height of ListView to 0, this is a trick to avoid crash while switch fragment.
    public void invisibleList() {
        if(rankingListAdapter == null)
            return;
        rankingListAdapter.invisibleList();
    }

}
