package ir.amirsobhan.earthquake;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.StringRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import ir.amirsobhan.earthquake.Helper.LocalizationManager;
import ir.amirsobhan.earthquake.UI.ThemeManager;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocalizationManager.getInstance(this).setLocale(LocalizationManager.APP_SETTING);
        ThemeManager.getInstance(this).setThemeConfig();
        setContentView(R.layout.settings_activity);
        MaterialToolbar toolbar = findViewById(R.id.settings_toolbar);
        if (LocalizationManager.getInstance(this).is(LocalizationManager.PERSIAN)){
            toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_forward_24);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }


    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    public static class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
        ListPreference lang,template,mapType;
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            Initialization();
        }

        private void Initialization(){
            getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

            lang = findPreference("lang");
            template = findPreference("theme");
            mapType = findPreference("map_type");

            template.setSummary(template.getEntry());
            mapType.setSummary(mapType.getEntry());
        }


        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            Preference preference  = findPreference(key);

            if (preference instanceof ListPreference && !key.equals("lang")) {
                ListPreference listPreference = (ListPreference) preference;
                preference.setSummary(listPreference.getEntry());
            }

            switch (key){
                case "lang":
                    needRestart(getString(R.string.language_set));
                case "theme":
                    needRestart(getString(R.string.theme_set));
            }
        }

        private void needRestart(String message){
            Snackbar snackbar = Snackbar.make(getView(),message,BaseTransientBottomBar.LENGTH_INDEFINITE);
            snackbar.setAction(R.string.ok, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent mStartActivity = new Intent(getContext(), MainActivity.class);
                    int mPendingIntentId = 123456;
                    PendingIntent mPendingIntent = PendingIntent.getActivity(getContext(), mPendingIntentId, mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
                    AlarmManager mgr = (AlarmManager)getContext().getSystemService(Context.ALARM_SERVICE);
                    mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
                    System.exit(0);
                }
            });
            snackbar.show();
        }
    }
}