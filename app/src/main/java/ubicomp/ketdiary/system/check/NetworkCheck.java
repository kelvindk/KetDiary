package ubicomp.ketdiary.system.check;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import ubicomp.ketdiary.KetdiaryApplication;

public class NetworkCheck {

	public static boolean networkCheck() {
		ConnectivityManager connectivityManager = (ConnectivityManager) KetdiaryApplication
				.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
		if (activeNetwork == null)
			return false;
		return activeNetwork.isConnected();
	}

}
