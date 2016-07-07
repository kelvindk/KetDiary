package ubicomp.ketdiary.utility.data.structure;

import android.util.EventLog;

import java.io.Serializable;
import java.util.ArrayList;

import ubicomp.ketdiary.R;
import ubicomp.ketdiary.fragments.event.EventLogStructure;

/**
 * Created by yu on 2016/7/2.
 */
public class TriggerRanking implements Comparable, Serializable {

    // Key of this object for deliver between activities through Intent.
    public static final String TRIGGER_RANKING_STRUCUTRE_KEY = "TriggerRanking";

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

    public int scenarioTypeToIconId() {
        int iconId = 0;
        switch (scenarioType) {
            case SLACKNESS:
                iconId = R.drawable.type_icon1;
                break;
            case BODY:
                iconId = R.drawable.type_icon2;
                break;
            case CONTROL:
                iconId = R.drawable.type_icon3;
                break;
            case IMPULSE:
                iconId = R.drawable.type_icon4;
                break;
            case EMOTION:
                iconId = R.drawable.type_icon5;
                break;
            case GET_ALONG:
                iconId = R.drawable.type_icon6;
                break;
            case SOCIAL:
                iconId = R.drawable.type_icon7;
                break;
            case ENTERTAIN:
                iconId = R.drawable.type_icon8;
                break;
        }
        return iconId;
    }
}
