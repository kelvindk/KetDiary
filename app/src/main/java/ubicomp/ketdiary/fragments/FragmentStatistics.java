package ubicomp.ketdiary.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import ubicomp.ketdiary.R;
import ubicomp.ketdiary.main_activity.FragmentSwitcher;
import ubicomp.ketdiary.utility.dialog.QuizCaller;
import ubicomp.ketdiary.utility.dialog.QuizDialog;
import ubicomp.ketdiary.utility.statistic.AnalysisCounterView;
import ubicomp.ketdiary.utility.statistic.StatisticPageView;
import ubicomp.ketdiary.utility.statistic.StatisticPagerAdapter;


/**
 * A placeholder fragment containing a simple view.
 */
public class FragmentStatistics extends Fragment implements QuizCaller {
    FragmentSwitcher fragmentSwitcher = null;

    private static final String TAG = "Statistics";

    private View view;
    private Activity activity;
    private RelativeLayout allLayout;
    private ViewPager statisticView;
    private StatisticPagerAdapter statisticViewAdapter;
    private FragmentStatistics fragmentStatistics;
    private LinearLayout analysisLayout;
    private ScrollView analysisView;
    private StatisticPageView[] analysisViews;
    private ImageView[] dots;
    private Drawable dot_on, dot_off;
    private LoadingHandler loadHandler;

    private ImageView questionButton;
    private static QuizDialog msgBox;
//    private AlphaAnimation quesitonAnimation;

    public FragmentStatistics() {
    }

    public FragmentStatistics(FragmentSwitcher fragmentSwitcher) {
        this.fragmentSwitcher = fragmentSwitcher;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        dot_on = getResources().getDrawable(R.drawable.statistic_node_yes);
        dot_off = getResources().getDrawable(R.drawable.statistic_node_no);

//        db = new DatabaseControl();
        Log.d("Ket", "onCreate fragment_statistics");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Ket", "onCreateView fragment_statistics");
        fragmentSwitcher.setFragmentOnlyDowndropTab(FragmentSwitcher.FRAGMENT_STATISTICS);

        view = inflater.inflate(R.layout.fragment_statistics, container, false);
        allLayout = (RelativeLayout) view.findViewById(R.id.statistic_fragment_layout);

        // The lower half view
        analysisLayout = (LinearLayout) view.findViewById(R.id.brac_analysis_layout);
        analysisView = (ScrollView) view.findViewById(R.id.brac_analysis);
//        questionButton = (ImageView) view.findViewById(R.id.quiz_button);
//        questionButton.setOnTouchListener(new ScaleOnTouchListener());

        // The upper half view
        statisticView = (ViewPager) view.findViewById(R.id.brac_statistics);
        statisticViewAdapter = new StatisticPagerAdapter();
        dots = new ImageView[3];
        dots[0] = (ImageView) view.findViewById(R.id.brac_statistic_dot0);
        dots[1] = (ImageView) view.findViewById(R.id.brac_statistic_dot1);
        dots[2] = (ImageView) view.findViewById(R.id.brac_statistic_dot2);

        loadHandler = new LoadingHandler();

        analysisLayout.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent motion) {
                return false;
            }
        });

        return view;
    }

    public void onResume() {
        super.onResume();

        fragmentStatistics = this;

        /** [LILY] Remove something related to Time and testFail **/

        analysisViews = new StatisticPageView[1];
        analysisViews[0] = new AnalysisCounterView();
//        analysisViews[1] = new AnalysisRankView();

        statisticViewAdapter = new StatisticPagerAdapter();
        msgBox = new QuizDialog((RelativeLayout) view, fragmentStatistics);

        loadHandler.sendEmptyMessage(0);
    }

    public void onPause() {
        if (loadHandler != null)
            loadHandler.removeMessages(0);
        clear();
        super.onPause();
    }

    public void clear() {
        statisticViewAdapter.clear();
        if (analysisLayout != null)
            analysisLayout.removeAllViews();

        for (int i = 0; i < analysisViews.length; ++i) {
            if (analysisViews[i] != null)
                analysisViews[i].clear();
        }

        if (msgBox != null)
            msgBox.clear();
    }

    private class StatisticOnPageChangeListener implements OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int idx) {

            for (int i = 0; i < 3; ++i)
                dots[i].setImageDrawable(dot_off);
            dots[idx].setImageDrawable(dot_on);
        }
    }


    @SuppressLint("HandlerLeak")
    private class LoadingHandler extends Handler {
        public void handleMessage(Message msg) {
            //MainActivity.getMainActivity().enableTabAndClick(false);
            statisticView.setAdapter(statisticViewAdapter);
            statisticView.addOnPageChangeListener(new StatisticOnPageChangeListener());
            statisticView.setSelected(true);
            analysisLayout.removeAllViews();

//            questionButton.setOnClickListener(new QuestionOnClickListener());

            for (int i = 0; i < analysisViews.length; ++i)
                if (analysisViews[i] != null)
                    analysisLayout.addView(analysisViews[i].getView());

            statisticViewAdapter.load();
            for (int i = 0; i < analysisViews.length; ++i)
                if (analysisViews[i] != null)
                    analysisViews[i].load();

            statisticView.setCurrentItem(0);

            for (int i = 0; i < 3; ++i)
                dots[i].setImageDrawable(dot_off);
            dots[0].setImageDrawable(dot_on);

            if (msgBox != null)
                msgBox.initialize();
        }
    }

    private class QuestionOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            /** [LILY] Remove ! coping skill on the top right **/
//            Intent intent = new Intent();
//            intent.setClass(activity, CopingActivty.class);
//            activity.startActivity(intent);
        }
    }

    public static void showQuizDialog() {
        msgBox.show(0);
    }

    @Override
    public void QuizDone() {
        // TODO Auto-generated method stub
    }

}
