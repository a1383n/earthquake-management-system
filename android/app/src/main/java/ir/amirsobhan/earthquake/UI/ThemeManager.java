package ir.amirsobhan.earthquake.UI;

import android.app.UiModeManager;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;

import ir.amirsobhan.earthquake.R;

public class ThemeManager {
    private final String KEY = "theme";
    public static final String DARK = "dark";
    public static final String LIGHT = "light";
    public static final String SYSTEM_DEFAULT = "system";

    Context context;
    SharedPreferences preferences;
    UiModeManager uiModeManager;

    public static ThemeManager getInstance(Context context) {
        return new ThemeManager(context);
    }

    public ThemeManager(Context context) {
        this.context = context;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        uiModeManager = (UiModeManager) context.getSystemService(Context.UI_MODE_SERVICE);

        if (preferences.getString(KEY,null) == null){
            preferences.edit().putString(KEY,LIGHT).apply();
        }
    }

    public String getThemeType() {
        return preferences.getString(KEY, LIGHT);
    }

    public void setThemeConfig() {
        switch (getThemeType()) {
            case LIGHT:
                if (uiModeManager.getNightMode() != UiModeManager.MODE_NIGHT_NO)
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case DARK:
                if (uiModeManager.getNightMode() != UiModeManager.MODE_NIGHT_YES)
                    uiModeManager.setNightMode(UiModeManager.MODE_NIGHT_YES);
                break;
            case SYSTEM_DEFAULT:
                if (uiModeManager.getNightMode() != UiModeManager.MODE_NIGHT_AUTO)
                    uiModeManager.setNightMode(UiModeManager.MODE_NIGHT_AUTO);
                break;
        }
    }


    public boolean isModeChanged() {
        switch (getThemeType()) {
            case LIGHT:
                return uiModeManager.getNightMode() != UiModeManager.MODE_NIGHT_NO;
            case DARK:
                return uiModeManager.getNightMode() != UiModeManager.MODE_NIGHT_YES;
            case SYSTEM_DEFAULT:
                return uiModeManager.getNightMode() != UiModeManager.MODE_NIGHT_AUTO;
        }
        return false;
    }

}
