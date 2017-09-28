package donate.tcs.com.myapplication.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import donate.tcs.com.myapplication.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void onSeeListClicked(View v){
        startActivity(new Intent(this, ShowGroupsActivity.class));
    }

    public void onAddEntryClicked(View v){
        startActivity(new Intent(this, AddEntryActivity.class));
    }
}
