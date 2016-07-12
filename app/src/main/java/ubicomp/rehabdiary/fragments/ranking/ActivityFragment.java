package ubicomp.rehabdiary.fragments.ranking;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ubicomp.rehabdiary.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class ActivityFragment extends Fragment {

    public ActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ranking_content, container, false);
    }
}
