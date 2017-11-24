package donate.tcs.com.myapplication.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Common class for Shared preferences
 */

public class Preferences {


    private final String sharedPreferencesKey = "BloodDonateAppPref";
    private Context mContext;
    private SharedPreferences sharedPreferences;

    public Preferences(Context context) {
        mContext = context;
    }

    public void saveString(String key, String value) {
        sharedPreferences = mContext.getSharedPreferences(sharedPreferencesKey, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(key, value).apply();
    }

    public String readString(String key) {
        sharedPreferences = mContext.getSharedPreferences(sharedPreferencesKey, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, null);
    }

    public void saveBoolean(String key, boolean value) {
        sharedPreferences = mContext.getSharedPreferences(sharedPreferencesKey, Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(key, value).apply();
    }

    public boolean readBoolean(String key) {
        sharedPreferences = mContext.getSharedPreferences(sharedPreferencesKey, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, false);
    }

    public void saveLong(String key, long value) {
        sharedPreferences = mContext.getSharedPreferences(sharedPreferencesKey, Context.MODE_PRIVATE);
        sharedPreferences.edit().putLong(key, value).apply();
    }

    public long readLong(String key) {
        sharedPreferences = mContext.getSharedPreferences(sharedPreferencesKey, Context.MODE_PRIVATE);
        return sharedPreferences.getLong(key, 0);
    }

    public void saveInt(String key, int value) {
        sharedPreferences = mContext.getSharedPreferences(sharedPreferencesKey, Context.MODE_PRIVATE);
        sharedPreferences.edit().putInt(key, value).apply();
    }

    public int readInt(String key) {
        sharedPreferences = mContext.getSharedPreferences(sharedPreferencesKey, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key, 0);
    }

    public void clearAllPreferences() {
        sharedPreferences = mContext.getSharedPreferences(sharedPreferencesKey, Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();
    }

    public void saveStringSet(String key, ArrayList<String> list) {

        Set<String> tasksSet = new HashSet<String>(list);
        sharedPreferences = mContext.getSharedPreferences(sharedPreferencesKey, Context.MODE_PRIVATE);
        sharedPreferences.edit().putStringSet(key, tasksSet).apply();
    }

    public ArrayList<String> readStringSet(String key) {
        sharedPreferences = mContext.getSharedPreferences(sharedPreferencesKey, Context.MODE_PRIVATE);
        Set<String> list = sharedPreferences.getStringSet(key, new HashSet<String>());
        return new ArrayList<String>(list);
    }

    public void clear(String key) {
        sharedPreferences = mContext.getSharedPreferences(sharedPreferencesKey, Context.MODE_PRIVATE);
        sharedPreferences.edit().remove(key).apply();
    }

}



