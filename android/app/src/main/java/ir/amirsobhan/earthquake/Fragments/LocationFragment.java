package ir.amirsobhan.earthquake.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.gson.Gson;
import com.google.maps.android.SphericalUtil;
import com.google.maps.android.collections.MarkerManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Locale;

import ir.amirsobhan.earthquake.Adapters.MapMarkerInfoWindowAdapter;
import ir.amirsobhan.earthquake.Helper.Converter;
import ir.amirsobhan.earthquake.Helper.SingleShotLocationProvider;
import ir.amirsobhan.earthquake.Models.ApiResponse;
import ir.amirsobhan.earthquake.Models.Earthquake;
import ir.amirsobhan.earthquake.R;
import ir.amirsobhan.earthquake.Retrofit.ApiService;
import ir.amirsobhan.earthquake.Retrofit.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnPolylineClickListener {
    private GoogleMap googleMap;
    private MapView mapView;
    private MaterialCardView banner;
    private MaterialButton banner_pos;
    private MaterialButton banner_neg;
    private boolean locationPermissionGranted;
    private ApiService apiService;
    private Marker marker;
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location, container, false);
        Initialization(view, savedInstanceState);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("در حال محاسبه...");
        progressDialog.setCancelable(false);
        return view;
    }

    private void Initialization(View view, Bundle bundle) {
        // Gets the MapView from the XML layout and creates it
        mapView = (MapView) view.findViewById(R.id.map_location);
        mapView.onCreate(bundle);
        mapView.getMapAsync(this);

        //Gets Banner
        banner = view.findViewById(R.id.location_banner_layout);
        banner_pos = view.findViewById(R.id.location_banner_pos_btn);
        banner_neg = view.findViewById(R.id.location_banner_neg_btn);

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            banner_pos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
                }
            });
            banner_neg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    banner.setVisibility(View.GONE);
                }
            });
            locationPermissionGranted = false;

        } else {
            banner.setVisibility(View.GONE);
            locationPermissionGranted = true;
        }

        apiService = RetrofitClient.getApiService();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = (googleMap);
        googleMap.setInfoWindowAdapter(new MapMarkerInfoWindowAdapter(getContext()));
        googleMap.setOnPolylineClickListener(this);
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                progressDialog.show();
                googleMap.clear();
                apiService.getNearbyEarthquakes(Math.round(latLng.latitude * 100.0) / 100.0, Math.round(latLng.longitude * 100.0) / 100.0).enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        googleMap.addMarker(new MarkerOptions()
                        .title("محل انتخابی شما")
                        .position(latLng)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
                        drawOnMap(response,latLng);
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        progressDialog.dismiss();
                    }
                });
            }
        });
        googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                progressDialog.show();
                googleMap.clear();
                getLocation();
                return true;
            }
        });
        getLocation();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    private void getLocation() {
        if (locationPermissionGranted) {
            SingleShotLocationProvider.requestSingleUpdate(getContext(), new SingleShotLocationProvider.LocationCallback() {
                @SuppressLint("MissingPermission")
                @Override
                public void onNewLocationAvailable(SingleShotLocationProvider.GPSCoordinates location) {
                    LatLng latLng = new LatLng(location.latitude, location.longitude);
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 9));
                    apiService.getNearbyEarthquakes(Math.round(latLng.latitude * 100.0) / 100.0, Math.round(latLng.longitude * 100.0) / 100.0).enqueue(new Callback<ApiResponse>() {
                        @Override
                        public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                            progressDialog.dismiss();
                            drawOnMap(response,latLng);
                        }

                        @Override
                        public void onFailure(Call<ApiResponse> call, Throwable t) {

                        }
                    });

                    googleMap.setMyLocationEnabled(true);
                }
            });
        }
    }

    private void drawOnMap(Response<ApiResponse> response,LatLng latLng){
        Earthquake earthquake = response.body().getData();

        if (earthquake != null) {
            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(earthquake.getLat(), earthquake.getLong()))
                    .title(earthquake.getProvince().getFaTitle() + "،" + earthquake.getRegion().getFaTitle())
                    .snippet(earthquake.getMag() + "")
                    .icon(BitmapDescriptorFactory.defaultMarker(Converter.magToBitmap(earthquake.getMag()))));


            // Instantiates a new CircleOptions object and defines the center and radius
            CircleOptions circleOptions = new CircleOptions()
                    .center(latLng)
                    .strokeColor(Color.GRAY)
                    .radius(50000); // In meters

            // Get back the mutable Circle
            googleMap.addCircle(circleOptions);

            PolylineOptions polylineOptions = new PolylineOptions()
                    .add(new LatLng(latLng.latitude,latLng.longitude),new LatLng(earthquake.getLat(),earthquake.getLong()));

            LatLng center = SphericalUtil.interpolate(new LatLng(latLng.latitude,latLng.longitude),new LatLng(earthquake.getLat(),earthquake.getLong()),0.5);

            marker = googleMap.addMarker(new MarkerOptions()
                    .position(center)
                    .title("فاصله")
                    .snippet(Math.round(SphericalUtil.computeDistanceBetween(new LatLng(latLng.latitude,latLng.longitude),new LatLng(earthquake.getLat(),earthquake.getLong()))) / 1000+" KM")
                    .icon(BitmapDescriptorFactory.fromBitmap(Converter.colorToBitmap(10,10,Color.TRANSPARENT))));
            marker.setTag("polyline");

            googleMap.setInfoWindowAdapter(new MarkerManager(googleMap));
            marker.showInfoWindow();
            googleMap.addPolyline(polylineOptions);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            banner.setVisibility(View.GONE);
            getLocation();
        }
    }

    @Override
    public void onPolylineClick(Polyline polyline) {
        marker.showInfoWindow();
    }
}
