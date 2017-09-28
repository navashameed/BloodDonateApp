package donate.tcs.com.myapplication;

import java.util.List;

/**
 * Created by 351863 on 27-09-2017.
 */

public class AppUtils {

    public static boolean idExists(String id){
       return BloodDonateRegisterApp.empIds.contains(id);
    }

    public static void updateIds(List<String> ids){
        BloodDonateRegisterApp.empIds= ids;
    }
}
