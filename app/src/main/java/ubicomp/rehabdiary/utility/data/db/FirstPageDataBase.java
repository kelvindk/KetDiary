package ubicomp.rehabdiary.utility.data.db;

import java.util.Calendar;

import ubicomp.rehabdiary.R;
import ubicomp.rehabdiary.utility.data.structure.AddScore;
import ubicomp.rehabdiary.utility.data.structure.IdentityScore;
import ubicomp.rehabdiary.utility.data.structure.TestDetail;
import ubicomp.rehabdiary.utility.data.structure.TestResult;
import ubicomp.rehabdiary.utility.data.structure.Cassette;
import ubicomp.rehabdiary.utility.statistic.CustomToast;
import ubicomp.rehabdiary.utility.system.PreferenceControl;

/**
 * Created by yu on 16/6/26.
 */
public class FirstPageDataBase {
    private DatabaseControl db;

    public FirstPageDataBase(){
        db = new DatabaseControl();
    }

    // Get user's ID
    public String getUserId(){
        return PreferenceControl.getUID();
    }

    // Get device ID
    public String getDeviceId(){
        return PreferenceControl.getDeviceId();
    }

    // Check user is Developer
    public boolean isDeveloper(){
        return PreferenceControl.isDeveloper();
    }

    // add a new test result
    public void addTestResult(TestResult data){
        int score = db.insertTestResult(data);

        int reasonBit = 0, toastId = 0;
        String reason = "";
        if(data.result == 0)
        {
            reasonBit += AddScore.TEST_PASS;
            reason = "檢測通過";
            toastId = R.string.after_test_pass;
        }
        if(data.result == 1) {
            reasonBit += AddScore.TEST_NO_PASS;
            reason = "檢測未通過";
            toastId = R.string.after_test_fail;
            if(score == 0)
                score = -1;
        }
        CustomToast.generateToast(toastId, score);
        AddScore addScore = new AddScore(System.currentTimeMillis(), score, 0, reason, 0, reasonBit);
        db.insertAddScore(addScore);
    }

    // add a new test detail
    public void addTestDetail(TestDetail data){
        db.insertTestDetail(data);
    }

    // get the cassette list
    public Cassette[] getAllCassette(){
        return db.getAllCassette();
    }

    // check cassette used
    public boolean checkCassetteUsed(String cassette_id){
        return db.checkCassette(cassette_id);
    }

    // set the cassette used
    public void setCassetteUsed(String cassette_id ){
        db.insertCassette(cassette_id );
    }

    public boolean checkTestStatus(Calendar cal){
        return db.checkTestStatus(cal);
    }

    public void addIdentityScore(IdentityScore data){
        db.insertIdentityScore(data);
    }
}
