package ir.amirsobhan.earthquake.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;

import java.util.List;

import ir.amirsobhan.earthquake.Adapters.MapMarkerAdapter;
import ir.amirsobhan.earthquake.Adapters.MapMarkerInfoWindowAdapter;
import ir.amirsobhan.earthquake.Models.Earthquake;
import ir.amirsobhan.earthquake.Models.EarthquakeMapMarker;
import ir.amirsobhan.earthquake.R;
import ir.amirsobhan.earthquake.Retrofit.ApiService;
import ir.amirsobhan.earthquake.Retrofit.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap googleMap;
    private MapView mapView;
    private List<Earthquake> earthquakeList;
    private ApiService apiService;
    private ClusterManager<EarthquakeMapMarker> clusterManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        Initialization(view, savedInstanceState);

        return view;
    }

    private void Initialization(View view, Bundle savedInstanceState) {
        // Gets the MapView from the XML layout and creates it
        mapView = (MapView) view.findViewById(R.id.google_map);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        apiService = RetrofitClient.getApiService();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = (googleMap);

        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        setUpClusterer();
        getEarthquakesList();
    }

    private void getEarthquakesList() {
        apiService.getEarthquakesList(1).enqueue(new Callback<List<Earthquake>>() {
            @Override
            public void onResponse(Call<List<Earthquake>> call, Response<List<Earthquake>> response) {
                if (response.isSuccessful()) {
                    for (Earthquake earthquake : response.body()) {
                        clusterManager.addItem(new EarthquakeMapMarker(
                                new LatLng(earthquake.getLat(),earthquake.getLong()),
                                earthquake.getProvince().getFaTitle()+"،"+earthquake.getRegion().getFaTitle(),
                                earthquake.getMag()+"",
                                earthquake.getMag()));
                    }
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(35.683333,51.416667),4.5F));
                }
            }

            @Override
            public void onFailure(Call<List<Earthquake>> call, Throwable t) {

            }
        });
    }

    private void setUpClusterer() {
        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        clusterManager = new ClusterManager<EarthquakeMapMarker>(getContext(), googleMap);
        clusterManager.setRenderer(new MapMarkerAdapter(getContext(),googleMap,clusterManager));

        clusterManager.getMarkerCollection().setInfoWindowAdapter(new MapMarkerInfoWindowAdapter(getContext()));

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        googleMap.setOnCameraIdleListener(clusterManager);
        googleMap.setOnMarkerClickListener(clusterManager);
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

}
