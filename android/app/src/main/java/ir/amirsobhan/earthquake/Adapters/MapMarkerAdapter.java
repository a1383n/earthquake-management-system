package ir.amirsobhan.earthquake.Adapters;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import ir.amirsobhan.earthquake.Helper.Converter;
import ir.amirsobhan.earthquake.Models.EarthquakeMapMarker;

public class MapMarkerAdapter extends DefaultClusterRenderer<EarthquakeMapMarker> {

    public MapMarkerAdapter(Context context, GoogleMap map, ClusterManager<EarthquakeMapMarker> clusterManager) {
        super(context, map, clusterManager);
    }

    @Override
    protected void onBeforeClusterItemRendered(@NonNull EarthquakeMapMarker item, @NonNull MarkerOptions markerOptions) {
        super.onBeforeClusterItemRendered(item, markerOptions);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(Converter.magToBitmap(item.getMag())));
    }

}
