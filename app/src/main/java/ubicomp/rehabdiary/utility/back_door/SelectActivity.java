package ubicomp.rehabdiary.utility.back_door;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ubicomp.rehabdiary.main_activity.KetdiaryApplication;
import ubicomp.rehabdiary.utility.data.db.DatabaseControl;
import ubicomp.rehabdiary.utility.data.structure.TriggerItem;
//import com.ubicomp.ketdiary.system.clicklog.ClickLogId;
//import ubicomp.ketdiary.ui.BarButtonGenerator;
//import com.ubicomp.ketdiary.ui.Typefaces;
//import ubicomp.ketdiary.ui.spinnergroup.MultiRadioGroup;
import ubicomp.rehabdiary.R;

/**
 * Activity for Selection Button
 * 
 * @author Andy Chen
 */
public class SelectActivity extends Activity {

	private LayoutInflater inflater;

	private Typeface wordTypefaceBold;

	private LinearLayout titleLayout;
	private LinearLayout mainLayout;
	private MultiRadioGroup noteGroup1;
	private MultiRadioGroup noteGroup2;
	private MultiRadioGroup noteGroup3;
	private MultiRadioGroup noteGroup4;
	private MultiRadioGroup noteGroup5;
	private MultiRadioGroup noteGroup6;
	private MultiRadioGroup noteGroup7;
	private MultiRadioGroup noteGroup8;
	private MultiRadioGroup noteGroupMood;
	
	private boolean[] result1;
	private boolean[] result2;
	private boolean[] result3;
	private boolean[] result4;
	private boolean[] result5;
	private boolean[] result6;
	private boolean[] result7;
	private boolean[] result8;
	private boolean[] resultMood;
	
	private View type1View;	
	private View type2View;
	private View type3View;
	private View type4View;
	private View type5View;
	private View type6View;
	private View type7View;
	private View type8View;
	private View typeMoodView;
	
	private String[] Type1Content;
	private String[] Type2Content;
	private String[] Type3Content;
	private String[] Type4Content;
	private String[] Type5Content;
	private String[] Type6Content;
	private String[] Type7Content;
	private String[] Type8Content;
	private String[] TypeMoodContent;
	

	private Activity activity;
	private DatabaseControl db;
	private static final String[] RESULT = {"陰性", "陽性", ""};
	//private NoteCatagory3 noteCategory;
	private static final String[] TYPE_TITLE = KetdiaryApplication.getContext().getResources().getStringArray(R.array.trigger_list);
	
	public static final int NOTE_UPPER_BOUND = 10; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_coping);

		this.activity = this;
		titleLayout = (LinearLayout) this
				.findViewById(R.id.coping_title_layout);
		mainLayout = (LinearLayout) this.findViewById(R.id.coping_main_layout);
		inflater = LayoutInflater.from(activity);
		wordTypefaceBold = Typefaces.getWordTypefaceBold();

		mainLayout.removeAllViews();

		View title = BarButtonGenerator.createTitleView( getResources().getString(R.string.help_page));
		titleLayout.addView(title);
		
		db = new DatabaseControl();
		//noteCategory = new NoteCatagory3();
		

		setViews1();
		setViews2();
		setViews3();
		setViews4();
		setViews5();
		setViews6();
		setViews7();
		setViews8();
		
	}
	
	private void setViews1(){
		
		RelativeLayout titleView = createListView(
				TYPE_TITLE[0], new OnClickListener() {
	
					private boolean visible = false;	
						@Override
					public void onClick(View v) {	
							ImageView list = (ImageView) v
									.findViewById(R.id.question_list);
							if (visible) {
								type1View.setVisibility(View.GONE);
								list.setVisibility(View.INVISIBLE);
							} else {
								type1View.setVisibility(View.VISIBLE);
								list.setVisibility(View.VISIBLE);
							}
							visible = !visible;
						}
					});
		mainLayout.addView(titleView);

		TriggerItem[] data = db.getTypeTriggerALLItem(1);
		if(data == null)
			data = new TriggerItem[0];
		Type1Content = new String[data.length];
		boolean[] socialSelected = new boolean[data.length];
		for (int i = 0; i < Type1Content.length; i++)
		{
			Type1Content[i] = data[i].getContent();
			socialSelected[i] = data[i].getShow();
		}

		/*noteGroup1 = new MultiRadioGroup(activity, Type1Content,
					socialSelected, NOTE_UPPER_BOUND, R.string.setting_limit,
					ClickLogId.SETTING_SELECT + 0);*/
		noteGroup1 = new MultiRadioGroup(activity, Type1Content,
				socialSelected, NOTE_UPPER_BOUND, R.string.setting_limit,
				0);

		type1View = noteGroup1.getView();
		mainLayout.addView(type1View);
		type1View.setVisibility(View.GONE);
		
	}
	private void setViews2(){
		
		RelativeLayout titleView = createListView(
				TYPE_TITLE[1], new OnClickListener() {
	
					private boolean visible = false;	
						@Override
					public void onClick(View v) {	
							ImageView list = (ImageView) v
									.findViewById(R.id.question_list);
							if (visible) {
								type2View.setVisibility(View.GONE);
								list.setVisibility(View.INVISIBLE);
							} else {
								type2View.setVisibility(View.VISIBLE);
								list.setVisibility(View.VISIBLE);
							}
							visible = !visible;
						}
					});
		mainLayout.addView(titleView);
		TriggerItem[] data = db.getTypeTriggerALLItem(2);
		if(data == null)
			data = new TriggerItem[0];
		Type2Content = new String[data.length];
		boolean[] socialSelected = new boolean[data.length];
		for (int i = 0; i < Type2Content.length; i++)
		{
			Type2Content[i] = data[i].getContent();
			socialSelected[i] = data[i].getShow();
		}
			noteGroup2 = new MultiRadioGroup(activity, Type2Content,
					socialSelected, NOTE_UPPER_BOUND, R.string.setting_limit,
					0);
			type2View = noteGroup2.getView();
			mainLayout.addView(type2View);
			type2View.setVisibility(View.GONE);
		
	}
private void setViews3(){
	
	RelativeLayout titleView = createListView(
			TYPE_TITLE[2], new OnClickListener() {

				private boolean visible = false;	
					@Override
				public void onClick(View v) {	
						ImageView list = (ImageView) v
								.findViewById(R.id.question_list);
						if (visible) {
							type3View.setVisibility(View.GONE);
							list.setVisibility(View.INVISIBLE);
						} else {
							type3View.setVisibility(View.VISIBLE);
							list.setVisibility(View.VISIBLE);
						}
						visible = !visible;
					}
				});
	mainLayout.addView(titleView);
	TriggerItem[] data = db.getTypeTriggerALLItem(3);
	if(data == null)
		data = new TriggerItem[0];
	Type3Content = new String[data.length];
	boolean[] socialSelected = new boolean[data.length];
	for (int i = 0; i < Type3Content.length; i++)
	{
		Type3Content[i] = data[i].getContent();
		socialSelected[i] = data[i].getShow();
	}
		noteGroup3 = new MultiRadioGroup(activity, Type3Content,
				socialSelected, NOTE_UPPER_BOUND, R.string.setting_limit,
				0);
		type3View = noteGroup3.getView();
		mainLayout.addView(type3View);
		type3View.setVisibility(View.GONE);
	
}
private void setViews4(){
	
	RelativeLayout titleView = createListView(
			TYPE_TITLE[3], new OnClickListener() {

				private boolean visible = false;	
					@Override
				public void onClick(View v) {	
						ImageView list = (ImageView) v
								.findViewById(R.id.question_list);
						if (visible) {
							type4View.setVisibility(View.GONE);
							list.setVisibility(View.INVISIBLE);
						} else {
							type4View.setVisibility(View.VISIBLE);
							list.setVisibility(View.VISIBLE);
						}
						visible = !visible;
					}
				});
	mainLayout.addView(titleView);
	TriggerItem[] data = db.getTypeTriggerALLItem(4);
	if(data == null)
		data = new TriggerItem[0];
	Type4Content = new String[data.length];
	boolean[] socialSelected = new boolean[data.length];
	for (int i = 0; i < Type4Content.length; i++)
	{
		Type4Content[i] = data[i].getContent();
		socialSelected[i] = data[i].getShow();
	}
		noteGroup4 = new MultiRadioGroup(activity, Type4Content,
				socialSelected, NOTE_UPPER_BOUND, R.string.setting_limit,
				0);
		type4View = noteGroup4.getView();
		mainLayout.addView(type4View);
		type4View.setVisibility(View.GONE);
	
}
private void setViews5(){
	
	RelativeLayout titleView = createListView(
			TYPE_TITLE[4], new OnClickListener() {

				private boolean visible = false;	
					@Override
				public void onClick(View v) {	
						ImageView list = (ImageView) v
								.findViewById(R.id.question_list);
						if (visible) {
							type5View.setVisibility(View.GONE);
							list.setVisibility(View.INVISIBLE);
						} else {
							type5View.setVisibility(View.VISIBLE);
							list.setVisibility(View.VISIBLE);
						}
						visible = !visible;
					}
				});
	mainLayout.addView(titleView);
	TriggerItem[] data = db.getTypeTriggerALLItem(5);
	if(data == null)
		data = new TriggerItem[0];
	Type5Content = new String[data.length];
	boolean[] socialSelected = new boolean[data.length];
	for (int i = 0; i < Type5Content.length; i++)
	{
		Type5Content[i] = data[i].getContent();
		socialSelected[i] = data[i].getShow();
	}
		noteGroup5 = new MultiRadioGroup(activity, Type5Content,
				socialSelected, NOTE_UPPER_BOUND, R.string.setting_limit,
				0);
		type5View = noteGroup5.getView();
		mainLayout.addView(type5View);
		type5View.setVisibility(View.GONE);
	
}
private void setViews6(){
	
	RelativeLayout titleView = createListView(
			TYPE_TITLE[5], new OnClickListener() {

				private boolean visible = false;	
					@Override
				public void onClick(View v) {	
						ImageView list = (ImageView) v
								.findViewById(R.id.question_list);
						if (visible) {
							type6View.setVisibility(View.GONE);
							list.setVisibility(View.INVISIBLE);
						} else {
							type6View.setVisibility(View.VISIBLE);
							list.setVisibility(View.VISIBLE);
						}
						visible = !visible;
					}
				});
	mainLayout.addView(titleView);
	TriggerItem[] data = db.getTypeTriggerALLItem(6);
	if(data == null)
		data = new TriggerItem[0];
	Type6Content = new String[data.length];
	boolean[] socialSelected = new boolean[data.length];
	for (int i = 0; i < Type6Content.length; i++)
	{
		Type6Content[i] = data[i].getContent();
		socialSelected[i] = data[i].getShow();
	}
		noteGroup6 = new MultiRadioGroup(activity, Type6Content,
				socialSelected, NOTE_UPPER_BOUND, R.string.setting_limit,
				0);
		type6View = noteGroup6.getView();
		mainLayout.addView(type6View);
		type6View.setVisibility(View.GONE);
	
}
private void setViews7(){
	
	RelativeLayout titleView = createListView(
			TYPE_TITLE[6], new OnClickListener() {

				private boolean visible = false;	
					@Override
				public void onClick(View v) {	
						ImageView list = (ImageView) v
								.findViewById(R.id.question_list);
						if (visible) {
							type7View.setVisibility(View.GONE);
							list.setVisibility(View.INVISIBLE);
						} else {
							type7View.setVisibility(View.VISIBLE);
							list.setVisibility(View.VISIBLE);
						}
						visible = !visible;
					}
				});
	mainLayout.addView(titleView);
	TriggerItem[] data = db.getTypeTriggerALLItem(7);
	if(data == null)
		data = new TriggerItem[0];
	Type7Content = new String[data.length];
	boolean[] socialSelected = new boolean[data.length];
	for (int i = 0; i < Type7Content.length; i++)
	{
		Type7Content[i] = data[i].getContent();
		socialSelected[i] = data[i].getShow();
	}
		noteGroup7 = new MultiRadioGroup(activity, Type7Content,
				socialSelected, NOTE_UPPER_BOUND, R.string.setting_limit,
				0);
		type7View = noteGroup7.getView();
		mainLayout.addView(type7View);
		type7View.setVisibility(View.GONE);
	
}
private void setViews8(){
	
	RelativeLayout titleView = createListView(
			TYPE_TITLE[7], new OnClickListener() {

				private boolean visible = false;	
					@Override
				public void onClick(View v) {	
						ImageView list = (ImageView) v
								.findViewById(R.id.question_list);
						if (visible) {
							type8View.setVisibility(View.GONE);
							list.setVisibility(View.INVISIBLE);
						} else {
							type8View.setVisibility(View.VISIBLE);
							list.setVisibility(View.VISIBLE);
						}
						visible = !visible;
					}
				});
	mainLayout.addView(titleView);
	TriggerItem[] data = db.getTypeTriggerALLItem(8);
	if(data == null)
		data = new TriggerItem[0];
	Type8Content = new String[data.length];
	boolean[] socialSelected = new boolean[data.length];
	for (int i = 0; i < Type8Content.length; i++)
	{
		Type8Content[i] = data[i].getContent();
		socialSelected[i] = data[i].getShow();
	}
		noteGroup8 = new MultiRadioGroup(activity, Type8Content,
				socialSelected, NOTE_UPPER_BOUND, R.string.setting_limit,
				0);
		type8View = noteGroup8.getView();
		mainLayout.addView(type8View);
		type8View.setVisibility(View.GONE);
	
}

	
	

	/*private void setViews9(){
		
		TestResult[] testResult = db.getAllPrimeTestResult();
		
//		AlertDialog.Builder builder = new AlertDialog.Builder(this);
//		builder.setTitle("確定修改結果?");
//		//builder.setPositiveButton("確定", new ModifyListener());
//		builder.setNegativeButton("取消", null);
//		AlertDialog cleanAlertDialog = builder.create();
		
		for(int i=0; i<testResult.length; i++){
			
			int year = testResult[i].getTv().getYear();
			int month = testResult[i].getTv().getMonth();
			int day = testResult[i].getTv().getDay();
			final long ts = testResult[i].getTv().getTimestamp();
			final int result = testResult[i].getResult();
			if(result < 0 || result > 1 )
				continue;
			
			String text = year+"年"+(month+1)+"月"+day+"日"+" 結果: "+RESULT[result];
			
			
			
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle( year+"年"+(month+1)+"月"+day+"日\n"+"確定修改成 "+ RESULT[result^1]+"?");
//			builder.setPositiveButton("確定", new ModifyListener(ts, result)); 
//			builder.setNegativeButton("取消", null);
			builder.setNegativeButton("確定", new ModifyListener(ts, result)); 
			builder.setPositiveButton("取消", null);
			AlertDialog cleanAlertDialog = builder.create();	
			RelativeLayout aboutView = createListView(text,
					new AlertOnClickListener(cleanAlertDialog));
			mainLayout.addView(aboutView);		
		}
	}*/
	private class AlertOnClickListener implements OnClickListener {

		private AlertDialog alertDialog;

		public AlertOnClickListener(AlertDialog ad) {
			this.alertDialog = ad;
		}

		@Override
		public void onClick(View v) {
			alertDialog.show();
		}
	}
	
	private void updateView(){
		mainLayout.removeAllViews();
		//setViews();
	}
	
	private class ModifyListener implements
	DialogInterface.OnClickListener {
		long ts;
		int result;
		public ModifyListener(long ts, int result){
			this.ts = ts;
			this.result = result;
		}
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			db.modifyResultByTs(ts, (result ^ 1) );
			updateView();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
	
	private void storeType1(){
		if(noteGroup1 == null)
			return;
		boolean[] socialSelected = noteGroup1.getResult();
		for (int i = 0; i < socialSelected.length; i++)
			db.updateTriggerItem(Type1Content[i], socialSelected[i]);
	}
	
	private void storeType2(){
		if(noteGroup2 == null)
			return;
		boolean[] socialSelected = noteGroup2.getResult();
		for (int i = 0; i < socialSelected.length; i++)
			db.updateTriggerItem(Type2Content[i], socialSelected[i]);
	}
	private void storeType3(){
		if(noteGroup3 == null)
			return;
		boolean[] socialSelected = noteGroup3.getResult();
		for (int i = 0; i < socialSelected.length; i++)
			db.updateTriggerItem(Type3Content[i], socialSelected[i]);
	}
	private void storeType4(){
		if(noteGroup4 == null)
			return;
		boolean[] socialSelected = noteGroup4.getResult();
		for (int i = 0; i < socialSelected.length; i++)
			db.updateTriggerItem(Type4Content[i], socialSelected[i]);
	}
	private void storeType5(){
		if(noteGroup5 == null)
			return;
		boolean[] socialSelected = noteGroup5.getResult();
		for (int i = 0; i < socialSelected.length; i++)
			db.updateTriggerItem(Type5Content[i], socialSelected[i]);
	}
	private void storeType6(){
		if(noteGroup6 == null)
			return;
		boolean[] socialSelected = noteGroup6.getResult();
		for (int i = 0; i < socialSelected.length; i++)
			db.updateTriggerItem(Type6Content[i], socialSelected[i]);
	}
	private void storeType7(){
		if(noteGroup7 == null)
			return;
		boolean[] socialSelected = noteGroup7.getResult();
		for (int i = 0; i < socialSelected.length; i++)
			db.updateTriggerItem(Type7Content[i], socialSelected[i]);
	}
	private void storeType8(){
		if(noteGroup8 == null)
			return;
		boolean[] socialSelected = noteGroup8.getResult();
		for (int i = 0; i < socialSelected.length; i++)
			db.updateTriggerItem(Type8Content[i], socialSelected[i]);
	}
	


	@Override
	protected void onPause() {
		storeType1();
		storeType2();
		storeType3();
		storeType4();
		storeType5();
		storeType6();
		storeType7();
		storeType8();
		
		super.onPause();
	}

	private RelativeLayout createListView(String titleStr, OnClickListener listener) {

		RelativeLayout layout = (RelativeLayout) inflater.inflate(
				R.layout.bar_list_item, null);
		TextView text = (TextView) layout
				.findViewById(R.id.question_description);
		text.setTypeface(wordTypefaceBold);
		text.setText(titleStr);
		layout.setOnClickListener(listener);
		return layout;
	}
	private RelativeLayout createListView(int titleStr, OnClickListener listener) {

		RelativeLayout layout = (RelativeLayout) inflater.inflate(
				R.layout.bar_list_item, null);
		TextView text = (TextView) layout
				.findViewById(R.id.question_description);
		text.setTypeface(wordTypefaceBold);
		text.setText(titleStr);
		layout.setOnClickListener(listener);
		return layout;
	}


}
