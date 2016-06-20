package ubicomp.ketdiary.fragments.saliva_test.test_states;

import ubicomp.ketdiary.fragments.saliva_test.SalivaTestAdapter;

/**
 * Created by kelvindk on 16/6/17.
 */
public abstract class TestStateTransition {

//    public static final int IDLE = 0;
//    public static final int CONNECTING = 1;
//    public static final int CONNECT_FAIL = 2;

    public static final int TEST_BUTTON_CLICK = 0;
    public static final int BLE_ENABLE_USER_PRESS_CANCEL = 1;
    public static final int BLE_ENABLE_USER_PRESS_CONFIRM = 2;
    public static final int BLE_CONNECTION_TIMEOUT = 3;

    private SalivaTestAdapter salivaTestAdapter = null;

    public TestStateTransition(SalivaTestAdapter salivaTestAdapter) {
        this.salivaTestAdapter = salivaTestAdapter;
    }


    /*
    *
    * */
    public abstract TestStateTransition transit(int trigger);

    public SalivaTestAdapter getSalivaTestAdapter() {
        return salivaTestAdapter;
    }



}
