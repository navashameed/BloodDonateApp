package donate.tcs.com.myapplication.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import donate.tcs.com.myapplication.bean.MemberDetails;

/**
 * Created by 351863 on 29-09-2017.
 */

@Database(entities = {MemberDetails.class}, version = 1, exportSchema = false)
public abstract class DataBaseRoomHelper extends RoomDatabase {
    private static DataBaseRoomHelper dataBaseRoomHelper;

    public static DataBaseRoomHelper getInstance(Context context) {
        if (dataBaseRoomHelper == null) {
            dataBaseRoomHelper = Room.databaseBuilder(context,
                    DataBaseRoomHelper.class, "blood_donor_list").allowMainThreadQueries().build();
        }
        return dataBaseRoomHelper;
    }

    public abstract DataEntryDao dataEntryDao();
}
