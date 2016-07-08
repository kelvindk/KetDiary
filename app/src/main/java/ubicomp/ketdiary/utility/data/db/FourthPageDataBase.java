package ubicomp.ketdiary.utility.data.db;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ubicomp.ketdiary.fragments.event.EventLogStructure;
import ubicomp.ketdiary.utility.data.structure.Reflection;
import ubicomp.ketdiary.utility.data.structure.TriggerRanking;

/**
 * Created by yu on 2016/7/2.
 */
public class FourthPageDataBase {
    private DatabaseControl db;

    public FourthPageDataBase(){
        db = new DatabaseControl();
    }

    public TriggerRanking[] getRankingList(){
        EventLogStructure[] data = db.getEventLogByScenario();
        if(data == null)
            return null;

        ArrayList<TriggerRanking> rankList = new ArrayList<TriggerRanking>();

        int now_index = -1;
        for (int i = 0; i < data.length; i++)
        {
            Log.d("GG", "create : " + data[i].createTime.getTimeInMillis());
            // next type
            if(now_index == -1 || !data[i].scenario.equals(data[i-1].scenario)) {
                now_index++;
                TriggerRanking rankItem = new TriggerRanking();
                rankItem.scenarioType = data[i].scenarioType;
                rankItem.scenario = data[i].scenario;

                rankList.add(rankItem);
            }

            // push into TriggerRanking
            TriggerRanking item = rankList.get(now_index);

            item.allDrugUseRisk += data[i].drugUseRiskLevel;
            item.eventLogNum += 1;

            if(!data[i].expectedBehavior.equals("")  || !data[i].expectedThought.equals(""))
                item.eventLogList.add(data[i]);

            if(data[i].isComplete)
                item.completeEventLogNum++;

            rankList.set(now_index, item);
        }

        // calculate the average
        for (int i = 0; i < rankList.size(); i++)
        {
            TriggerRanking item = rankList.get(i);

            item.averageDrugUseRisk = (float) item.allDrugUseRisk / item.eventLogNum;

            rankList.set(i, item);
        }

        // calculate the ratio and no pass day
        for (int i = 0; i < rankList.size(); i++)
        {
            TriggerRanking item = rankList.get(i);

            item.completePercentage = (float) item.completeEventLogNum / item.eventLogNum;
            item.noPassDay = db.getNoPassNum(item.scenario);

            rankList.set(i, item);
        }
        // Copy to Array
        TriggerRanking[] returnList = new TriggerRanking[rankList.size()];
        for (int i = 0; i < rankList.size(); i++)
            returnList[i] = rankList.get(i);

        // sort
        Arrays.sort(returnList);

        return returnList;
    }
}
