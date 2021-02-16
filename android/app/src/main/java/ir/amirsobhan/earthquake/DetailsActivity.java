package ir.amirsobhan.earthquake;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
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

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import ir.amirsobhan.earthquake.Adapters.MapMarkerInfoWindowAdapter;
import ir.amirsobhan.earthquake.Helper.Converter;
import ir.amirsobhan.earthquake.Helper.Utils.PersianCalendar;
import ir.amirsobhan.earthquake.Models.Earthquake;

public class DetailsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG = "DetailsActivity";
    private Earthquake earthquake;
    MaterialToolbar toolbar;
    MapView mapView;
    TextView mag,province,dep,region,date;
    PersianCalendar persianCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Initialization(savedInstanceState);


        // Add delay for smooth animation
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mag.setText(Converter.toFaNum(earthquake.getMag().toString()));
            }
        },500);
    }


    private void Initialization(Bundle bundle) {
        earthquake = new Gson().fromJson(getIntent().getStringExtra("json"), Earthquake.class);
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

        //Set values
        province.setText(earthquake.getProvince().getFaTitle());
        region.setText(earthquake.getRegion().getFaTitle());
        dep.setText("در عمق ");
        dep.append(Converter.toFaNum(earthquake.getDep().toString()));
        dep.append(" کیلومتری");

        persianCalendar = new PersianCalendar();
        persianCalendar.setTimeInMillis(earthquake.getTimestamp());

        date.setText(Converter.toFaNum(persianCalendar.getPersianLongDateAndTime()));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this,R.raw.map_dark));
        googleMap.setInfoWindowAdapter(new MapMarkerInfoWindowAdapter(getApplicationContext()));
        MarkerOptions options = new MarkerOptions()
                .position(new LatLng(earthquake.getLat(),earthquake.getLong()))
                .icon(BitmapDescriptorFactory.defaultMarker(Converter.magToBitmap(earthquake.getMag())))
                .title(earthquake.getProvince().getFaTitle()+"،"+earthquake.getRegion().getFaTitle())
                .snippet(earthquake.getMag().toString());
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