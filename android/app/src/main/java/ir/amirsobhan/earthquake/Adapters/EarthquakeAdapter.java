package ir.amirsobhan.earthquake.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.zakariya.stickyheaders.SectioningAdapter;

import java.util.List;

import ir.amirsobhan.earthquake.Helper.Converter;
import ir.amirsobhan.earthquake.Helper.Utils.PersianCalendar;
import ir.amirsobhan.earthquake.Models.Earthquake;
import ir.amirsobhan.earthquake.R;

public class EarthquakeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    public List<Earthquake> earthquakeList;
    public static int VIEW_TYPE_DATE = 0;
    public static int VIEW_TYPE_DETAIL = 1;

    public EarthquakeAdapter(Context context, List<Earthquake> earthquakeList) {
        this.context = context;
        addViewType(earthquakeList);
        this.earthquakeList = earthquakeList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       if (viewType == VIEW_TYPE_DETAIL){
           return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.earthquake_recycler_row, parent, false));
       }else{
           return new StickyDateViewHolder(LayoutInflater.from(context).inflate(R.layout.earthquake_recycler_date_row,parent,false));
       }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    public int getItemViewType(int position) {
        return this.earthquakeList.get(position).getViewType();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
        //Get item from list
        Earthquake earthquake = earthquakeList.get(position);

        if (earthquake.getViewType() == VIEW_TYPE_DETAIL) {
            ViewHolder viewHolder = (ViewHolder) holder;
            //Set date
            PersianCalendar calendar = new PersianCalendar();
            calendar.setPersianDate(
                    Integer.valueOf(earthquake.getDate().getDate().getYear()),
                    Integer.valueOf(earthquake.getDate().getDate().getMonth()),
                    Integer.valueOf(earthquake.getDate().getDate().getDay())
            );


            //Set values
            viewHolder.city.setText(earthquake.getReg1().getCity());
            viewHolder.state.setText(earthquake.getReg1().getState());
            viewHolder.mag.setText(Converter.toFaNum(earthquake.getMag()));

            viewHolder.head_date = calendar.getPersianLongDate();
            //Set Properties
            viewHolder.mag.setBackgroundColor(Converter.magToColor(Double.valueOf(earthquake.getMag())));

            calendar.set(PersianCalendar.HOUR_OF_DAY, Integer.valueOf(earthquake.getDate().getTime().getHour()));
            calendar.set(PersianCalendar.MINUTE, Integer.valueOf(earthquake.getDate().getTime().getMin()));
            calendar.set(PersianCalendar.SECOND, (int) Math.round(Double.valueOf(earthquake.getDate().getTime().getSec())));


            viewHolder.date.setText(Converter.toFaNum(calendar.getPersianLongDateAndTime()));
        }else{
            //Set date
            PersianCalendar calendar = new PersianCalendar();
            calendar.setPersianDate(
                    Integer.valueOf(earthquake.getDate().getDate().getYear()),
                    Integer.valueOf(earthquake.getDate().getDate().getMonth()),
                    Integer.valueOf(earthquake.getDate().getDate().getDay())
            );

            ((StickyDateViewHolder) holder).head_date.setText(Converter.toFaNum(calendar.getPersianLongDate()));
        }
    }

    @Override
    public int getItemCount() {
        return earthquakeList.size();
    }

    private List<Earthquake> addViewType(List<Earthquake> earthquakeList) {
//
//        Earthquake eq = new Earthquake();
//        eq.setViewType(VIEW_TYPE_DATE);
//        eq.setDate(earthquakeList.get(1).getDate());
//        earthquakeList.add(0, eq);

        for (int i = 1; i < earthquakeList.size(); i++) {
            int i2 = i - 1;
            if (earthquakeList.get(i2).getViewType() != VIEW_TYPE_DATE && !earthquakeList.get(i2).getDate().getDate().getDay().equals(earthquakeList.get(i).getDate().getDate().getDay())) {
                Earthquake earthquake = new Earthquake();
                earthquake.setViewType(VIEW_TYPE_DATE);
                earthquake.setDate(earthquakeList.get(i).getDate());
                earthquakeList.add(i, earthquake);
            }
        }
        return earthquakeList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mag, city, state, date;
        public String head_date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mag = itemView.findViewById(R.id.eq_row_mag);
            city = itemView.findViewById(R.id.eq_row_city);
            state = itemView.findViewById(R.id.eq_row_state);
            date = itemView.findViewById(R.id.eq_row_date);
        }
    }
    public class StickyDateViewHolder extends RecyclerView.ViewHolder {
        public TextView head_date;

        public StickyDateViewHolder(@NonNull View itemView) {
            super(itemView);
            head_date = itemView.findViewById(R.id.eq_row_head_date);
        }
    }
}
