package ubicomp.ketdiary.utility.data.structure;

import android.util.EventLog;

import java.util.ArrayList;

import ubicomp.ketdiary.fragments.event.EventLogStructure;

/**
 * Created by yu on 2016/7/2.
 */
public class TriggerRanking implements Comparable{

    // Scenario type
    public EventLogStructure.ScenarioTypeEnum scenarioType = EventLogStructure.ScenarioTypeEnum.NULL;

    // Selected scenario.
    public String scenario = "";

    // sum of each DrugUseRisk
    public int allDrugUseRisk = 0;

    // amount of no pass day
    public int noPassDay = 0;

    // amount of EventLog
    public int eventLogNum = 0;

    // iscomplete EventLog
    public int completeEventLogNum = 0;

    // complete ratio
    public float completePercentage = 0;

    // average of DrugUseRisk : allDrugUseRisk/eventLogNum
    public float averageDrugUseRisk = 0;

    //
    public ArrayList<EventLogStructure> eventLogList = new ArrayList<EventLogStructure>();

    // Decreasing
    public int compareTo(Object item){
        if(this.allDrugUseRisk > ((TriggerRanking)item).allDrugUseRisk){
            return -1;
        }else{
            return 1;
        }
    }
}
