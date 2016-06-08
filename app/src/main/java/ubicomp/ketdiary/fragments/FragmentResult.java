package ubicomp.ketdiary.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ubicomp.ketdiary.R;
import ubicomp.ketdiary.ui.FragmentSwitcher;

/**
 * A placeholder fragment containing a simple view for Result fragment.
 */
public class FragmentResult extends Fragment {
    FragmentSwitcher fragmentSwitcher = null;

    public FragmentResult(FragmentSwitcher fragmentSwitcher) {
        this.fragmentSwitcher = fragmentSwitcher;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Ket", "onCreate fragment_result");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Ket", "onCreateView fragment_result");
        fragmentSwitcher.setFragmentOnlyDowndropTab(FragmentSwitcher.FRAGMENT_RESULT);

        View fragmentResultView = inflater.inflate(R.layout.fragment_result, container, false);

        return fragmentResultView;
    }

}
