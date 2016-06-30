package ubicomp.ketdiary.fragments.event;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

/**
 *  Data structure of event log.
 *
 * Created by kelvindk on 16/6/13.
 */
public class EventLogStructure implements Serializable {

    // Key of this object for deliver between activities through Intent.
    public static final String EVENT_LOG_STRUCUTRE_KEY = "EventLogStructure";


    // Eight scenario type enum.
    public enum ScenarioTypeEnum {
        SLACKNESS, //鬆懈
        BODY, //身體
        CONTROL, //控制
        IMPULSE, //衝動
        EMOTION, //情緒
        GET_ALONG, //相處
        SOCIAL, //社交
        ENTERTAIN, //同樂
        NULL,
    };


    public enum TherapyStatusEnum {
        NOT_YET, //未審核
        GOOD, // 己驗證
        BAD, // 有錯誤
        DISCUSSED, // 己與心理師討論
        NULL,
    };

    // A timestamps of event create time.
    public Calendar createTime = null;

    // A timestamps of edit action.
    public Calendar editTime = null;

    // Event timestamp.
    public Calendar eventTime = null;

    // Scenario type
    public ScenarioTypeEnum scenarioType = ScenarioTypeEnum.NULL;

    // Selected scenario.
    public String scenario = "";

    // Risk level of drug use. 1~5. 0: not yet to set.
    public int drugUseRiskLevel = 0;

    public boolean checkOriginalBehavior = false;
    public String originalBehavior = "";

    public boolean checkOriginalEmotion = false;
    public String originalEmotion = "";

    public boolean checkOriginalThought = false;
    public String originalThought = "";

    public boolean checkExpectedBehavior = false;
    public String expectedBehavior = "";

    public boolean checkExpectedEmotion = false;
    public String expectedEmotion = "";

    public boolean checkExpectedThought = false;
    public String expectedThought = "";

    // Event status related therapist validation.
    public TherapyStatusEnum therapyStatus = TherapyStatusEnum.NULL;

    // Whether this event is filled just after routine test.
    public boolean isAfterTest = false;

    // Whether this event is completely filled.
    public boolean isComplete = false;

}
