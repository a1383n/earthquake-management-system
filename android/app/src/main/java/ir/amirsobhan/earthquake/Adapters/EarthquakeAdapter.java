package ir.amirsobhan.earthquake.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ir.amirsobhan.earthquake.Models.Earthquake;
import ir.amirsobhan.earthquake.R;

public class EarthquakeAdapter extends RecyclerView.Adapter<EarthquakeAdapter.ViewHolder> {
    private Context context;
    private List<Earthquake> earthquakeList;

    public EarthquakeAdapter(Context context, List<Earthquake> earthquakeList) {
        this.context = context;
        this.earthquakeList = earthquakeList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.earthquake_recycler_row,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Earthquake earthquake = earthquakeList.get(position);
        holder.city.setText(earthquake.getReg1().getCity());
        holder.state.setText(earthquake.getReg1().getState());
        holder.mag.setText(earthquake.getMag());
        holder.date.setText(earthquake.getDate().getDate().getYear());
    }

    @Override
    public int getItemCount() {
        return earthquakeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mag,city,state,date;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mag = itemView.findViewById(R.id.eq_row_mag);
            city = itemView.findViewById(R.id.eq_row_city);
            state = itemView.findViewById(R.id.eq_row_state);
            date = itemView.findViewById(R.id.eq_row_date);
        }
    }
}
