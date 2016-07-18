package ubicomp.rehabdiary.utility.statistic;

import android.graphics.Typeface;
import android.os.Build;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ubicomp.rehabdiary.R;
import ubicomp.rehabdiary.fragments.FragmentStatistics;
import ubicomp.rehabdiary.utility.back_door.Typefaces;
import ubicomp.rehabdiary.utility.data.db.DatabaseControl;
import ubicomp.rehabdiary.utility.system.Config;
import ubicomp.rehabdiary.utility.system.PreferenceControl;

public class AnalysisCounterView extends StatisticPageView {

	// Get the object of the layout
	private TextView couponValue, pointValue;
	private RelativeLayout barLayout;
	public static ImageView couponLevel, QuizButton;
	public static AnalysisCounterView analysisCounterView = null;

	// Get the coupon images that change with the points
	private final static int[] levelId = {R.drawable.analyse_coupon_level0,
			R.drawable.analyse_coupon_level1, R.drawable.analyse_coupon_level2, R.drawable.analyse_coupon_level3,
			R.drawable.analyse_coupon_level4, R.drawable.analyse_coupon_level5, R.drawable.analyse_coupon_level6,
			R.drawable.analyse_coupon_level7, R.drawable.analyse_coupon_level8, R.drawable.analyse_coupon_level9,
			R.drawable.analyse_coupon_level10};

	private DatabaseControl db;
	private Typeface digitTypefaceBold;
	private AlphaAnimation barTitleAnimation;

	// Set up initial value and set quiz click action
	public AnalysisCounterView() {
		super(R.layout.analysis_counter_view);

		analysisCounterView = this;
		db = new DatabaseControl();

		barLayout = (RelativeLayout) view.findViewById(R.id.analysis_counter_bar_layout);

		digitTypefaceBold = Typefaces.getDigitTypefaceBold();
		couponValue = (TextView) view.findViewById(R.id.analysis_counter_coupon_value);
		pointValue = (TextView) view.findViewById(R.id.analysis_counter_coupon_point);
		couponValue.setTypeface(digitTypefaceBold);
		pointValue.setTypeface(digitTypefaceBold);

		couponLevel = (ImageView)view.findViewById(R.id.analysis_counter_coupon_level);

		QuizButton = (ImageView) view.findViewById(R.id.analysis_quiz);
		QuizButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				QuizButton.setEnabled(false);
				FragmentStatistics.showQuizDialog();
			}
		});

	}

	@Override
	public void clear() {
	}

	@Override
	public void load() {
		updateCounter();
	}

	@Override
	public void onCancel() {
		clear();
	}

	// have the title bar twinkle
	private void setAnimation() {
		barTitleAnimation = new AlphaAnimation(1.0F, 0.0F);
		barTitleAnimation.setDuration(200);
		barTitleAnimation.setRepeatCount(Animation.INFINITE);
		barTitleAnimation.setRepeatMode(Animation.REVERSE);
		barLayout.setAnimation(barTitleAnimation);
		barTitleAnimation.start();
	}

	@SuppressWarnings("deprcation")
	public void updateCounter() {

		int prev_coupon_count = PreferenceControl.lastShowedCoupon();
		int total_point = PreferenceControl.getPoint();
		int curr_coupon_count = PreferenceControl.getCoupon();

		int coupon_level_counter =
				(total_point%Config.COUPON_CREDITS) / (Config.COUPON_CREDITS/10);

		PreferenceControl.setShowedCoupon(curr_coupon_count);
		PreferenceControl.setCouponChange(false);

		// evoke the twinkling effect of the title bar when the coupon value change
		if (prev_coupon_count < curr_coupon_count) {
			setAnimation();
			if (Build.VERSION.SDK_INT < 16)
				barLayout.setBackgroundDrawable(context.getResources()
						.getDrawable(R.drawable.analysis_title_bar_highlight));
			else
				barLayout.setBackground(context.getResources().getDrawable(
						R.drawable.analysis_title_bar_highlight));
		} else {
			if (Build.VERSION.SDK_INT < 16)
				barLayout.setBackgroundDrawable(context.getResources()
						.getDrawable(R.drawable.analyse_line));
			else
				barLayout.setBackground(context.getResources().getDrawable(
						R.drawable.analyse_line));
		}

		// update coupon level icon to show how much point to go
		couponLevel.setImageResource(levelId[coupon_level_counter]);

		// update numeric value for coupon and point
		couponValue.setText(String.valueOf(curr_coupon_count));
		pointValue.setText(String.valueOf(Config.COUPON_CREDITS - total_point%Config.COUPON_CREDITS));
		couponValue.invalidate();
		pointValue.invalidate();
	}

	public static AnalysisCounterView getAnalysisCounterView() {
		return analysisCounterView;
	}
}