package ir.amirsobhan.earthquake;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import ir.amirsobhan.earthquake.Adapters.MapMarkerInfoWindowAdapter;
import ir.amirsobhan.earthquake.Helper.Converter;
import ir.amirsobhan.earthquake.Helper.LocalizationManager;
import ir.amirsobhan.earthquake.Helper.Utils.PersianCalendar;
import ir.amirsobhan.earthquake.Models.Earthquake;
import ir.amirsobhan.earthquake.UI.ThemeManager;

public class DetailsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG = "DetailsActivity";
    private Earthquake earthquake;
    MaterialToolbar toolbar;
    MapView mapView;
    TextView mag,province,dep,region,date;
    PersianCalendar persianCalendar;
    boolean isDarkMode,isEnglish = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        LocalizationManager.getInstance(this).setLocale(LocalizationManager.APP_SETTING);

        isDarkMode = ThemeManager.getInstance(this).getCurrentTheme() == ThemeManager.DARK;
        isEnglish = LocalizationManager.getInstance(this).is(LocalizationManager.ENGLISH);

        Initialization(savedInstanceState);


        // Add delay for smooth animation
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mag.setText((isEnglish) ? earthquake.getMag().toString() : Converter.toFaNum(earthquake.getMag().toString()));
            }
        },500);
    }


    private void Initialization(Bundle bundle) {
        earthquake = new Gson().fromJson(getIntent().getStringExtra("json"), Earthquake.class);

        if (TextUtils.isEmpty(earthquake.getRegion().getEnTitle()))
            earthquake.getRegion().setEnTitle(earthquake.getRegion().getFaTitle());

        toolbar = findViewById(R.id.materialToolbar);
        mapView = findViewById(R.id.mapView);
        mag = findViewById(R.id.details_mag);
        province = findViewById(R.id.details_province);
        region = findViewById(R.id.details_region);
        dep = findViewById(R.id.details_dep);
        date = findViewById(R.id.details_date);

        mapView.onCreate(bundle);
        mapView.getMapAsync(this);

        //Set property
        mag.setBackgroundColor(Converter.magToColor(earthquake.getMag()));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Set values
        if (!isEnglish) {
            province.setText(earthquake.getProvince().getFaTitle());
            region.setText(earthquake.getRegion().getFaTitle());
            dep.setText("در عمق ");
            dep.append(Converter.toFaNum(earthquake.getDep().toString()));
            dep.append(" کیلومتری");

            persianCalendar = new PersianCalendar();
            persianCalendar.setTimeInMillis(earthquake.getTimestamp());

            date.setText(Converter.toFaNum(persianCalendar.getPersianLongDateAndTime()));

            toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_forward_24);
        }else{
            province.setText(earthquake.getProvince().getEnTitle());
            region.setText(earthquake.getRegion().getEnTitle());

            dep.setText("In depth ");
            dep.append(earthquake.getDep().toString());
            dep.append(" Kilometer");

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(earthquake.getTimestamp());


            SimpleDateFormat format = new SimpleDateFormat("EEEE, MMMM d, yyyy 'at' h:mm a");

            date.setText(format.format(calendar.getTime()));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (isDarkMode)
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this,R.raw.map_dark));

        googleMap.setInfoWindowAdapter(new MapMarkerInfoWindowAdapter(getApplicationContext()));
        MarkerOptions options = new MarkerOptions()
                .position(new LatLng(earthquake.getLat(),earthquake.getLong()))
                .icon(BitmapDescriptorFactory.defaultMarker(Converter.magToBitmap(earthquake.getMag())))
                .snippet(earthquake.getMag().toString());

        if (!isEnglish){
            options.title(earthquake.getProvince().getFaTitle()+"،"+earthquake.getRegion().getFaTitle());
        }else{
            options.title(earthquake.getProvince().getEnTitle()+"،"+earthquake.getRegion().getEnTitle());
        }
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(earthquake.getLat(),earthquake.getLong()),9));
        Marker marker = googleMap.addMarker(options);
        marker.showInfoWindow();

    }

    @Override
    protected void attachBaseContext(Context newBase) {
            super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //Empty mag text for smooth animation
        mag.setText("");
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}