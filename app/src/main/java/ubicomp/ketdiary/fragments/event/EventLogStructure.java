package ubicomp.ketdiary.fragments.event;

import java.util.ArrayList;
import java.util.Calendar;

/**
 *  Data structure of event log.
 *
 * Created by kelvindk on 16/6/13.
 */
public class EventLogStructure {

    // Eight scenario type enum.
    public enum ScenarioEnum {
        SLACKNESS, //鬆懈
        BODY, //身體
        CONTROL, //控制
        IMPULSE, //衝動
        EMOTION, //情緒
        GET_ALONG, //相處
        SOCIAL, //社交
        ENTERTAIN, //同樂
    };

    // Eight emotion type enum.
    public enum EmotionEnum {
        ENGRY, //生氣
        SAD, //難過
        NERVOUS, //緊張
        HATE, //厭惡
        HAPPY, //開心
        SCARED, //驚恐
        BORING, //無聊
        DESIRE, //渴望
    };


    public enum TherapyStatusEnum {
        NOT_YET, //未審核
        GOOD,
        BAD,
    };


    // A list contains timestamps of edit action.
    public ArrayList<Calendar> editTime = new ArrayList<Calendar>();

    // Event timestamp.
    public Calendar eventTime = null;

    // Scenario type
    ScenarioEnum scnearioType = null;

    // Selected scenario.
    String scenario = null;

    // Risk level of drug use. 1~5. 0: not yet to set.
    int drugUseRiskLevel = 0;

    String originalBehavior = null;

    EmotionEnum originalEmotion = null;

    String originalThought = null;

    String expectedlBehavior = null;

    EmotionEnum expectedEmotion = null;

    String expectedThought = null;

    TherapyStatusEnum therapyStatus = null;

    // Whether this event is filled just after routine test.
    boolean isAfterTest = false;

    // Whether this event is completely filled.
    boolean isComplete = false;

}
