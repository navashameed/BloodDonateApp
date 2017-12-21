package donate.tcs.com.myapplication.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import donate.tcs.com.myapplication.R;
import donate.tcs.com.myapplication.bean.MemberDetails;

public class ActionsListActivity extends BaseActivity {

    private final String[] groups = {"All", "A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-"};

    private FirebaseAuth mFirebaseAuth;

    private TextView totalDonorsTextView;
    private LinearLayout bloodDonorsGroupTotalList;

    final List<MemberDetails> memberDetailsList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bloodDonorsGroupTotalList = findViewById(R.id.blood_groups_list_container);
        totalDonorsTextView = findViewById(R.id.total_donors_text);
        mFirebaseAuth = FirebaseAuth.getInstance();


        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        int permissionCheck1 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED || permissionCheck1 != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CALL_PHONE},
                    100);

        } else {
            showBloodDonorsDetails();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 100: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //PERMISSION  GRANTED
                    showBloodDonorsDetails();
                } else {
                    finish();
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void onSeeListClicked(View v) {
        startActivity(new Intent(this, ShowGroupsActivity.class));
    }

    public void onAddEntryClicked(View v) {
        startActivity(new Intent(this, AddEntryActivity.class));
    }

    private void loadLogInView() {
        Intent intent = new Intent(this, LogInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_logout:
                mFirebaseAuth.signOut();
                loadLogInView();
                break;

            case R.id.action_share_list:

                createExcelAndShare();
                break;

            case R.id.action_import_data:
                try {
                    importFromExcel();
                    showBloodDonorsDetails();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;

        }
        return super.onOptionsItemSelected(item);
    }


    private void showBloodDonorsDetails() {
        final DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("memberslist");
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
                populateBloodDonorsView();
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                hideProgressDialog();
                Toast.makeText(ActionsListActivity.this, "Failed fetching list.", Toast.LENGTH_LONG).show();

            }
        });
    }

    private void populateBloodDonorsView() {
        totalDonorsTextView.setText(" Total Donors: " + memberDetailsList.size());

        bloodDonorsGroupTotalList.removeAllViews();

        int bloodGroupCountAPlus = 0, bloodGroupCountAMinus = 0, bloodGroupCountBPlus = 0, bloodGroupCountBMinus = 0,
                bloodGroupCountABPlus = 0, bloodGroupCountABMinus = 0, bloodGroupCountOPlus = 0, bloodGroupCountOMinus = 0;


        for (MemberDetails memberDetails : memberDetailsList) {
            switch (memberDetails.bloodGroup) {
                case "A+":
                    bloodGroupCountAPlus++;
                    break;

                case "A-":
                    bloodGroupCountAMinus++;
                    break;

                case "B+":
                    bloodGroupCountBPlus++;
                    break;

                case "B-":
                    bloodGroupCountBMinus++;
                    break;

                case "AB+":
                    bloodGroupCountABPlus++;
                    break;

                case "AB-":
                    bloodGroupCountABMinus++;
                    break;

                case "O+":
                    bloodGroupCountOPlus++;
                    break;

                case "O-":
                    bloodGroupCountOMinus++;
                    break;
            }
        }


        for (int i = 0; i < 4; i++) {
            LayoutInflater inflater = LayoutInflater.from(this);
            View inflatedVIew = inflater.inflate(R.layout.blood_group_total_item, null, false);
            TextView groupView = inflatedVIew.findViewById(R.id.group_text);
            TextView leftMinusText = inflatedVIew.findViewById(R.id.left_minus_text);
            TextView rightPlusText = inflatedVIew.findViewById(R.id.right_plus_text);

            switch (i) {
                case 0:
                    groupView.setText("A");
                    leftMinusText.setText(bloodGroupCountAMinus + "");
                    rightPlusText.setText(bloodGroupCountAPlus + "");
                    break;
                case 1:
                    groupView.setText("B");
                    leftMinusText.setText(bloodGroupCountBMinus + "");
                    rightPlusText.setText(bloodGroupCountBPlus + "");
                    break;
                case 2:
                    groupView.setText("AB");
                    leftMinusText.setText(bloodGroupCountABMinus + "");
                    rightPlusText.setText(bloodGroupCountABPlus + "");
                    break;
                case 3:
                    groupView.setText("O");
                    leftMinusText.setText(bloodGroupCountOMinus + "");
                    rightPlusText.setText(bloodGroupCountOPlus + "");
                    break;

            }

            bloodDonorsGroupTotalList.addView(inflatedVIew);
        }
    }


    private void createExcelAndShare() {
        File folder = new File(Environment.getExternalStorageDirectory()
                + "/BloodDonate");

        boolean var = false;
        if (!folder.exists())
            var = folder.mkdir();

        System.out.println("" + var);


        final String filename = folder.toString() + "/" + "blood_donors_data.csv";


        try {

            FileWriter fw = new FileWriter(filename);

            fw.append("Name");
            fw.append(',');

            fw.append("EmployeeId");
            fw.append(',');

            fw.append("BloodGroup");
            fw.append(',');

            fw.append("PhoneNumber");
            fw.append(',');

            fw.append("EmailId");
            fw.append(',');

            fw.append('\n');

            for (MemberDetails memberDetails : memberDetailsList) {
                fw.append(memberDetails.name);
                fw.append(',');

                fw.append(memberDetails.employeeId);
                fw.append(',');

                fw.append(memberDetails.bloodGroup);
                fw.append(',');

                fw.append(memberDetails.phoneNumber);
                fw.append(',');

                fw.append(memberDetails.emailId);
                fw.append(',');

                fw.append('\n');
            }
            // fw.flush();
            fw.close();

            Uri u1 = null;
            u1 = Uri.parse(filename);
//
//            Intent sendIntent = new Intent(Intent.ACTION_SEND);
//            sendIntent.setData(Uri.parse("mailto:"));
//            sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Blood donors list");
//            sendIntent.putExtra(Intent.EXTRA_STREAM, u1);
//            sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            sendIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//            sendIntent.setType("text/html");
//            startActivity(sendIntent);

            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("text/plain");
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Blood donors list");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "PFA the list");
            File root = Environment.getExternalStorageDirectory();
            String pathToMyAttachedFile = "BloodDonate" + "/blood_donors_data.csv";
            File file = new File(root, pathToMyAttachedFile);
            if (!file.exists() || !file.canRead()) {
                return;
            }
            Uri uri = Uri.fromFile(file);
            emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
            startActivity(Intent.createChooser(emailIntent, "Pick an Email provider"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void importFromExcel() throws FileNotFoundException {
        showProgressDialog();
        File root = Environment.getExternalStorageDirectory();
        String pathToMyAttachedFile = "BloodDonate" + "/blood_donors_data.csv";
        File file = new File(root, pathToMyAttachedFile);

        ArrayList<MemberDetails> newMemberDetailsList = new ArrayList<>();
        String[] data;
        int count = 0;
        if (file.exists()) {
            BufferedReader br = new BufferedReader(new FileReader(file));
            try {
                String csvLine;

                while ((csvLine = br.readLine()) != null) {
                    data = csvLine.split(",");
                    try {
                        if (count > 0) {
                            MemberDetails memberDetails = new MemberDetails();
                            memberDetails.name = data[0];
                            memberDetails.employeeId = data[1];
                            memberDetails.bloodGroup = data[2];
                            memberDetails.phoneNumber = data[3];
                            memberDetails.emailId = data[4];
                            newMemberDetailsList.add(memberDetails);
                        }
                        count++;
                    } catch (Exception e) {
                        Log.e("Problem", e.toString());
                    }
                }

                MemberDetails memberDetails = new MemberDetails();
                memberDetails.name = "Sample";
                memberDetails.employeeId = "Sample";
                memberDetails.bloodGroup = "Sample";
                memberDetails.phoneNumber = "Sample";
                memberDetails.emailId = "Sample";
                newMemberDetailsList.add(memberDetails);
            } catch (IOException ex) {
                hideProgressDialog();
                throw new RuntimeException("Error in reading CSV file: " + ex);
            }
        } else {
            Toast.makeText(getApplicationContext(), "file not exists", Toast.LENGTH_SHORT).show();
        }

        final DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("memberslist");

        showProgressDialog();
        myRef.removeValue();

        for (MemberDetails memberDetails : newMemberDetailsList) {
            myRef.child(memberDetails.phoneNumber).setValue(memberDetails);
        }

        hideProgressDialog();
    }

}
