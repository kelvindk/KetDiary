package ubicomp.ketdiary.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ubicomp.ketdiary.MainActivity;
import ubicomp.ketdiary.R;
import ubicomp.ketdiary.main_activity.FragmentSwitcher;

/**
 * A placeholder fragment containing a simple view for TestWaitResult fragment.
 */
public class FragmentTestWaitResult extends Fragment {

    private FragmentSwitcher fragmentSwitcher = null;
    private MainActivity mainActivity = null;

    public FragmentTestWaitResult(FragmentSwitcher fragmentSwitcher, MainActivity mainActivity) {
        this.fragmentSwitcher = fragmentSwitcher;
        this.mainActivity = mainActivity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Ket", "onCreate FragmentTestWaitResult");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Ket", "onCreateView FragmentTestWaitResult");
        fragmentSwitcher.setFragmentOnlyDowndropTab(FragmentSwitcher.FRAGMENT_TEST);

        View fragmentWaitResultView = inflater.inflate(R.layout.fragment_test_wait_result, container, false);

        return fragmentWaitResultView;
    }
}
