package ubicomp.ketdiary.utility.data.structure;

public class AddScore {
	private TimeValue tv;
	private int addScore;
	private int accumulation, weeklyAccumulation;
	private String reason;
	private int reasonBits;

	public static final int TEST_PASS = 	1;
	public static final int TEST_NO_PASS = 	2;
	public static final int NOTE = 			4;
	public static final int THINKING = 		8;
	public static final int REFLECTION = 	16;
	public static final int IDENTITY = 		32;
	public static final int COPYINGSKILL = 	64;
	public static final int QUESTION = 		128;
	public static final int VIEW_EVENT =    256;

	public AddScore(long ts, int addScore, int accumulation, String reason, int weeklyAccumulation, int reasonBits){
		this.tv = TimeValue.generate(ts);
		this.addScore = addScore;
		this.accumulation = accumulation;
		this.reason = reason;
		this.weeklyAccumulation = weeklyAccumulation;
		this.reasonBits = reasonBits;
	}

	public TimeValue getTv() {
		return tv;
	}

	public int getAddScore(){
		return addScore;
	}

	public int getAccumulation(){
		return accumulation;
	}

	public String getReason(){
		return reason;
	}

	public int getWeeklyAccumulation(){
		return weeklyAccumulation;
	}

	public void setAccumulation(int v){
		accumulation = v;
	}

	public void setWeeklyAccumulation(int v){
		weeklyAccumulation = v;
	}

	public void setReason(String s){
		reason = s;
	}

	public int getReasonBits(){
		return reasonBits;
	}

	public static boolean isTestPass(int n)
	{
		return ((n & TEST_PASS) > 0);
	}

	public static boolean isTestNoPass(int n)
	{
		return ((n & TEST_NO_PASS) > 0);
	}

	public static boolean isNote(int n)
	{
		return ((n & NOTE) > 0);
	}

	public static boolean isThinking(int n)
	{
		return ((n & THINKING) > 0);
	}

	public static boolean isReflection(int n)
	{
		return ((n & REFLECTION) > 0);
	}

	public static boolean isIdentity(int n)
	{
		return ((n & IDENTITY) > 0);
	}

	public static boolean isCopySkill(int n)
	{
		return ((n & COPYINGSKILL) > 0);
	}

	public static boolean isQuestion(int n)
	{
		return ((n & QUESTION) > 0);
	}

}
