package ubicomp.ketdiary.utility.data.db;

import ubicomp.ketdiary.utility.data.structure.TestDetail;
import ubicomp.ketdiary.utility.data.structure.TestResult;
import ubicomp.ketdiary.utility.data.structure.Cassette;
import ubicomp.ketdiary.utility.system.PreferenceControl;

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
        db.insertTestResult(data);
    }

    // add a new test detail
    public void addTestDetail(TestDetail data){
        db.insertTestDetail(data);
    }

    // get the cassette list
    public Cassette[] getAllCassette(){
        return db.getAllCassette();
    }

    // set the cassette used
    public void setCassetteUsed(String cassette_id ){
        db.insertCassette(cassette_id );
    }

}
