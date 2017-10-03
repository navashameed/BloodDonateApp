package donate.tcs.com.myapplication.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import donate.tcs.com.myapplication.bean.DataEntry;
import donate.tcs.com.myapplication.database.DataBaseRoomHelper;
import donate.tcs.com.myapplication.database.DatabaseHelper;
import donate.tcs.com.myapplication.R;

/**
 * Created by 351863 on 27-09-2017.
 */

public class ShowGroupsActivity extends AppCompatActivity {

    private final String [] groups = {"All", "A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-"};
    private RecyclerView recyclerView;
    private TextView groupTitle;
    private DonorListAdapter mAdapter;
    //DatabaseHelper dbHelper;
    DataBaseRoomHelper dataBaseRoomHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.see_groups_activity);
        getSupportActionBar().setTitle("See List");
        recyclerView = findViewById(R.id.recycler_view);
        groupTitle = findViewById(R.id.group_title);
        groupTitle.setText("All");
        //dbHelper = DatabaseHelper.getInstance(getApplicationContext());
        dataBaseRoomHelper = DataBaseRoomHelper.getInstance(getApplicationContext());

        setRecyclerView();
    }

    private void setRecyclerView(){
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new DonorListAdapter(Arrays.asList(dataBaseRoomHelper.dataEntryDao().getAllItems()), new DonorListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DataEntry item) {

            }
        });
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_see_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter:
                showFilters();
                break;
        }
        return true;
    }

    private void showFilters(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick a group");
        builder.setItems(groups, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                List<DataEntry> itemsList;
                if(which ==0 ){
                    itemsList = Arrays.asList(dataBaseRoomHelper.dataEntryDao().getAllItems());
                    groupTitle.setText("All");
                }
                else{
                    itemsList = Arrays.asList(dataBaseRoomHelper.dataEntryDao().getItemForGroup(groups[which]));
                    groupTitle.setText(groups[which]);
                }
                mAdapter.setItemsList(itemsList);
            }
        });
        builder.show();
    }
}
