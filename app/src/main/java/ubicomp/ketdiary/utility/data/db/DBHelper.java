package ubicomp.ketdiary.utility.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Database Helper for initializing the database or update the database
 * 
 * @author Andy Chen
 */
public class DBHelper extends SQLiteOpenHelper {

	/* SQLiteOpenHelper. need to migrate with */
	private static final String DATABASE_NAME = "rehabdiary";
	private static final int DB_VERSION = 20;

	/**
	 * Constructor
	 * 
	 * @param context
	 *            Application Context
	 */
	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TestResult Table
		db.execSQL("CREATE TABLE TestResult ("
				+ " id INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ " result INTEGER," + " cassetteId CHAR[255] NOT NULL,"
				+ " year INTEGER NOT NULL," 
				+ " month INTEGER NOT NULL," + " day INTEGER NOT NULL,"
				+ " ts INTEGER NOT NULL," + " week INTEGER NOT NULL,"
				+ " isPrime INTEGER NOT NULL, "	+ " isFilled INTEGER NOT NULL , "
				+ " weeklyScore INTEGER NOT NULL," + " score INTEGER NOT NULL,"
				+ " upload INTEGER NOT NULL DEFAULT 0" + ")");
		
		
		//  NoteAdd Table
		/*db.execSQL("CREATE TABLE NoteAdd ("
				+ " id INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ " isAfterTest INT NOT NULL," + " year INTEGER NOT NULL,"
				+ " month INTEGER NOT NULL," + " day INTEGER NOT NULL,"
				+ " ts INTEGER NOT NULL," + " week INTEGER NOT NULL,"
				+ " recordYear INTEGER NOT NULL,"
				+ " recordMonth INTEGER NOT NULL,"
				+ " recordDay INTEGER NOT NULL," 
				+ " timeSlot INTEGER NOT NULL," + " category INTEGER NOT NULL,"
				+ " type INTEGER NOT NULL," + " items INTEGER NOT NULL, "
				+ " impact INTEGER NOT NULL, "+ " action CHAR[255], "
				+ " feeling CHAR[255], " + " thinking CHAR[255], "
				+ " finished INTEGER NOT NULL,"
				+ " weeklyScore INTEGER NOT NULL," + " score INTEGER NOT NULL,"
				+ " relationKey INTEGER NOT NULL,"
				+ " upload INTEGER NOT NULL DEFAULT 0," 
				+ " acceptance INTEGER NOT NULL DEFAULT 0,"//version 13->14
				+ " recordData INTEGER NOT NULL DEFAULT 0,"//version 19->20
				+ " certification INTEGER NOT NULL DEFAULT 0,"
				+ " depth INTEGER NOT NULL DEFAULT 0,"
				+ " deleted INTEGER NOT NULL DEFAULT 0"
				+ ")");
		*/
		
		db.execSQL("CREATE TABLE TestDetail ("
				+ " id INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ " cassetteId CHAR[255] NOT NULL," + " year INTEGER NOT NULL,"
				+ " month INTEGER NOT NULL," + " day INTEGER NOT NULL,"
				+ " ts INTEGER NOT NULL," + " week INTEGER NOT NULL,"
				+ " failedState INTEGER NOT NULL," + " firstVoltage INTEGER NOT NULL,"
				+ " secondVoltage INTEGER NOT NULL," + " devicePower INTEGER NOT NULL, "
				+ " colorReading INTEGER NOT NULL, "+ " connectionFailRate FLOAT, " 
				+ " failedReason CHAR[255], "
				+ " hardwareVersion CHAR[255], "
				+ " upload INTEGER NOT NULL DEFAULT 0" + ")");
		
		db.execSQL("CREATE TABLE Ranking (" + " user_id CHAR[255] PRIMERY KEY,"
				+ " total_score INTEGER NOT NULL,"
				+ " test_score INTEGER NOT NULL  DEFAULT 0,"
				+ " note_score INTEGER NOT NULL  DEFAULT 0,"
				+ " question_score INTEGER NOT NULL  DEFAULT 0,"
				+ " coping_score INTEGER NOT NULL  DEFAULT 0,"
				
				+ " times_score INTEGER NOT NULL  DEFAULT 0,"
				+ " pass_score INTEGER NOT NULL  DEFAULT 0,"
				+ " normalQ_score INTEGER NOT NULL  DEFAULT 0,"
				+ " randomQ_score INTEGER NOT NULL  DEFAULT 0"+")");

		db.execSQL("CREATE TABLE RankingShort ("
				+ " user_id CHAR[255] PRIMERY KEY,"
				+ " total_score INTEGER NOT NULL DEFAULT 0" + ")");
		
		db.execSQL("CREATE TABLE QuestionTest ("
				+ " id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ " year INTEGER NOT NULL," + " month INTEGER NOT NULL,"
				+ " day INTEGER NOT NULL," + " ts INTEGER NOT NULL,"
				+ " week INTEGER NOT NULL," + " timeSlot INTEGER NOT NULL,"
				+ " questionType INTEGER NOT NULL,"
				+ " isCorrect INTEGER NOT NULL DEFAULT 0,"
				+ " selection CHAR[255] NOT NULL,"
				+ " choose INTEGER NOT NULL DEFAULT 0,"
				+ " score INTEGER NOT NULL,"
				+ " upload INTEGER NOT NULL DEFAULT 0" + ")");
		
		db.execSQL("CREATE TABLE CopingSkill ("
				+ " id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ " year INTEGER NOT NULL," + " month INTEGER NOT NULL,"
				+ " day INTEGER NOT NULL," + " ts INTEGER NOT NULL,"
				+ " week INTEGER NOT NULL," + " timeSlot INTEGER NOT NULL,"
				+ " skillType INTEGER NOT NULL,"
				+ " skillSelect INTEGER NOT NULL DEFAULT 0,"
				+ " recreation CHAR[255] NOT NULL,"
				+ " score INTEGER NOT NULL,"
				+ " upload INTEGER NOT NULL DEFAULT 0" + ")");
		
		db.execSQL("CREATE TABLE ExchangeHistory ("
				+ " id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ " ts INTEGER NOT NULL,"
				+ " exchangeCounter INTEGER NOT NULL,"
				+ " upload INTEGER NOT NULL DEFAULT 0,"
				+ " testSuccess INTEGER NOT NULL DEFAULT 0,"
				+ " hasData INTEGER NOT NULL DEFAULT 0" + ")");
		
		db.execSQL("CREATE TABLE Cassette ("
				+ " id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ " ts INTEGER NOT NULL,"
				+ " cassetteId CHAR[255] NOT NULL,"
				+ " isUsed INTEGER NOT NULL,"
				+ " upload INTEGER NOT NULL DEFAULT 0)");
		
		//Appeal
		db.execSQL("CREATE TABLE Appeal ("
				+ " id INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ " ts INTEGER NOT NULL,"
				+ " appealType INTEGER NOT NULL, "	
				+ " appealTimes INTEGER NOT NULL, "
				+ " upload INTEGER NOT NULL DEFAULT 0" + ")");
		
		//reflection
		/*db.execSQL("CREATE TABLE Reflection ("
				+ " id INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ " ts INTEGER NOT NULL,"
				+ " action CHAR[255], "	
				+ " feeling CHAR[255], "
				+ " thinking CHAR[255], "
				+ " relationKey INTEGER NOT NULL," 
				+ " upload INTEGER NOT NULL DEFAULT 0," 
				+ " acceptance INTEGER NOT NULL DEFAULT 0," //version 13->14
				+ " certification INTEGER NOT NULL DEFAULT 0,"
				+ " depth INTEGER NOT NULL DEFAULT 0,"
				+ " deleted INTEGER NOT NULL DEFAULT 0"
				+ ")");
		*/

		//risk  version 14->15
		db.execSQL("CREATE TABLE Risk ("
				+ " id INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ " item INTEGER NOT NULL DEFAULT 0,"
				+ " description CHAR[255],"
				+ " show INTEGER NOT NULL DEFAULT 0"
				+ ")");
		
		//score  version 15->16
		db.execSQL("CREATE TABLE Score ("
				+ " id INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ " addScore INTEGER NOT NULL DEFAULT 0,"
				+ " accumulation INTEGER NOT NULL DEFAULT 0,"
				+ " ts INTEGER NOT NULL,"
				+ " reason CHAR[255], "
				+ " upload INTEGER NOT NULL DEFAULT 0," //version 16->17
				+ " weeklyAccumulation NOT NULL DEFAULT 0,"
				+ " reasonBit NOT NULL DEFAULT 0"
				+ ")");
		
		//identity version 17->18
		db.execSQL("CREATE TABLE Identity ("
				+ " id INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ " ts INTEGER NOT NULL,"
				+ " score INTEGER NOT NULL DEFAULT 0,"
				+ " relationKey INTEGER NOT NULL,"
				+ " isReflection INTEGER NOT NULL DEFAULT 0, "
				+ " upload INTEGER NOT NULL DEFAULT 0, "
				+ " another INTEGER NOT NULL DEFAULT 0,"
				+ " anotherID CHAR[255]"
				+ ")");
		
		//History version 17->18
		db.execSQL("CREATE TABLE History ("
				+ " id INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ " ts INTEGER NOT NULL,"
				+ " item INTEGER NOT NULL DEFAULT 0,"
				+ " type INTEGER NOT NULL DEFAULT 0,"
				+ " content CHAR[255] NOT NULL"
				+ ")");

		//OtherNoteAdd version 19->20
		db.execSQL("CREATE TABLE OtherNoteAdd ("
				+ " id INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ " userId CHAR[255] NOT NULL, "
				+ " ts INTEGER NOT NULL,"
				+ " key INTEGER NOT NULL DEFAULT 0,"
				+ " isReflection INTEGER NOT NULL DEFAULT 0,"
				+ " event CHAR[255] NOT NULL, "
				+ " thinking CHAR[255] NOT NULL, "
				+ " feeling CHAR[255] NOT NULL "
				+ ")");

		db.execSQL("CREATE TABLE EventLog ("
				+ " id INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ " editTime INTEGER NOT NULL, "
				+ " eventTime INTEGER NOT NULL, "
				+ " creatTime INTEGER NOT NULL, "
				+ " scenarioType INTEGER NOT NULL DEFAULT 0, "
				+ " scenario CHAR[255] NOT NULL, "
				+ " drugUseRiskLevel NOT NULL DEFAULT 0, "
				+ " originalBehavior CHAR[255] NOT NULL, "
				+ " originalEmotion CHAR[255] NOT NULL DEFAULT 0, "
				+ " originalThought CHAR[255] NOT NULL, "
				+ " expectedBehavior CHAR[255] NOT NULL, "
				+ " expectedEmotion CHAR[255] NOT NULL DEFAULT 0, "
				+ " expectedThought CHAR[255] NOT NULL, "
				+ " therapyStatus INTEGER NOT NULL DEFAULT 0, "
				+ " isAfterTest INTEGER NOT NULL DEFAULT 0, "
				+ " isComplete INTEGER NOT NULL DEFAULT 0"
				+ ")");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int old_ver, int new_ver) {

	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
	}

	@Override
	public synchronized void close() {
		super.close();
	}

}
