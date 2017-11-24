package donate.tcs.com.myapplication.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import donate.tcs.com.myapplication.bean.MemberDetails;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

/**
 * Created by 351863 on 27-09-2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "cupboardTest.db";
    private static final int DATABASE_VERSION = 1;

    private static DatabaseHelper dbHelper;

    public static DatabaseHelper getInstance(Context context){
        if(dbHelper == null){
           dbHelper = new DatabaseHelper(context);
        }
        return dbHelper;
    }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    static {
        // register our models
        cupboard().register(MemberDetails.class);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // this will ensure that all tables are created
        cupboard().withDatabase(db).createTables();
        // add indexes and other database tweaks in this method if you want
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // this will upgrade tables, adding columns and new tables.
        // Note that existing columns will not be converted
        cupboard().withDatabase(db).upgradeTables();
        // do migration work if you have an alteration to make to your schema here
    }

    public long insertEntry(String empId, String name , String bloodGroup, String phoneNumber){
        MemberDetails memberDetails = new MemberDetails(Long.valueOf(empId), name, empId, bloodGroup, phoneNumber);
        return cupboard().withDatabase(getWritableDatabase()).put(memberDetails);
    }

    public List<MemberDetails> getItemForGroup(String bloodGroup){
       return cupboard().withDatabase(getReadableDatabase()).query(MemberDetails.class).withSelection( "bloodGroup = ?", bloodGroup).list();
    }

    public List<MemberDetails> getAllItems(){
        return  cupboard().withDatabase(getReadableDatabase()).query(MemberDetails.class).list();
    }

    public int getItemCount(String bloodGroup){
        return getItemForGroup(bloodGroup).size();
    }

    public List<String> getExistingIds(){
        List<String> idsList = new ArrayList<>();
        idsList.clear();
        List<MemberDetails> memberDetailsList = cupboard().withDatabase(getReadableDatabase()).query(MemberDetails.class).list();
        for(MemberDetails entry: memberDetailsList){
           idsList.add(entry.employeeId);
        }
        return idsList;
    }

}
