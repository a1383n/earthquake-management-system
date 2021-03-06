package ir.amirsobhan.earthquake;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.FirebaseMessaging;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import ir.amirsobhan.earthquake.Adapters.ViewPagerAdapter;
import ir.amirsobhan.earthquake.Helper.LocalizationManager;
import ir.amirsobhan.earthquake.UI.ThemeManager;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView navigationView;
    ViewPager2 viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocalizationManager.getInstance(this).setLocale(LocalizationManager.APP_SETTING);
        ThemeManager.getInstance(this).setThemeConfig();
        setContentView(R.layout.activity_main);

        Initialization();


        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_home:
                        viewPager.setCurrentItem(0);
                        return true;
                    case R.id.navigation_map:
                        viewPager.setCurrentItem(1);
                        return true;
                    case R.id.navigation_location:
                        viewPager.setCurrentItem(2);
                        return true;
                    case R.id.navigation_chart:
                        viewPager.setCurrentItem(3);
                        return true;
                    case R.id.navigation_search:
                        viewPager.setCurrentItem(4);
                        return true;
                }
                return false;
            }
        });

    }

    public void Initialization(){
        navigationView = findViewById(R.id.bottom_navigation);
        viewPager = findViewById(R.id.main_viewpager);
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(),getLifecycle()));

        viewPager.setUserInputEnabled(false);
        viewPager.setOffscreenPageLimit(5);

        findViewById(R.id.settingButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,SettingsActivity.class));
            }
        });

        // Obtain the FirebaseAnalytics instance.
        FirebaseAnalytics.getInstance(this);

        // Obtain the FirebaseMessaging instance.
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }


}