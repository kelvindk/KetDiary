package ubicomp.ketdiary.fragments.event;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ubicomp.ketdiary.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class ActivityIncompleteFragment extends Fragment {

    public ActivityIncompleteFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_event, container, false);
    }
}
