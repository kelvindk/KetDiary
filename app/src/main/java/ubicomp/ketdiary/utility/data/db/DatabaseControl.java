package ubicomp.ketdiary.utility.data.db;

import android.app.AlarmManager;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import ubicomp.ketdiary.fragments.event.EventLogStructure;
import ubicomp.ketdiary.main_activity.KetdiaryApplication;
import ubicomp.ketdiary.utility.data.structure.AddScore;
import ubicomp.ketdiary.utility.data.structure.Appeal;
import ubicomp.ketdiary.utility.data.structure.Cassette;
import ubicomp.ketdiary.utility.data.structure.CopingSkill;
import ubicomp.ketdiary.utility.data.structure.ExchangeHistory;
import ubicomp.ketdiary.utility.data.structure.History;
import ubicomp.ketdiary.utility.data.structure.IdentityScore;
import ubicomp.ketdiary.utility.data.structure.NoteAdd;
import ubicomp.ketdiary.utility.data.structure.QuestionTest;
import ubicomp.ketdiary.utility.data.structure.Rank;
import ubicomp.ketdiary.utility.data.structure.Reflection;
import ubicomp.ketdiary.utility.data.structure.TestDetail;
import ubicomp.ketdiary.utility.data.structure.TestResult;
import ubicomp.ketdiary.utility.data.structure.ThinkingEvent;
import ubicomp.ketdiary.utility.data.structure.TimeValue;
import ubicomp.ketdiary.utility.data.structure.TriggerItem;
import ubicomp.ketdiary.utility.system.PreferenceControl;
import ubicomp.ketdiary.utility.system.check.StartDateCheck;
import ubicomp.ketdiary.utility.system.check.WeekNumCheck;

import java.util.Calendar;


/**
 * This class is used for controlling database on the mobile phone side
 * 
 * @author Andy Chen
 */

public class DatabaseControl {
	/**
	 * SQLiteOpenHelper
	 * 
	 * @see com.ubicomp.ketdiary.data.db.DBHelper
	 */
	private final static String TAG = "DatabaseControl";
	private SQLiteOpenHelper dbHelper = null;
	/** SQLLiteDatabase */
	private SQLiteDatabase db = null;
	/** Lock for preventing congestion */
	private static final Object sqlLock = new Object();

	/** Constructor of DatabaseControl */
	public DatabaseControl() {
		dbHelper = new DBHelper(KetdiaryApplication.getContext());
	}
	
	// TestResult

	/**
	 * This method is used for getting all prime brac Detection
	 * 
	 * @return An array of Detection. If there are no detections, return null
	 * @see com.ubicomp.ketdiary.data.structure.TestResult
	 */
	
	public TestResult[] getAllPrimeTestResult() {
		synchronized (sqlLock) {
			db = dbHelper.getReadableDatabase();
			String sql = "SELECT * FROM TestResult WHERE isPrime = 1 ORDER BY ts ASC";
			Cursor cursor = db.rawQuery(sql, null);
			int count = cursor.getCount();
			if (count == 0) {
				cursor.close();
				db.close();
				return null;
			}

			TestResult[] testResult = new TestResult[count];
			for (int i = 0; i < count; ++i) {
				cursor.moveToPosition(i);
				int result = cursor.getInt(1);
				String cassetteId = cursor.getString(2);
				long ts = cursor.getLong(6);
				int isPrime = cursor.getInt(8);
				int isFilled= cursor.getInt(9);
				int weeklyScore = cursor.getInt(10);
				int score = cursor.getInt(11);
				testResult[i] = new TestResult(result, ts, cassetteId, isPrime, isFilled, weeklyScore, score);
			}

			cursor.close();
			db.close();
			return testResult;
		}
	}

	/**
	 * This method is used for the latest result detection
	 * 
	 * @return TestResult. If there are no TestResult, return a dummy data.
	 * @see com.ubicomp.ketdiary.data.structure.TestResult
	 *
	 */
	
	public TestResult getLatestTestResult() {
		synchronized (sqlLock) {
			db = dbHelper.getReadableDatabase();
			String sql = "SELECT * FROM TestResult ORDER BY ts DESC LIMIT 1";
			Cursor cursor = db.rawQuery(sql, null);
			if (!cursor.moveToFirst()) {
				cursor.close();
				db.close();
				return new TestResult(0, 0, "ket_default", 0, 0, 0, 0);
			}

			int result = cursor.getInt(1);
			String cassetteId = cursor.getString(2);
			long ts = cursor.getLong(6);
			int isPrime = cursor.getInt(8);
			int isFilled= cursor.getInt(9);
			int weeklyScore = cursor.getInt(10);
			int score = cursor.getInt(11);
			TestResult testResult = new TestResult(result, ts, cassetteId, isPrime, isFilled, weeklyScore, score);

			cursor.close();
			db.close();
			return testResult;
		}
	}
	
	public int isPrimeTestResult(long ts) {
		TestResult tResult = getLatestTestResult();
		
		TimeValue now = TimeValue.generate(ts);
		TimeValue pre = tResult.getTv();

		if(now.isSameDay(pre))
			return 0;
		return 1;
	}
	
	public TestResult getLatestTestResultID() {
		synchronized (sqlLock) {
			db = dbHelper.getReadableDatabase();
			String sql = "SELECT * FROM TestResult ORDER BY id DESC LIMIT 1";
			Cursor cursor = db.rawQuery(sql, null);
			if (!cursor.moveToFirst()) {
				cursor.close();
				db.close();
				return new TestResult(0, 0, "ket_default", 0, 0, 0, 0);
			}

			int result = cursor.getInt(1);
			String cassetteId = cursor.getString(2);
			long ts = cursor.getLong(6);
			int isPrime = cursor.getInt(8);
			int isFilled= cursor.getInt(9);
			int weeklyScore = cursor.getInt(10);
			int score = cursor.getInt(11);
			TestResult testResult = new TestResult(result, ts, cassetteId, isPrime, isFilled, weeklyScore, score);

			cursor.close();
			db.close();
			return testResult;
		}
	}
	
	/**
	 * This method is used for inserting a result detection
	 * 
	 * @return # of credits got by the user
	 * @param data
	 *            Inserted TestResult
	 * @param update
	 *            If update = true, the previous prime detection will be
	 *            replaced by current Detection
	 * @see com.ubicomp.ketdiary.data.structure.TestResult
	 */

	public void insertTestResult(TestResult data) {
		synchronized (sqlLock) {

			db = dbHelper.getWritableDatabase();
			ContentValues content = new ContentValues();
			content.put("result", data.getResult());
			content.put("cassetteId", data.getCassette_id());
			content.put("year", data.getTv().getYear());
			content.put("month", data.getTv().getMonth());
			content.put("day", data.getTv().getDay());
			content.put("ts", data.getTv().getTimestamp());
			content.put("week", data.getTv().getWeek());
			content.put("isPrime", 1);
			content.put("isFilled", data.getIsFilled());
			//content.put("weeklyScore", weeklyScore + addScore);
			//content.put("score", score + addScore);
			db.insert("TestResult", null, content);
			db.close();
		}
	}

	public int insertTestResult(TestResult data, boolean update) {
		synchronized (sqlLock) {

			TestResult prev_data = getLatestTestResult();
			TestResult prevID_data = getLatestTestResultID();
			int weeklyScore = prevID_data.getWeeklyScore();
			if (prevID_data.getTv().getWeek() < data.getTv().getWeek())
				weeklyScore = prev_data.getWeeklyScore();
			if (prev_data.getTv().getWeek() < data.getTv().getWeek())
				weeklyScore = 0;
			int score = prevID_data.getScore();
			db = dbHelper.getWritableDatabase();
			Log.d("GG","01");
			if (!update) {
				boolean isPrime = !(data.isSameDay(prev_data) || data.isSameDay(prevID_data));
				// add by Andy
				int result = data.getResult();
//				if(isPrime){
//					if(result == 0)
//						PreferenceControl.setPosition(1);
//					else
//						PreferenceControl.setPosition(-1);
//				}
				//
				int isPrimeValue = isPrime ? 1 : 0;
				int addScore = 0;
				addScore += isPrimeValue;
				addScore += isPrime && data.getResult()== 0 ? 1 : 0;
				Log.d("GG","isprime : " + isPrimeValue +", result : "+ (isPrime && data.getResult()== 0 ? 1 : 0));
				if (!StartDateCheck.afterStartDate())
					addScore = 0;

				ContentValues content = new ContentValues();
				content.put("result", data.getResult());
				content.put("cassetteId", data.getCassette_id());
				content.put("year", data.getTv().getYear());
				content.put("month", data.getTv().getMonth());
				content.put("day", data.getTv().getDay());
				content.put("ts", data.getTv().getTimestamp());
				content.put("week", data.getTv().getWeek());
				content.put("isPrime", isPrimeValue);
				content.put("isFilled", data.getIsFilled());
				content.put("weeklyScore", weeklyScore + addScore);
				content.put("score", score + addScore);
				db.insert("TestResult", null, content);
				db.close();
				return addScore;
			} else {  //把之前的isPrime設成0
				int addScore = data.getResult()== 0 ? 1 : 0;
				if (!StartDateCheck.afterStartDate())
					addScore = 0;
				String sql = "UPDATE TestResult SET isPrime = 0 WHERE ts ="
						+ prev_data.getTv().getTimestamp();
				db.execSQL(sql);
				ContentValues content = new ContentValues();
				content.put("result", data.getResult());
				content.put("cassetteId", data.getCassette_id());
				content.put("year", data.getTv().getYear());
				content.put("month", data.getTv().getMonth());
				content.put("day", data.getTv().getDay());
				content.put("ts", data.getTv().getTimestamp());
				content.put("week", data.getTv().getWeek());
				content.put("isPrime", 1);
				content.put("isFilled", data.getIsFilled());
				content.put("weeklyScore", weeklyScore + addScore);
				content.put("score", score + addScore);
				db.insert("TestResult", null, content);
				db.close();
				return addScore;
			}
		}
	}
	
	public TestResult getDayTestResult(int Year, int Month, int Day) {
		synchronized (sqlLock) {

			db = dbHelper.getReadableDatabase();
			String sql;
			Cursor cursor;

			sql = "SELECT * FROM TestResult WHERE year = " + Year
					+ " AND month = " + Month + " AND day = "
					+ Day +" AND isPrime = 1";
			cursor = db.rawQuery(sql, null);
			int count = cursor.getCount();
			if (!cursor.moveToFirst()) {
				cursor.close();
				db.close();
				return new TestResult(-1, 0, "ket_default", 0, 0, 0, 0);
			}
			

			int result = cursor.getInt(1);
			String cassetteId = cursor.getString(2);
			long ts = cursor.getLong(6);
			int isPrime = cursor.getInt(8);
			int isFilled= cursor.getInt(9);
			int weeklyScore = cursor.getInt(10);
			int score = cursor.getInt(11);
			TestResult testResult = new TestResult(result, ts, cassetteId, isPrime, isFilled, weeklyScore, score);
			cursor.close();
			db.close();

			return testResult;
		}
	}
	
	
	
	/**
	 * This method is used for getting result of today's prime detections
	 * 
	 * @return result 
	 */
	public int getTodayPrimeResult() {
		synchronized (sqlLock) {
			int result;
			Calendar cal = Calendar.getInstance();
			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH);
			int day = cal.get(Calendar.DATE);

			db = dbHelper.getReadableDatabase();

			String sql = "SELECT result FROM TestResult WHERE year = "
					+ year + " AND month = " + month + " AND day = " + day
					+ " AND isPrime = 1" + " ORDER BY ASC";
			Cursor cursor = db.rawQuery(sql, null);

			int count = cursor.getCount();
			result = cursor.getInt(1);

			cursor.close();
			db.close();
			return result;
		}
	}
	
	/**
	 * This method is used for getting result of today's prime detections
	 * 
	 * @return count 
	 */
	public int getTodayTestCount() {
		synchronized (sqlLock) {
			//int result;
			Calendar cal = Calendar.getInstance();
			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH);
			int day = cal.get(Calendar.DATE);

			db = dbHelper.getReadableDatabase();

			String sql = "SELECT result FROM TestResult WHERE year = "
					+ year + " AND month = " + month + " AND day = " + day;
			Cursor cursor = db.rawQuery(sql, null);

			int count = cursor.getCount();
			//result = cursor.getInt(1);

			cursor.close();
			db.close();
			return count;
		}
	}
	
	public int getDayTestCount() {
		synchronized (sqlLock) {
			//int result;
			Calendar cal = Calendar.getInstance();
			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH);
			int day = cal.get(Calendar.DATE);

			db = dbHelper.getReadableDatabase();

			String sql = "SELECT result FROM TestResult WHERE year = "
					+ year + " AND month = " + month + " AND day = " + day;
			Cursor cursor = db.rawQuery(sql, null);

			int count = cursor.getCount();
			//result = cursor.getInt(1);

			cursor.close();
			db.close();
			return count;
		}
	}
	
	/**
	 * This method is used for getting result of today's prime detections
	 * 
	 * @return count 
	 */
	public int noTestDayCount(long prev_ts, long ts) {  //TODO:
		synchronized (sqlLock) {
			//int result;
			TimeValue prev_tv = TimeValue.generate(prev_ts);
			TimeValue tv = TimeValue.generate(ts);
			
			int noTestDay = 0;
			int passDay = 0;
			final long DAY = AlarmManager.INTERVAL_DAY;
			
			if(tv.isSameDay(prev_tv) || prev_ts >= ts){
				Log.i(TAG, "Day: " + 0 );
				return 0;
			}
			
			db = dbHelper.getReadableDatabase();
			
			while(!tv.isSameDay(prev_tv) && prev_ts < ts){
				
				int year = prev_tv.getYear();
				int month = prev_tv.getMonth();
				int day = prev_tv.getDay();
				Log.i(TAG, "Day: " + day );
				
				String sql = "SELECT result FROM TestResult WHERE year = "
					+ year + " AND month = " + month + " AND day = " + day;
				Cursor cursor = db.rawQuery(sql, null);
				int count = cursor.getCount();
				if(count == 0){
					noTestDay++;
				}
				cursor.close();
				prev_ts = prev_ts + DAY;
				prev_tv = TimeValue.generate(prev_ts);
				passDay++;
			}
			Log.i(TAG, "PassDay: " + passDay + " NoTestDay " + noTestDay);
			if(noTestDay > 0 && StartDateCheck.afterStartDate()){			
				PreferenceControl.setCheckBars(true);
				PreferenceControl.setPosition(-noTestDay);
			}
			PreferenceControl.setOpenAppTimestamp();
			db.close();
			return noTestDay;
		}
	}

	/**
	 * This method is used for getting result of previous n-day prime
	 * detections
	 * 
	 * @return An array of float (length = n_days*3) [idx], idx%3=0:morning,
	 *         idx%3=1:afternoon, idx%[3]=2:night
	 */
	public int[] getMultiDaysPrimeBrac(int n_days) {
		synchronized (sqlLock) {
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			final long DAY = AlarmManager.INTERVAL_DAY;
			long ts_days = (long) (n_days - 1) * DAY;
			long start_ts = cal.getTimeInMillis() - ts_days;

			String sql = "SELECT result,ts FROM TestResult WHERE ts >="
					+ start_ts + " AND isPrime = 1" + " ORDER BY ts ASC";
			db = dbHelper.getReadableDatabase();
			Cursor cursor = db.rawQuery(sql, null);

			int[] result = new int[n_days];
			long ts_from = start_ts;
			long ts_to = start_ts + DAY;
			int pointer = 0;
			int count = cursor.getCount();

			for (int i = 0; i < result.length; ++i) {
				int _result = -1;
				long _ts;
				result[i] = _result;
				while (pointer < count) {
					cursor.moveToPosition(pointer);
					_result = cursor.getInt(0);
					_ts = cursor.getLong(1);
					if (_ts < ts_from) {
						++pointer;
						continue;
					} else if (_ts >= ts_to) {
						break;
					}
					result[i] = _result;
					break;
				}
				ts_from += DAY;
				ts_to += DAY;

			}
			cursor.close();
			db.close();
			return result;
		}
	}
	
	/**
	 * This method is used for getting result of previous n-day prime
	 * detections
	 * 
	 * @return 
	 */
	public int[] getWeeklyPrimeBrac() {
		synchronized (sqlLock) {
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			
			int FIRST_DAY = Calendar.MONDAY;
			int day=1;
			while (cal.get(Calendar.DAY_OF_WEEK) != FIRST_DAY) {
				cal.add(Calendar.DATE, -1);
				day++;
	        }
			final long DAY = AlarmManager.INTERVAL_DAY;
			long start_ts = cal.getTimeInMillis();

			String sql = "SELECT result,ts FROM TestResult WHERE ts >="
					+ start_ts + " AND isPrime = 1" + " ORDER BY ts ASC";
			db = dbHelper.getReadableDatabase();
			Cursor cursor = db.rawQuery(sql, null);

			int[] result = new int[day];
			long ts_from = start_ts;
			long ts_to = start_ts + DAY;
			int pointer = 0;
			int count = cursor.getCount();

			for (int i = 0; i < result.length; ++i) {
				int _result = -1;
				long _ts;
				result[i] = _result;
				while (pointer < count) {
					cursor.moveToPosition(pointer);
					_result = cursor.getInt(0);
					_ts = cursor.getLong(1);
					if (_ts < ts_from) {
						++pointer;
						continue;
					} else if (_ts >= ts_to) {
						break;
					}
					result[i] = _result;
					break;
				}
				ts_from += DAY;
				ts_to += DAY;

			}
			cursor.close();
			db.close();
			return result;
		}
	}

	/**
	 * This method is used for labeling which TestResult is uploaded to the
	 * server
	 * 
	 * @param ts
	 *            timestamp of the detection
	 */
	public void setTestResultUploaded(long ts) {
		synchronized (sqlLock) {
			db = dbHelper.getWritableDatabase();
			String sql = "UPDATE TestResult SET upload = 1 WHERE ts=" + ts;
			db.execSQL(sql);
			db.close();
		}
	}
	
	public void modifyResultByTs(long ts, int result) {
		synchronized (sqlLock) {

			db = dbHelper.getReadableDatabase();
			String sql;
			//sql = "SELECT * FROM TestResult WHERE ts = " + ts + " AND isPrime = 1";
			sql = "UPDATE TestResult SET result = " + result + ", upload = 0 WHERE ts=" + ts;
			db.execSQL(sql);
			db.close();

			return ;
		}
	}

	/**
	 * This method is used for getting weekly scores of current week's
	 * TestResult
	 * 
	 * @return An array of weekly score. Length=# weeks
	 */
	public Integer[] getTestResultScoreByWeek() {
		synchronized (sqlLock) {
			db = dbHelper.getReadableDatabase();
			int curWeek = WeekNumCheck.getWeek(Calendar.getInstance()
					.getTimeInMillis());
			Integer[] scores = new Integer[curWeek + 1];

			String sql = "SELECT weeklyScore, week FROM TestResult WHERE week<="
					+ curWeek + " GROUP BY week";

			Cursor cursor = db.rawQuery(sql, null);
			int count = cursor.getCount();
			int pointer = 0;
			int week = 0;
			for (int i = 0; i < scores.length; ++i) {
				while (pointer < count) {
					cursor.moveToPosition(pointer);
					week = cursor.getInt(1);
					if (week < i) {
						++pointer;
						continue;
					} else if (week > i)
						break;
					int weeklyScore = cursor.getInt(0);
					scores[i] = weeklyScore;
					break;
				}
			}
			for (int i = 0; i < scores.length; ++i)
				if (scores[i] == null)
					scores[i] = 0;

			cursor.close();
			db.close();
			return scores;
		}
	}

	/**
	 * This method is used for getting TestResult which are not uploaded to the
	 * server
	 * 
	 * @return An array of TestResult. If there are no TestResult, return null.
	 * @see com.ubicomp.ketdiary.data.structure.TestResult
	 */
	
	
	public TestResult[] getAllNotUploadedTestResult() {
		synchronized (sqlLock) {
			db = dbHelper.getReadableDatabase();
			long cur_ts = System.currentTimeMillis();
			String sql;

			sql = "SELECT * FROM TestResult WHERE upload = 0  ORDER BY ts ASC";

			Cursor cursor = db.rawQuery(sql, null);
			int count = cursor.getCount();
			if (count == 0) {
				cursor.close();
				db.close();
				return null;
			}

			TestResult[] testResults = new TestResult[count];
			for (int i = 0; i < count; ++i) {
				cursor.moveToPosition(i);
				int result = cursor.getInt(1);
				String cassetteId = cursor.getString(2);
				long ts = cursor.getLong(6);
				int isPrime = cursor.getInt(8);
				int isFilled= cursor.getInt(9);
				int weeklyScore = cursor.getInt(10);
				int score = cursor.getInt(11);
				testResults[i] = new TestResult(result, ts, cassetteId, isPrime, isFilled, weeklyScore, score);
			}
			cursor.close();
			db.close();
			return testResults;
		}
	}

	/**
	 * This method is used for checking if the user do the brac detection at
	 * this time slot
	 * 
	 * @return true if the user do a brac detection at the current time slot
	 */
	
	public boolean detectionIsDone() {
		synchronized (sqlLock) {
			db = dbHelper.getReadableDatabase();
			long ts = System.currentTimeMillis();
			TimeValue tv = TimeValue.generate(ts);
			String sql = "SELECT id FROM TestResult WHERE" + " year ="
					+ tv.getYear() + " AND month = " + tv.getMonth()
					+ " AND day= " + tv.getDay();
			Cursor cursor = db.rawQuery(sql, null);
			boolean result = cursor.getCount() > 0;
			cursor.close();
			db.close();
			return result;
		}
	}

	/**
	 * This method is used for counting total passed prime detections
	 * 
	 * @return # of passed prime detections
	 */
	
	public int getPrimeTestPassTimes() {
		synchronized (sqlLock) {
			db = dbHelper.getReadableDatabase();
			String sql = "SELECT * FROM TestResult WHERE isPrime = 1 AND result = 0";
			Cursor cursor = db.rawQuery(sql, null);
			int count = cursor.getCount();
			cursor.close();
			db.close();
			return count;
		}
	}

	/**
	 * This method is used for checking if the user can replace the current
	 * detection
	 * 
	 * @return true if the user is allowed to replace the detection
	 */
	public boolean canTryAgain() {
		synchronized (sqlLock) {
			TimeValue curTV = TimeValue.generate(System.currentTimeMillis());
			int year = curTV.getYear();
			int month = curTV.getMonth();
			int day = curTV.getDay();
			//int timeslot = curTV.getTimeslot();
			db = dbHelper.getReadableDatabase();
			String sql = "SELECT * FROM TestResult WHERE year=" + year
					+ " AND month=" + month + " AND day=" + day;
			Cursor cursor = db.rawQuery(sql, null);
			return (cursor.getCount() == 1);
		}
	}
	
	
	
	// **** NoteAdd ****

	/**
	 * Get the latest NoteAdd
	 * 
	 * @return NoteAdd.
	 *
	 * @see com.ubicomp.ketdiary.data.structure.NoteAdd
	 */
	
	public NoteAdd getLatestNoteAdd() {
		synchronized (sqlLock) {
			db = dbHelper.getReadableDatabase();
			String sql;
			Cursor cursor;
			sql = "SELECT * FROM NoteAdd ORDER BY ts DESC LIMIT 1";
			cursor = db.rawQuery(sql, null);
			if (!cursor.moveToFirst()) {
				cursor.close();
				db.close();
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(0);
				int year = cal.get(Calendar.YEAR);
				int month = cal.get(Calendar.MONTH);
				int day = cal.get(Calendar.DAY_OF_MONTH);
				
				return new NoteAdd(0, 0, year, month, day,0 , 0, 0, 0, 0, null,null,null,0, 0, 0, 0);
			}
			int isAfterTest = cursor.getInt(1);
			long ts = cursor.getLong(5);
			int year = cursor.getInt(7);
			int month = cursor.getInt(8);
			int day = cursor.getInt(9);
			int timeslot = cursor.getInt(10);
			int category = cursor.getInt(11);
			int type = cursor.getInt(12);
			int items = cursor.getInt(13);
			int impact = cursor.getInt(14);
			//String reason = cursor.getString(15);
			String action = cursor.getString(15);
			String feeling = cursor.getString(16);
			String thinking = cursor.getString(17);
			int finished = cursor.getInt(18);
			int weeklyScore = cursor.getInt(19);
			int score = cursor.getInt(20);
			int key = cursor.getInt(21);
			NoteAdd noteAdd = new NoteAdd(isAfterTest, ts, year, month, day, timeslot, 
					category, type, items, impact, action, feeling,thinking,finished, weeklyScore, score, key);
			
			cursor.close();
			db.close();
			return noteAdd;
			
		}
	}

	/**
	 * Insert an NoteAdd result
	 * 
	 * @return # of credits got by the user
	 * @see com.ubicomp.ketdiary.data.structure.NoteAdd
	 */
	
	public int insertNoteAdd(NoteAdd data) {
		insertHistoryWithNoteAdd(data);

		synchronized (sqlLock) {
			NoteAdd prev_data = getLatestNoteAdd();
			int addScore = 0;
			if (!prev_data.getTv().isSameTimeBlock(data.getTv()))
				addScore = 1;
			if (!StartDateCheck.afterStartDate())
				addScore = 0;

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
			content.put("weeklyScore", prev_data.getWeeklyScore() + addScore);
			content.put("score", prev_data.getScore() + addScore);
			content.put("relationKey", data.getKey());
			content.put("acceptance", 0);
			db.insert("NoteAdd", null, content);
			db.close();
			return addScore;
		}
	}

	/**
	 * Get all NoteAdd results which are not uploaded to the server
	 * 
	 * @return An array of NoteAdd results If there are no
	 *         NoteAdd, return null.
	 * @see com.ubicomp.ketdiary.data.structure.NoteAdd
	 */
	
	public NoteAdd[] getNotUploadedNoteAdd() {
		synchronized (sqlLock) {
			NoteAdd[] data = null;

			db = dbHelper.getReadableDatabase();
			String sql;
			Cursor cursor;

			sql = "SELECT * FROM NoteAdd WHERE upload = 0";
			cursor = db.rawQuery(sql, null);
			int count = cursor.getCount();
			if (count == 0) {
				cursor.close();
				db.close();
				return null;
			}

			data = new NoteAdd[count];

			for (int i = 0; i < count; ++i) {
				cursor.moveToPosition(i);
				int isAfterTest = cursor.getInt(1);
				long ts = cursor.getLong(5);
				int year = cursor.getInt(7);
				int month = cursor.getInt(8);
				int day = cursor.getInt(9);
				int timeslot=cursor.getInt(10);
				int category = cursor.getInt(11);
				int type = cursor.getInt(12);
				int items = cursor.getInt(13);
				int impact = cursor.getInt(14);
				String action = cursor.getString(15);
				String feeling = cursor.getString(16);
				String thinking = cursor.getString(17);
				int finished = cursor.getInt(18);
				
				int weeklyScore = cursor.getInt(19);
				int score = cursor.getInt(20);
				int key = cursor.getInt(21);
				data[i] = new NoteAdd(isAfterTest, ts, year, month, day, timeslot, category, type, items, impact, action, feeling, thinking, finished, weeklyScore, score, key);
			}

			cursor.close();
			db.close();

			return data;
		}
	}
	
	public NoteAdd[] getNoteAddType(int _type) {
		synchronized (sqlLock) {
			NoteAdd[] data = null;

			db = dbHelper.getReadableDatabase();
			String sql;
			Cursor cursor;

			sql = "SELECT * FROM NoteAdd WHERE items = "+_type;
			cursor = db.rawQuery(sql, null);
			int count = cursor.getCount();
			if (count == 0) {
				cursor.close();
				db.close();
				return null;
			}

			data = new NoteAdd[count];

			for (int i = 0; i < count; ++i) {
				cursor.moveToPosition(i);
				int isAfterTest = cursor.getInt(1);
				long ts = cursor.getLong(5);
				int year = cursor.getInt(7);
				int month = cursor.getInt(8);
				int day = cursor.getInt(9);
				int timeslot=cursor.getInt(10);
				int category = cursor.getInt(11);
				int type = cursor.getInt(12);
				int items = cursor.getInt(13);
				int impact = cursor.getInt(14);
				String action = cursor.getString(15);
				String feeling = cursor.getString(16);
				String thinking = cursor.getString(17);
				int finished = cursor.getInt(18);
				
				int weeklyScore = cursor.getInt(19);
				int score = cursor.getInt(20);
				int key = cursor.getInt(21);
				data[i] = new NoteAdd(isAfterTest, ts, year, month, day, timeslot, category, type, items, impact, action, feeling, thinking, finished, weeklyScore, score, key);
			}

			cursor.close();
			db.close();

			return data;
		}
	}
	
	public NoteAdd[] getNotUploadedNoteAdd2() {
		synchronized (sqlLock) {
			NoteAdd[] data = null;

			db = dbHelper.getReadableDatabase();
			String sql;
			Cursor cursor;

			sql = "SELECT * FROM NoteAdd WHERE upload = 2";
			cursor = db.rawQuery(sql, null);
			int count = cursor.getCount();
			if (count == 0) {
				cursor.close();
				db.close();
				return null;
			}

			data = new NoteAdd[count];

			for (int i = 0; i < count; ++i) {
				cursor.moveToPosition(i);
				int isAfterTest = cursor.getInt(1);
				long ts = cursor.getLong(5);
				int year = cursor.getInt(7);
				int month = cursor.getInt(8);
				int day = cursor.getInt(9);
				int timeslot=cursor.getInt(10);
				int category = cursor.getInt(11);
				int type = cursor.getInt(12);
				int items = cursor.getInt(13);
				int impact = cursor.getInt(14);
				String action = cursor.getString(15);
				String feeling = cursor.getString(16);
				String thinking = cursor.getString(17);
				int finished = cursor.getInt(18);
				
				int weeklyScore = cursor.getInt(19);
				int score = cursor.getInt(20);
				int key = cursor.getInt(21);
				data[i] = new NoteAdd(isAfterTest, ts, year, month, day, timeslot, category, type, items, impact, action, feeling, thinking, finished, weeklyScore, score, key);
			}

			cursor.close();
			db.close();

			return data;
		}
	}

	/**
	 * Label the NoteAdd result uploaded
	 *
	 * @param ts
	 *            Timestamp of the uploaded EmotionManagement
	 * @see com.ubicomp.ketdiary.data.structure.NoteAdd
	 */
	
	public void setNoteAddUploaded(long ts) {
		synchronized (sqlLock) {
			db = dbHelper.getWritableDatabase();
			String sql = "UPDATE NoteAdd SET upload = 1 WHERE ts = "
					+ ts;
			db.execSQL(sql);
			db.close();
		}
	}

	/**
	 * Know the NoteAdd reflect or not
	 *
	 *
	 *
	 * @see com.ubicomp.ketdiary.data.structure.NoteAdd
	 */
	public boolean getNoteAddReflection(int relation_key) {
		synchronized (sqlLock) {
			NoteAdd[] data = null;

			db = dbHelper.getReadableDatabase();
			String sql;
			Cursor cursor;

			sql = "SELECT * FROM Reflection WHERE relationKey = " + relation_key;
			cursor = db.rawQuery(sql, null);
			int count = cursor.getCount();
			if (count == 0) {
				cursor.close();
				db.close();
				return false;
			}

			return true;
		}
	}
	
	public Reflection getNoteAddLastestReflection(int relation_key) {
		synchronized (sqlLock) {

			db = dbHelper.getReadableDatabase();
			String sql;
			Cursor cursor;

			sql = "SELECT * FROM Reflection WHERE relationKey = " + relation_key + " ORDER BY ts DESC";
			cursor = db.rawQuery(sql, null);
			int count = cursor.getCount();
			if (count == 0) {
				cursor.close();
				db.close();
				return null;
			}
			cursor.moveToPosition(0);
			long ts = cursor.getLong(1);
			String action = cursor.getString(2);
			String feeling = cursor.getString(3);
			String thinking = cursor.getString(4);
			int key = cursor.getInt(5);
			Reflection data = new Reflection(ts, action, feeling, thinking, key);
			
			return data;

		}
	}

	
	public NoteAdd[] getAllNoteAdd() {
		synchronized (sqlLock) {
			db = dbHelper.getReadableDatabase();
			String sql = "SELECT * FROM NoteAdd WHERE impact >= 0 AND items > 0 ORDER BY recordYear, recordMonth, recordDay, timeslot ASC"; // TODO: Just get useful data
			Cursor cursor = db.rawQuery(sql, null);
			int count = cursor.getCount();
			if (count == 0) {
				cursor.close();
				db.close();
				return null;
			}

			NoteAdd[] noteAdd = new NoteAdd[count];
			for (int i = 0; i < count; ++i) {
				cursor.moveToPosition(i);
				int isAfterTest = cursor.getInt(1);
				long ts = cursor.getLong(5);
				int year = cursor.getInt(7);
				int month = cursor.getInt(8);
				int day = cursor.getInt(9);
				int timeslot = cursor.getInt(10);
				int category = cursor.getInt(11);
				int type = cursor.getInt(12);
				int items = cursor.getInt(13);
				int impact = cursor.getInt(14);
				//String reason = cursor.getString(15);
				String action = cursor.getString(15);
				String feeling = cursor.getString(16);
				String thinking = cursor.getString(17);
				int finished = cursor.getInt(18);
				int weeklyScore = cursor.getInt(19);
				int score = cursor.getInt(20);
				int key = cursor.getInt(21); 
				noteAdd[i] = new NoteAdd(isAfterTest, ts, year, month, day, 
						timeslot, category, type, items, impact, action, feeling, thinking, finished, weeklyScore, score, key);
			}

			cursor.close();
			db.close();
			return noteAdd;
		}
	}
	
	public NoteAdd[] getAllFinishedNoteAdd() {
		synchronized (sqlLock) {
			db = dbHelper.getReadableDatabase();
			String sql = "SELECT * FROM NoteAdd WHERE impact >= 0 AND items > 0 AND finished == 1 ORDER BY recordYear, recordMonth, recordDay, timeslot ASC"; // TODO: Just get useful data
			Cursor cursor = db.rawQuery(sql, null);
			int count = cursor.getCount();
			if (count == 0) {
				cursor.close();
				db.close();
				return null;
			}

			NoteAdd[] noteAdd = new NoteAdd[count];
			for (int i = 0; i < count; ++i) {
				cursor.moveToPosition(i);
				int isAfterTest = cursor.getInt(1);
				long ts = cursor.getLong(5);
				int year = cursor.getInt(7);
				int month = cursor.getInt(8);
				int day = cursor.getInt(9);
				int timeslot = cursor.getInt(10);
				int category = cursor.getInt(11);
				int type = cursor.getInt(12);
				int items = cursor.getInt(13);
				int impact = cursor.getInt(14);
				//String reason = cursor.getString(15);
				String action = cursor.getString(15);
				String feeling = cursor.getString(16);
				String thinking = cursor.getString(17);
				int finished = cursor.getInt(18);
				int weeklyScore = cursor.getInt(19);
				int score = cursor.getInt(20);
				int key = cursor.getInt(21); 
				noteAdd[i] = new NoteAdd(isAfterTest, ts, year, month, day, 
						timeslot, category, type, items, impact, action, feeling, thinking, finished, weeklyScore, score, key);
			}

			cursor.close();
			db.close();
			return noteAdd;
		}
	}
	
	
	/**
	 * Get NoteAdd results by date
	 * 
	 * @param rYear
	 *            record Year
	 * @param rMonth
	 *            record Month (0~11)
	 * @param rDay
	 *            record Day of Month
	 *
	 * @see com.ubicomp.ketdiary.data.structure.NoteAdd
	 */
	
	public NoteAdd[] getDayNoteAdd(int rYear, int rMonth, int rDay) {
		synchronized (sqlLock) {
			NoteAdd[] data = null;

			db = dbHelper.getReadableDatabase();
			String sql;
			Cursor cursor;

			sql = "SELECT * FROM NoteAdd WHERE recordYear = " + rYear
					+ " AND recordMonth = " + rMonth + " AND recordDay = "
					+ rDay +" AND type > -1 ORDER BY id DESC";
			cursor = db.rawQuery(sql, null);
			int count = cursor.getCount();
			if (count == 0) {
				cursor.close();
				db.close();
				return null;
			}

			data = new NoteAdd[count];

			for (int i = 0; i < count; ++i) {
				cursor.moveToPosition(i);
				int isAfterTest = cursor.getInt(1);
				long ts = cursor.getLong(5);
				int year = cursor.getInt(7);
				int month = cursor.getInt(8);
				int day = cursor.getInt(9);
				int timeslot = cursor.getInt(10);
				int category = cursor.getInt(11);
				int type = cursor.getInt(12);
				int items = cursor.getInt(13);
				int impact = cursor.getInt(14);
				//String reason = cursor.getString(15);
				String action = cursor.getString(15);
				String feeling = cursor.getString(16);
				String thinking = cursor.getString(17);
				int finished = cursor.getInt(18);
				int weeklyScore = cursor.getInt(19);
				int score = cursor.getInt(20);
				int key = cursor.getInt(21);
				data[i] = new NoteAdd(isAfterTest, ts, year, month, day, 
						timeslot, category, type, items, impact, action, feeling, thinking, finished, weeklyScore, score, key);
			}

			cursor.close();
			db.close();

			return data;
		}
	}
	
	
	public NoteAdd[] getDayNoteAddbyCategory(int rYear, int rMonth, int rDay, int category) {
		synchronized (sqlLock) {
			NoteAdd[] data = null;

			db = dbHelper.getReadableDatabase();
			String sql;
			Cursor cursor;

			sql = "SELECT * FROM NoteAdd WHERE recordYear = " + rYear
					+ " AND recordMonth = " + rMonth + " AND recordDay = "
					+ rDay +" AND category = " + category + " ORDER BY id DESC";
			cursor = db.rawQuery(sql, null);
			int count = cursor.getCount();
			if (count == 0) {
				cursor.close();
				db.close();
				return null;
			}

			data = new NoteAdd[count];

			for (int i = 0; i < count; ++i) {
				cursor.moveToPosition(i);
				int isAfterTest = cursor.getInt(1);
				long ts = cursor.getLong(5);
				int year = cursor.getInt(7);
				int month = cursor.getInt(8);
				int day = cursor.getInt(9);
				int timeslot = cursor.getInt(10);
				int category2 = cursor.getInt(11);
				int type = cursor.getInt(12);
				int items = cursor.getInt(13);
				int impact = cursor.getInt(14);
				//String reason = cursor.getString(15);
				String action = cursor.getString(15);
				String feeling = cursor.getString(16);
				String thinking = cursor.getString(17);
				int finished = cursor.getInt(18);
				int weeklyScore = cursor.getInt(19);
				int score = cursor.getInt(20);
				int key = cursor.getInt(21);
				data[i] = new NoteAdd(isAfterTest, ts, year, month, day, 
						timeslot, category2, type, items, impact, action, feeling,thinking, finished, weeklyScore, score, key);
			}

			cursor.close();
			db.close();

			return data;
		}
	}
	
	
	/**
	 * Get if there are EmotionManagement results at the date
	 * 
	 * @param ts
	 *            TimeValue of the date
	 * @return true if exists EmotionManagement
	 * @see com.ubicomp.ketdiary.data.structure.NoteAdd
	 */
	
	public NoteAdd getTsNoteAdd(long ts) {
		synchronized (sqlLock) {
			NoteAdd data = null;
			
			db = dbHelper.getReadableDatabase();
			String sql;
			Cursor cursor;

			sql = "SELECT * FROM NoteAdd WHERE" + " ts ="
					+ ts  ;
			cursor = db.rawQuery(sql, null);
			int count = cursor.getCount();
			if (!cursor.moveToFirst()) {
				cursor.close();
				db.close();
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(0);
				int year = cal.get(Calendar.YEAR);
				int month = cal.get(Calendar.MONTH);
				int day = cal.get(Calendar.DAY_OF_MONTH);
				
				return new NoteAdd(0, 0, year, month, day, -1 , -1, -1, -1, -1, null,null,null,0, 0, 0, 0);
			}
			int isAfterTest = cursor.getInt(1);
			//long ts = cursor.getLong(5);
			int year = cursor.getInt(7);
			int month = cursor.getInt(8);
			int day = cursor.getInt(9);
			int timeslot = cursor.getInt(10);
			int category = cursor.getInt(11);
			int type = cursor.getInt(12);
			int items = cursor.getInt(13);
			int impact = cursor.getInt(14);
			//String reason = cursor.getString(15);
			String action = cursor.getString(15);
			String feeling = cursor.getString(16);
			String thinking = cursor.getString(17);
			int finished = cursor.getInt(18);
			int weeklyScore = cursor.getInt(19);
			int score = cursor.getInt(20);
			int key = cursor.getInt(21);
			NoteAdd noteAdd = new NoteAdd(isAfterTest, ts, year, month, day, timeslot, 
					category, type, items, impact, action, feeling, thinking, finished, weeklyScore, score, key);
			
			cursor.close();
			db.close();
			return noteAdd;
		}
	}
	
	
	public NoteAdd[] getDayNoteAddPos(int rYear, int rMonth, int rDay) {
		synchronized (sqlLock) {
			NoteAdd[] data = null;

			db = dbHelper.getReadableDatabase();
			String sql;
			Cursor cursor;

			sql = "SELECT * FROM NoteAdd WHERE recordYear = " + rYear
					+ " AND recordMonth = " + rMonth + " AND recordDay = "
					+ rDay +" AND type > -1 ORDER BY id DESC";
			cursor = db.rawQuery(sql, null);
			int count = cursor.getCount();
			if (count == 0) {
				cursor.close();
				db.close();
				return null;
			}

			data = new NoteAdd[count];

			for (int i = 0; i < count; ++i) {
				cursor.moveToPosition(i);
				int isAfterTest = cursor.getInt(1);
				long ts = cursor.getLong(5);
				int year = cursor.getInt(7);
				int month = cursor.getInt(8);
				int day = cursor.getInt(9);
				int timeslot = cursor.getInt(10);
				int category = cursor.getInt(11);
				int type = cursor.getInt(12);
				int items = cursor.getInt(13);
				int impact = cursor.getInt(14);
				//String reason = cursor.getString(15);
				String action = cursor.getString(15);
				String feeling = cursor.getString(16);
				String thinking = cursor.getString(17);
				int finished = cursor.getInt(18);
				int weeklyScore = cursor.getInt(19);
				int score = cursor.getInt(20);
				int key = cursor.getInt(21);
				data[i] = new NoteAdd(isAfterTest, ts, year, month, day, 
						timeslot, category, type, items, impact, action, feeling, thinking, finished, weeklyScore, score, key);
			}

			cursor.close();
			db.close();

			return data;
		}
	}
	
	public int[] getNoteAddTypeRank(long start_ts, long end_ts) {
		synchronized (sqlLock) {
			NoteAdd[] data = null;
			int[] rank = new int[8];

			db = dbHelper.getReadableDatabase();
			String sql;
			Cursor cursor;
			
			
			for(int i=1; i<=8; i++){
				sql = "SELECT * FROM NoteAdd WHERE type= "+i+" ORDER BY id DESC";
				cursor = db.rawQuery(sql, null);
				int count = cursor.getCount();
				rank[i-1] = count;
			}
			
			int max=0;
			int max_index=-1;
			for(int i=0; i<8; i++){
				Log.d(TAG,"type "+(i+1)+"count "+rank[i]);
				if(rank[i] > max){
					max = rank[i];
					max_index = i;
				}	
			}
			int[] result = new int[4];
			result[0] = max_index;
			int j=0;
			for(int i=0; i<8; i++){
				if(j<3){
					if(rank[i] < max){
						result[++j] = i;
					}
				}
			}
			for(int i=0; i<result.length;i++){
				Log.d(TAG, "result:"+result[i]);
			}
			return result;
		}
	}

	/**
	 * Get the latest 4 reasons of EmotionManagement by reason type
	 * 
	 * @param type
	 *            reason type of EmotionManagement.
	 * @return An array of reasons. There are no reasons, return null
	 */
	/*
	public String[] getEmotionManagementString(int type) {
		synchronized (sqlLock) {
			db = dbHelper.getReadableDatabase();
			String sql = "SELECT DISTINCT reason FROM EmotionManagement WHERE type = "
					+ type + " ORDER BY ts DESC LIMIT 4";
			String[] out = null;

			Cursor cursor = db.rawQuery(sql, null);
			if (cursor.getCount() == 0) {
				cursor.close();
				db.close();
				return null;
			}
			out = new String[cursor.getCount()];

			for (int i = 0; i < out.length; ++i)
				if (cursor.moveToPosition(i))
					out[i] = cursor.getString(0);

			cursor.close();
			db.close();
			return out;
		}
	}*/

	/**
	 * Get if there are EmotionManagement results at the date
	 * 
	 * @param tv
	 *            TimeValue of the date
	 * @return true if exists EmotionManagement
	 * @see ubicomp.soberdiary.data.structure.TimeValue
	 */
	/*
	public boolean hasEmotionManagement(TimeValue tv) {
		synchronized (sqlLock) {
			db = dbHelper.getReadableDatabase();
			String sql;
			Cursor cursor;

			sql = "SELECT * FROM EmotionManagement WHERE" + " recordYear ="
					+ tv.getYear() + " AND recordMonth=" + tv.getMonth()
					+ " AND recordDay =" + tv.getDay();
			cursor = db.rawQuery(sql, null);
			int count = cursor.getCount();
			cursor.close();
			db.close();
			return count > 0;
		}
	}*/
	
	
	// Ranking

	/**
	 * Get the user's rank
	 * 
	 * @return Rank. If there are no data, return dummy data with UID=""
	 * @see com.ubicomp.ketdiary.data.structure.Rank
	 */
	public Rank getMyRank() {
		synchronized (sqlLock) {
			db = dbHelper.getReadableDatabase();
			String sql = "SELECT * FROM Ranking WHERE user_id='"
					+ PreferenceControl.getUID() + "'";
			Cursor cursor = db.rawQuery(sql, null);
			if (!cursor.moveToFirst()) {
				cursor.close();
				db.close();
				return new Rank("", 0);
			}
			String uid = cursor.getString(0);
			int score = cursor.getInt(1);
			int test = cursor.getInt(2);
			int note = cursor.getInt(3);
			int question = cursor.getInt(4);
			int coping = cursor.getInt(5);
			int[] additionals = new int[4];
			for (int j = 0; j < additionals.length; ++j)
				additionals[j] = cursor.getInt(6 + j);
			Rank rank = new Rank(uid, score, test, note, question, coping, additionals);
			cursor.close();
			db.close();
			return rank;
		}
	}

	/**
	 * Get all user's ranks
	 * 
	 * @return An array of Rank. If there are no Rank, return null.
	 * @see com.ubicomp.ketdiary.data.structure.Rank
	 */
	public Rank[] getAllRanks() {
		synchronized (sqlLock) {
			Rank[] ranks = null;
			db = dbHelper.getReadableDatabase();
			String sql = "SELECT * FROM Ranking ORDER BY total_score DESC, user_id ASC";
			Cursor cursor = db.rawQuery(sql, null);
			int count = cursor.getCount();
			if (count == 0) {
				cursor.close();
				db.close();
				return null;
			}
			ranks = new Rank[count];
			for (int i = 0; i < count; ++i) {
				cursor.moveToPosition(i);
				String uid = cursor.getString(0);
				int score = cursor.getInt(1);
				int test = cursor.getInt(2);
				int note = cursor.getInt(3);
				int question = cursor.getInt(4);
				int coping = cursor.getInt(5);
				int[] additionals = new int[4];
				for (int j = 0; j < additionals.length; ++j)
					additionals[j] = cursor.getInt(5 + j);
				ranks[i] = new Rank(uid, score, test, note, question, coping, additionals);
			}
			cursor.close();
			db.close();
			return ranks;
		}
	}

	/**
	 * Get the user's rank in a short period
	 * 
	 * @return An array of Rank. If there are no Rank, return null.
	 * @see com.ubicomp.ketdiary.data.structure.Rank
	 */
	public Rank[] getAllRankShort() {
		synchronized (sqlLock) {
			Rank[] ranks = null;
			db = dbHelper.getReadableDatabase();
			String sql = "SELECT * FROM RankingShort ORDER BY total_score DESC, user_id ASC";
			Cursor cursor = db.rawQuery(sql, null);
			int count = cursor.getCount();
			if (count == 0) {
				cursor.close();
				db.close();
				return null;
			}
			ranks = new Rank[count];
			for (int i = 0; i < count; ++i) {
				cursor.moveToPosition(i);
				String uid = cursor.getString(0);
				int score = cursor.getInt(1);
				ranks[i] = new Rank(uid, score);
			}
			cursor.close();
			db.close();
			return ranks;
		}
	}

	/**
	 * Truncate the Ranking table
	 * 
	 * @see com.ubicomp.ketdiary.data.structure.Rank
	 */
	public void clearRank() {
		synchronized (sqlLock) {
			db = dbHelper.getWritableDatabase();
			String sql = "DELETE  FROM Ranking";
			db.execSQL(sql);
			db.close();
		}
	}

	/**
	 * Update the Rank
	 * 
	 * @param data
	 *            Updated Rank
	 * @see com.ubicomp.ketdiary.data.structure.Rank
	 */
	public void updateRank(Rank data) {
		synchronized (sqlLock) {
			db = dbHelper.getWritableDatabase();
			String sql = "SELECT * FROM Ranking WHERE user_id = '"
					+ data.getUid() + "'";
			Cursor cursor = db.rawQuery(sql, null);
			if (cursor.getCount() == 0) {
				ContentValues content = new ContentValues();
				content.put("user_id", data.getUid());
				content.put("total_score", data.getScore());
				content.put("test_score", data.getTest());
				content.put("note_score", data.getNote());
				content.put("question_score", data.getQuestion());
				content.put("coping_score", data.getCoping());
				content.put("times_score", data.getTestTimes());
				content.put("pass_score", data.getTestPass());
				content.put("normalQ_score", data.getNormalQ());
				content.put("randomQ_score", data.getRandomQ());
				
				db.insert("Ranking", null, content);
			} else {
				sql = "UPDATE Ranking SET" + " total_score = "
						+ data.getScore() + "," + " test_score = "
						+ data.getTest() + "," + " note_score = "
						+ data.getNote() + "," + " question_score="
						+ data.getQuestion() + "," + " coping_score = "
						+ data.getCoping() + "," + " times_score = "
						+ data.getTestTimes() + "," + " pass_score = "
						+ data.getTestPass() + "," + " normalQ_score = "
						+ data.getNormalQ() + "," + " randomQ_score = "
						+ data.getRandomQ()
						+ " WHERE user_id = " + "'" + data.getUid() + "'";
				db.execSQL(sql);
			}
			cursor.close();
			db.close();
		}
	}

	/**
	 * Truncate the RankingShort table
	 * 
	 * @see com.ubicomp.ketdiary.data.structure.Rank
	 */
	public void clearRankShort() {
		synchronized (sqlLock) {
			db = dbHelper.getWritableDatabase();
			String sql = "DELETE  FROM RankingShort";
			db.execSQL(sql);
			db.close();
		}
	}

	/**
	 * Update the Rank in a short period
	 * 
	 * @param data
	 *            Updated Rank in a short period
	 * @see com.ubicomp.ketdiary.data.structure.Rank
	 */
	public void updateRankShort(Rank data) {
		synchronized (sqlLock) {
			db = dbHelper.getWritableDatabase();
			String sql = "SELECT * FROM RankingShort WHERE user_id = '"
					+ data.getUid() + "'";
			Cursor cursor = db.rawQuery(sql, null);
			if (cursor.getCount() == 0) {
				ContentValues content = new ContentValues();
				content.put("user_id", data.getUid());
				content.put("total_score", data.getScore());
				db.insert("RankingShort", null, content);
			} else {
				sql = "UPDATE RankingShort SET" + " total_score = "
						+ data.getScore() + " WHERE user_id = " + "'"
						+ data.getUid() + "'";
				db.execSQL(sql);
			}
			cursor.close();
			db.close();
		}
	}
	
	
	
	
	 
	//Cassette   //TODO: Working on here
	
	public Cassette[] getAllCassette() {
		synchronized (sqlLock) {
			Cassette[] cassette = null;
			db = dbHelper.getReadableDatabase();
			String sql = "SELECT * FROM Cassette ORDER BY ts DESC";
			Cursor cursor = db.rawQuery(sql, null);
			int count = cursor.getCount();
			if (count == 0) {
				cursor.close();
				db.close();
				return null;
			}
			cassette = new Cassette[count];
			for (int i = 0; i < count; ++i) {
				cursor.moveToPosition(i);
				long ts = cursor.getLong(1);
				String cid = cursor.getString(2);
				int isUsed = cursor.getInt(3);
				cassette[i] = new Cassette(ts, isUsed, cid);
			}
			cursor.close();
			db.close();
			return cassette;
		}
	}
	
	public void modifyCassetteById(String casseteId, int isUsed) {
		synchronized (sqlLock) {

			db = dbHelper.getReadableDatabase();
			String sql;
			//sql = "SELECT * FROM TestResult WHERE ts = " + ts + " AND isPrime = 1";
			sql = "UPDATE Cassette SET isUsed = " + isUsed + " WHERE cassetteId = '" + casseteId + "'";
			db.execSQL(sql);
			db.close();

			return ;
		}
	}
	
	public void deleteCassetteById(String casseteeId) {
		synchronized (sqlLock) {

			db = dbHelper.getReadableDatabase();
			String sql;
			//sql = "SELECT * FROM TestResult WHERE ts = " + ts + " AND isPrime = 1";
			sql = "DELETE FROM Cassette WHERE cassetteId= '" + casseteeId + "'";
			db.execSQL(sql);
			db.close();

			return ;
		}
	}

	
	
	public void insertCassette(String cassette_id ){
		db = dbHelper.getWritableDatabase();
		ContentValues content = new ContentValues();
		content.put("cassetteId", cassette_id);
		content.put("isUsed", 1);
		content.put("ts", 0);
		db.insert("Cassette", null, content);
		db.close();
		return ;
			
	}
	
	public boolean checkCassette(String cassette_id){
		synchronized (sqlLock) {
			db = dbHelper.getWritableDatabase();
			String sql = "SELECT * FROM Cassette WHERE cassetteId = '"
					+ cassette_id + "'" + " AND isUsed = 1";
			Cursor cursor = db.rawQuery(sql, null);
			boolean check;
			if (cursor.getCount() == 0) {
				check = true;
			}
			else{
				check = false;
			}
			cursor.close();
			db.close();
			return check;
		}
	}
	
	/**
	 * Truncate the RankingShort table
	 * 
	 * @see com.ubicomp.ketdiary.data.structure.Cassette
	 */
	public void clearCassette() {
		synchronized (sqlLock) {
			db = dbHelper.getWritableDatabase();
			String sql = "DELETE FROM Cassette";
			db.execSQL(sql);
			db.close();
		}
	}

	/**
	 * Update CassetteId from Server 
	 * 
	 * @param data
	 *            Updated CassetteId in a short period
	 * @see com.ubicomp.ketdiary.data.structure.Cassette
	 */
	public void updateCassette(Cassette data) {
		synchronized (sqlLock) {
			db = dbHelper.getWritableDatabase();
			String sql = "SELECT * FROM Cassette WHERE cassetteId = '"
					+ data.getCassetteId() + "'";
			Cursor cursor = db.rawQuery(sql, null);
			if (cursor.getCount() == 0) {
				ContentValues content = new ContentValues();
				content.put("ts", data.getTv().getTimestamp());
				content.put("cassetteId", data.getCassetteId());
				content.put("isUsed", data.getisUsed());
				db.insert("Cassette", null, content);
			} else {
				sql = "UPDATE Cassette SET" + " ts = "
						+ data.getTv().getTimestamp() + "," + " isUsed = "
						+ data.getisUsed() + " WHERE cassetteId = " 
						+ "'"+ data.getCassetteId() + "'";
				db.execSQL(sql);
			}
			cursor.close();
			db.close();
		}
	}

	
	// TestDetail
	
	/**
	 * This method is used for the latest TestResult
	 *
	 * If there are no TestResult, return a dummy data.
	 * @see com.ubicomp.ketdiary.data.structure.TestResult
	 *
	 */
	
	public TestDetail getLatestTestDetail() {
		synchronized (sqlLock) {
			db = dbHelper.getReadableDatabase();
			String sql = "SELECT * FROM TestDetail ORDER BY ts DESC LIMIT 1";
			Cursor cursor = db.rawQuery(sql, null);
			if (!cursor.moveToFirst()) {
				cursor.close();
				db.close();
				return new TestDetail("", 0, 0, 0, 0, 0, 0, 0, "", "");
			}

			long ts = cursor.getLong(5);
			String cassetteId = cursor.getString(1) ;
			int failedState = cursor.getInt(7);
			int firstVoltage = cursor.getInt(8);
			int secondVoltage = cursor.getInt(9);
			int devicePower = cursor.getInt(10);
			int colorReading = cursor.getInt(11);
			float connectionFailRate = cursor.getFloat(12);
			String failedReason = cursor.getString(13);
			String hardwareVersion = cursor.getString(14);

			TestDetail testDetail = new TestDetail(cassetteId, ts, failedState, firstVoltage,
					secondVoltage, devicePower, colorReading,
	                connectionFailRate, failedReason, hardwareVersion);

			cursor.close();
			db.close();
			return testDetail;
		}
	}

		/**
		 * Insert a TestDetail recorded detailed information of breath condition
		 * when the user takes BrAC tests
		 * 
		 * @param data
		 *            inserted BreathDetail
		 * @see com.ubicomp.ketdiary.data.structure.TestDetail
		 */
		public void insertTestDetail(TestDetail data) {
			synchronized (sqlLock) {
				db = dbHelper.getWritableDatabase();

				String sql = "SELECT * FROM TestDetail WHERE ts ="
						+ data.getTv().getTimestamp();
				Cursor cursor = db.rawQuery(sql, null);
				if (!cursor.moveToFirst()) {
					ContentValues content = new ContentValues();
					content.put("year", data.getTv().getYear());
					content.put("month", data.getTv().getMonth());
					content.put("day", data.getTv().getDay());
					content.put("ts", data.getTv().getTimestamp());
					content.put("week", data.getTv().getWeek());
					content.put("cassetteId", data.getCassetteId());
					content.put("failedState", data.getFailedState());
					content.put("firstVoltage", data.getFirstVoltage());
					content.put("secondVoltage", data.getSecondVoltage());
					content.put("devicePower", data.getDevicePower());
					content.put("colorReading", data.getColorReading());
					content.put("connectionFailRate",
							data.getConnectionFailRate());
					content.put("failedReason", data.getFailedReason());
					content.put("hardwareVersion", data.getHardwareVersion());
					db.insert("TestDetail", null, content);
				}
				cursor.close();
				db.close();
			}
		}

		/**
		 * Get all TestDetail which are not uploaded to the server
		 * 
		 * @return An array of BreathDetail. If there are no BreathDetail, return
		 *         null.
		 * @see com.ubicomp.ketdiary.data.structure.TestDetail
		 */
		public TestDetail[] getNotUploadedTestDetail() {
			synchronized (sqlLock) {
				TestDetail[] data = null;
				db = dbHelper.getReadableDatabase();
				String sql;
				Cursor cursor;
				sql = "SELECT * FROM TestDetail WHERE upload = 0";
				cursor = db.rawQuery(sql, null);
				int count = cursor.getCount();
				if (count == 0) {
					cursor.close();
					db.close();
					return null;
				}

				data = new TestDetail[count];

				for (int i = 0; i < count; ++i) {
					cursor.moveToPosition(i);
					long ts = cursor.getLong(5);
					String cassetteId = cursor.getString(1) ;
					int failedState = cursor.getInt(7);
					int firstVoltage = cursor.getInt(8);
					int secondVoltage = cursor.getInt(9);
					int devicePower = cursor.getInt(10);
					int colorReading = cursor.getInt(11);
					float connectionFailRate = cursor.getFloat(12);
					String failedReason = cursor.getString(13);
					String hardwardVersion = cursor.getString(14);

					data[i] = new TestDetail(cassetteId, ts, failedState, firstVoltage,
							secondVoltage, devicePower, colorReading,
			                connectionFailRate, failedReason, hardwardVersion);
				}
				cursor.close();
				db.close();
				return data;
			}
		}

		/**
		 * Label the TestDetail uploaded
		 * 
		 * @param ts
		 *            Timestamp of the uploaded TestDetail
		 * @see com.ubicomp.ketdiary.data.structure.TestDetail
		 */
		public void setTestDetailUploaded(long ts) {
			synchronized (sqlLock) {
				db = dbHelper.getWritableDatabase();
				String sql = "UPDATE TestDetail SET upload = 1 WHERE ts = " + ts;
				db.execSQL(sql);
				db.close();
			}
		}


		
		// QuestionTest

		/**
		 * Get the latest StorytellingTest result
		 * 
		 * @return StorytellingTest. If there are no StorytellingTest, return a
		 *         dummy data.
		 * @see com.ubicomp.ketdiary.data.structure.QuestionTest
		 */
		public QuestionTest getLatestQuestionTest() {
			synchronized (sqlLock) {
				db = dbHelper.getReadableDatabase();
				String sql;
				Cursor cursor;
				sql = "SELECT * FROM QuestionTest WHERE isCorrect = 1 ORDER BY ts DESC LIMIT 1";
				cursor = db.rawQuery(sql, null);
				if (!cursor.moveToFirst()) {
					cursor.close();
					db.close();
					return new QuestionTest(0, 0, 0, "", 0, 0);
				}
				long ts = cursor.getLong(4);
				int type = cursor.getInt(7);
				int isCorrect = cursor.getInt(8);
				String selection = cursor.getString(9);
				int choose = cursor.getInt(10);
				int score = cursor.getInt(11);
				
				cursor.close();
				db.close();
				
				return new QuestionTest(ts, type, isCorrect, selection,
						choose, score);
			}
		}

		/**
		 * Insert a QuestionTest result
		 * 
		 * @return # of credits got by the user
		 * @param data
		 *            inserted StorytellingTest
		 * @see com.ubicomp.ketdiary.data.structure.QuestionTest
		 */
		public int insertQuestionTest(QuestionTest data) {
			synchronized (sqlLock) {
				QuestionTest prev_data = getLatestQuestionTest();
				int addScore = 0;
				if (!prev_data.getTv().isSameTimeBlock(data.getTv())
						&& (data.getisCorrect()==1) )
					addScore = 1;
				
				if(data.getQuestionType() == 1 && (data.getisCorrect()==1)){
					addScore = 3;
				}
				
				if (!StartDateCheck.afterStartDate())
					addScore = 0;
				
								
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
				content.put("score", prev_data.getScore() + addScore);
				db.insert("QuestionTest", null, content);
				db.close();
				return addScore;
			}
		}

		/**
		 * Get all StorytellingTest results which are not uploaded to the server
		 * 
		 * @return An array of StorytellingTest. If there are no StorytellingTest,
		 *         return null.
		 * @see com.ubicomp.ketdiary.data.structure.QuestionTest
		 */
		public QuestionTest[] getNotUploadedQuestionTest() {
			synchronized (sqlLock) {
				QuestionTest[] data = null;

				db = dbHelper.getReadableDatabase();
				String sql;
				Cursor cursor;

				sql = "SELECT * FROM QuestionTest WHERE upload = 0";
				cursor = db.rawQuery(sql, null);
				int count = cursor.getCount();
				if (count == 0) {
					cursor.close();
					db.close();
					return null;
				}

				data = new QuestionTest[count];

				for (int i = 0; i < count; ++i) {
					cursor.moveToPosition(i);
					long ts = cursor.getLong(4);
					int type = cursor.getInt(7);
					int isCorrect = cursor.getInt(8);
					String selection = cursor.getString(9);
					int choose = cursor.getInt(10);
					int score = cursor.getInt(11);
					data[i] = new QuestionTest(ts, type, isCorrect, selection,
							choose, score);
				}

				cursor.close();
				db.close();

				return data;
			}
		}

		/**
		 * Label the QuestionTest result uploaded
		 * 
		 * @param ts
		 *            Timestamp of the uploaded StorytellingTest
		 * @see com.ubicomp.ketdiary.data.structure.QuestionTest
		 */
		public void setQuestionTestUploaded(long ts) {
			synchronized (sqlLock) {
				db = dbHelper.getWritableDatabase();
				String sql = "UPDATE QuestionTest SET upload = 1 WHERE ts = "
						+ ts;
				db.execSQL(sql);
				db.close();
			}
		}
		
		/**
		 * This method is used for checking if the user do the brac detection at
		 * this time slot
		 * 
		 * @return true if the user do a brac detection at the current time slot
		 */
		
		public boolean randomQuestionDone() {
			synchronized (sqlLock) {
				db = dbHelper.getReadableDatabase();
				long ts = System.currentTimeMillis();
				TimeValue tv = TimeValue.generate(ts);
				String sql = "SELECT id FROM QuestionTest WHERE" + " year ="
						+ tv.getYear() + " AND month = " + tv.getMonth()
						+ " AND day= " + tv.getDay() + " AND questionType= 1";
				Cursor cursor = db.rawQuery(sql, null);
				boolean result = cursor.getCount() > 0;
				cursor.close();
				db.close();
				return result;
			}
		}
		
		// CopingSkill

		/**
		 * Get the latest CopingSkill result
		 * 
		 * @return CopingSkill. If there are no CopingSkill, return a dummy data.
		 * @see com.ubicomp.ketdiary.data.structure.CopingSkill
		 */
		public CopingSkill getLatestCopingSkill() {
			synchronized (sqlLock) {
				db = dbHelper.getReadableDatabase();
				String sql;
				Cursor cursor;
				sql = "SELECT * FROM CopingSkill ORDER BY ts DESC LIMIT 1";
				cursor = db.rawQuery(sql, null);
				if (!cursor.moveToFirst()) {
					cursor.close();
					db.close();
					return new CopingSkill(0, 0, 0, null, 0);
				}
				long ts = cursor.getLong(4);
				int skillType = cursor.getInt(7);
				int skillSelect = cursor.getInt(8);
				String recreation = cursor.getString(9);
				int score = cursor.getInt(10);
				
				cursor.close();
				db.close();
				
				return new CopingSkill(ts, skillType, skillSelect, recreation, score);
				
			}
		}

		/**
		 * Insert an CopingSkill result
		 * 
		 * @return # credits got by the user
		 * @see com.ubicomp.ketdiary.data.structure.CopingSkill
		 */
		public int insertCopingSkill(CopingSkill data) {
			synchronized (sqlLock) {
				CopingSkill prev_data = getLatestCopingSkill();
				int addScore = 0;
				if (!prev_data.getTv().isSameTimeBlock(data.getTv()))
					addScore = 1;
				if (!StartDateCheck.afterStartDate())
					addScore = 0;
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
				content.put("score", prev_data.getScore() + addScore);
				db.insert("CopingSkill", null, content);
				db.close();
				return addScore;
			}
		}

		/**
		 * Get all CopingSkill results which are not uploaded to the server
		 * 
		 * @return An array of EmotionDIY. If there are no EmotionDIY, return null.
		 * @see com.ubicomp.ketdiary.data.structure.CopingSkill
		 */
		public CopingSkill[] getNotUploadedCopingSkill() {
			synchronized (sqlLock) {
				CopingSkill[] data = null;

				db = dbHelper.getReadableDatabase();
				String sql;
				Cursor cursor;

				sql = "SELECT * FROM CopingSkill WHERE upload = 0";
				cursor = db.rawQuery(sql, null);
				int count = cursor.getCount();
				if (count == 0) {
					cursor.close();
					db.close();
					return null;
				}

				data = new CopingSkill[count];

				for (int i = 0; i < count; ++i) {
					cursor.moveToPosition(i);
					long ts = cursor.getLong(4);
					int skillType = cursor.getInt(7);
					int skillSelect = cursor.getInt(8);
					String recreation = cursor.getString(9);
					int score = cursor.getInt(10);
					data[i] = new CopingSkill(ts, skillType, skillSelect, recreation, score);
				}

				cursor.close();
				db.close();

				return data;
			}
		}

		/**
		 * Label the CopingSkill result uploaded
		 * 
		 * @param ts
		 *            Timestamp of the Emotion DIY result
		 * @see com.ubicomp.ketdiary.data.structure.CopingSkill
		 */
		public void setCopingSkillUploaded(long ts) {
			synchronized (sqlLock) {
				db = dbHelper.getWritableDatabase();
				String sql = "UPDATE CopingSkill SET upload = 1 WHERE ts = " + ts;
				db.execSQL(sql);
				db.close();
			}
		}
		
		// ExchangeHistory

		/**
		 * Insert a ExchangeHistory when the user exchanges credits for coupons
		 * 
		 * @param data
		 *            inserted ExchangeHistory
		 * @see com.ubicomp.ketdiary.data.structure.ExchangeHistory
		 */
		public void insertExchangeHistory(ExchangeHistory data) {
			synchronized (sqlLock) {
				db = dbHelper.getWritableDatabase();
				ContentValues content = new ContentValues();
				content.put("ts", data.getTv().getTimestamp());
				content.put("exchangeCounter", data.getExchangeNum());
				db.insert("ExchangeHistory", null, content);
				db.close();
			}
		}

		/**
		 * Get all ExchangeHistory which are not uploaded to the server
		 * 
		 * @return An array of ExchangeHistory. If there are no ExchangeHistory,
		 *         return null.
		 * @see com.ubicomp.ketdiary.data.structure.ExchangeHistory
		 */
		public ExchangeHistory[] getNotUploadedExchangeHistory() {
			synchronized (sqlLock) {
				ExchangeHistory[] data = null;
				db = dbHelper.getReadableDatabase();
				String sql;
				Cursor cursor;
				sql = "SELECT * FROM ExchangeHistory WHERE upload = 0";
				cursor = db.rawQuery(sql, null);
				int count = cursor.getCount();
				if (count == 0) {
					cursor.close();
					db.close();
					return null;
				}

				data = new ExchangeHistory[count];

				for (int i = 0; i < count; ++i) {
					cursor.moveToPosition(i);
					long ts = cursor.getLong(1);
					int exchangeCounter = cursor.getInt(2);
					data[i] = new ExchangeHistory(ts, exchangeCounter);
				}
				cursor.close();
				db.close();
				return data;
			}
		}

		/**
		 * Label the ExchangeHistory uploaded
		 * 
		 * @param ts
		 *            Timestamp of the uploaded ExchangeHistory
		 * @see com.ubicomp.ketdiary.data.structure.ExchangeHistory
		 */
		public void setExchangeHistoryUploaded(long ts) {
			synchronized (sqlLock) {
				db = dbHelper.getWritableDatabase();
				String sql = "UPDATE ExchangeHistory SET upload = 1 WHERE ts = "
						+ ts;
				db.execSQL(sql);
				db.close();
			}
		}
		
		
		/**
		 * Insert a Appeal
		 * 
		 * @param data
		 *            inserted Appeal
		 * @see com.ubicomp.ketdiary.data.structure.Appeal
		 */
		public void insertAppeal(Appeal data) {
			synchronized (sqlLock) {
				db = dbHelper.getWritableDatabase();

				ContentValues content = new ContentValues();
				content.put("ts", data.getTv().getTimestamp());
				content.put("appealType", data.getAppealType());
				content.put("appealTimes", data.getAppealTimes());
				db.insert("Appeal", null, content);
				db.close();
				
			}
		}
		
		/**
		 * Get all Appeal which are not uploaded to the server
		 * 
		 * @return An array of Appeal. If there are no Appeal,
		 *         return null.
		 * @see com.ubicomp.ketdiary.data.structure.Appeal
		 */
		public Appeal[] getNotUploadedAppeal() {
			synchronized (sqlLock) {
				Appeal[] data = null;
				db = dbHelper.getReadableDatabase();
				String sql;
				Cursor cursor;
				sql = "SELECT * FROM Appeal WHERE upload = 0";
				cursor = db.rawQuery(sql, null);
				int count = cursor.getCount();
				if (count == 0) {
					cursor.close();
					db.close();
					return null;
				}

				data = new Appeal[count];

				for (int i = 0; i < count; ++i) {
					cursor.moveToPosition(i);
					long ts = cursor.getLong(1);
					int _type = cursor.getInt(2);
					int _time = cursor.getInt(3);
					data[i] = new Appeal(ts, _type, _time);
				}
				cursor.close();
				db.close();
				return data;
			}
		}
		
		/**
		 * Label the Appeal uploaded
		 * 
		 * @param ts
		 *            Timestamp of the uploaded Appeal
		 * @see com.ubicomp.ketdiary.data.structure.Appeal
		 */
		public void setAppealUploaded(long ts) {
			synchronized (sqlLock) {
				db = dbHelper.getWritableDatabase();
				String sql = "UPDATE Appeal SET upload = 1 WHERE ts = "
						+ ts;
				db.execSQL(sql);
				db.close();
			}
		}
		

		public void insertReflection(Reflection data) {
			Log.i("GG", "inserReflectionSQL");
			synchronized (sqlLock) {
				db = dbHelper.getWritableDatabase();
				ContentValues content = new ContentValues();
				
				content.put("ts", data.getTv().getTimestamp());
				content.put("action", data.getAction());
				content.put("feeling", data.getFeeling());
				content.put("thinking", data.getThinking());
				content.put("relationKey", data.getKey());
				content.put("acceptance", 0);
				db.insert("Reflection", null, content);
				db.close();
			}
		}

		public Reflection[] getNotUploadedReflection() {
			Log.i("GG", "getNotUploadReflectionSQL");
			synchronized (sqlLock) {
				Reflection[] data = null;
				db = dbHelper.getReadableDatabase();
				String sql;
				Cursor cursor;
				sql = "SELECT * FROM Reflection WHERE upload = 0";
				cursor = db.rawQuery(sql, null);
				int count = cursor.getCount();
				if (count == 0) {
					cursor.close();
					db.close();
					return null;
				}

				data = new Reflection[count];

				for (int i = 0; i < count; ++i) {
					cursor.moveToPosition(i);
					long ts = cursor.getLong(1);
					String action = cursor.getString(2);
					String feeling = cursor.getString(3);
					String thinking = cursor.getString(4);
					int key = cursor.getInt(5);
					data[i] = new Reflection(ts, action, feeling, thinking, key);
				}
				cursor.close();
				db.close();
				return data;
			}
		}

		public NoteAdd getNoteAddByRelationKey(int relation_key) {
			synchronized (sqlLock) {
				NoteAdd data = null;

				db = dbHelper.getReadableDatabase();
				String sql;
				Cursor cursor;

				sql = "SELECT * FROM NoteAdd WHERE relationKey = " + relation_key;
				cursor = db.rawQuery(sql, null);
				int count = cursor.getCount();
				if (count == 0) {
					cursor.close();
					db.close();
					return null;
				}

				cursor.moveToPosition(0);

				int isAfterTest = cursor.getInt(1);
				long ts = cursor.getLong(5);
				int year = cursor.getInt(7);
				int month = cursor.getInt(8);
				int day = cursor.getInt(9);
				int timeslot = cursor.getInt(10);
				int category = cursor.getInt(11);
				int type = cursor.getInt(12);
				int items = cursor.getInt(13);
				int impact = cursor.getInt(14);
				//String reason = cursor.getString(15);
				String action = cursor.getString(15);
				String feeling = cursor.getString(16);
				String thinking = cursor.getString(17);
				int finished = cursor.getInt(18);
				int weeklyScore = cursor.getInt(19);
				int score = cursor.getInt(20);
				int key = cursor.getInt(21);
				data = new NoteAdd(isAfterTest, ts, year, month, day,
						timeslot, category, type, items, impact, action, feeling, thinking, finished, weeklyScore, score, key);

				return data;
			}
		}

		public void setReflectionUploaded(long ts) {
			synchronized (sqlLock) {
				db = dbHelper.getWritableDatabase();
				String sql = "UPDATE Reflection SET upload = 1 WHERE ts = "
						+ ts;
				db.execSQL(sql);
				db.close();
			}
		}
		
		/**
		 * update a AddNote(Thinking)
		 * 
		 *
		 * 
		 */
		public void updateThinking(String thinking, int key) {
			
			synchronized (sqlLock) {

				db = dbHelper.getReadableDatabase();
				String sql;
				Cursor cursor;
				sql = "SELECT * FROM NoteAdd WHERE relationKey = " + key;
				cursor = db.rawQuery(sql, null);
				if (!cursor.moveToFirst()) {
					cursor.close();
					db.close();
					return;
				}
				int isAfterTest = cursor.getInt(1);
				int year = cursor.getInt(2);
				int month = cursor.getInt(3);
				int day = cursor.getInt(4);
				long ts = cursor.getLong(5);
				int week = cursor.getInt(6);
				int recordyear = cursor.getInt(7);
				int recordmonth = cursor.getInt(8);
				int recordday = cursor.getInt(9);
				int timeslot = cursor.getInt(10);
				int category = cursor.getInt(11);
				int type = cursor.getInt(12);
				int items = cursor.getInt(13);
				int impact = cursor.getInt(14);
				String action = cursor.getString(15);
				String feeling = cursor.getString(16);
				//String thinking = cursor.getString(17);
				int finished = cursor.getInt(18);
				int weeklyScore = cursor.getInt(19);
				int score = cursor.getInt(20);
				//int key = cursor.getInt(21);
				int isupload = cursor.getInt(22);
				
				
				
				db = dbHelper.getWritableDatabase();
				ContentValues content = new ContentValues();
				
				content.put("isAfterTest", isAfterTest);
				content.put("year", year);
				content.put("month", month);
				content.put("day", day);
				content.put("ts", ts);
				content.put("week", week);
				content.put("timeslot", timeslot);
				content.put("recordYear", recordyear);
				content.put("recordMonth", recordmonth);
				content.put("recordDay", recordday);
				content.put("category", category);
				content.put("type", type);
				content.put("items", items);
				content.put("impact", impact);
				content.put("action", action);
				content.put("feeling", feeling);
				content.put("thinking", thinking);
				content.put("finished", 1);
				content.put("weeklyScore", weeklyScore);
				content.put("score", score);
				content.put("relationKey", key);
				
				if(isupload == 1)
					isupload = 2;
				content.put("upload", isupload);
				
				String where = "relationKey = " + key;
				if(db.update("NoteAdd", content, where, null) > 0)
					Log.d("GG", "update success");
				
				db.close();
			}
		}
		

		public void insertAddScore(AddScore data) {
			AddScore preScore = getLastestAddScore();
			data.setAccumulation(preScore.getAccumulation() + data.getAddScore());

			int weeklyScore = preScore.getWeeklyAccumulation();
			if (preScore.getTv().getWeek() < data.getTv().getWeek())
				weeklyScore = preScore.getWeeklyAccumulation();
			if (preScore.getTv().getWeek() < data.getTv().getWeek())
				weeklyScore = 0;
			data.setWeeklyAccumulation(weeklyScore + data.getAddScore());

			Log.i("GG", "inserScoreSQL");
			synchronized (sqlLock) {
				db = dbHelper.getWritableDatabase();
				ContentValues content = new ContentValues();
				
				content.put("ts", data.getTv().getTimestamp());
				content.put("addScore", data.getAddScore());
				content.put("accumulation", data.getAccumulation());
				content.put("reason", data.getReason());
				content.put("weeklyAccumulation", data.getWeeklyAccumulation());
				content.put("reasonBit", data.getReasonBits());
				db.insert("Score", null, content);
				db.close();
			}
		}

		public AddScore getLastestAddScore() {
			synchronized (sqlLock) {
				db = dbHelper.getReadableDatabase();
				String sql;
				Cursor cursor;
				sql = "SELECT * FROM Score ORDER BY ts DESC";
				cursor = db.rawQuery(sql, null);
				if (!cursor.moveToFirst()) {
					cursor.close();
					db.close();
					return new AddScore(0, 0, 0, null, 0, 0);
				}
				int addScore = cursor.getInt(1);
				int accumulation = cursor.getInt(2);
				long ts = cursor.getLong(3);
				String reason = cursor.getString(4);
				int weeklyAccumulation = cursor.getInt(6);
				int reasonBits = cursor.getInt(7);
				
				cursor.close();
				db.close();
				
				return new AddScore(ts, addScore, accumulation, reason, weeklyAccumulation, reasonBits);
			}
		}
		
		public AddScore[] getNotUploadedAddScore() {
			Log.i("GG", "getNotUploadScoreSQL");
			synchronized (sqlLock) {
				AddScore[] data = null;
				db = dbHelper.getReadableDatabase();
				String sql;
				Cursor cursor;
				sql = "SELECT * FROM Score WHERE upload = 0";
				cursor = db.rawQuery(sql, null);
				int count = cursor.getCount();
				if (count == 0) {
					cursor.close();
					db.close();
					return null;
				}

				data = new AddScore[count];

				for (int i = 0; i < count; ++i) {
					cursor.moveToPosition(i);
					int addScore = cursor.getInt(1);
					int accumulation = cursor.getInt(2);
					long ts = cursor.getLong(3);
					String reason = cursor.getString(4);
					int weeklyAccumulation = cursor.getInt(6);
					int reasonBits = cursor.getInt(7);
					data[i] = new AddScore(ts, addScore, accumulation, reason, weeklyAccumulation, reasonBits);
				}
				cursor.close();
				db.close();
				return data;
			}
		}

		public void setAddScoreUploaded(long ts) {
			synchronized (sqlLock) {
				db = dbHelper.getWritableDatabase();
				String sql = "UPDATE Score SET upload = 1 WHERE ts = "
						+ ts;
				db.execSQL(sql);
				db.close();
			}
		}
		
		public ThinkingEvent[] getAllThinking() {
			synchronized (sqlLock) {
				db = dbHelper.getReadableDatabase();
				String sql = "SELECT * FROM NoteAdd WHERE impact >= 0 AND items > 0 ORDER BY recordYear, recordMonth, recordDay, timeslot ASC"; // TODO: Just get useful data
				Cursor cursor = db.rawQuery(sql, null);
				int count = cursor.getCount();
				
				String sql2 = "SELECT * FROM Reflection WHERE upload = 0";
				Cursor cursor2 = db.rawQuery(sql2, null);
				int count2 = cursor2.getCount();

				ThinkingEvent[] thinkingEvent = new ThinkingEvent[count + count2];
				
				for (int i = 0; i < count; ++i) {
					cursor.moveToPosition(i);
					String action = cursor.getString(15);
					String feeling = cursor.getString(16);
					String thinking = cursor.getString(17);
					int key = cursor.getInt(21);
					thinkingEvent[i] = new ThinkingEvent(action, feeling, thinking, key, 0);
				}
				
				for (int i = 0; i < count2; ++i) {
					cursor2.moveToPosition(i);
					String action = cursor2.getString(2);
					String feeling = cursor2.getString(3);
					String thinking = cursor2.getString(4);
					int key = cursor2.getInt(5);
					thinkingEvent[count + i] = new ThinkingEvent(action, feeling, thinking, key, 1);
				}

				cursor.close();
				db.close();
				return thinkingEvent;
			}
		}

		public void insertIdentityScore(IdentityScore data) {
			Log.i("GG", "inserScoreSQL");
			synchronized (sqlLock) {
				db = dbHelper.getWritableDatabase();
				ContentValues content = new ContentValues();
				
				content.put("ts", data.getTv().getTimestamp());
				content.put("score", data.getScore());
				content.put("relationKey", data.getKey());
				content.put("isReflection", data.getIsReflection());
				db.insert("Identity", null, content);
				db.close();
			}
		}
		
		public IdentityScore[] getNotUploadedIdentityScore() {
			Log.i("GG", "getNotUploadScoreSQL");
			synchronized (sqlLock) {
				IdentityScore[] data = null;
				db = dbHelper.getReadableDatabase();
				String sql;
				Cursor cursor;
				sql = "SELECT * FROM Identity WHERE upload = 0";
				cursor = db.rawQuery(sql, null);
				int count = cursor.getCount();
				if (count == 0) {
					cursor.close();
					db.close();
					return null;
				}

				data = new IdentityScore[count];

				for (int i = 0; i < count; ++i) {
					cursor.moveToPosition(i);
					long ts = cursor.getLong(1);
					int score = cursor.getInt(2);
					int key = cursor.getInt(3);
					int isReflection = cursor.getInt(4);
					data[i] = new IdentityScore(ts, score, key, isReflection);
				}
				cursor.close();
				db.close();
				return data;
			}
		}
		public void setIdentityScoreUploaded(long ts) {
			synchronized (sqlLock) {
				db = dbHelper.getWritableDatabase();
				String sql = "UPDATE Identity SET upload = 1 WHERE ts = "
						+ ts;
				db.execSQL(sql);
				db.close();
			}
		}


		public void insertHistoryWithNoteAdd(NoteAdd data) {

			synchronized (sqlLock) {
				db = dbHelper.getWritableDatabase();
				ContentValues content = new ContentValues();

				content.put("ts", data.getTv().getTimestamp());
				content.put("item", data.getItems());
				content.put("type", 1);
				content.put("content", data.getAction());
				db.insert("History", null, content);
				db.close();
			}

			if(data.getThinking() != "")
			synchronized (sqlLock) {
				db = dbHelper.getWritableDatabase();
				ContentValues content = new ContentValues();

				content.put("ts", data.getTv().getTimestamp());
				content.put("item", data.getItems());
				content.put("type", 2);
				content.put("content", data.getThinking());
				db.insert("History", null, content);
				db.close();
			}
		}


		public void insertHistoryWithRefleciton(Reflection data) {
			NoteAdd noteAdd = getNoteAddByRelationKey(data.getKey());
			synchronized (sqlLock) {
				db = dbHelper.getWritableDatabase();
				ContentValues content = new ContentValues();

				content.put("ts", data.getTv().getTimestamp());
				content.put("item", noteAdd.getItems());
				content.put("type", 3);
				content.put("content", data.getAction());
				db.insert("History", null, content);
				db.close();
			}

			if (data.getThinking() != "")
				synchronized (sqlLock) {
					db = dbHelper.getWritableDatabase();
					ContentValues content = new ContentValues();

					content.put("ts", data.getTv().getTimestamp());
					content.put("item", noteAdd.getItems());
					content.put("type", 4);
					content.put("content", data.getThinking());
					db.insert("History", null, content);
					db.close();
				}
		}

		public void insertHistory(History data) {

			synchronized (sqlLock) {
				db = dbHelper.getWritableDatabase();
				ContentValues content = new ContentValues();
				
				content.put("ts", data.getTv().getTimestamp());
				content.put("item", data.getItem());
				content.put("type", data.getType());
				content.put("content", data.getContent());
				db.insert("History", null, content);
				db.close();
			}
		}

		public History[] getHistory(int item, int type) {
			synchronized (sqlLock) {
				History[] data = null;
				db = dbHelper.getReadableDatabase();
				String sql = "SELECT * FROM History WHERE item = "+ item +" AND type = " + type;
				Cursor cursor = db.rawQuery(sql, null);
				int count = cursor.getCount();
				if (count == 0) {
					cursor.close();
					db.close();
					return null;
				}

				data = new History[count];
				for (int i = 0; i < count; ++i) {
					cursor.moveToPosition(i);
					long ts = cursor.getLong(1);
					int _item = cursor.getInt(2);
					int _type = cursor.getInt(3);
					String content = cursor.getString(4);
					//Log.d("GG", "in getAllTriggerItem new triggeritem : "+ item+","+content + ","+ show);
					data[i] = new History(ts, _item, _type, content);
				}
				cursor.close();
				db.close();
				return data;
			}
		}

		public void insertTriggerItem(TriggerItem data) {

			synchronized (sqlLock) {
				db = dbHelper.getWritableDatabase();
				ContentValues content = new ContentValues();
				
				content.put("item", data.getItem());
				content.put("description", data.getContent());
				content.put("show", 0);
				db.insert("Risk", null, content);
				db.close();
			}
		}
		

		public boolean findTriggerItem(int data) {

			synchronized (sqlLock) {
				db = dbHelper.getReadableDatabase();
				String sql;
				Cursor cursor;
				sql = "SELECT * FROM Risk WHERE item = " + data;
				cursor = db.rawQuery(sql, null);
				int count = cursor.getCount();
				Log.d("GG", "in findTriggerItem count = "+count);
				if (count == 0) {
					return false;
				}
				return true;
			}
		}
		
		public TriggerItem[] getAllTriggerItem() {
			synchronized (sqlLock) {

				TriggerItem[] data = null;
				db = dbHelper.getReadableDatabase();
				String sql = "SELECT * FROM Risk";
				Cursor cursor = db.rawQuery(sql, null);
				int count = cursor.getCount();
				if (count == 0) {

					cursor.close();
					db.close();
					return null;
				}
				
				data = new TriggerItem[count];
				for (int i = 0; i < count; ++i) {
					cursor.moveToPosition(i);
					int item = cursor.getInt(1);
					String content = cursor.getString(2);
					int show = cursor.getInt(3);

					data[i] = new TriggerItem(item, content, show > 0);
				}
				cursor.close();
				db.close();
				return data;
			}
		}

	public TriggerItem[] getTypeTriggerItem(int type) {
		synchronized (sqlLock) {

			TriggerItem[] data = null;
			db = dbHelper.getReadableDatabase();
			String sql = "SELECT * FROM Risk WHERE item >= " + type*100 + " AND item <= " + (type*100+99);
			Cursor cursor = db.rawQuery(sql, null);
			int count = cursor.getCount();
			if (count == 0) {

				cursor.close();
				db.close();
				return null;
			}

			data = new TriggerItem[count];
			for (int i = 0; i < count; ++i) {
				cursor.moveToPosition(i);
				int item = cursor.getInt(1);
				String content = cursor.getString(2);
				int show = cursor.getInt(3);

				data[i] = new TriggerItem(item, content, show > 0);
			}
			cursor.close();
			db.close();
			return data;
		}
	}

	public void insertEventLog(EventLogStructure data) {

		synchronized (sqlLock) {
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

			db.insert("EventLog", null, content);
			db.close();
		}
	}

	public EventLogStructure[] getAllEventLog() {
		synchronized (sqlLock) {

			EventLogStructure[] data = null;
			db = dbHelper.getReadableDatabase();
			String sql = "SELECT * FROM EventLog";
			Cursor cursor = db.rawQuery(sql, null);
			int count = cursor.getCount();
			if (count == 0) {
				cursor.close();
				db.close();
				return null;
			}

			Log.d("GG", "count = "+ count);

			data = new EventLogStructure[count];
			for (int i = 0; i < count; ++i) {
				cursor.moveToPosition(i);

				data[i] = new EventLogStructure();

				data[i].editTime = Calendar.getInstance();
				data[i].eventTime = Calendar.getInstance();
				data[i].createTime = Calendar.getInstance();
				data[i].editTime.setTimeInMillis(cursor.getLong(1));
				data[i].eventTime.setTimeInMillis(cursor.getLong(2));
				data[i].createTime.setTimeInMillis(cursor.getLong(3));
				data[i].scenarioType = EventLogStructure.ScenarioTypeEnum.values()[cursor.getInt(4)];
				data[i].scenario = cursor.getString(5);
				data[i].drugUseRiskLevel = cursor.getInt(6);
				data[i].originalBehavior = cursor.getString(7);
				data[i].originalEmotion = cursor.getString(8);
				data[i].originalThought = cursor.getString(9);
				data[i].expectedBehavior = cursor.getString(10);
				data[i].expectedEmotion = cursor.getString(11);
				data[i].expectedThought = cursor.getString(12);
				data[i].therapyStatus = EventLogStructure.TherapyStatusEnum.values()[cursor.getInt(13)];
				data[i].isAfterTest = (cursor.getInt(14) > 0);
				data[i].isComplete =  (cursor.getInt(15) > 0);
			}
			cursor.close();
			db.close();
			return data;
		}
	}

	public EventLogStructure[] getAfterEventLog(Calendar cal) {
		synchronized (sqlLock) {
			long ts = cal.getTimeInMillis();
			EventLogStructure[] data = null;
			db = dbHelper.getReadableDatabase();
			String sql = "SELECT * FROM EventLog WHERE eventTime >= " + ts;
			Cursor cursor = db.rawQuery(sql, null);
			int count = cursor.getCount();
			if (count == 0) {
				cursor.close();
				db.close();
				return null;
			}

			Log.d("GG", "count = "+ count);

			data = new EventLogStructure[count];
			for (int i = 0; i < count; ++i) {
				cursor.moveToPosition(i);

				data[i] = new EventLogStructure();

				data[i].editTime = Calendar.getInstance();
				data[i].eventTime = Calendar.getInstance();
				data[i].createTime = Calendar.getInstance();
				data[i].editTime.setTimeInMillis(cursor.getLong(1));
				data[i].eventTime.setTimeInMillis(cursor.getLong(2));
				data[i].createTime.setTimeInMillis(cursor.getLong(3));
				data[i].scenarioType = EventLogStructure.ScenarioTypeEnum.values()[cursor.getInt(4)];
				data[i].scenario = cursor.getString(5);
				data[i].drugUseRiskLevel = cursor.getInt(6);
				data[i].originalBehavior = cursor.getString(7);
				data[i].originalEmotion = cursor.getString(8);
				data[i].originalThought = cursor.getString(9);
				data[i].expectedBehavior = cursor.getString(10);
				data[i].expectedEmotion = cursor.getString(11);
				data[i].expectedThought = cursor.getString(12);
				data[i].therapyStatus = EventLogStructure.TherapyStatusEnum.values()[cursor.getInt(13)];
				data[i].isAfterTest = (cursor.getInt(14) > 0);
				data[i].isComplete =  (cursor.getInt(15) > 0);
			}
			cursor.close();
			db.close();
			return data;
		}
	}

	public EventLogStructure getTsEventLog(long ts) {
		synchronized (sqlLock) {
			EventLogStructure data = null;
			db = dbHelper.getReadableDatabase();
			String sql = "SELECT * FROM EventLog WHERE createTime = " + ts;
			Cursor cursor = db.rawQuery(sql, null);
			int count = cursor.getCount();
			if (count == 0) {
				cursor.close();
				db.close();
				return null;
			}

			Log.d("GG", "count = "+ count);

			data = new EventLogStructure();
			for (int i = 0; i < count; ++i) {
				cursor.moveToPosition(i);

				data = new EventLogStructure();

				data.editTime = Calendar.getInstance();
				data.eventTime = Calendar.getInstance();
				data.createTime = Calendar.getInstance();
				data.editTime.setTimeInMillis(cursor.getLong(1));
				data.eventTime.setTimeInMillis(cursor.getLong(2));
				data.createTime.setTimeInMillis(cursor.getLong(3));
				data.scenarioType = EventLogStructure.ScenarioTypeEnum.values()[cursor.getInt(4)];
				data.scenario = cursor.getString(5);
				data.drugUseRiskLevel = cursor.getInt(6);
				data.originalBehavior = cursor.getString(7);
				data.originalEmotion = cursor.getString(8);
				data.originalThought = cursor.getString(9);
				data.expectedBehavior = cursor.getString(10);
				data.expectedEmotion = cursor.getString(11);
				data.expectedThought = cursor.getString(12);
				data.therapyStatus = EventLogStructure.TherapyStatusEnum.values()[cursor.getInt(13)];
				data.isAfterTest = (cursor.getInt(14) > 0);
				data.isComplete =  (cursor.getInt(15) > 0);
			}
			cursor.close();
			db.close();
			return data;
		}
	}

}
