package ubicomp.ketdiary.utility.data.structure;

import java.util.Calendar;

public class IdentityScore {
	public Calendar eventTime;
	public Calendar createTime;
	public int isReflection;
	public int score;

	public IdentityScore(Calendar createTime, int score, Calendar eventTime, int isReflection){
		this.createTime = Calendar.getInstance();
		this.eventTime = Calendar.getInstance();

		this.score = score;
		this.createTime = createTime;
		this.eventTime = eventTime;
		this.isReflection = isReflection;
	}

	public Calendar getCreateTime() {
		return createTime;
	}

	public int getScore() {
		return score;
	}

	public Calendar getEventTime() {
		return eventTime;
	}

	public int getIsReflection() {
		return isReflection;
	}


}
