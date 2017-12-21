package donate.tcs.com.myapplication.activities;

import android.os.Bundle;
import android.widget.TextView;

import donate.tcs.com.myapplication.R;

/**
 * Created by navas on 06/12/17.
 */

public class HomeScreenActivity extends BaseActivity {

    TextView totalDonorsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.see_groups_activity);
        getSupportActionBar().setTitle("Home");

        totalDonorsTextView = findViewById(R.id.total_donors_text);
    }
}
