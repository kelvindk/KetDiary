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
 * A placeholder fragment containing a simple view for Test fragment.
 */
public class FragmentTest extends Fragment {
    FragmentSwitcher fragmentSwitcher = null;

    public FragmentTest(FragmentSwitcher fragmentSwitcher) {
        this.fragmentSwitcher = fragmentSwitcher;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Ket", "onCreate fragment_test");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Ket", "onCreateView fragment_test");
        fragmentSwitcher.setFragmentOnlyDowndropTab(FragmentSwitcher.FRAGMENT_TEST);

        View fragmentTestView = inflater.inflate(R.layout.fragment_test, container, false);

        return fragmentTestView;
    }
}
