package donate.tcs.com.myapplication.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import donate.tcs.com.myapplication.bean.DataEntry;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

/**
 * Created by 351863 on 29-09-2017.
 */

@Dao
public interface DataEntryDao {

    @Insert
    public long insertEntry(DataEntry entry);

    @Query("SELECT * FROM DataEntry WHERE bloodGroup = :bloodGroup")
    public DataEntry[] getItemForGroup(String bloodGroup);

    @Query("SELECT * FROM DataEntry")
    public DataEntry[] getAllItems();

}
