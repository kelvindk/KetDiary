package ubicomp.ketdiary.utility.back_door;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import ubicomp.ketdiary.main_activity.KetdiaryApplication;
import ubicomp.ketdiary.R;

public class CustomToastSmall {

	private static Toast toast = null;

	private static View layout = null;
	private static TextView toastText;

	public static void generateToast(int str_id) {
		Context context = KetdiaryApplication.getContext();
		if (toast != null) {
			toast.cancel();
			toast = null;
		}
		toast = new Toast(context);

		if (layout == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			layout = inflater.inflate(R.layout.toast_normal, null);
			toastText = (TextView) layout.findViewById(R.id.custom_toast_text);
			toastText.setTypeface(Typefaces.getWordTypefaceBold());
		}
		toast.setView(layout);
		toast.setDuration(Toast.LENGTH_SHORT);
		toastText.setText(str_id);
		toast.show();
	}
	public static void generateToast(String str_id) {
		Context context = KetdiaryApplication.getContext();
		if (toast != null) {
			toast.cancel();
			toast = null;
		}
		toast = new Toast(context);

		if (layout == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			layout = inflater.inflate(R.layout.toast_normal, null);
			toastText = (TextView) layout.findViewById(R.id.custom_toast_text);
			toastText.setTypeface(Typefaces.getWordTypefaceBold());
		}
		toast.setView(layout);
		toast.setDuration(Toast.LENGTH_SHORT);
		toastText.setText(str_id);
		toast.show();
	}

}
