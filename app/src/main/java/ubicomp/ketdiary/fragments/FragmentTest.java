package ubicomp.ketdiary.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import ubicomp.ketdiary.MainActivity;
import ubicomp.ketdiary.R;
import ubicomp.ketdiary.fragments.saliva_test.SalivaTestAdapter;
import ubicomp.ketdiary.main_activity.FragmentSwitcher;
import ubicomp.ketdiary.utility.system.PreferenceControl;
import ubicomp.ketdiary.utility.test.bluetoothle.BluetoothLE;

/**
 * A placeholder fragment containing a simple view for Test fragment.
 */
public class FragmentTest extends Fragment {
    private MainActivity mainActivity = null;
    private FragmentSwitcher fragmentSwitcher = null;

    private SalivaTestAdapter salivaTestAdapter = null;

    public FragmentTest(FragmentSwitcher fragmentSwitcher, MainActivity mainActivity) {
        this.mainActivity = mainActivity;
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

    @Override
    public void onStart() {
        super.onStart();

        // New SalivaTestAdapter for handling the saliva test process.
        salivaTestAdapter = new SalivaTestAdapter(mainActivity);
    }


}
