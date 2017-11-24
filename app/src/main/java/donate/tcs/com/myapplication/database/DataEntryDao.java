package donate.tcs.com.myapplication.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import donate.tcs.com.myapplication.bean.MemberDetails;

/**
 * Created by 351863 on 29-09-2017.
 */

@Dao
public interface DataEntryDao {

    @Insert
    public long insertEntry(MemberDetails entry);

    @Query("SELECT * FROM MemberDetails WHERE bloodGroup = :bloodGroup")
    public MemberDetails[] getItemForGroup(String bloodGroup);

    @Query("SELECT * FROM MemberDetails")
    public MemberDetails[] getAllItems();

}
