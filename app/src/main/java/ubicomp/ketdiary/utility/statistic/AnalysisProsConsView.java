package ubicomp.ketdiary.utility.statistic;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ubicomp.ketdiary.R;
import ubicomp.ketdiary.utility.back_door.Typefaces;
import ubicomp.ketdiary.utility.data.db.DatabaseControl;
import ubicomp.ketdiary.utility.system.PreferenceControl;

public class AnalysisProsConsView extends StatisticPageView {

	private TextView title;
	private static final String TAG = "ProsCons";
	private TextView help;
	private TextView target, curMoney, targetMoney;
	private DatabaseControl db;

	private ImageView currentBar;
	private ImageView barStart, barEnd, bar_followed;
	private ImageView bar;

	private String targetGood;
	private int goal;
	private int drinkCost;
	private int currentMoney;

	private RelativeLayout layout;

	private Typeface wordTypeface, wordTypefaceBold, digitTypefaceBold;

	private String dollor_sign;

	private BarHandler barHandler = new BarHandler();
	private AlphaAnimation arrowAnimation;
	/** [LILY] Remove debug related **/
//	private boolean debug = PreferenceControl.isDebugMode();
	//for debug
	private static int count = 20;

	public AnalysisProsConsView() {
		super(R.layout.analysis_saving_view);
		db = new DatabaseControl();
		
		wordTypeface = Typefaces.getWordTypeface();
		wordTypefaceBold=Typefaces.getDigitTypefaceBold();
		digitTypefaceBold = Typefaces.getDigitTypefaceBold();

		//targetGood = PreferenceControl.getSavingGoal();
		goal = PreferenceControl.getSavingGoalMoney();
		drinkCost = PreferenceControl.getSavingDrinkCost();

		dollor_sign = context.getResources().getString(R.string.dollor_sign);
		//title = (TextView) view.findViewById(R.id.analysis_saving_title);
		
		//bar.setVisibility(View.INVISIBLE);
		help = (TextView) view.findViewById(R.id.analysis_saving_help);
		help.setTypeface(wordTypefaceBold);
		targetMoney = (TextView) view.findViewById(R.id.analysis_target_money);
		targetMoney.setTypeface(wordTypefaceBold);
		
		help.setText(PreferenceControl.getNegativeGoal());
		targetMoney.setText(PreferenceControl.getPostiveGoal());

		currentBar = (ImageView) view
				.findViewById(R.id.analysis_pros_position);
		bar_followed=(ImageView) view
				.findViewById(R.id.analysis_pros_position_followed);
		barStart = (ImageView) view
				.findViewById(R.id.analysis_pros_backward);
		barEnd = (ImageView) view
				.findViewById(R.id.analysis_pros_forward);
		bar = (ImageView) view.findViewById(R.id.analysis_saving_bar);

		layout = (RelativeLayout) view
				.findViewById(R.id.analysis_position_followed_layout);
	}

	@Override
	public void clear() {
		if (barHandler != null)
			barHandler.removeMessages(0);
	}

	@SuppressLint("CutPasteId")
	@Override
	public void load() {
		
	
		
		barHandler.sendEmptyMessage(0);
		//updateBar();
		

		

		//int curDrink = db.getPrimeDetectionPassTimes();
		//currentMoney = curDrink * drinkCost;

		//String cur_money = dollor_sign + currentMoney;
		//String goal_money = dollor_sign + goal;

		//curMoney.setText(cur_money);
		//targetMoney.setText(goal_money);
		//target.setText(targetGood);

	}

	@Override
	public void onCancel() {
		clear();
	}
	
	private void setAnimation(View v){
		arrowAnimation = new AlphaAnimation(1.0F, 0.0F);
		arrowAnimation.setDuration(200);
		arrowAnimation.setRepeatCount(Animation.INFINITE);
		arrowAnimation.setRepeatMode(Animation.REVERSE);
		v.setAnimation(arrowAnimation);
		arrowAnimation.start();
	}
	
	private void updateBar(){
		//int today_situation=19; //pass or fail
		boolean check = PreferenceControl.getCheckResult();
		boolean checkBars = PreferenceControl.getCheckBars();
		int today_situation = PreferenceControl.getPosition();
		int barWidth = bar.getRight() - bar.getLeft();
		int positionWidth = currentBar.getRight() - currentBar.getLeft();
//		int leftWidth = barStart.getRight() - barStart.getLeft();
//		int rightWidth = barEnd.getRight() - barEnd.getLeft();
		
		
		int maxWidth = barWidth - positionWidth;
		int width_per_block=maxWidth/20;
		
//		//for debug
//		today_situation = count--;
//		checkBars = true;
		
		int nextWidth = width_per_block*today_situation;
		
		Log.d(TAG,""+today_situation);
		
		//if(check || checkBars){
		if(checkBars){
			int curWidth = width_per_block*PreferenceControl.getLastPosition();
			if(nextWidth>curWidth){
				if(nextWidth>=maxWidth){
					nextWidth=maxWidth;
					barEnd.setVisibility(View.INVISIBLE);
					barStart.setVisibility(View.INVISIBLE);
					}
				else{
					barStart.setVisibility(View.INVISIBLE);
					barEnd.setVisibility(View.VISIBLE);
					setAnimation(barEnd);
				}
			}
			else if(nextWidth<curWidth){
				if(nextWidth<0){
					nextWidth=0;
					barStart.setVisibility(View.INVISIBLE);
					barEnd.setVisibility(View.INVISIBLE);
				}
				else{
					barStart.setVisibility(View.VISIBLE);
					barEnd.setVisibility(View.INVISIBLE);
					setAnimation(barStart);
				}
			}
			else{
				barStart.setVisibility(View.INVISIBLE);
				barEnd.setVisibility(View.INVISIBLE);
			}
			PreferenceControl.setLastPosition(today_situation);
		}
		RelativeLayout.LayoutParams currentBarParam = (RelativeLayout.LayoutParams) bar_followed.getLayoutParams();
		currentBarParam.width = nextWidth;
		layout.updateViewLayout(bar_followed, currentBarParam);
		currentBar.setVisibility(View.VISIBLE);
		
		PreferenceControl.setCheckBars(false);

	}
	
	private class BarHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			updateBar();
			
			long curTime = System.currentTimeMillis();
			long testTime = PreferenceControl.getLatestTestCompleteTime();
			long pastTime = curTime - testTime;
			boolean testFail = PreferenceControl.isTestFail();	

			/** [LILY] Remove WAIT_RESULT_TIME which is manipulated in MainActivity **/
//			if(testFail){
//			}
//			else if( PreferenceControl.getCheckResult() && pastTime >= MainActivity.WAIT_RESULT_TIME){
//				PreferenceControl.setCheckResult(false);
//			}
//			else if ( PreferenceControl.getCheckResult() && pastTime < MainActivity.WAIT_RESULT_TIME ){
//			}
//			else{
//			}
			
		}
	}

}
