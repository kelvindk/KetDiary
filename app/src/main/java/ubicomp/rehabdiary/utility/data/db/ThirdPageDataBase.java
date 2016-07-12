package ubicomp.rehabdiary.utility.data.db;

import java.util.Calendar;

import ubicomp.rehabdiary.fragments.event.EventLogStructure;
import ubicomp.rehabdiary.utility.data.structure.TriggerItem;

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

    // edit a old EventLog
    public void editEventLog(EventLogStructure data)
    {
        db.updateOldEventLog(data);
        db.insertEventLog(data);
    }

    // get all EventLog
    public EventLogStructure[] getAllEventLog(){
        return db.getAllEventLog();
    }

    // get all EventLog after a time
    public EventLogStructure[] getLaterEventLog(Calendar cal){
        return db.getAfterEventLog(cal);
    }

    // get all EventLog which isComplete is false
    public EventLogStructure[] getNotCompleteEventLog(){
        return db.getNotCompleteEventLog();
    }

    // get all trigger
    public TriggerItem[] getAllTrigger()
    {
        return db.getAllTriggerItem();
    }

    // get the riggers with type
    public TriggerItem[] getTypeTrigger(int type)
    {
        return db.getTypeTriggerItem(type);
    }


    // get the recent N originalBehavior
    public String[] getHistoryOriginalBehavior(String scenario, int n){
        return db.getEventLogMessageByScenario(scenario, 1, n);
    }

    // get the recent N originalThought
    public String[] getHistoryOriginalThought(String scenario, int n){
        return db.getEventLogMessageByScenario(scenario, 2, n);
    }

    // get the recent N expectedBehavior
    public String[] getHistoryExpectedBehavior(String scenario, int n){
        return db.getEventLogMessageByScenario(scenario, 3, n);
    }

    // get the recent N expectedThought
    public String[] getHistoryExpectedThought(String scenario, int n){
        return db.getEventLogMessageByScenario(scenario, 4, n);
    }

    // debug use
    public void insertDummyTrigger()
    {
        TriggerItem[] items = new TriggerItem[16];

        int[] num = { 100, 101, 200, 201, 300, 301, 400, 401,
                    500, 501, 600, 601, 700, 701, 800, 801};

        String[] content = {
                "沒事情做", "沒有人陪我",
                "身體不舒服", "失眠",
                "心情很好", "感到自在且放鬆",
                "想要證明毒品對自己不是問題", "認為偶爾吸毒但不會上癮",
                "不知道為什麼就想用藥", "在常用藥或買藥的地方",
                "與親人相處上出狀況", "與朋友相處上出狀況",
                "被朋友邀約用藥覺得不好拒絕", "被朋友一直建議到某些地方用藥",
                "與老朋友相聚，想要更開心", "與朋友同樂，想助興"
        };

        for (int i = 0; i < 16; i++) {
            items[i] = new TriggerItem(num[i], content[i], true);
            db.insertTriggerItem(items[i]);
        }

    }
}
