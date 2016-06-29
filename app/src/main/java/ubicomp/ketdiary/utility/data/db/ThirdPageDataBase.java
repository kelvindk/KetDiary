package ubicomp.ketdiary.utility.data.db;

import java.util.Calendar;

import ubicomp.ketdiary.fragments.event.EventLogStructure;

/**
 * Created by yu on 16/6/27.
 */
public class ThirdPageDataBase {
    private DatabaseControl db;

    public ThirdPageDataBase(){
        db = new DatabaseControl();
    }

    // add a new EventLog to database
    public void addNewEventLog(EventLogStructure data){
        db.insertEventLog(data);
    }

    // get all EventLog
    public EventLogStructure[] getAllEventLog(){
        return db.getAllEventLog();
    }

    // get all EventLog
    public EventLogStructure[] getLaterEventLog(Calendar cal){
        return db.getAfterEventLog(cal);
    }


}
