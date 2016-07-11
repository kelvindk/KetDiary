package ubicomp.ketdiary.utility.data.db;

import java.util.Calendar;

import ubicomp.ketdiary.R;
import ubicomp.ketdiary.utility.data.structure.AddScore;
import ubicomp.ketdiary.utility.statistic.CustomToast;
import ubicomp.ketdiary.utility.system.PreferenceControl;
/**
 * Created by yu on 2016/7/10.
 */
public class AddScoreDataBase {
    private DatabaseControl db;

    public AddScoreDataBase(){
        db = new DatabaseControl();
    }

    public void addScoreEventLog(int index1, int index2)
    {
        Calendar cal = Calendar.getInstance();
        long ts = cal.getTimeInMillis();

        // lastest time about add score for event
        Calendar pre_event = Calendar.getInstance();
        pre_event.setTimeInMillis(PreferenceControl.getLastestNoteAddTimestamp());

        // lastest time about add score for thinking
        Calendar pre_thinking = Calendar.getInstance();
        pre_thinking.setTimeInMillis(PreferenceControl.getLastestThinkingTimestamp());

        // lastest time about add score for reflection
        Calendar pre_reflection = Calendar.getInstance();
        pre_reflection.setTimeInMillis(PreferenceControl.getLastestReflectionTimestamp());

        boolean eventAble = !(cal.get(Calendar.YEAR) == pre_event.get(Calendar.YEAR) &&
                cal.get(Calendar.DAY_OF_YEAR) == pre_event.get(Calendar.DAY_OF_YEAR));

        boolean thinkingAble = !(cal.get(Calendar.YEAR) == pre_thinking.get(Calendar.YEAR) &&
                cal.get(Calendar.DAY_OF_YEAR) == pre_thinking.get(Calendar.DAY_OF_YEAR));

        boolean reflectAble = !(cal.get(Calendar.YEAR) == pre_reflection.get(Calendar.YEAR) &&
                cal.get(Calendar.DAY_OF_YEAR) == pre_reflection.get(Calendar.DAY_OF_YEAR));


        String reason = "";
        int reasonBit = 0;
        int score = 0;

        // 0 ~ 2 ; 3 ~ 4 is event, 5 is thinking, 6 ~ 8 is reflection
        if(eventAble && index1 < 4 && index2 >= 4)
        {
            score++;
            reason += "填寫事件 ";
            reasonBit += AddScore.NOTE;
            PreferenceControl.setLastestNoteAddTimestamp(ts);
        }
        if(thinkingAble && index1 < 5 && index2 >= 5)
        {
            score++;
            reason += "填寫想法 ";
            reasonBit += AddScore.THINKING;
            PreferenceControl.setLastestThinkingTimestamp(ts);
        }
        if(reflectAble && index1 < 8 && index2 >= 8)
        {
            score++;
            reason += "填寫反思 ";
            reasonBit += AddScore.REFLECTION;
            PreferenceControl.setLastestReflectionTimestamp(ts);
        }

        // show toast and insert to database
        if(score > 0) {
            CustomToast.generateToast(setCustomToastMessage(reasonBit), score);
            AddScore addScore = new AddScore(ts, score, 0, reason, 0, reasonBit);
            db.insertAddScore(addScore);
        }
    }

    public void addScoreViewEventLog()
    {
        Calendar cal = Calendar.getInstance();
        long ts = cal.getTimeInMillis();

        // lastest time about add score for view event
        Calendar pre_event = Calendar.getInstance();
        pre_event.setTimeInMillis(PreferenceControl.getLastestViewEventTimestamp());


        boolean eventAble = !(cal.get(Calendar.YEAR) == pre_event.get(Calendar.YEAR) &&
                cal.get(Calendar.DAY_OF_YEAR) == pre_event.get(Calendar.DAY_OF_YEAR));

        // show toast and insert to database
        if(eventAble) {
            CustomToast.generateToast(R.string.add_score_view_event, 1);
            AddScore addScore = new AddScore(ts, 1, 0, "瀏覽事件", 0, AddScore.VIEW_EVENT);
            db.insertAddScore(addScore);

            PreferenceControl.setLastestViewEventTimestamp(ts);
        }
    }

    public void addScoreIdentity()
    {
        Calendar cal = Calendar.getInstance();
        long ts = cal.getTimeInMillis();

        // lastest time about add score for identity
        Calendar pre_event = Calendar.getInstance();
        pre_event.setTimeInMillis(PreferenceControl.getLastestIdentifyTimestamp());


        boolean eventAble = !(cal.get(Calendar.YEAR) == pre_event.get(Calendar.YEAR) &&
                cal.get(Calendar.DAY_OF_YEAR) == pre_event.get(Calendar.DAY_OF_YEAR));

        // show toast and insert to database
        if(eventAble) {
            CustomToast.generateToast(R.string.add_score_identity, 1);
            AddScore addScore = new AddScore(ts, 1, 0, "填寫認同度", 0, AddScore.IDENTITY);
            db.insertAddScore(addScore);

            PreferenceControl.setLastestIdentifyTimestamp(ts);
        }
    }

    private int setCustomToastMessage(int reason){
        if(reason == AddScore.NOTE + AddScore.THINKING + AddScore.REFLECTION)
            return R.string.add_score_event_thinking_reflection;
        if(reason == AddScore.NOTE + AddScore.THINKING)
            return R.string.add_score_event_thinking;
        if(reason == AddScore.THINKING + AddScore.REFLECTION)
            return R.string.add_score_thinking_reflection;
        if(reason == AddScore.NOTE)
            return R.string.add_score_event;
        if(reason == AddScore.THINKING)
            return R.string.add_score_thinking;
        if(reason == AddScore.REFLECTION)
            return R.string.add_score_reflection;
        return 0;
    }
}
