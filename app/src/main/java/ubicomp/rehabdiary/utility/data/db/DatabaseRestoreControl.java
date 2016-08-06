package ubicomp.rehabdiary.utility.data.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ubicomp.rehabdiary.fragments.event.EventLogStructure;
import ubicomp.rehabdiary.main_activity.KetdiaryApplication;
import ubicomp.rehabdiary.utility.data.structure.AddScore;
import ubicomp.rehabdiary.utility.data.structure.CopingSkill;
import ubicomp.rehabdiary.utility.data.structure.NoteAdd;
import ubicomp.rehabdiary.utility.data.structure.QuestionTest;
import ubicomp.rehabdiary.utility.data.structure.TestResult;

/**
 * Control insertion and modification on database for the restore process
 * 
 * @author Andy Chen
// * @see ubicomp.soberdiary.data.database.DatabaseRestore
 */
public class DatabaseRestoreControl {

	private SQLiteOpenHelper dbHelper = null;
	private SQLiteDatabase db = null;

	public DatabaseRestoreControl() {
		dbHelper = new DBHelper(KetdiaryApplication.getContext());
	}

	/**
	 * Restore TestResult
	 * 
	 * @param data
	 *            TestResult data
	 */
	public void restoreTestResult(TestResult data) {
		db = dbHelper.getWritableDatabase();
		if (data.getIsPrime() == 1) {
			String sql = "UPDATE TestResult SET isPrime = 0" + " WHERE year ="
					+ data.getTv().getYear() + " AND month="
					+ data.getTv().getMonth() + " AND day ="
					+ data.getTv().getDay();
			db.execSQL(sql);
		}
		ContentValues content = new ContentValues();
		content.put("result", data.getResult());
		content.put("cassetteId", data.getCassette_id());
		content.put("year", data.getTv().getYear());
		content.put("month", data.getTv().getMonth());
		content.put("day", data.getTv().getDay());
		content.put("ts", data.getTv().getTimestamp());
		content.put("week", data.getTv().getWeek());
		content.put("isPrime", data.getIsPrime());
		content.put("isFilled", data.getIsFilled());
		content.put("weeklyScore", data.getWeeklyScore());
		content.put("score", data.getScore());
		content.put("upload", 1);
		db.insert("TestResult", null, content);
		db.close();
	}

	/**
	 * Restore NoteAdd
	 * 
	 * @param data
	 *            NoteAdd data
	 */
	public void restoreNoteAdd(NoteAdd data) {
		db = dbHelper.getWritableDatabase();
		ContentValues content = new ContentValues();
		content.put("isAfterTest", data.getIsAfterTest());
		content.put("year", data.getTv().getYear());
		content.put("month", data.getTv().getMonth());
		content.put("day", data.getTv().getDay());
		content.put("ts", data.getTv().getTimestamp());
		content.put("week", data.getTv().getWeek());
		content.put("timeslot", data.getTimeSlot());
		content.put("recordYear", data.getRecordTv().getYear());
		content.put("recordMonth", data.getRecordTv().getMonth());
		content.put("recordDay", data.getRecordTv().getDay());
		content.put("category", data.getCategory());
		content.put("type", data.getType());
		content.put("items", data.getItems());
		content.put("impact", data.getImpact());
		content.put("action", data.getAction());
		content.put("feeling", data.getFeeling());
		content.put("thinking", data.getThinking());
		content.put("finished", data.getFinished());
		content.put("weeklyScore", data.getWeeklyScore());
		content.put("score", data.getScore());
		content.put("upload", 1);
		db.insert("NoteAdd", null, content);
		db.close();
	}

	/**
	 * Restore EventLogStructure
	 *
	 * @param data
	 *            EventLogStructure data
	 */
	public void restoreEventLog(EventLogStructure data) {
		db = dbHelper.getWritableDatabase();
		ContentValues content = new ContentValues();

		content.put("editTime", data.editTime.getTimeInMillis());
		content.put("eventTime", data.eventTime.getTimeInMillis());
		content.put("createTime", data.createTime.getTimeInMillis());
		content.put("scenarioType", data.scenarioType.ordinal());
		content.put("scenario", data.scenario);
		content.put("drugUseRiskLevel", data.drugUseRiskLevel);
		content.put("originalBehavior", data.originalBehavior);
		content.put("originalEmotion", data.originalEmotion);
		content.put("originalThought", data.originalThought);
		content.put("expectedBehavior", data.expectedBehavior);
		content.put("expectedEmotion", data.expectedEmotion);
		content.put("expectedThought", data.expectedThought);
		content.put("therapyStatus", data.therapyStatus.ordinal());
		content.put("isAfterTest", data.isAfterTest? 1:0);
		content.put("isComplete", data.isComplete? 1:0);
		content.put("isLastest", 1);
		content.put("upload", 1);

		db.insert("EventLog", null, content);
		db.close();
	}

	/**
	 * Restore Score
	 *
	 * @param data
	 *            EventLogStructure data
	 */
	public void restoreScore(AddScore data) {
		db = dbHelper.getWritableDatabase();
		ContentValues content = new ContentValues();

		content.put("ts", data.getTv().getTimestamp());
		content.put("addScore", data.getAddScore());
		content.put("accumulation", data.getAccumulation());
		content.put("reason", data.getReason());
		content.put("weeklyAccumulation", data.getWeeklyAccumulation());
		content.put("reasonBit", data.getReasonBits());
		content.put("upload", 1);

		db.insert("Score", null, content);
		db.close();
	}

	/**
	 * Restore QuestionTest
	 * 
	 * @param data
	 *            QuestionTest data
	 */
	public void restoreQuestionTest(QuestionTest data) {
		db = dbHelper.getWritableDatabase();
		ContentValues content = new ContentValues();
		content.put("year", data.getTv().getYear());
		content.put("month", data.getTv().getMonth());
		content.put("day", data.getTv().getDay());
		content.put("ts", data.getTv().getTimestamp());
		content.put("week", data.getTv().getWeek());
		content.put("timeslot", data.getTv().getTimeslot());
		content.put("questionType", data.getQuestionType());
		content.put("isCorrect", data.getisCorrect());
		content.put("selection", data.getSelection());
		content.put("choose", data.getChoose());
		content.put("score", data.getScore());
		content.put("upload", 1);
		db.insert("QuestionTest", null, content);
		db.close();
	}

	/**
	 * Restore CopingSkill
	 * 
	 * @param data
	 *            CopingSkill data
	 */
	public void restoreCopingSkill(CopingSkill data) {
		db = dbHelper.getWritableDatabase();
		ContentValues content = new ContentValues();
		content.put("year", data.getTv().getYear());
		content.put("month", data.getTv().getMonth());
		content.put("day", data.getTv().getDay());
		content.put("ts", data.getTv().getTimestamp());
		content.put("week", data.getTv().getWeek());
		content.put("timeslot", data.getTv().getTimeslot());
		content.put("skillType", data.getSkillType());
		content.put("skillSelect", data.getSkillSelect());
		content.put("recreation", data.getRecreation());
		content.put("score", data.getScore());
		content.put("upload", 1);
		db.insert("CopingSkill", null, content);
		db.close();
	}


	/** Truncate the database */
	public void deleteAll() {
		db = dbHelper.getWritableDatabase();
		String sql = null;
		sql = "DELETE FROM TestResult";
		db.execSQL(sql);
		//sql = "DELETE FROM NoteAdd";
		//db.execSQL(sql);
		sql = "DELETE FROM TestDetail";
		db.execSQL(sql);
		sql = "DELETE FROM Ranking";
		db.execSQL(sql);
		sql = "DELETE FROM RankingShort";
		db.execSQL(sql);
		sql = "DELETE FROM QuestionTest";
		db.execSQL(sql);
		sql = "DELETE FROM CopingSkill";
		db.execSQL(sql);
		sql = "DELETE FROM ExchangeHistory";
		db.execSQL(sql);
		db.close();
	}
}
