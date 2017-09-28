package donate.tcs.com.myapplication.bean;

import nl.qbusict.cupboard.annotation.Index;

/**
 * Created by 351863 on 27-09-2017.
 */

public class DataEntry {

    @Index
    public Long _id; // Since cupboard doesnt provide primary key constraints , add employee id for this field.
    public String name;
    public String employeeId;
    public String bloodGroup;
    public String phoneNumber;

    public DataEntry() {
    }

    public DataEntry(Long id, String name, String employeeId, String bloodGroup, String phoneNumber) {
        this._id = id;
        this.name = name;
        this.employeeId = employeeId;
        this.bloodGroup = bloodGroup;
        this.phoneNumber = phoneNumber;
    }
}
