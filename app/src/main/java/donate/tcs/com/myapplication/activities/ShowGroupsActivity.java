package donate.tcs.com.myapplication.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.ajts.androidmads.library.SQLiteToExcel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import donate.tcs.com.myapplication.bean.MemberDetails;
import donate.tcs.com.myapplication.database.DataBaseRoomHelper;
import donate.tcs.com.myapplication.R;

/**
 * Created by 351863 on 27-09-2017.
 */

public class ShowGroupsActivity extends BaseActivity {

    private final String[] groups = {"All", "A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-"};
    private RecyclerView recyclerView;
    private TextView groupTitle;
    private DonorListAdapter mAdapter;
    //DatabaseHelper dbHelper;
    DataBaseRoomHelper dataBaseRoomHelper;
    final List<MemberDetails> memberDetailsList = new ArrayList<>();
    private boolean isDeleteMode = false;
    private MenuItem deleteItem;


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

    private void setRecyclerView() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        setData();
        mAdapter = new DonorListAdapter(memberDetailsList, new DonorListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MemberDetails item) {
                //TODO SHow the dialog for calling and sms
            }

            @Override
            public void onDeleteItemClick(MemberDetails item) {
                deleteItem(item.employeeId);
            }
        });
        recyclerView.setAdapter(mAdapter);
    }

    private void deleteItem(final String id) {
        final DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("memberslist");
        showProgressDialog();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                hideProgressDialog();
                memberDetailsList.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    MemberDetails memberDetails = postSnapshot.getValue(MemberDetails.class);
                    if (memberDetails.employeeId.equals(id)) {
                        myRef.child(id).removeValue();
                    } else {
                        memberDetailsList.add(memberDetails);
                    }
                }
                sortList();
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                hideProgressDialog();
                Toast.makeText(ShowGroupsActivity.this, "Failed deleting from list.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setData() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("memberslist");
        showProgressDialog();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                hideProgressDialog();
                memberDetailsList.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    MemberDetails memberDetails = postSnapshot.getValue(MemberDetails.class);
                    memberDetailsList.add(memberDetails);
                }
                sortList();
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                hideProgressDialog();
                Toast.makeText(ShowGroupsActivity.this, "Failed fetching details from db.", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_see_list, menu);
        deleteItem = menu.findItem(R.id.delete_items);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter:
                showFilters();
                break;

            case R.id.action_add:
                startActivity(new Intent(this, AddEntryActivity.class).putExtra("aaa", "aaa"));
                break;

            case R.id.delete_items:
                isDeleteMode = !isDeleteMode;
                deleteItem.setIcon(isDeleteMode ? R.drawable.ic_tick : R.drawable.ic_delete);

                if (isDeleteMode) {
                    mAdapter.setDeleteMode();
                } else {
                    mAdapter.resetDeleteMode();
                }
                break;

        }
        return true;
    }

    private void showFilters() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick a group");
        builder.setItems(groups, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, final int which) {
                dialog.dismiss();
                List<MemberDetails> itemsList;
                if (which == 0) {
                    //itemsList = Arrays.asList(dataBaseRoomHelper.dataEntryDao().getAllItems());
                    setData();
                    groupTitle.setText("All");
                } else {
                    //itemsList = Arrays.asList(dataBaseRoomHelper.dataEntryDao().getItemForGroup(groups[which]));
                    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("memberslist");
                    showProgressDialog();
                    myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            hideProgressDialog();
                            memberDetailsList.clear();
                            for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                                MemberDetails memberDetails = postSnapshot.getValue(MemberDetails.class);
                                if (memberDetails.bloodGroup.equalsIgnoreCase(groups[which])) {
                                    memberDetailsList.add(memberDetails);
                                }
                            }
                            sortList();
                            mAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            hideProgressDialog();
                            Toast.makeText(ShowGroupsActivity.this, "Failed fetching details from db.", Toast.LENGTH_SHORT).show();
                        }
                    });
                    groupTitle.setText(groups[which]);
                }
                //mAdapter.setItemsList(itemsList);
            }
        });
        builder.show();
    }

    private void sortList() {
        Collections.sort(memberDetailsList, new Comparator<MemberDetails>() {
            @Override
            public int compare(MemberDetails memberDetails, MemberDetails t1) {
                return memberDetails.name.compareTo(t1.name);
            }
        });
    }


    private void convertDbToExcel() {
        String directory_path = Environment.getExternalStorageDirectory().getPath() + "/Backup/";
        File file = new File(directory_path);
        if (!file.exists()) {
            file.mkdirs();
        }
        // Export SQLite DB as EXCEL FILE
        SQLiteToExcel sqliteToExcel = new SQLiteToExcel(getApplicationContext(), "blood_donor_list", directory_path);
        sqliteToExcel.exportAllTables("users.xls", new SQLiteToExcel.ExportListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onCompleted(String filePath) {
                System.out.println("completed converting");
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }
}
