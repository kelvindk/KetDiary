package ubicomp.rehabdiary.utility.system.clicklog;

public class ClickLogId {
	/*
	 * Format [page] [item]
	 */


	/*
	*  Page1 - saliva test.
	* */
	public static final int PAGE1_SELECT = 1;
	public static final int PAGE1_START_BUTTON = 0101;
	public static final int PAGE1_IDENTITY_QUESTION_CONFIRM = 0102;
	public static final int PAGE1_IDENTITY_QUESTION_CANCEL = 0103;
	public static final int PAGE1_CONFIG_BUTTON = 0104;
	public static final int PAGE1_COPING_TOPS_SWEEP = 0105;



	/*
	*  Page2 - statistics.
	* */
	public static final int PAGE2_SELECT = 2;
	public static final int PAGE2_QUESTION = 0201;
	public static final int PAGE2_QUESTION_CONFIRM = 0202;
	public static final int PAGE2_QUESTION_CANCEL = 0203;
	public static final int PAGE2_DAY_VIEW = 0204;
	public static final int PAGE2_WEEK_VIEW = 0205;
	public static final int PAGE2_MONTH_VIEW = 0206;
	public static final int PAGE2_COPING_BUTTON = 0207;


	/*
	*  Page3 - event.
	* */
	public static final int PAGE3_SELECT = 3;
	public static final int PAGE3_ALARM_BUTTON = 301;
	public static final int PAGE3_NEW_BUTTON = 302;
	public static final int PAGE3_EVENT_LIST_CLICK = 303;

	/*
	* Event content page.
	* */
	public static final int PAGE3_EVENT_CONTENT_EDIT_BUTTON = 304;
	public static final int PAGE3_EVENT_CONTENT_ITEM_BUTTON = 305;

	/*
	*  Page4 - ranking.
	* */
	public static final int PAGE4_SELECT = 4;
	public static final int PAGE4_RANKING_LIST_CLICK = 0401;
	public static final int PAGE4_BACK_PRESS = 0402;

	/*
	*  HELP SETTING
	* */
	public static final int HELP_SELECT = 5;
	public static final int HELP_ABOUT = 501;
	public static final int HELP_SETTING = 502;
	public static final int SETTING_RECREATION = 503;
	public static final int SETTING_CONTACT = 504;
	public static final int SETTING_DEVICE_ID = 505;
	public static final int SETTING_PASSWORD = 506;
	public static final int SETTING_EDIT_RECREATION = 507;
	public static final int SETTING_EDIT_CONTACT = 508;
	public static final int SETTING_EDIT_DEVICE_ID = 509;
	public static final int SETTING_PASSWORD_ABLE = 510;
	public static final int SETTING_PASSWORD_ENABLE = 511;
	public static final int SETTING_PASSWORD_CHANGE = 512;

	/*
	*  Create new event.
	* */
	public static final int NEW_EVENT_NEXT_BUTTON = 601;
	public static final int NEW_EVENT_PREVIOUS_BUTTON = 602;
	public static final int NEW_EVENT_SAVE_BUTTON = 603;
	public static final int NEW_EVENT_CANCEL_CONFIRM_BUTTON = 604;

    /*
	*  Activity onStart & onStop.
	* */
    public static final int MAIN_ACTIVITY_ON_START = 701;
    public static final int MAIN_ACTIVITY_ON_STOP = 702;
    public static final int RANKING_CONTENT_ACTIVITY_ON_START = 703;
    public static final int RANKING_CONTENT_ACTIVITY_ON_STOP = 704;
    public static final int EVENT_CONTENT_ACTIVITY_ON_START = 705;
    public static final int EVENT_CONTENT_ACTIVITY_ON_STOP = 706;
    public static final int CREATE_EVENT_ACTIVITY_ON_START = 707;
    public static final int CREATE_EVENT_ACTIVITY_ON_STOP = 708;
    public static final int HELP_ACTIVITY_ON_START = 709;
    public static final int HELP_ACTIVITY_ON_STOP = 710;
    public static final int SETTING_ACTIVITY_ON_START = 711;
    public static final int SETTING_ACTIVITY_ON_STOP = 712;


}
