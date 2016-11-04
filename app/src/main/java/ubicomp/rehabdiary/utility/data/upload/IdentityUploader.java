package ubicomp.rehabdiary.utility.data.upload;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import ubicomp.rehabdiary.fragments.event.EventLogStructure;
import ubicomp.rehabdiary.utility.data.db.DatabaseControl;
import ubicomp.rehabdiary.utility.data.file.MainStorage;
import ubicomp.rehabdiary.utility.data.structure.AddScore;
import ubicomp.rehabdiary.utility.data.structure.Appeal;
import ubicomp.rehabdiary.utility.data.structure.CopingSkill;
import ubicomp.rehabdiary.utility.data.structure.ExchangeHistory;
import ubicomp.rehabdiary.utility.data.structure.IdentityScore;
import ubicomp.rehabdiary.utility.data.structure.NoteAdd;
import ubicomp.rehabdiary.utility.data.structure.QuestionTest;
import ubicomp.rehabdiary.utility.data.structure.Reflection;
import ubicomp.rehabdiary.utility.data.structure.TestDetail;
import ubicomp.rehabdiary.utility.data.structure.TestResult;
import ubicomp.rehabdiary.utility.system.check.DefaultCheck;
import ubicomp.rehabdiary.utility.system.check.NetworkCheck;

/**
 * Used for upload data to the server
 * 
 * @author Andy Chen
 */
public class IdentityUploader {

	private static DataUploadTask uploader = null;
	private static Thread cleanThread = null;

	private static final String TAG = "UPLOAD";

	/** Upload the data & remove uploaded data */
	public static void upload() {

		/*if (cleanThread != null && !cleanThread.isInterrupted()) { //May be used some day.
			cleanThread.interrupt();
			cleanThread = null;
		}

		cleanThread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Cleaner.clean();
				} catch (Exception e) {
				}
			}
		});
		cleanThread.start();
		try {
			cleanThread.join(500);
		} catch (InterruptedException e) {
		}*/


		if (DefaultCheck.check() || !NetworkCheck.networkCheck())
			return;

		if (SynchronizedLock.sharedLock.tryLock()) {
			SynchronizedLock.sharedLock.lock();
			uploader = new DataUploadTask();
			uploader.execute();
		}
	}
	

	/** AsyncTask handles the data uploading task */
	public static class DataUploadTask extends AsyncTask<Void, Void, Void> {
		
		
		private DatabaseControl db;
		
		/** ENUM UPLOAD ERROR */
		public static final int ERROR = -1;
		/** ENUM UPLOAD SUCCESS */
		public static final int SUCCESS = 1;
		private File logDir;
		
		/** Constructor */
		public DataUploadTask() {
			db = new DatabaseControl();
			logDir = new File(MainStorage.getMainStorageDirectory(),
					"sequence_log");
		}

		@Override
		protected Void doInBackground(Void... arg0) {

			Log.d(TAG, "upload start");

			//Identity update thinking
			IdentityScore identityScore[] = db.getAllUploadedIdentityScore();
			if (identityScore != null) {
				for (int i = 0; i < identityScore.length; ++i) {
					if (connectToServer(identityScore[i]) == ERROR)
						Log.d(TAG, "FAIL TO UPLOAD - IdentityScore");
				}
			}
			
			return null;
		}
		
		private String[] getNotUploadedClickLog() {
			if (!logDir.exists()) {
				Log.d(TAG, "Cannot find clicklog dir");
				return null;
			}

			String[] all_logs = null;
			String latestUpload = null;
			File latestUploadFile = new File(logDir, "latest_uploaded");
			if (latestUploadFile.exists()) {
				try {
					@SuppressWarnings("resource")
					BufferedReader br = new BufferedReader(new FileReader(latestUploadFile));
					latestUpload = br.readLine();
				} catch (IOException e) {
				}
			}
			all_logs = logDir.list(new logFilter(latestUpload));
			return all_logs;
		}

		private class logFilter implements FilenameFilter {
			String _latestUpload;
			String today;

			@SuppressLint("SimpleDateFormat")
			public logFilter(String latestUpload) {
				_latestUpload = latestUpload;
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(System.currentTimeMillis());
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd");
				today = sdf.format(cal.getTime()) + ".txt";
			}

			@Override
			public boolean accept(File arg0, String arg1) {
				if (arg1.equals("latest_uploaded"))
					return false;
				else {
					if (today.compareTo(arg1) > 0)
						if (_latestUpload == null || (_latestUpload != null && (arg1.compareTo(_latestUpload)) > 0))
							return true;
					return false;
				}
			}
		}
		
		private void set_uploaded_logfile(String name) {
			File latestUploadFile = new File(logDir, "latest_uploaded");
			BufferedWriter writer;
			try {
				writer = new BufferedWriter(new FileWriter(latestUploadFile));
				writer.write(name);
				writer.newLine();
				writer.flush();
				writer.close();
			} catch (IOException e) {
				writer = null;
			}
		}

		@Override
		protected void onPostExecute(Void result) {
			uploader = null;
			SynchronizedLock.sharedLock.unlock();
		}

		@Override
		protected void onCancelled() {
			uploader = null;
			SynchronizedLock.sharedLock.unlock();
		}

		private int connectToServer() {
			try {
				DefaultHttpClient httpClient = HttpSecureClientGenerator.getSecureHttpClient();
				HttpPost httpPost = HttpPostGenerator.genPost();
				if (!upload(httpClient, httpPost))
					return ERROR;
			} catch (Exception e) {
				Log.d(TAG, "EXCEPTION:" + e.toString());
				return ERROR;
			}
			return SUCCESS;
		}
		
		
		private int connectToServer(TestResult data) {
			try {
				DefaultHttpClient httpClient = HttpSecureClientGenerator
						.getSecureHttpClient();
				
				HttpPost httpPost = HttpPostGenerator.genPost(data);
				if (upload(httpClient, httpPost)){
					db.setTestResultUploaded(data.getTv().getTimestamp());
					Log.d(TAG, "Upload TestResult Success.");
				}
				else
					return ERROR;
			} catch (Exception e) {
				Log.d(TAG, "EXCEPTION:" + e.toString());
				return ERROR;
			}
			return SUCCESS;
		}
		
		private int connectToServer(NoteAdd data) {
			try {
				DefaultHttpClient httpClient = HttpSecureClientGenerator
						.getSecureHttpClient();
				HttpPost httpPost = HttpPostGenerator.genPost(data);
				if (upload(httpClient, httpPost)){
					db.setNoteAddUploaded(data.getTv().getTimestamp());
					Log.d(TAG, "Upload NoteAdd Success.");
				}
				else
					return ERROR;
			} catch (Exception e) {
				Log.d(TAG, "EXCEPTION:" + e.toString());
				return ERROR;
			}
			return SUCCESS;
		}
		
		private int connectToServerAddNoteThinking(NoteAdd data) {
			try {
				DefaultHttpClient httpClient = HttpSecureClientGenerator
						.getSecureHttpClient();
				HttpPost httpPost = HttpPostGenerator.genPostAddNoteThinking(data);
				if (upload(httpClient, httpPost)){
					db.setNoteAddUploaded(data.getTv().getTimestamp());
					Log.d(TAG, "Upload NoteAdd Thinking Success.");
				}
				else
					return ERROR;
			} catch (Exception e) {
				Log.d(TAG, "EXCEPTION:" + e.toString());
				return ERROR;
			}
			return SUCCESS;
		}
		
		private int connectToServer(TestDetail data) {
			try {
				DefaultHttpClient httpClient = HttpSecureClientGenerator
						.getSecureHttpClient();
				HttpPost httpPost = HttpPostGenerator.genPost(data);
				if (upload(httpClient, httpPost)){
					db.setTestDetailUploaded(data.getTv().getTimestamp());
					Log.d(TAG, "Upload TestDetail Success.");
				}
				else
					return ERROR;
			} catch (Exception e) {
				Log.d(TAG, "EXCEPTION:" + e.toString());
				return ERROR;
			}
			return SUCCESS;
		}
		
		private int connectToServer(QuestionTest data) {
			try {
				DefaultHttpClient httpClient = HttpSecureClientGenerator
						.getSecureHttpClient();
				HttpPost httpPost = HttpPostGenerator.genPost(data);
				if (upload(httpClient, httpPost)){
					db.setQuestionTestUploaded(data.getTv().getTimestamp());
					Log.d(TAG, "Upload QuestionTest Success.");
				}
				else
					return ERROR;
			} catch (Exception e) {
				Log.d(TAG, "EXCEPTION:" + e.toString());
				return ERROR;
			}
			return SUCCESS;
		}
		
		private int connectToServer(CopingSkill data) {
			try {
				DefaultHttpClient httpClient = HttpSecureClientGenerator
						.getSecureHttpClient();
				HttpPost httpPost = HttpPostGenerator.genPost(data);
				if (upload(httpClient, httpPost)){
					db.setCopingSkillUploaded(data.getTv().getTimestamp());
					Log.d(TAG, "Upload CopingSkill Success.");
				}
				else
					return ERROR;
			} catch (Exception e) {
				Log.d(TAG, "EXCEPTION:" + e.toString());
				return ERROR;
			}
			return SUCCESS;
		}
		
		private int connectToServer(ExchangeHistory data) {// ExchangeHistory
			try {
				DefaultHttpClient httpClient = HttpSecureClientGenerator
						.getSecureHttpClient();
				HttpPost httpPost = HttpPostGenerator.genPost(data);
				if (upload(httpClient, httpPost)){
					db.setExchangeHistoryUploaded(data.getTv().getTimestamp());
					Log.d(TAG, "Upload ExchangeHistory Success.");
				}
				else
					return ERROR;
			} catch (Exception e) {
				Log.d(TAG, "EXCEPTION:" + e.toString());
				return ERROR;
			}
			return SUCCESS;
		}
		
		private int connectToServer(Appeal data) {// Appeal
			try {
				
				DefaultHttpClient httpClient = HttpSecureClientGenerator
						.getSecureHttpClient();
				HttpPost httpPost = HttpPostGenerator.genPost(data);
				if (upload(httpClient, httpPost)){
					db.setAppealUploaded(data.getTv().getTimestamp());
					Log.d(TAG, "Upload Appeal Success.");
				}
				else
					return ERROR;
			} catch (Exception e) {
				Log.d(TAG, "EXCEPTION:" + e.toString());
				return ERROR;
			}
			return SUCCESS;
		}
		
		private int connectToServer(Reflection data) {// Reflection
			try {
				
				DefaultHttpClient httpClient = HttpSecureClientGenerator
						.getSecureHttpClient();
				HttpPost httpPost = HttpPostGenerator.genPost(data);
				
				if (upload(httpClient, httpPost)){
					
					db.setReflectionUploaded(data.getTv().getTimestamp());
					Log.d(TAG, "Upload Reflection Success.");
				}
				else
					return ERROR;
			} catch (Exception e) {
				Log.d(TAG, "EXCEPTION:" + e.toString());
				return ERROR;
			}
			return SUCCESS;
		}
		
		private int connectToServer(AddScore data) {// AddScore
			try {
				
				DefaultHttpClient httpClient = HttpSecureClientGenerator
						.getSecureHttpClient();
				HttpPost httpPost = HttpPostGenerator.genPost(data);
				if (upload(httpClient, httpPost)){
					db.setAddScoreUploaded(data.getTv().getTimestamp());
					Log.d(TAG, "Upload Score Success.");
				}
				else
					return ERROR;
			} catch (Exception e) {
				Log.d(TAG, "EXCEPTION:" + e.toString());
				return ERROR;
			}
			return SUCCESS;
		}
		
		private int connectToServer(IdentityScore data) {// AddScore
			try {
				
				DefaultHttpClient httpClient = HttpSecureClientGenerator
						.getSecureHttpClient();
				HttpPost httpPost = HttpPostGenerator.genPost(data);
				if (upload(httpClient, httpPost)){
					db.setAddScoreUploaded(data.getCreateTime().getTimeInMillis());
					Log.d(TAG, "Upload Score Success.");
				}
				else
					return ERROR;
			} catch (Exception e) {
				Log.d(TAG, "EXCEPTION:" + e.toString());
				return ERROR;
			}
			return SUCCESS;
		}

		private int connectToServer(EventLogStructure data) {// AddScore
			try {

				DefaultHttpClient httpClient = HttpSecureClientGenerator
						.getSecureHttpClient();
				HttpPost httpPost = HttpPostGenerator.genPost(data);
				if (upload(httpClient, httpPost)){
					db.setEventLogUploaded(data.editTime.getTimeInMillis());
					Log.d(TAG, "Upload EventLog Success.");
				}
				else
					return ERROR;
			} catch (Exception e) {
				Log.d(TAG, "EXCEPTION:" + e.toString());
				return ERROR;
			}
			return SUCCESS;
		}
		
		private int connectToServer(File data) {// ClickLog
			try {
				DefaultHttpClient httpClient = HttpSecureClientGenerator.getSecureHttpClient();
				HttpPost httpPost = HttpPostGenerator.genPost(data);
				if (upload(httpClient, httpPost)){
					set_uploaded_logfile(data.getName());
					Log.d(TAG, "Upload ClickLog Success.");
				}
				else
					return ERROR;
			} catch (Exception e) {
				Log.d(TAG, "EXCEPTION:" + e.toString());
				return ERROR;
			}
			return SUCCESS;
		}
		
		
		
		
		// handle upload respond message
		private boolean upload(HttpClient httpClient, HttpPost httpPost) {
			HttpResponse httpResponse;
			ResponseHandler<String> res = new BasicResponseHandler();
			boolean result = false;
			try {
				httpResponse = httpClient.execute(httpPost);
				int httpStatusCode = httpResponse.getStatusLine().getStatusCode();
				result = (httpStatusCode == HttpStatus.SC_OK);
				if (result) {
					String response = res.handleResponse(httpResponse).toString();
					Log.d(TAG, "response=" + response);
					result &= (response.contains("upload success"));
					Log.d(TAG, "result=" + result);
				} else {
					Log.d(TAG, "fail result=" + result);
				}
			} catch (ClientProtocolException e) {
				Log.d(TAG, "ClientProtocolException " + e.toString());
			} catch (IOException e) {
				Log.d(TAG, "IOException " + e.toString());
			} finally {
				if (httpClient != null) {
					ClientConnectionManager ccm = httpClient.getConnectionManager();
					if (ccm != null)
						ccm.shutdown();
				}
			}
			return result;
		}
	}

}
