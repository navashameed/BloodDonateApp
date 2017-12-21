package donate.tcs.com.myapplication.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import donate.tcs.com.myapplication.AppUtils;
import donate.tcs.com.myapplication.bean.MemberDetails;
import donate.tcs.com.myapplication.database.DataBaseRoomHelper;
import donate.tcs.com.myapplication.database.DatabaseHelper;
import donate.tcs.com.myapplication.R;

/**
 * Created by 351863 on 27-09-2017.
 */

public class AddEntryActivity extends BaseActivity {

    private Spinner bloodTypesSpinner;
    private EditText nameEditText;
    private EditText empIdText;
    private EditText phoneNumberText;
    private EditText emailIdText;
    private Button addButton;

    private String bloodGroup;
    private DataBaseRoomHelper db;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_entry_activity);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bloodTypesSpinner = findViewById(R.id.blood_groups_list);
        nameEditText = findViewById(R.id.input_name);
        empIdText = findViewById(R.id.input_emp_id);
        phoneNumberText = findViewById(R.id.input_phone_number);
        emailIdText = findViewById(R.id.input_email_id);
        addButton = findViewById(R.id.add_entry_button);

        getSupportActionBar().setTitle("Add Entry");

        db = DataBaseRoomHelper.getInstance(getApplicationContext());

        mDatabase = FirebaseDatabase.getInstance().getReference();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.blood_groups, android.R.layout.simple_spinner_item);
        bloodTypesSpinner.setAdapter(adapter);
        bloodTypesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                bloodGroup = adapterView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //validation
                String name = nameEditText.getText().toString();
                String empId = empIdText.getText().toString();
                String phoneNumber = phoneNumberText.getText().toString();
                String emailId = emailIdText.getText().toString();
                if (name.isEmpty()) {
                    Toast.makeText(AddEntryActivity.this, "Please enter the name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (empId.isEmpty()) {
                    Toast.makeText(AddEntryActivity.this, "Please enter the employee id", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (bloodGroup.isEmpty() || bloodGroup.equalsIgnoreCase("select")) {
                    Toast.makeText(AddEntryActivity.this, "Blood Group is mandatory. Please select one", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (emailId.length() > 0 && !isValidEmail(emailId)) {
                    Toast.makeText(AddEntryActivity.this, "Please enter a valid email id.", Toast.LENGTH_SHORT).show();
                    return;
                }

                //insertDataToDatabase(empId, name, bloodGroup, phoneNumber );
                //insertToRoomDb(empId, name, bloodGroup, phoneNumber );
                insertToFirebaseDb(empId, name, bloodGroup, phoneNumber, emailId);

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void insertToFirebaseDb(String empId, final String name, String bloodGroup, String phoneNumber, String emailId) {
        MemberDetails memberDetails = new MemberDetails(Long.valueOf(empId), name, empId, bloodGroup, phoneNumber, emailId);

        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                hideProgressDialog();

                if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("aaa")) {
                    finish();
                    Toast.makeText(AddEntryActivity.this,
                            "User added successfully",
                            Toast.LENGTH_SHORT).show();
                } else {
                    showDialog("Success", "User " + "'" + name + "'" + " successfully added to the group");
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }


            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                hideProgressDialog();
                showDialog("Error", "Error occured. Please try  again.");
            }
        });

        showProgressDialog();

        mDatabase.child("memberslist").child(memberDetails.phoneNumber).setValue(memberDetails);
    }

//    private void insertDataToDatabase(String empId, String name, String bloodGroup, String phoneNumber) {
//        DatabaseHelper dbHelper = DatabaseHelper.getInstance(getApplicationContext());
//        if (AppUtils.idExists(empId)) {
//            showDialog("Error", "Id already exists. Modify details is work in progress");
//            return;
//        }
//        long id = dbHelper.insertEntry(empId, name, bloodGroup, phoneNumber);
//        AppUtils.updateIds(dbHelper.getExistingIds());
//
//        if (id > -1) {
//            showDialog("Success", "You have been successfully added to the group");
//        } else {
//            showDialog("Error", "Error occured. Please try  again.");
//        }
//    }
//
//    private void insertToRoomDb(String empId, String name, String bloodGroup, String phoneNumber) {
//        MemberDetails memberDetails = new MemberDetails(Long.valueOf(empId), name, empId, bloodGroup, phoneNumber);
//        long id = 0;
//        try {
//            id = db.dataEntryDao().insertEntry(memberDetails);
//        } catch (SQLiteConstraintException e) {
//            showDialog("Error", "Id already exists. Modify details is work in progress");
//            return;
//        }
//
//        if (id > -1) {
//            showDialog("Success", "You have been successfully added to the group");
//        } else {
//            showDialog("Error", "Error occured. Please try  again.");
//        }
//    }


    private void showDialog(String title, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(AddEntryActivity.this).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
}
