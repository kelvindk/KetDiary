package ubicomp.ketdiary.utility.daybook;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;

import ubicomp.ketdiary.R;
import ubicomp.ketdiary.main_activity.KetdiaryApplication;
import ubicomp.ketdiary.utility.data.db.DatabaseControl;
import ubicomp.ketdiary.utility.data.structure.NoteAdd;
import ubicomp.ketdiary.utility.data.structure.TestResult;
import ubicomp.ketdiary.utility.data.structure.TimeValue;
import ubicomp.ketdiary.utility.statistic.Typefaces;
import ubicomp.ketdiary.utility.system.PreferenceControl;

public class SectionsPagerAdapter extends PagerAdapter {
	
	private static final String TAG = "Calendar";
	
    private View[] pageViewList;
    public static final int TAG_PAGE_YEAR = R.string.TAG_PAGE_YEAR;
    public static final int TAG_PAGE_MONTH = R.string.TAG_PAGE_MONTH;
    private GridLayout[] glCalendar;
    private Calendar mCalendar;
    private static final int THIS_DAY = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    private static final int THIS_MONTH = Calendar.getInstance().get(Calendar.MONTH);
    private static final int THIS_YEAR = Calendar.getInstance().get(Calendar.YEAR);
    
    private static View thisDayView;
    private static View selectedView;
    public static final int TAG_CAL_CELL_DAY = R.string.TAG_CAL_CELL_DAY;
    public static final int TAG_CAL_CELL_MONTH = R.string.TAG_CAL_CELL_MONTH;
    public static final int TAG_CAL_CELL_PAGE_MONTH = R.string.TAG_CAL_CELL_PAGE_MONTH;
    public static final int TAG_CAL_CELL_YEAR = R.string.TAG_CAL_CELL_YEAR;
    public static final int TAG_CAL_CELL_TS = R.string.TAG_CAL_CELL_TS;
    public static final int TAG_changedot = -1;
    public static final int TAG_ADDNOTE = R.string.TAG_CAL_CELL_NOTEADD;
    		
    //private static final Calendar startDay = PreferenceControl.getStartDate();
    //private static final Calendar startDay = PreferenceControl.getStartDateMinus();
    private static final Calendar startDay = PreferenceControl.getFirstUsedDateMinus();
    private static final Calendar today = Calendar.getInstance();
    
    private Context context;

    private LayoutInflater inflater;
    private DatabaseControl db;
    private int sustainMonth;

    private boolean[] isPageViewInitialized ;//= new boolean[Database.SUSTAINED_MONTHS];
    
    private static final int[] dotId2 = { 0, R.drawable.dot_color1, R.drawable.dot_color2,
    	R.drawable.dot_color3, R.drawable.dot_color4, R.drawable.dot_color5,
    	R.drawable.dot_color6, R.drawable.dot_color7, R.drawable.dot_color8
    };
    
    private static final int[] dotId = { 0, R.drawable.filter_color1, R.drawable.filter_color2,
    	R.drawable.filter_color3, R.drawable.filter_color4, R.drawable.filter_color5,
    	R.drawable.filter_color6, R.drawable.filter_color7, R.drawable.filter_color8
    };
    
    
    //original dot
    private static final int[] dotId3 = { 0, R.drawable.cell_dot1, R.drawable.cell_dot2,
    	R.drawable.cell_dot3, R.drawable.cell_dot4, R.drawable.cell_dot5,
    	R.drawable.cell_dot6, R.drawable.cell_dot7, R.drawable.cell_dot8
    };

    public SectionsPagerAdapter(View[] pageViewList){
        this.pageViewList = pageViewList;
        context = KetdiaryApplication.getContext();
        inflater = LayoutInflater.from(KetdiaryApplication.getContext());
        
        db = new DatabaseControl();
        sustainMonth = PreferenceControl.getSustainMonth();
        isPageViewInitialized = new boolean[pageViewList.length];
        glCalendar = new GridLayout[pageViewList.length];
    }
	
	@Override
	public int getCount() {
		return pageViewList.length;
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
        if(isPageViewInitialized[position] != true){
            initPageView(pageViewList[position], position);
            isPageViewInitialized[position] = true;
        }
        container.addView(pageViewList[position]);
		return pageViewList[position];
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(pageViewList[position]);
	}
	
	@Override
	public boolean isViewFromObject(View v, Object o) {
		return v == o;
	}
	
	public float getDensity(){
		 DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		 return metrics.density;
		}
	
	public float convertDpToPixel(float dp){
	    float px = dp * getDensity();
	    //Log.d(TAG, "density:" + getDensity());
	    return px;
	}
	
    private void initPageView(View pageView, int position){

        int pageViewYear = Integer.valueOf((pageView.getTag(TAG_PAGE_YEAR)).toString());
        int pageViewMonth = Integer.valueOf((pageView.getTag(TAG_PAGE_MONTH)).toString());
        glCalendar[position] = (GridLayout) pageView.findViewById(R.id.gl_calendar);
        
        // Initialize the calendar
        Calendar mCalendar = Calendar.getInstance();
        int maxDaysOfWeek = mCalendar.getMaximum(Calendar.DAY_OF_WEEK);
        mCalendar.setFirstDayOfWeek(Calendar.MONDAY);

        mCalendar.set(pageViewYear, pageViewMonth, 1);
        int maxWeeksOfMonth = mCalendar.getActualMaximum(Calendar.WEEK_OF_MONTH);

        // Our first day of week is Monday, but "Calendar.SUNDAY" is still 1 not 7,
        // so we need to deal with the special situation that first day of month is Sunday.
        if(mCalendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)  // If the first day of the month is Sunday
            mCalendar.add(Calendar.DAY_OF_MONTH, -6);
        else
            mCalendar.add(Calendar.DAY_OF_MONTH, -(mCalendar.get(Calendar.DAY_OF_WEEK)-2));
        TimeValue tv = TimeValue.generate(mCalendar.getTimeInMillis());
        
        View cellView;
        TextView calDateText;
        ImageView calDot1, calDot2, calDot3, date_result, calDot15,calDot25;
        int result = -1;
        NoteAdd[] noteAdds;
        TestResult testResult=null;

        for (int i=0;i<maxWeeksOfMonth*maxDaysOfWeek;++i){
            
            cellView = inflater.inflate(R.layout.calendar_cell, null, false);

            // Set the invisible days(dummy days before 1st day of that month)
            if (mCalendar.get(Calendar.MONTH) != pageViewMonth){
                glCalendar[position].addView(cellView);

                cellView.setVisibility(View.INVISIBLE);
                mCalendar.add(Calendar.DAY_OF_MONTH, 1);
                
                LayoutParams params = cellView.getLayoutParams();
                params.width = (int)convertDpToPixel((float)39.33);
                params.height =(int)convertDpToPixel((float)30);
                cellView.setLayoutParams(params);
                continue;
            }
            
            calDateText = (TextView) cellView.findViewById(R.id.tv_calendar_date);
            date_result = (ImageView) cellView.findViewById(R.id.iv_date_result);

            
            cellView.setTag(TAG_CAL_CELL_DAY, mCalendar.get(Calendar.DAY_OF_MONTH));
            cellView.setTag(TAG_CAL_CELL_MONTH, mCalendar.get(Calendar.MONTH));
            cellView.setTag(TAG_CAL_CELL_PAGE_MONTH, pageViewMonth);
            cellView.setTag(TAG_CAL_CELL_YEAR, mCalendar.get(Calendar.YEAR));
            cellView.setTag(TAG_CAL_CELL_TS, mCalendar.getTimeInMillis());
            
            calDateText.setGravity(Gravity.CENTER);
            
            calDateText.setText(mCalendar.get(Calendar.DAY_OF_MONTH) + "");
            calDateText.setTextColor(context.getResources().getColor(R.color.text_gray2));
            calDateText.setTypeface(Typefaces.getDigitTypefaceBold());
            
            
            if ( mCalendar.get(Calendar.MONTH) == pageViewMonth){

            	if(mCalendar.getTimeInMillis()> startDay.getTimeInMillis() && mCalendar.getTimeInMillis() <= today.getTimeInMillis()+86400000){

            		//noteAdds = db.getDayNoteAdd(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));
            		//cellView.setTag(TAG_ADDNOTE, noteAdds);
            		
            		//if(noteAdds!= null)
            		//	updateNoteAdd(cellView, noteAdds);
            		
            		

            		testResult = db.getDayTestResult( mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH) );
            		result = -1;
            		if(testResult.getTv().getTimestamp() != 0){
            			result = testResult.getResult();
            			if(result == 0)
            				date_result.setImageResource(R.drawable.bigbluedot);
            				//cellView.setTag(TAG_changedot,result);}
            			else if(result == 1){
            				date_result.setImageResource(R.drawable.bigreddot);
            				//cellView.setTag(TAG_changedot,result);
            			}
            		}
            		else{
            			//cellView.setTag(TAG_changedot,result);
            			date_result.setImageResource(R.drawable.biggraydot);
            		}
            		cellView.setTag(TAG_changedot,result);
            		calDateText.setTextColor(context.getResources().getColor(R.color.white));
            	}
            	else if(mCalendar.getTimeInMillis() < startDay.getTimeInMillis()){
                    // If current day is before the start day
            		calDateText.setTextColor(context.getResources().getColor(R.color.date_before_gray));
                    cellView.setOnClickListener(null);
                }
            	
            	else{
                    // If current day is a future day
            		calDateText.setTextColor(context.getResources().getColor(R.color.text_gray2));
                    cellView.setOnClickListener(null);
            	}
            }
            
            // Initialize the selected view on current day
            if (mCalendar.get(Calendar.DAY_OF_MONTH) == THIS_DAY && mCalendar.get(Calendar.MONTH) == THIS_MONTH && mCalendar.get(Calendar.YEAR) == THIS_YEAR) {
                thisDayView = cellView;
                if (selectedView == null){
                    selectedView = thisDayView;
                }
            }
            
            // Highlight the selected day
            int selectedDay, selectedMonth, selectedYear;
            if (selectedView != null){
                selectedDay = Integer.valueOf(selectedView.getTag(TAG_CAL_CELL_DAY).toString());
                selectedMonth = Integer.valueOf(selectedView.getTag(TAG_CAL_CELL_MONTH).toString());
                selectedYear = Integer.valueOf(selectedView.getTag(TAG_CAL_CELL_YEAR).toString());

                if (mCalendar.get(Calendar.DAY_OF_MONTH) == selectedDay && mCalendar.get(Calendar.MONTH) == selectedMonth && mCalendar.get(Calendar.YEAR) == selectedYear) {
                    selectedView = cellView;
                    calDateText.setTextColor(context.getResources().getColor(R.color.black));
                    calDateText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                    
                    ImageView selectedDayIV = (ImageView) selectedView.findViewById(R.id.iv_date_result);
                    if(result==0)
                    	selectedDayIV.setImageResource(R.drawable.bigbluedot2);
                    else if (result == 1)
                    	selectedDayIV.setImageResource(R.drawable.bigreddot2);
                    else
                    	selectedDayIV.setImageResource(R.drawable.biggraydot2);
                }
            }
            
            glCalendar[position].addView(cellView);
            mCalendar.add(Calendar.DAY_OF_MONTH, 1);
            
            LayoutParams params = cellView.getLayoutParams();
//            params.width = (int)convertDpToPixel((float)39.33);
            params.width =(int)convertDpToPixel((float)48.33);
            params.height =(int)convertDpToPixel((float)30);
            //params.width=118;
            //params.height=130;
            cellView.setLayoutParams(params);
        }

    }

    public void updateCalendar(){
    	for(int ii = 0; ii < glCalendar.length; ii++){
    		if(glCalendar[ii] == null)
    			continue;
	    	for(int i = 0 ; i < glCalendar[ii].getChildCount(); i++){
	    		View cellView = glCalendar[ii].getChildAt(i);
	    		NoteAdd[] noteAdds = (NoteAdd[]) cellView.getTag(TAG_ADDNOTE);
	    		/*if(noteAdds != null){
	    			updateNoteAdd(cellView, noteAdds);
	    		}*/
	    	}
    	}
    }
    
    public void updateRecentDay(){
    	if(pageViewList.length < 1 || glCalendar[pageViewList.length-1] == null )
    		return;
    	int num = glCalendar[pageViewList.length-1].getChildCount();
    	for(int i = 0; i<num ; i++){
    		View cellView = glCalendar[pageViewList.length-1].getChildAt(i);
    		if(cellView.getVisibility() != View.INVISIBLE){
    			int year = (Integer) cellView.getTag(TAG_CAL_CELL_YEAR);
    			int month = (Integer) cellView.getTag(TAG_CAL_CELL_MONTH);
	    		int day = (Integer) cellView.getTag(TAG_CAL_CELL_DAY);
	    		if( day >= today.get(Calendar.DAY_OF_MONTH)-2 && day <= today.get(Calendar.DAY_OF_MONTH)){
	    			NoteAdd[] noteAdds = db.getDayNoteAdd(year, month, day);
	    			cellView.setTag(TAG_ADDNOTE, noteAdds);
	    			/*if(noteAdds!= null){
	    				updateNoteAdd(cellView, noteAdds);
	    			}*/
	    		}    		
	    		if( day > today.get(Calendar.DAY_OF_MONTH))
	    			break;
    		}
    	}
    }

    public View getSelectedView(){
        return selectedView;
    }
    public View getThisDayView(){
        return thisDayView;
    }
    public void asignSelecteViewToThisDayView(){
        selectedView = thisDayView;
    }

}
