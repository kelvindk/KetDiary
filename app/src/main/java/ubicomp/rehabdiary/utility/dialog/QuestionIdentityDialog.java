package ubicomp.rehabdiary.utility.dialog;

import java.sql.Date;
import java.util.Calendar;
import java.util.Random;


import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SeekBar;

import ubicomp.rehabdiary.R;
import ubicomp.rehabdiary.fragments.event.EventLogStructure;
import ubicomp.rehabdiary.main_activity.KetdiaryApplication;
import ubicomp.rehabdiary.utility.back_door.Typefaces;
import ubicomp.rehabdiary.utility.data.db.AddScoreDataBase;
import ubicomp.rehabdiary.utility.data.db.DatabaseControl;
import ubicomp.rehabdiary.utility.data.structure.AddScore;
import ubicomp.rehabdiary.utility.data.structure.IdentityScore;
import ubicomp.rehabdiary.utility.system.clicklog.ClickLog;
import ubicomp.rehabdiary.utility.system.clicklog.ClickLogId;

public class QuestionIdentityDialog {
	private Activity activity;
	private QuestionIdentityDialog noteFragment = this;
	private static final String TAG = "ADD_PAGE";

	private Context context;
	private LayoutInflater inflater;
	private RelativeLayout boxLayout = null;
	private LinearLayout questionLayout;

	private RelativeLayout mainLayout;
	private String[] type_str;
	
	private TextView tv_event, tv_feeling, tv_thinking, tv_confirm, tv_cancel;
	private ImageView radio1, radio2, radio3, radio4;
	private static ImageView iv_type;
	private SeekBar bar;
	
	private LinearLayout layout1, layout2, layout3, layout4;

	private Typeface wordTypeface, wordTypefaceBold, digitTypeface,
			digitTypefaceBold;

	private DatabaseControl db;
	private AddScoreDataBase addScoreDataBase;

	private long createTime = 0;
	private boolean isReflection = false;
	private int score = 0;
	private boolean showed;
	
	public QuestionIdentityDialog(RelativeLayout mainLayout){
		
		this.context = KetdiaryApplication.getContext();
		this.inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mainLayout = mainLayout;
		
		db = new DatabaseControl();
		addScoreDataBase = new AddScoreDataBase();
		
		wordTypeface = Typefaces.getWordTypeface();
		wordTypefaceBold = Typefaces.getWordTypefaceBold();
		digitTypeface = Typefaces.getDigitTypeface();
		digitTypefaceBold = Typefaces.getDigitTypefaceBold();
		
		type_str = context.getResources().getStringArray(R.array.trigger_list);

	}
	
	protected void setting() {
		
		boxLayout = (RelativeLayout) inflater.inflate(
				R.layout.dialog_answer_identity, null);
		boxLayout.setVisibility(View.INVISIBLE);
		
		tv_event = (TextView) boxLayout.findViewById(R.id.question_event);
		tv_feeling = (TextView) boxLayout.findViewById(R.id.question_feeling);
		tv_thinking = (TextView) boxLayout.findViewById(R.id.question_thinking);
		bar = (SeekBar) boxLayout.findViewById(R.id.question_seek_bar);
		
		tv_confirm = (TextView) boxLayout.findViewById(R.id.question_identity_confirm);
		tv_cancel = (TextView)boxLayout.findViewById(R.id.question_identity_cancel);
		
		tv_confirm.setOnClickListener(new ConfirmOnClickListener());
		tv_cancel.setOnClickListener(new CancelOnClickListener());

		settingQuestion();
	}
	

	
	/** Initialize the dialog */
	public void initialize() {
		setting();
		mainLayout.addView(boxLayout);

		//RelativeLayout.LayoutParams boxParam = (RelativeLayout.LayoutParams) boxLayout.getLayoutParams();
		RelativeLayout.LayoutParams boxParam = (LayoutParams) boxLayout
				.getLayoutParams();
		boxParam.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
		boxParam.width = LayoutParams.MATCH_PARENT;
		boxParam.height = LayoutParams.MATCH_PARENT;
	}
	
	/** show the dialog */
	public void show() {
		if(showed)
			boxLayout.setVisibility(View.VISIBLE);
	}
	
	/** remove the dialog and release the resources */
	public void clear() {
		if (mainLayout != null && boxLayout != null
				&& boxLayout.getParent() != null
				&& boxLayout.getParent().equals(mainLayout))
			mainLayout.removeView(boxLayout);
	}
	
	/** close the dialog */
	public void close() {
		if (boxLayout != null)
			boxLayout.setVisibility(View.INVISIBLE);
	}


	private class ConfirmOnClickListener implements View.OnClickListener {
		@Override
		/**Cancel and dismiss the check check dialog*/
		public void onClick(View v) {
			ClickLog.Log(ClickLogId.PAGE1_IDENTITY_QUESTION_CONFIRM);

			close();
			sendAnswer();
			addScoreDataBase.addScoreIdentit();

		}

	}


	private class CancelOnClickListener implements View.OnClickListener {
		@Override
		/**Calling out*/
		public void onClick(View v) {
			ClickLog.Log(ClickLogId.PAGE1_IDENTITY_QUESTION_CANCEL);
			close();
		}
	}

	public void settingQuestion() {
		showed = true;
		EventLogStructure[] data = db.getAllEventLog();

		if(data == null)
		{
			showed = false;
			return;
		}
		int len = data.length;
		if(len == 0)
			return;
		Random ran;
		ran = new Random();

		int index = ran.nextInt(len);
		isReflection = ran.nextInt(2) > 0;
		createTime = data[index].createTime.getTimeInMillis();

		SpannableString scenarior = new SpannableString("當"+data[index].scenario+"時，");
		int len_scenarior = scenarior.length();

		scenarior.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.question_identity)),
				1, len_scenarior - 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

		tv_event.setText(scenarior);
		if(!isReflection)
		{
			tv_feeling.setText(data[index].originalThought);
			tv_thinking.setText(data[index].originalBehavior);
		}
		else {
			tv_feeling.setText(data[index].expectedThought);
			tv_thinking.setText(data[index].expectedBehavior);
		}
	}

	private void sendAnswer() {
		long ts = System.currentTimeMillis();
		score = bar.getProgress();

		Calendar createCal = Calendar.getInstance();
		createCal.setTimeInMillis(createTime);
		Calendar eventCal = Calendar.getInstance();
		eventCal.setTimeInMillis(ts);

		IdentityScore data = new IdentityScore(eventCal, score, createCal, isReflection?1:0);
		db.insertIdentityScore(data);
	}

}
