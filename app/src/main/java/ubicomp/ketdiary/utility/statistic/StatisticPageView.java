package ubicomp.ketdiary.utility.statistic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import ubicomp.ketdiary.main_activity.KetdiaryApplication;

public abstract class StatisticPageView {
	protected Context context;
	protected View view;
	private LayoutInflater inflater;

	public StatisticPageView(int layout_id) {
		this.context = KetdiaryApplication.getContext();
		inflater = LayoutInflater.from(context);
		view = inflater.inflate(layout_id, null);
	}

	public View getView() {
		return view;
	}

	abstract public void load();

	abstract public void onCancel();

	abstract public void clear();

}
