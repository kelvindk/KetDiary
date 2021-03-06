package ubicomp.ketdiary.utility.back_door;

import android.graphics.Typeface;

import ubicomp.ketdiary.main_activity.KetdiaryApplication;

/**
 * Class for loading custom typefaces
 * 
 * @author Stanley Wang
 */
public class Typefaces {
	private static Typeface wordTypeface, digitTypeface, wordTypefaceBold,
			digitTypefaceBold;

	/**
	 * Get typeface for nomal digits
	 * 
	 * @return Typeface for normal digits
	 */
	public static Typeface getDigitTypeface() {
		if (digitTypeface == null)
			digitTypeface = Typeface.createFromAsset(KetdiaryApplication.getContext()
					.getAssets(), "fonts/dinproregular.ttf");
		return digitTypeface;
	}

	/**
	 * Get typeface for bold digits
	 * 
	 * @return Typeface for bold digits
	 */
	public static Typeface getDigitTypefaceBold() {
		if (digitTypefaceBold == null)
			digitTypefaceBold = Typeface.createFromAsset(KetdiaryApplication.getContext()
					.getAssets(), "fonts/dinpromedium.ttf");
		return digitTypefaceBold;
	}

	/**
	 * Get typeface for nomal words
	 * 
	 * @return Typeface for normal words
	 */
	public static Typeface getWordTypeface() {
		if (wordTypeface == null)
			wordTypeface = Typeface.createFromAsset(KetdiaryApplication.getContext()
					.getAssets(), "fonts/DFLiHeiStd-W3.otf");
		return wordTypeface;
	}

	/**
	 * Get typeface for bold words
	 * 
	 * @return Typeface for bold words
	 */
	public static Typeface getWordTypefaceBold() {
		if (wordTypefaceBold == null)
			wordTypefaceBold = Typeface.createFromAsset(KetdiaryApplication.getContext()
					.getAssets(), "fonts/DFLiHeiStd-W5.otf");
		return wordTypefaceBold;
	}

	/** For initializing all the typefaces */
	public static void initAll() {
		getDigitTypeface();
		getDigitTypefaceBold();
		getWordTypeface();
		getWordTypefaceBold();
	}
}
