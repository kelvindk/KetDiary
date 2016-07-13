package ubicomp.ketdiary.utility.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import java.util.Random;

import ubicomp.rehabdiary.R;
import ubicomp.rehabdiary.main_activity.KetdiaryApplication;
import ubicomp.rehabdiary.utility.data.db.DatabaseControl;
import ubicomp.rehabdiary.utility.data.structure.AddScore;
import ubicomp.rehabdiary.utility.data.structure.QuestionTest;
import ubicomp.rehabdiary.utility.dialog.QuizCaller;
import ubicomp.rehabdiary.utility.statistic.AnalysisCounterView;
import ubicomp.rehabdiary.utility.statistic.CustomToast;
import ubicomp.rehabdiary.utility.statistic.Typefaces;
import ubicomp.rehabdiary.utility.system.PreferenceControl;


/**
 * Note after testing
 * 答對題目 & 一段時間後才換題目
 * 
 * @author Andy
 */
public class QuizDialog{
	
	private Activity activity;
	private QuizDialog noteFragment = this;
	private QuizCaller quizCaller;
	private static final String TAG = "ADD_PAGE";

	private Context context;
	private LayoutInflater inflater;
	private RelativeLayout boxLayout = null;
	private LinearLayout questionLayout;
	
	private static String question = "";
	private static String answer = "";
	private String selectedAnswer = "";
	private static String[] selection;

	private static String question2 = "";
	private static String[] selection2;
	private static String answer2 = "";
	
	private RelativeLayout mainLayout;
	private String[] type_str;
	
	private TextView tv_question, tv_answer1, tv_answer2, tv_answer3, tv_answer4, tv_confirm, tv_cancel;
	private ImageView radio1, radio2, radio3, radio4;
	private static ImageView iv_type;
	private LinearLayout layout1, layout2, layout3, layout4;
	/** @see Typefaces */
	private Typeface wordTypeface, wordTypefaceBold, digitTypeface,
			digitTypefaceBold;
	
	private int select = -1;
	private DatabaseControl db;
	private int questionType = 0;
	private static boolean change = true;
	private static boolean change2 = true;
	private static long last_visit= 0;
	private static long last_visit2=0;
	
	private Handler mHandler = new Handler();
	
	private static final int[] iconId = {R.drawable.type_icon1, R.drawable.type_icon2, R.drawable.type_icon3,
		R.drawable.type_icon4, R.drawable.type_icon5, R.drawable.type_icon6, R.drawable.type_icon7,
		R.drawable.type_icon8};
	
	private static final int[] dotId2 = { 0, R.drawable.dot_color1, R.drawable.dot_color2,
    	R.drawable.dot_color3, R.drawable.dot_color4, R.drawable.dot_color5,
    	R.drawable.dot_color6, R.drawable.dot_color7, R.drawable.dot_color8
    };
	
	
	public QuizDialog(RelativeLayout mainLayout, QuizCaller quizCaller){
		
		this.context = KetdiaryApplication.getContext();
		this.inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mainLayout = mainLayout;
		
		db = new DatabaseControl();
		this.quizCaller = quizCaller;
		
		wordTypeface = Typefaces.getWordTypeface();
		wordTypefaceBold = Typefaces.getWordTypefaceBold();
		digitTypeface = Typefaces.getDigitTypeface();
		digitTypefaceBold = Typefaces.getDigitTypefaceBold();
		
		type_str = context.getResources().getStringArray(R.array.trigger_list);
	    setting();
	    mainLayout.addView(boxLayout);

	}
	
	protected void setting() {
		
		boxLayout = (RelativeLayout) inflater.inflate(
				R.layout.dialog_answer_question, null);
		boxLayout.setVisibility(View.INVISIBLE);
		
		tv_question = (TextView) boxLayout.findViewById(R.id.question_question);
		tv_answer1 = (TextView) boxLayout.findViewById(R.id.question_answer1);
		tv_answer2 = (TextView) boxLayout.findViewById(R.id.question_answer2);
		tv_answer3 = (TextView) boxLayout.findViewById(R.id.question_answer3);
		tv_answer4 = (TextView) boxLayout.findViewById(R.id.question_answer4);
		tv_cancel = (TextView)  boxLayout.findViewById(R.id.question_cancel);
		tv_confirm = (TextView) boxLayout.findViewById(R.id.question_confirm);
		iv_type = (ImageView)boxLayout.findViewById(R.id.question_image);
		
		tv_confirm.setOnClickListener(new ConfirmOnClickListener());
		tv_cancel.setOnClickListener(new CancelOnClickListener());
		
		radio1 = (ImageView) boxLayout.findViewById(R.id.question_radio1);
		radio2 = (ImageView) boxLayout.findViewById(R.id.question_radio2);
		radio3 = (ImageView) boxLayout.findViewById(R.id.question_radio3);
		radio4 = (ImageView) boxLayout.findViewById(R.id.question_radio4);
		
		//radio1.setOnClickListener(new RadioOnClickListener());
		//radio2.setOnClickListener(new RadioOnClickListener());
		//radio3.setOnClickListener(new RadioOnClickListener());
		//radio4.setOnClickListener(new RadioOnClickListener());
		
		layout1 = (LinearLayout) boxLayout.findViewById(R.id.question_answer1_layout);
		layout2 = (LinearLayout) boxLayout.findViewById(R.id.question_answer2_layout);
		layout3 = (LinearLayout) boxLayout.findViewById(R.id.question_answer3_layout);
		layout4 = (LinearLayout) boxLayout.findViewById(R.id.question_answer4_layout);
		
		layout1.setOnClickListener(new RadioOnClickListener());
		layout2.setOnClickListener(new RadioOnClickListener());
		layout3.setOnClickListener(new RadioOnClickListener());
		layout4.setOnClickListener(new RadioOnClickListener());
		
		
	}
	

	
	/** Initialize the dialog */
	public void initialize() {
		//RelativeLayout.LayoutParams boxParam = (RelativeLayout.LayoutParams) boxLayout.getLayoutParams();
		LayoutParams boxParam = (LayoutParams) boxLayout
				.getLayoutParams();
		boxParam.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
		boxParam.width = LayoutParams.MATCH_PARENT;
		boxParam.height = LayoutParams.MATCH_PARENT;
		
		
	}
	
	/** show the dialog */
	public void show(int type) {
			if(System.currentTimeMillis() - last_visit > 10*60*1000){
				change = true;
			}
			if(System.currentTimeMillis() - last_visit2 > 10*60*1000){
				change2 = true;
			}
		
			if(type == 1){
				if(change2){
					change2 = false;
					selection = settingQuestion2();
					last_visit2 = System.currentTimeMillis();
				}
			}
			else{
				if(change){
					change = false;
					selection = settingQuestion();
					last_visit = System.currentTimeMillis();
				}
			}
			
		selectedAnswer = "" ;
		tv_answer1.setText(selection[0]);
		tv_answer2.setText(selection[1]);
		tv_answer3.setText(selection[2]);
		tv_answer4.setText(selection[3]);
			
		tv_question.setText(question);

		/** [LILY] EnableTabAndClick remove **/
//		MainActivity.getMainActivity().enableTabAndClick(false);
		boxLayout.setVisibility(View.VISIBLE);

	}
	
	public void show2(int type) {
		if(System.currentTimeMillis() - last_visit > 10*60*1000){
			change = true;
		}
		if(System.currentTimeMillis() - last_visit2 > 10*60*1000){
			change2 = true;
		}
	
		if(type == 1){
			if(change2){
				change2 = false;
				selection2 = settingQuestion2();
				last_visit2 = System.currentTimeMillis();
			}
		}
		else{
			if(change){
				change = false;
				selection = settingQuestion();
				last_visit = System.currentTimeMillis();
			}
		}
		
		iv_type.setVisibility(View.VISIBLE);
	selectedAnswer = "" ;
	tv_answer1.setText(selection2[0]);
	tv_answer2.setText(selection2[1]);
	tv_answer3.setText(selection2[2]);
	tv_answer4.setText(selection2[3]);
		
	tv_question.setText(question2);

	/** [LILY] Remove EnableTabAndClick **/
//	MainActivity.getMainActivity().enableTabAndClick(false);
	boxLayout.setVisibility(View.VISIBLE);

}
	
	/** remove the dialog and release the resources */
	public void clear() {
		if (mainLayout != null && boxLayout != null
				&& boxLayout.getParent() != null
				&& boxLayout.getParent().equals(mainLayout))
			mainLayout.removeView(boxLayout);
	}
	
	public Runnable ableClickQuestion = new Runnable(){
		public void run() {
			AnalysisCounterView.QuizButton.setEnabled(true);
		}
	};
	
	/** close the dialog */
	public void close() {
		resetAllImage();
		if (boxLayout != null)
			boxLayout.setVisibility(View.INVISIBLE);
	}
	
	private String[] settingQuestion2() {
		
		questionType = 1;
		
		String[] questions = null;
		String[] answers = null;
		Resources r = KetdiaryApplication.getContext().getResources();
		
		Random rand = new Random();
		int qid = rand.nextInt(8);
		question2 = r.getString(R.string.question_type);
		answers = r.getStringArray(R.array.trigger_list);
		
		answer2 = new String(answers[qid]);
		iv_type.setImageResource(iconId[qid]);
		iv_type.setVisibility(View.VISIBLE);
		
		String[] tempSelection = new String[4];
		for (int i = 0; i < tempSelection.length; ++i){	
			int index = qid+i;
			if(index>= 8)
				index%=8;
		
			tempSelection[i] = answers[index];
		}
		shuffleArray(tempSelection);
		String[] selectAns = new String[4];
		for (int i = 0; i < selectAns.length; ++i)
			selectAns[i] = tempSelection[i];
		
		//int ans_id = rand.nextInt(selectAns.length); //把隨機一個選項換成答案
		//selectAns[ans_id] = answer;

		return selectAns;
	}
	
	
	private String[] settingQuestion() {
		String[] questions = null;
		String[] answers = null;
		Resources r = KetdiaryApplication.getContext().getResources();
		
		questions = r.getStringArray(R.array.question_1);
		answers = r.getStringArray(R.array.question_answer_1);
		Random rand = new Random();
		int qid = rand.nextInt(questions.length);
		question = questions[qid];
		answer = new String(answers[qid * 4]);
		

		String[] tempSelection = new String[4];
		for (int i = 0; i < tempSelection.length; ++i)
			tempSelection[i] = answers[qid * 4 + i];
		shuffleArray(tempSelection);
		String[] selectAns = new String[4];
		for (int i = 0; i < selectAns.length; ++i)
			selectAns[i] = tempSelection[i];


		// swap option with string "以上皆是" "以上皆非" to the last option
		for (int i = 0; i < selectAns.length; ++i)
			if (selectAns[i].equals("以上皆是")) {
				selectAns[i] = selectAns[selectAns.length - 1];
				selectAns[selectAns.length - 1] = "以上皆是";
				break;
			}
			else if ((selectAns[i]).equals("以上皆非")) {
				selectAns[i] = selectAns[selectAns.length - 1];
				selectAns[selectAns.length - 1] = "以上皆非";
				break;
			}

		
		//int ans_id = rand.nextInt(selectAns.length); //把隨機一個選項換成答案
		//selectAns[ans_id] = answer;

		return selectAns;
	}

	private static void shuffleArray(String[] ar) {
		Random rnd = new Random();
		for (int i = ar.length - 1; i > 0; --i) {
			int index = rnd.nextInt(i + 1);
			String a = ar[index];
			ar[index] = ar[i];
			ar[i] = a;
		}
	}

	private class ConfirmOnClickListener implements View.OnClickListener {

		@Override
		/**Cancel and dismiss the check check dialog*/
		public void onClick(View v) {
//			ClickLog.Log(ClickLogId.STATISTIC_QUESTIONTEST_CONFIRM);
			
			long ts = System.currentTimeMillis();
			int isCorrect = 0;
			String selection = selectedAnswer;
			int choose = select;
			if(selectedAnswer.equals(answer)){
				isCorrect = 1;
			}
			else{
				
			}

			QuestionTest questionTest = new QuestionTest(ts, questionType, isCorrect, selection, choose, 0);
			int addScore = db.insertQuestionTest(questionTest);
			
			
			if(isCorrect == 0){
				CustomToast.generateToast(R.string.question_wrong, -1);
			}
			else{//答對換題目
				CustomToast.generateToast(R.string.question_correct, addScore);
				change = true;
				if(addScore > 0) {
					AddScore nowScore = new AddScore(System.currentTimeMillis(), addScore, 0, "答對題目", 0, AddScore.QUESTION);
					db.insertAddScore(nowScore);
				}
			}
			
			PreferenceControl.setPoint(addScore);
			
			if(questionType == 1){
				PreferenceControl.setRandomQustion(false);
				quizCaller.QuizDone();
				if (PreferenceControl.checkCouponChange())
					PreferenceControl.setCouponChange(true);
			}

			AnalysisCounterView.getAnalysisCounterView().updateCounter();

			/** [LILY] Remove enable tab and click **/
//			MainActivity.getMainActivity().enableTabAndClick(true);
			//mHandler.postDelayed(ableClickQuestion, CustomToast.getDuration());
			mHandler.postDelayed(ableClickQuestion, 2000);
			close();
			//clear();	
		}
	}


	private class CancelOnClickListener implements View.OnClickListener {
		@Override
		/**Calling out*/
		public void onClick(View v) {
//			ClickLog.Log(ClickLogId.STATISTIC_QUESTIONTEST_CANCEL);

			/** [LILY] disable enable tab and click **/
//			MainActivity.getMainActivity().enableTabAndClick(true);
			AnalysisCounterView.QuizButton.setEnabled(true);
        	close();
			//clear();
		}
	}
	
	/** OnClickListener for checking out */
	private class RadioOnClickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			resetAllImage();
			switch (v.getId()){
			case R.id.question_answer1_layout:
//				ClickLog.Log(ClickLogId.STATISTIC_QUESTIONTEST_SELECT_A);
				radio1.setImageResource(R.drawable.radio_node_select);
				selectedAnswer = selection[0];
				select = 0;
				break;
			case R.id.question_answer2_layout:
//				ClickLog.Log(ClickLogId.STATISTIC_QUESTIONTEST_SELECT_B);
				radio2.setImageResource(R.drawable.radio_node_select);
				selectedAnswer = selection[1];
				select = 1;
				break;
			case R.id.question_answer3_layout:
//				ClickLog.Log(ClickLogId.STATISTIC_QUESTIONTEST_SELECT_C);
				radio3.setImageResource(R.drawable.radio_node_select);
				selectedAnswer = selection[2];
				select = 2;
				break;
			case R.id.question_answer4_layout:
//				ClickLog.Log(ClickLogId.STATISTIC_QUESTIONTEST_SELECT_D);
				radio4.setImageResource(R.drawable.radio_node_select);
				selectedAnswer = selection[3];
				select = 3;
				break;
			/*
			default:				
				((ImageView)v).setImageResource(R.drawable.radio_node_select);
				break;
				*/				
			}
	
		}
	}
	
	private void resetAllImage(){
		radio1.setImageResource(R.drawable.radio_node);
		radio2.setImageResource(R.drawable.radio_node);
		radio3.setImageResource(R.drawable.radio_node);
		radio4.setImageResource(R.drawable.radio_node);
	}
	
}
