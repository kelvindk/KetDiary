package ubicomp.rehabdiary.utility.data.db;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

//import ubicomp.ketdiary.PreSettingActivity;
import ubicomp.rehabdiary.fragments.event.EventLogStructure;
import ubicomp.rehabdiary.utility.data.file.MainStorage;
import ubicomp.rehabdiary.utility.data.structure.AddScore;
import ubicomp.rehabdiary.utility.data.structure.CopingSkill;
import ubicomp.rehabdiary.utility.data.structure.NoteAdd;
import ubicomp.rehabdiary.utility.data.structure.QuestionTest;
import ubicomp.rehabdiary.utility.data.structure.TestResult;
import ubicomp.rehabdiary.utility.system.PreferenceControl;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * This class is an AsyncTask for handling Database restore procedure
 * 
 * @author Stanley Wang
// * @see ubicomp.soberdiary.data.database.DatabaseRestoreControl
 */
public class DatabaseRestore extends AsyncTask<Void, Void, Void> {

	private String uid;
	private File dir;
	private File zipFile;
	private Context context;

	private boolean hasFile = false;
	private DatabaseRestoreControl db = new DatabaseRestoreControl();

	private static final String TAG = "RESTORE";
	private ProgressDialog dialog = null;

	/**
	 * Constructor
	 * 
	 * @param uid
	 *            UserId
	 * @param context
	 *            Context of the Activity
	 */
	public DatabaseRestore(String uid, Context context) {
		this.uid = uid;
		this.context = context;

		dir = MainStorage.getMainStorageDirectory();
		zipFile = new File(dir, uid + ".zip");
		hasFile = zipFile.exists();
	}


	@Override
	protected void onPreExecute() {
		dialog = new ProgressDialog(context);
		dialog.setMessage("Please Wait...");
		dialog.setCancelable(false);
		dialog.show();
	}

	@Override
	protected Void doInBackground(Void... arg0) {
		if (hasFile) {
			unzip();
			db.deleteAll();
			
			restorePatient();
			restoreTestResult();
			//restoreNoteAdd();
			restoreEventLog();
			restoreScore();
			restoreQuestionTest();
			restoreCopingSkill();

		}
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		if (dialog != null)
			dialog.dismiss();
//		Intent intent = new Intent(context, PreSettingActivity.class);
//		context.startActivity(intent);
	}

	/** Unzip the backup file */
	private void unzip() {
		try {
			ZipInputStream zin = new ZipInputStream(
					new FileInputStream(zipFile));
			ZipEntry ze = null;
			while ((ze = zin.getNextEntry()) != null) {
				if (ze.isDirectory()) {
					File d = new File(dir + "/" + ze.getName());
					d.mkdirs();
				} else {
					File outFile = new File(dir, ze.getName());
					FileOutputStream fout = new FileOutputStream(outFile);
					for (int c = zin.read(); c != -1; c = zin.read())
						fout.write(c);
					zin.closeEntry();
					fout.close();
				}
			}
			zin.close();
		} catch (Exception e) {
			Log.d(TAG, "EXECEPTION: " + e.getMessage());
		}
	}

	/**
	 * Restore Table Patient related data Set user ID (UID), start date, and
	 * the # of self-help counters exchanged for coupons
	 */
	private void restorePatient() {
		String filename = "patient";
		File f = new File(dir + "/" + uid + "/" + filename + ".restore");
		if (f.exists()) {
			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(new DataInputStream(
								new FileInputStream(f))));
				String str = reader.readLine();
				if (str == null)
					Log.d(TAG, "No Patient");
				else {

					PreferenceControl.setUID(uid);

					str = reader.readLine();
					String[] data = str.split(",");

					String[] dateInfo = data[1].split("-"); //TODO: Start Date 不知道為什麼沒設到
					int year = Integer.valueOf(dateInfo[0]);
					int month = Integer.valueOf(dateInfo[1]) - 1;
					int day = Integer.valueOf(dateInfo[2]);

					PreferenceControl.setStartDate(year, month, day);

					int usedScore = Integer.valueOf(data[2]);
					int position = Integer.valueOf(data[3]);
					
					PreferenceControl.setAbsPosition(position);
					PreferenceControl.setUsedCounter(usedScore);
				}
				reader.close();
			} catch (FileNotFoundException e) {
				Log.d(TAG, "NO " + filename);
			} catch (IOException e) {
				Log.d(TAG, "READ FAIL " + filename);
			}
		}
	}

	/** Restore from the table TestResult */
	private void restoreTestResult() {
		String filename = "testResult";
		File f = new File(dir + "/" + uid + "/" + filename + ".restore");
		if (f.exists()) {
			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(new DataInputStream(
								new FileInputStream(f))));
				String str = reader.readLine();
				if (str == null)
					Log.d(TAG, "No " + filename);
				else {
					while ((str = reader.readLine()) != null) {
						String[] data = str.split(",");
						long ts = Long.valueOf(data[0]);
						int result = Integer.valueOf(data[1]);
						int isPrime = Integer.valueOf(data[2]);
						int isFilled= Integer.valueOf(data[3]);
						int score = Integer.valueOf(data[4]);;
						TestResult testResult = new TestResult(result, ts, "", isPrime, isFilled, 0, score);

						db.restoreTestResult(testResult);
					}
				}
				reader.close();
			} catch (FileNotFoundException e) {
				Log.d(TAG, "NO " + filename);
			} catch (IOException e) {
				Log.d(TAG, "READ FAIL " + filename);
			}
		}
	}
	
	/** Restore from the table NoteAdd */
	private void restoreNoteAdd() {
		String filename = "noteAdd";
		File f = new File(dir + "/" + uid + "/" + filename + ".restore");
		if (f.exists()) {
			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(new DataInputStream(
								new FileInputStream(f))));
				String str = reader.readLine();
				if (str == null)
					Log.d(TAG, "No " + filename);
				else {
					while ((str = reader.readLine()) != null) {
						String[] data = str.split(",");
						long timestamp = Long.valueOf(data[0]);
						int isAfterTest = Integer.valueOf(data[1]);
						
						String[] dateInfo = data[2].split("-");
						int year = Integer.valueOf(dateInfo[0]);
						int month = Integer.valueOf(dateInfo[1]) - 1;
						int day = Integer.valueOf(dateInfo[2]);
						
						int timeslot = Integer.valueOf(data[3]);
						
						int category = Integer.valueOf(data[4]);
						int type = Integer.valueOf(data[5]);
						int items = Integer.valueOf(data[6]);
						int impact = Integer.valueOf(data[7]);

						StringBuilder sb = new StringBuilder();

						/*sb.append(data[8]);
						for (int i = 6; i < data.length; ++i) {
							sb.append(",");
							sb.append(data[i]);
						}
						
						String description = sb.toString();*/
						sb.append(data[8]);
						String action = sb.toString();
						
						sb = new StringBuilder();
						sb.append(data[9]);
						String feeling = sb.toString();
						
						sb = new StringBuilder();
						sb.append(data[10]);
						String thinking = sb.toString();
						
						int finished = Integer.valueOf(data[11]);
						int score = Integer.valueOf(data[12]);
						int key = Integer.valueOf(data[13]);
						NoteAdd noteAdd = new NoteAdd(isAfterTest,
								timestamp, year, month, day, timeslot,
								category, type, items, impact, action, feeling, thinking, finished, 0, score, key);
						db.restoreNoteAdd(noteAdd);
					}
				}
				reader.close();
			} catch (FileNotFoundException e) {
				Log.d(TAG, "NO " + filename);
			} catch (IOException e) {
				Log.d(TAG, "READ FAIL " + filename);
			}
		}
	}

	private void restoreEventLog() {
		long now_time = 0;
		String filename = "eventLog";
		File f = new File(dir + "/" + uid + "/" + filename + ".restore");
		if (f.exists()) {
			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(new DataInputStream(
								new FileInputStream(f))));
				String str = reader.readLine();
				if (str == null)
					Log.d(TAG, "No " + filename);
				else {
					while ((str = reader.readLine()) != null) {
						EventLogStructure eventLog = new EventLogStructure();
						eventLog.editTime = Calendar.getInstance();
						eventLog.eventTime = Calendar.getInstance();
						eventLog.createTime = Calendar.getInstance();

						String[] data = str.split(",");
						eventLog.editTime.setTimeInMillis(Long.valueOf(data[0]));
						eventLog.eventTime.setTimeInMillis(Long.valueOf(data[1]));
						eventLog.createTime.setTimeInMillis(Long.valueOf(data[2]));

						eventLog.scenarioType = EventLogStructure.ScenarioTypeEnum.values()[Integer.valueOf(data[3])];

						StringBuilder sb = null;
						sb = new StringBuilder();
						sb.append(data[4]);
						eventLog.scenario = sb.toString();

						eventLog.drugUseRiskLevel = Integer.valueOf(data[5]);

						if(data.length > 6) {
							sb = new StringBuilder();
							sb.append(data[6]);
							eventLog.originalBehavior = sb.toString();
						}
						if(data.length > 7) {
							sb = new StringBuilder();
							sb.append(data[7]);
							eventLog.originalEmotion = sb.toString();
						}
						if(data.length > 8) {
							sb = new StringBuilder();
							sb.append(data[8]);
							eventLog.originalThought = sb.toString();
						}
						if(data.length > 9) {
							sb = new StringBuilder();
							sb.append(data[9]);
							eventLog.expectedBehavior = sb.toString();
						}
						if(data.length > 10) {
							sb = new StringBuilder();
							sb.append(data[10]);
							eventLog.expectedEmotion = sb.toString();
						}
						if(data.length > 11) {
							sb = new StringBuilder();
							sb.append(data[11]);
							eventLog.expectedThought = sb.toString();
						}

						if(!eventLog.originalBehavior.equals("") && !eventLog.originalEmotion.equals("") &&
								!eventLog.originalThought.equals("") && !eventLog.expectedBehavior.equals("") &&
								!eventLog.expectedEmotion.equals("") && !eventLog.expectedThought.equals(""))
							eventLog.isComplete = true;

						if(!eventLog.expectedBehavior.equals("") && !eventLog.expectedEmotion.equals("") && !eventLog.expectedThought.equals(""))
							eventLog.isReflected = true;


						if(eventLog.createTime.getTimeInMillis() > now_time) {
							db.restoreEventLog(eventLog);
							now_time = eventLog.createTime.getTimeInMillis();
						}
					}
				}
				reader.close();
			} catch (FileNotFoundException e) {
				Log.d(TAG, "NO " + filename);

			} catch (IOException e) {
				Log.d(TAG, "READ FAIL " + filename);
			}
		}
	}

	private void restoreScore() {
		String filename = "score";
		File f = new File(dir + "/" + uid + "/" + filename + ".restore");
		if (f.exists()) {
			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(new DataInputStream(
								new FileInputStream(f))));
				String str = reader.readLine();
				if (str == null)
					Log.d(TAG, "No " + filename);
				else {
					while ((str = reader.readLine()) != null) {
						String[] data = str.split(",");

						int addScore = Integer.valueOf(data[0]);
						int accumulation = Integer.valueOf(data[1]);
						long timestamp = Long.valueOf(data[2]);

						StringBuilder sb = null;
						sb = new StringBuilder();
						sb.append(data[3]);
						String reason= sb.toString();

						int weeklyAccumulation = Integer.valueOf(data[4]);
						int reasonBits = Integer.valueOf(data[5]);

						AddScore insertScore = new AddScore(timestamp, addScore, accumulation, reason, weeklyAccumulation, reasonBits);

						db.restoreScore(insertScore);
					}
				}
				reader.close();
			} catch (FileNotFoundException e) {
				Log.d(TAG, "NO " + filename);

			} catch (IOException e) {
				Log.d(TAG, "READ FAIL " + filename);
			}
		}
	}

	/**
	 * Restore from the table QuestionTest (Only restored # of self-help counters
	 * got by the user)
	 */
	private void restoreQuestionTest() {
		String filename = "questionTest";
		File f = new File(dir + "/" + uid + "/" + filename + ".restore");
		if (f.exists()) {
			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(new DataInputStream(
								new FileInputStream(f))));
				String str = reader.readLine();
				if (str == null)
					Log.d(TAG, "No " + filename);
				else {
					while ((str = reader.readLine()) != null) {
						String[] data = str.split(",");
						long timestamp = Long.valueOf(data[0]);
						int score = Integer.valueOf(data[1]);
						QuestionTest questionTest = new QuestionTest(timestamp, 0, 1,
								"", 0,score);
						db.restoreQuestionTest(questionTest);
					}
				}
				reader.close();
			} catch (FileNotFoundException e) {
				Log.d(TAG, "NO " + filename);
			} catch (IOException e) {
				Log.d(TAG, "READ FAIL " + filename);
			}
		}
	}

	/**
	 * Restore from the table CopingSkill (Only restore the # self-help
	 * counters got by the user)
	 */
	private void restoreCopingSkill() {
		String filename = "copingSkill";
		File f = new File(dir + "/" + uid + "/" + filename + ".restore");
		if (f.exists()) {
			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(new DataInputStream(
								new FileInputStream(f))));
				String str = reader.readLine();
				if (str == null)
					Log.d(TAG, "No " + filename);
				else {
					while ((str = reader.readLine()) != null) {
						String[] data = str.split(",");
						long timestamp = Long.valueOf(data[0]);
						int score = Integer.valueOf(data[1]);
						CopingSkill copingSkill = new CopingSkill(
								timestamp, 0, 0, "", score);
						db.restoreCopingSkill(copingSkill);
					}
				}
				reader.close();
			} catch (FileNotFoundException e) {
				Log.d(TAG, "NO " + filename);
			} catch (IOException e) {
				Log.d(TAG, "READ FAIL " + filename);
			}
		}
	}

	

//	private void moveFiles(File src, File dst) {
//		InputStream in;
//		try {
//			in = new FileInputStream(src);
//			OutputStream out = new FileOutputStream(dst);
//			byte[] buf = new byte[4096];
//			int len;
//			while ((len = in.read(buf)) > 0)
//				out.write(buf, 0, len);
//			in.close();
//			out.close();
//		} catch (Exception e) {
//		}
//	}

}
