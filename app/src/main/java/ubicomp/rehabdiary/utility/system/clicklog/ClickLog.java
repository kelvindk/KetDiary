package ubicomp.rehabdiary.utility.system.clicklog;

import android.util.Log;

import ubicomp.rehabdiary.utility.data.file.MainStorage;
import ubicomp.rehabdiary.utility.system.check.DefaultCheck;
import ubicomp.rehabdiary.utility.system.check.LockCheck;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;

public class ClickLog {

	public static void Log(int id) {
		if (DefaultCheck.check() || LockCheck.check())
			return;

		int message = id;
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
		try(FileWriter fw = new FileWriter(logFile, true);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter out = new PrintWriter(bw))
		{
			out.println(timestamp + " " + message);
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