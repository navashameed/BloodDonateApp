package donate.tcs.com.myapplication.activities;

import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import donate.tcs.com.myapplication.AppUtils;
import donate.tcs.com.myapplication.bean.DataEntry;
import donate.tcs.com.myapplication.database.DataBaseRoomHelper;
import donate.tcs.com.myapplication.database.DatabaseHelper;
import donate.tcs.com.myapplication.R;

/**
 * Created by 351863 on 27-09-2017.
 */

public class AddEntryActivity extends AppCompatActivity {

    private Spinner bloodTypesSpinner;
    private EditText nameEditText;
    private EditText empIdText;
    private EditText phoneNumberText;
    private Button addButton;

    private String bloodGroup;
    DataBaseRoomHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_entry_activity);
        bloodTypesSpinner =  findViewById(R.id.blood_groups_list);
        nameEditText =  findViewById(R.id.input_name);
        empIdText =  findViewById(R.id.input_emp_id);
        phoneNumberText =  findViewById(R.id.input_phone_number);
        addButton =  findViewById(R.id.add_entry_button);

        getSupportActionBar().setTitle("Add Entry");

        db = DataBaseRoomHelper.getInstance(getApplicationContext());

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.blood_groups, android.R.layout.simple_spinner_item);
        bloodTypesSpinner.setAdapter(adapter);
        bloodTypesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                bloodGroup =  adapterView.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //validation
                String name = nameEditText.getText().toString();
                String empId = empIdText.getText().toString();
                String phoneNumber = phoneNumberText.getText().toString();
                if(name.isEmpty()){
                   Toast.makeText(AddEntryActivity.this,"Please enter the name", Toast.LENGTH_SHORT).show();
                   return;
                }
                if(empId.isEmpty()){
                    Toast.makeText(AddEntryActivity.this,"Please enter the employee id", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(bloodGroup.isEmpty() || bloodGroup.equalsIgnoreCase("select")){
                    Toast.makeText(AddEntryActivity.this,"Blood Group is mandatory. Please select one", Toast.LENGTH_SHORT).show();
                    return;
                }

                //insertDataToDatabase(empId, name, bloodGroup, phoneNumber );
                insertToRoomDb(empId, name, bloodGroup, phoneNumber );

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void insertDataToDatabase(String empId, String name , String bloodGroup, String phoneNumber ){
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(getApplicationContext());
        if(AppUtils.idExists(empId)){
            showDialog("Error", "Id already exists. Modify details is work in progress");
            return;
        }
        long id = dbHelper.insertEntry(empId, name, bloodGroup, phoneNumber);
        AppUtils.updateIds(dbHelper.getExistingIds());

        if(id > -1){
            showDialog("Success", "You have been successfully added to the group");
        }
        else {
            showDialog("Error", "Error occured. Please try  again.");
        }
    }

    private  void insertToRoomDb(String empId, String name , String bloodGroup, String phoneNumber){
        DataEntry dataEntry = new DataEntry(Long.valueOf(empId), name, empId, bloodGroup, phoneNumber);
        long id = 0;
        try {
            id = db.dataEntryDao().insertEntry(dataEntry);
        } catch (SQLiteConstraintException e) {
            showDialog("Error", "Id already exists. Modify details is work in progress");
            return;
        }

        if(id > -1){
            showDialog("Success", "You have been successfully added to the group");
        }
        else {
            showDialog("Error", "Error occured. Please try  again.");
        }
    }


    private  void showDialog(String title, String message){
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
