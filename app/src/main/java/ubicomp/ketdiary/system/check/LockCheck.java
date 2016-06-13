package ubicomp.ketdiary.system.check;

import ubicomp.ketdiary.system.PreferenceControl;

import java.util.Calendar;

public class LockCheck {
	public static boolean check() {
		if (PreferenceControl.isLocked()) {
			Calendar c_1 = PreferenceControl.getLockDate();
			Calendar c = Calendar.getInstance();
			if (c_1.before(c))
				return true;
		}
		return false;
	}

}
