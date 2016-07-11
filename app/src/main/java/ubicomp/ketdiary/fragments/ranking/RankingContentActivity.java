package ubicomp.ketdiary.fragments.ranking;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ubicomp.ketdiary.R;
import ubicomp.ketdiary.fragments.FragmentRanking;
import ubicomp.ketdiary.utility.data.db.AddScoreDataBase;
import ubicomp.ketdiary.utility.data.structure.TriggerRanking;

/**
 * Created by kelvindk on 16/7/4.
 */
public class RankingContentActivity extends AppCompatActivity {

    public static final int BROWSING_COUNTDOWN = 10000; // 10000

    private ListView listView = null;
    private RankingContentListAdapter rankingContentListAdapter = null;

    private TriggerRanking triggerRanking = null;
    private int triggerRankingPosition = 0;

    // Countdown timer for checking user's browsing time in this page.
    private CountDownTimer browsingCountdown = null;

    public TriggerRanking getTriggerRanking() {
        return triggerRanking;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking_content);

        // Set full screen.
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN );

        // Retrieve EventLogStructure.
        Bundle bundle = getIntent().getExtras();
        triggerRanking = (TriggerRanking) bundle.getSerializable(TriggerRanking.TRIGGER_RANKING_STRUCUTRE_KEY);
        triggerRankingPosition = bundle.getInt(FragmentRanking.FRAGMENT_RANKING_KEY);


        // Setup listView.
        listView = (ListView) findViewById(R.id.fragment_ranking_content_list_view);

        rankingContentListAdapter = new RankingContentListAdapter(this, listView);

        listView.setAdapter(rankingContentListAdapter);

        // Enable toolbar on create event activity with back button on the top left.
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_ranking_content_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void loadTriggerContent() {
        // Set ranking number.
        TextView fragment_ranking_content_rank =
                (TextView) findViewById(R.id.fragment_ranking_content_rank);
        fragment_ranking_content_rank.setText((triggerRankingPosition+1)+"");

        // Set average risk level.
        RatingBar fragment_ranking_content_drug_use_risk_level =
                (RatingBar) findViewById(R.id.fragment_ranking_content_drug_use_risk_level);
        fragment_ranking_content_drug_use_risk_level.
                setRating(triggerRanking.averageDrugUseRisk);

        // Set scenario icon.
        ImageView fragment_ranking_content_category_icon
                = (ImageView) findViewById(R.id.fragment_ranking_content_category_icon);
        fragment_ranking_content_category_icon.
                setImageResource(triggerRanking.scenarioTypeToIconId());

        // Set scenario.
        TextView fragment_ranking_content_description
                = (TextView) findViewById(R.id.fragment_ranking_content_description);
        fragment_ranking_content_description.setText(triggerRanking.scenario);

        // Set number of event for this scenario type.
        TextView fragment_ranking_content_frequency_text
                = (TextView)  findViewById(R.id.fragment_ranking_content_frequency_text);
        fragment_ranking_content_frequency_text.setText(triggerRanking.eventLogNum+"");

        // Set number of saliva fails.
        TextView fragment_ranking_content_test_fail_text
                = (TextView)  findViewById(R.id.fragment_ranking_content_test_fail_text);
        fragment_ranking_content_test_fail_text.setText(triggerRanking.noPassDay+"");

        // Set percentage of reflection progress.
        int percentage = (int)(triggerRanking.completePercentage*100);
        TextView ranking_content_reflection_progress_text
                = (TextView) findViewById(R.id.ranking_content_reflection_progress_text);
        ranking_content_reflection_progress_text.setText(percentage+"%");

        ProgressBar ranking_content_reflection_progress_bar
                = (ProgressBar) findViewById(R.id.ranking_content_reflection_progress_bar);
        ranking_content_reflection_progress_bar.setProgress(percentage);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the icon on the top right of toolbar in CreateEventActivity.
        getMenuInflater().inflate(R.menu.menu_ranking_content, menu);

        return true;
    }

    @Override
    public void onResume() {
        Log.d("Ket", "RankingContentActivity onResume");

        // Start countdown timer to check whether user's browsing is over 10 sec.
        browsingCountdown = new CountDownTimer(BROWSING_COUNTDOWN, BROWSING_COUNTDOWN){
            @Override
            public void onFinish() {
                // Invoke add score.
                Log.d("AddScore", "addScoreViewPage4Detail");
                AddScoreDataBase addScoreDataBase = new AddScoreDataBase();
                addScoreDataBase.addScoreViewPage4Detail();
            }

            @Override
            public void onTick(long millisUntilFinished) {
                // No op.
//                Log.d("AddScore", "Page4 - 2  Tick "+ millisUntilFinished/1000);
            }
        }.start();

        // Load trigger on the page.
        loadTriggerContent();
        rankingContentListAdapter.refreshListViewContent();
        super.onResume();
    }


    @Override
    public void onPause() {
        Log.d("Ket", "EventContentActivity onPause");

        // Cancel browsing countdown when leaving this page.
        browsingCountdown.cancel();

        super.onPause();
    }
}
