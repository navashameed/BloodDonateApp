package donate.tcs.com.myapplication;

import android.app.Application;
import android.os.Handler;

import java.util.List;

/**
 * Created by 351863 on 27-09-2017.
 */

public class BloodDonateRegisterApp extends Application {

    public static List<String> empIds;

    @Override
    public void onCreate() {
        super.onCreate();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                empIds = DatabaseHelper.getInstance(getApplicationContext()).getExistingIds();
            }
        },1000);

    }
}
