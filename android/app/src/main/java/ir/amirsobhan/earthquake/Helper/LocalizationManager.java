package ir.amirsobhan.earthquake.Helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import androidx.preference.PreferenceManager;

import java.util.Locale;

public class LocalizationManager {
    public static final String PERSIAN = "fa";
    public static final String ENGLISH = "en";
    public static final String DEVICE = "device";
    public static final String APP_SETTING = "setting";

    Context context;
    Resources resources;
    Configuration configuration;
    DisplayMetrics displayMetrics;
    private SharedPreferences preferences;

    public static LocalizationManager getInstance(Context context) {
        return new LocalizationManager(context);
    }

    public LocalizationManager(Context context) {
        this.context = context;
        resources = context.getResources();
        configuration = resources.getConfiguration();
        displayMetrics = resources.getDisplayMetrics();

        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setLocale(String tag){
        if (tag.equals(APP_SETTING)){
            String lang = preferences.getString("lang","en");
            configuration.setLocale(new Locale(lang));
            resources.updateConfiguration(configuration,displayMetrics);
        }else if (tag.equals(DEVICE)){
            // Do nothing!
        }else{
            configuration.setLocale(new Locale(tag));
            resources.updateConfiguration(configuration,displayMetrics);
        }
    }

    public boolean is(String tag){
        return configuration.getLocales().toLanguageTags().equals(tag);
    }
}
