package ubicomp.rehabdiary.fragments.event;

import java.io.Serializable;
import java.util.Calendar;

import ubicomp.rehabdiary.R;

/**
 *  Data structure of event log.
 *
 * Created by kelvindk on 16/6/13.
 */
public class EventLogStructure implements Serializable {

    // Key of this object for deliver between activities through Intent.
    public static final String EVENT_LOG_STRUCUTRE_KEY = "EventLogStructure";

    public final static int MIDNIGHT = 0;
    public final static int MORNING = 1;
    public final static int AFTERNOON = 2;
    public final static int NIGHT = 3;


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
        ALL,
        NULL,
    };


    public enum TherapyStatusEnum {
        NOT_YET, //未審核
        GOOD, // 己審核
        BAD, // 待修正
        DISCUSSED, // 己討論
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

    public boolean reviseOriginalBehavior = false;
    public String originalBehavior = "";

    public boolean reviseOriginalEmotion = false;
    public String originalEmotion = "";

    public boolean reviseOriginalThought = false;
    public String originalThought = "";

    public boolean reviseExpectedBehavior = false;
    public String expectedBehavior = "";

    public boolean reviseExpectedEmotion = false;
    public String expectedEmotion = "";

    public boolean reviseExpectedThought = false;
    public String expectedThought = "";

    // Event status related therapist validation.
    public TherapyStatusEnum therapyStatus = TherapyStatusEnum.NULL;

    // Whether this event is filled just after routine test.
    public boolean isAfterTest = false;

    // Whether user has test today.
    public boolean isTestToday = false;

    // Whether this event is completely filled.
    public boolean isComplete = false;

    // Whether this event has been reflected.
    public boolean isReflected = false;


    public String eventTimeToString() {
        String week = "";
        switch(eventTime.get(Calendar.DAY_OF_WEEK)){
            case Calendar.SUNDAY:
                week = "(日)";
                break;
            case Calendar.MONDAY:
                week = "(一)";
                break;
            case Calendar.TUESDAY:
                week = "(二)";
                break;
            case Calendar.WEDNESDAY:
                week = "(三)";
                break;
            case Calendar.THURSDAY:
                week = "(四)";
                break;
            case Calendar.FRIDAY:
                week = "(五)";
                break;
            case Calendar.SATURDAY:
                week = "(六)";
        }

        String timePeriod = "";
        int hour = eventTime.get(Calendar.HOUR_OF_DAY);

        switch(hour/6) {
            case MIDNIGHT:
                timePeriod = "半夜";
                break;
            case MORNING:
                timePeriod = "早上";
                break;
            case AFTERNOON:
                timePeriod = "下午";
                break;
            case NIGHT:
                timePeriod = "晚上";
                break;
        }
        return (eventTime.get(Calendar.MONTH)+1)+"/"+eventTime.get(Calendar.DAY_OF_MONTH)+ " "
                +week+" "+timePeriod;

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

    public String therapyStatusToIconString() {
        String status = "";
        switch (therapyStatus) {
            case NOT_YET:
            case NULL:
                status = "未審核";
                break;
            case GOOD:
                status = "己審核";
                break;
            case BAD:
                status = "待修正";
                break;
            case DISCUSSED:
                status = "己討論";
                break;
        }
        return status;
    }

    public int therapyStatusToIconId() {
        int iconId = 0;
        switch (therapyStatus) {
            case NOT_YET:
                iconId = R.drawable.blank2;
                break;
            case GOOD:
                iconId = R.drawable.circle2;
                break;
            case BAD:
                iconId = R.drawable.tri2;
                break;
            case DISCUSSED:
                iconId = R.drawable.star2;
                break;
        }
        return iconId;
    }

}
