package ubicomp.ketdiary.utility.system.clicklog;

import android.util.Log;

import ubicomp.ketdiary.utility.data.file.MainStorage;
import ubicomp.ketdiary.utility.system.check.DefaultCheck;
import ubicomp.ketdiary.utility.system.check.LockCheck;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;

public class ClickLog {

	public static void Log(long id) {
		if (DefaultCheck.check() || LockCheck.check())
			return;

		long message = id;
		long timestamp = System.currentTimeMillis();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd");
		String date = sdf.format(timestamp);

		File mainStorage = MainStorage.getMainStorageDirectory();
		File dir = new File(mainStorage, "sequence_log");
		if (!dir.exists()) {
			dir.mkdirs();
		}

		File logFile = new File(dir, date + ".txt");
		DataOutputStream ds = null;
		try {
			ds = new DataOutputStream(new BufferedOutputStream(
					new FileOutputStream(logFile, logFile.exists())));
			ds.writeLong(timestamp);
			ds.writeLong(message);
			ds.flush();
		} catch (Exception e) {
			Log.d("CLICKLOG", "WRITE FAIL");
		} finally {
			try {
				if (ds != null)
					ds.close();
			} catch (Exception e) {
			}
		}
	}

}