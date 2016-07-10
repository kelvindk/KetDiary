package ubicomp.ketdiary.fragments.test_pending;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ubicomp.ketdiary.R;

/**
 * Created by kelvindk on 16/7/10.
 */
public class FragmentTestPendingPage extends Fragment {

    public static final String TAG = "FragmentTestPendingPage";

    private String copingTipString = "";

    public static final FragmentTestPendingPage newInstance(String copingTipString) {
        FragmentTestPendingPage fragment = new FragmentTestPendingPage();
        Bundle bundle = new Bundle(1);
        bundle.putString(TAG, copingTipString);
        fragment.setArguments(bundle);
        return fragment;
    }

    public FragmentTestPendingPage() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        copingTipString = getArguments().getString(TAG);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_test_pending_page, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView copingText = (TextView) view.findViewById(R.id.fragment_test_pending_coping_text);
        copingText.setText(Html.fromHtml(copingTipString));
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
