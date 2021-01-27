package ir.amirsobhan.earthquake.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import ir.amirsobhan.earthquake.Helper.Converter;
import ir.amirsobhan.earthquake.R;

public class MapMarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private Context context;
    private View window;

    public MapMarkerInfoWindowAdapter(Context context) {
        this.context = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        View view = LayoutInflater.from(context).inflate(R.layout.marker_info_layout,null);

        TextView mag = view.findViewById(R.id.map_row_mag);
        TextView reg = view.findViewById(R.id.map_info_reg1);

        String title =  marker.getTitle();
        if (!title.equals("")){
            reg.setText(title);
        }
        String snippet =  marker.getSnippet();
        if (!title.equals("")){
            mag.setText(Converter.toFaNum(snippet));
        }

        ConstraintLayout layout = view.findViewById(R.id.map_info_layout);
        layout.setBackgroundColor(Converter.magToColor(Double.parseDouble(marker.getSnippet())));

        window = view;

        return window;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
