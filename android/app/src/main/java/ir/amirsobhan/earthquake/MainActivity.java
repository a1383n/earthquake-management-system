package ir.amirsobhan.earthquake;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import ir.amirsobhan.earthquake.Adapters.ViewPagerAdapter;
import ir.amirsobhan.earthquake.Fragments.LocationFragment;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView navigationView;
    ViewPager2 viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Initialization();
    }

    public void Initialization(){
        navigationView = findViewById(R.id.bottom_navigation);
        viewPager = findViewById(R.id.main_viewpager);
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(),getLifecycle()));

        viewPager.setUserInputEnabled(false);
        viewPager.setOffscreenPageLimit(5);

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

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }
}