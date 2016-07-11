package ubicomp.ketdiary.utility.data.upload;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import ubicomp.ketdiary.utility.data.upload.DataUploader;
import ubicomp.ketdiary.utility.system.check.DefaultCheck;

/**
 * Service for uploading data onto the server
 * 
 * @author Stanley Wang
 */
public class UploadService extends Service {

	/**
	 * Start the uploading service
	 * 
	 * @param context
	 *            Application context
	 */
	public static void startUploadService(Context context) {
		Intent intent = new Intent(context, UploadService.class);
		context.startService(intent);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);

		if (DefaultCheck.check())
			return Service.START_REDELIVER_INTENT;

		DataUploader.upload();

		return Service.START_REDELIVER_INTENT;
	}

}