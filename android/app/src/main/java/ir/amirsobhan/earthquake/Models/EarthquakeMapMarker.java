package ir.amirsobhan.earthquake.Models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class EarthquakeMapMarker implements ClusterItem {
    private final LatLng position;
    private final String title;
    private final String snippet;
    private final double mag;

    public EarthquakeMapMarker(LatLng position, String title, String snippet, double mag) {
        this.position = position;
        this.title = title;
        this.snippet = snippet;
        this.mag = mag;
    }

    @NonNull
    @Override
    public LatLng getPosition() {
        return position;
    }

    @Nullable
    @Override
    public String getTitle() {
        return title;
    }

    @Nullable
    @Override
    public String getSnippet() {
        return snippet;
    }

    public double getMag() {
        return mag;
    }
}
