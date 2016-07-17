package ubicomp.rehabdiary.utility.dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;

import ubicomp.rehabdiary.R;
import ubicomp.rehabdiary.utility.system.PreferenceControl;

/**
 * Created by kelvindk on 16/7/17.
 */
public class PasswordLockDialogActivity extends Activity {

    public static final int PASSWORD_LOCK_INT_KEY = 312;

    // Password dialog object.
    PasswordPage passwordPage = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_lock_dialog);

        // Password lock.
        passwordPage =
                new PasswordPage((RelativeLayout) findViewById(R.id.password_lock_dialog_activity_layout),
                        PasswordPage.LOGIN_APP);
        passwordPage.setActivity(this);


        passwordPage.initialize();
        passwordPage.show();

        Log.d("Ket", "PasswordLockDialogActivity onCreate");
    }


    @Override
    public void onStart() {
        Log.d("Ket", "PasswordLockDialogActivity onStart");


        super.onStart();
    }

    public void onPasswordCorrectListener() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);

        super.onBackPressed();
    }
}
