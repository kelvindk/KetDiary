package ubicomp.ketdiary.system.check;

import ubicomp.ketdiary.system.PreferenceControl;

import java.util.Calendar;

public class StartDateCheck {
	public static boolean afterStartDate() {
		Calendar now = Calendar.getInstance();
		Calendar start_date = PreferenceControl.getStartDate();

		return now.after(start_date);
	}
}
