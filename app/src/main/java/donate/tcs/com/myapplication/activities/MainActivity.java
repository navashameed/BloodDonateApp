package donate.tcs.com.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import donate.tcs.com.myapplication.Constants;
import donate.tcs.com.myapplication.utils.Preferences;

/**
 * Created by navas on 11/10/17.
 */

public class MainActivity extends BaseActivity {

    Preferences preferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = new Preferences(getApplicationContext());
        if(preferences.readBoolean(Constants.IS_LOGGED_INTO_APP)){
            startActivity(new Intent(this, ActionsListActivity.class));
        }
        else{
            startActivity(new Intent(this, LogInActivity.class));
        }
        finish();

    }
}
