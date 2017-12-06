package donate.tcs.com.myapplication.activities;

import android.os.Bundle;

import donate.tcs.com.myapplication.R;
import donate.tcs.com.myapplication.database.DataBaseRoomHelper;

/**
 * Created by navas on 06/12/17.
 */

public class HomeScreenActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.see_groups_activity);
        getSupportActionBar().setTitle("Home");
    }
}
