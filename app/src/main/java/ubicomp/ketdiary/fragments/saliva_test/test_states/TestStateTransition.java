package ubicomp.ketdiary.fragments.saliva_test.test_states;

import ubicomp.ketdiary.fragments.saliva_test.SalivaTestAdapter;

/**
 * Created by kelvindk on 16/6/17.
 */
public abstract class TestStateTransition {

    /*** Various types of trigger ***/
    public static final int TEST_BUTTON_CLICK = 0;
    public static final int BLE_ENABLE_USER_PRESS_CANCEL = 1;
    public static final int BLE_ENABLE_USER_PRESS_CONFIRM = 2;
    public static final int BLE_CONNECTION_TIMEOUT = 3;
    public static final int BLE_DEVICE_CONNECTED = 4;
    public static final int BLE_DEVICE_DISCONNECTED = 5;
    public static final int BLE_NO_CASSETTE_PLUGGED = 6;
    public static final int BLE_CASSETTE_PLUGGED = 7;
    public static final int BLE_WRITE_CHAR_FAIL = 8;
    public static final int BLE_UPDATE_SALIVA_VOLTAGE = 9;
    public static final int TEST_TRANSIT_STAGE3 = 10;
    public static final int TEST_TRANSIT_STAGE3_RESPIT = 11;
    public static final int TEST_FINISH = 12;
    public static final int DEVICE_LOW_BATTERY = 13;
    public static final int BLE_GET_IMAGE_SUCCESS = 14;
    public static final int BLE_GET_IMAGE_FAILURE = 15;
    public static final int CANCEL_COUNTDOWN_TIMER = 16;

    private SalivaTestAdapter salivaTestAdapter = null;

    public TestStateTransition(SalivaTestAdapter salivaTestAdapter) {
        this.salivaTestAdapter = salivaTestAdapter;
    }


    /*
    * transit() is an abstract method with parameter "trigger" to be implemented by child classes.
    * */
    public abstract TestStateTransition transit(int trigger);


    /*
    *  Get salivaTestAdapter to access UI resource.
    * */
    public SalivaTestAdapter getSalivaTestAdapter() {
        return salivaTestAdapter;
    }



}
