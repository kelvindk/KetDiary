package ubicomp.rehabdiary.utility.statistic;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ubicomp.rehabdiary.R;
import ubicomp.rehabdiary.main_activity.KetdiaryApplication;

public class BarButtonGenerator {
	private static final LayoutInflater inflater = (LayoutInflater) KetdiaryApplication
			.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	private static Typeface wordTypefaceBold = Typefaces.getWordTypefaceBold();
	private static Typeface wordTypeface = Typefaces.getWordTypeface();
	private static Context context = KetdiaryApplication.getContext();

	public static View createTextView(int textStr) {
		LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.bar_text_item, null);
		TextView text = (TextView) layout
				.findViewById(R.id.question_description);
		text.setTypeface(wordTypefaceBold);
		text.setText(textStr);

		return layout;
	}

	public static View createIconView(String textStr, int DrawableId,
									  OnClickListener listener) {

		RelativeLayout layout = (RelativeLayout) inflater.inflate(
				R.layout.bar_icon_item, null);
		TextView text = (TextView) layout
				.findViewById(R.id.question_description);
		text.setTypeface(wordTypefaceBold);
		text.setText(textStr);

		ImageView icon = (ImageView) layout.findViewById(R.id.question_icon);
		if (DrawableId > 0)
			icon.setImageDrawable(KetdiaryApplication.getContext().getResources()
					.getDrawable(DrawableId));

		layout.setOnClickListener(listener);

		return layout;
	}

	public static View createTitleView(int titleStr) {

		RelativeLayout layout = (RelativeLayout) inflater.inflate(
				R.layout.bar_titlebar, null);
		TextView text = (TextView) layout.findViewById(R.id.titlebar_text);
		text.setTypeface(wordTypefaceBold);
		text.setText(titleStr);

		return layout;
	}

	public static View createAnimationView(int anim_id) {
		RelativeLayout layout = (RelativeLayout) inflater.inflate(
				R.layout.bar_animation_item, null);
		ImageView img = (ImageView) layout
				.findViewById(R.id.question_animation);
		img.setImageResource(anim_id);
		TextView text = (TextView) layout
				.findViewById(R.id.question_animation_right_button);
		text.setTypeface(wordTypefaceBold);
		return layout;
	}

	public static View createSettingButtonView(int textStr,
											   OnClickListener listener) {

		RelativeLayout layout = (RelativeLayout) inflater.inflate(
				R.layout.bar_setting_item, null);
		TextView text = (TextView) layout
				.findViewById(R.id.question_description);
		text.setTypeface(wordTypeface);
		text.setText(textStr);

		layout.setOnClickListener(listener);

		return layout;
	}
	public static View createSettingButtonView2(String textStr,
												OnClickListener listener) {

		RelativeLayout layout = (RelativeLayout) inflater.inflate(
				R.layout.bar_setting_item, null);
		TextView text = (TextView) layout
				.findViewById(R.id.question_description);
		text.setTypeface(wordTypeface);
		text.setText(textStr);

		layout.setOnClickListener(listener);

		return layout;
	}

}
