package donate.tcs.com.myapplication.bean;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import nl.qbusict.cupboard.annotation.Index;

/**
 * Created by 351863 on 27-09-2017.
 */

@Entity
public class DataEntry {

    @PrimaryKey
    @NonNull
    public Long _id; // Since cupboard doesnt provide primary key constraints , add employee id for this field.
    public String name;
    public String employeeId;
    public String bloodGroup;
    public String phoneNumber;


    public DataEntry() {
    }

    @Ignore
    public DataEntry(Long id, String name, String employeeId, String bloodGroup, String phoneNumber) {
        this._id = id;
        this.name = name;
        this.employeeId = employeeId;
        this.bloodGroup = bloodGroup;
        this.phoneNumber = phoneNumber;
    }
}
