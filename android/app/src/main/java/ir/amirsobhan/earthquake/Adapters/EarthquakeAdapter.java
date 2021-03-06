package ir.amirsobhan.earthquake.Adapters;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ir.amirsobhan.earthquake.DetailsActivity;
import ir.amirsobhan.earthquake.Helper.Converter;
import ir.amirsobhan.earthquake.Helper.LocalizationManager;
import ir.amirsobhan.earthquake.Helper.Utils.PersianCalendar;
import ir.amirsobhan.earthquake.Models.Earthquake;
import ir.amirsobhan.earthquake.R;

public class EarthquakeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    private Activity activity;
    public List<Earthquake> earthquakeList;
    public static int VIEW_TYPE_DATE = 0;
    public static int VIEW_TYPE_DETAIL = 1;

    public EarthquakeAdapter(Context context, Activity activity, List<Earthquake> earthquakeList) {
        this.context = context;
        this.activity = activity;
        this.earthquakeList = earthquakeList;
        addViewType(earthquakeList);

        LocalizationManager.getInstance(context).setLocale(LocalizationManager.APP_SETTING);
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
            if (!LocalizationManager.getInstance(context).is(LocalizationManager.ENGLISH)) {
                //Set date
                PersianCalendar calendar = new PersianCalendar();
                calendar.setTimeInMillis(earthquake.getTimestamp());
                //Set values
                viewHolder.city.setText(earthquake.getRegion().getFaTitle());
                viewHolder.state.setText(earthquake.getProvince().getFaTitle());
                viewHolder.mag.setText(Converter.toFaNum(earthquake.getMag().toString()));
                viewHolder.head_date = calendar.getPersianLongDate();
                //Set Properties
                viewHolder.mag.setBackgroundColor(Converter.magToColor(earthquake.getMag()));
                viewHolder.date.setText(Converter.toFaNum(calendar.getPersianLongDateAndTime()));
            }else{
                if (TextUtils.isEmpty(earthquake.getRegion().getEnTitle()))
                    earthquake.getRegion().setEnTitle(earthquake.getRegion().getFaTitle());

                //Set date
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(earthquake.getTimestamp());
                SimpleDateFormat format = new SimpleDateFormat("EEEE, MMMM d, yyyy 'at' h:mm a");
                SimpleDateFormat format2 = new SimpleDateFormat("EEEE, MMMM d, yyyy");
                //Set values
                viewHolder.city.setText(earthquake.getRegion().getEnTitle());
                viewHolder.state.setText(earthquake.getProvince().getEnTitle());
                viewHolder.mag.setText(earthquake.getMag().toString());
                viewHolder.head_date = format2.format(calendar.getTime());
                //Set Properties
                viewHolder.mag.setBackgroundColor(Converter.magToColor(earthquake.getMag()));
                viewHolder.date.setText(format.format(calendar.getTime()));
            }
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,DetailsActivity.class);
                    intent.putExtra("json",new Gson().toJson(earthquake));
                    Pair[] pairs = new Pair[1];
                    pairs[0] = new Pair<View,String>(((ViewHolder) holder).mag,"magTransition");
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(activity,pairs);
                    context.startActivity(intent,options.toBundle());
                }
            });
        }else{
            //Set date
            PersianCalendar calendar = new PersianCalendar();
            calendar.setTimeInMillis(earthquake.getTimestamp());

            ((StickyDateViewHolder) holder).head_date.setText(Converter.toFaNum(calendar.getPersianLongDate()));
        }
    }

    @Override
    public int getItemCount() {
        return earthquakeList.size();
    }

    public List<Earthquake> addViewType(List<Earthquake> earthquakeList) {
        for (int i = 1; i < earthquakeList.size(); i++) {
            int i2 = i - 1;

            //Set date
            PersianCalendar calendar_1 = new PersianCalendar();
            calendar_1.setTimeInMillis(earthquakeList.get(i).getTimestamp());
            //
            PersianCalendar calendar_2 = new PersianCalendar();
            calendar_2.setTimeInMillis(earthquakeList.get(i2).getTimestamp());

            if (earthquakeList.get(i2).getViewType() != VIEW_TYPE_DATE && calendar_2.getPersianDay() != calendar_1.getPersianDay()) {
                Earthquake earthquake = new Earthquake();
                earthquake.setViewType(VIEW_TYPE_DATE);
                earthquake.setTimestamp(earthquakeList.get(i).getTimestamp());
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
