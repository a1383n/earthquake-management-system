package ir.amirsobhan.earthquake;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.google.android.material.appbar.MaterialToolbar;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import ir.amirsobhan.earthquake.Helper.LocalizationManager;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocalizationManager.getInstance(this).setLocale(LocalizationManager.APP_SETTING);
        setContentView(R.layout.settings_activity);
        MaterialToolbar toolbar = findViewById(R.id.settings_toolbar);
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

            if (preference instanceof ListPreference && !key.equals("lang")){
                ListPreference listPreference = (ListPreference) preference;
                preference.setSummary(listPreference.getEntry());
            }else if (key.equals("lang")){
                getActivity().recreate();
            }
        }
    }
}