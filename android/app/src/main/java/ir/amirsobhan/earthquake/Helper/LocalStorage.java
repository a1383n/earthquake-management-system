package ir.amirsobhan.earthquake.Helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class LocalStorage {
    private static LocalStorage localStorage;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public static final String IS_FIRST_TIME_LAUNCH = "first_time_launch";

    public static LocalStorage getInstance(Context context){
       return (localStorage == null) ? new LocalStorage(context) : localStorage;
    }

    public LocalStorage(Context context) {
        this.context = context;

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();
    }

    public boolean getBoolean(String key){
        return sharedPreferences.getBoolean(key,false);
    }

    public void setBoolean(String key,boolean b){
        editor.putBoolean(key,b).apply();
    }
}
