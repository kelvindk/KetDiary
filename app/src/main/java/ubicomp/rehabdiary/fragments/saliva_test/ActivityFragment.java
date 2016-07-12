package ubicomp.rehabdiary.fragments.saliva_test;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ubicomp.rehabdiary.R;

/**
 * A placeholder fragment containing a simple view for saliva test.
 */
public class ActivityFragment extends Fragment {

    public ActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_test, container, false);
    }
}
