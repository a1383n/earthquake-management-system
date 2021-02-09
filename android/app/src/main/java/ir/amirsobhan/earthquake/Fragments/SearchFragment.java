
package ir.amirsobhan.earthquake.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.sardari.daterangepicker.customviews.DateRangeCalendarView;
import com.sardari.daterangepicker.dialog.DatePickerDialog;
import com.sardari.daterangepicker.utils.PersianCalendar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ir.amirsobhan.earthquake.Adapters.EarthquakeAdapter;
import ir.amirsobhan.earthquake.Helper.Converter;
import ir.amirsobhan.earthquake.Helper.EndlessRecyclerViewScrollListener;
import ir.amirsobhan.earthquake.Models.Earthquake;
import ir.amirsobhan.earthquake.Models.Province;
import ir.amirsobhan.earthquake.Models.Region;
import ir.amirsobhan.earthquake.Models.RegionList;
import ir.amirsobhan.earthquake.R;
import ir.amirsobhan.earthquake.Retrofit.ApiService;
import ir.amirsobhan.earthquake.Retrofit.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {
    private static final String TAG = "SearchFragment";
    ApiService apiService = RetrofitClient.getApiService();
    MaterialSpinner provinceSpinner,regionSpinner,dateSpinner;
    List<String> provinceList = new ArrayList<>();
    List<String> regionList = new ArrayList<>();
    long timestamp = Converter.toShortTimestamp(new PersianCalendar().getTimeInMillis());
    int provinceID,regionID = 0;
    RecyclerView recyclerView;
    EarthquakeAdapter adapter;
    List<Earthquake> earthquakeList = new ArrayList<>();
    TextView textView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search,container,false);

        Initialization(view);

        setupSpinners();

        return view;
    }

    private void Initialization(View view){
        provinceSpinner = view.findViewById(R.id.province_spinner);
        regionSpinner = view.findViewById(R.id.region_spinner);
        dateSpinner = view.findViewById(R.id.date_spinner);
        recyclerView = view.findViewById(R.id.recyclerview_search);
        textView = view.findViewById(R.id.search_info);

        provinceSpinner.setHint("شهر");
        regionSpinner.setHint("منطقه");
        dateSpinner.setHint("زمان");

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new EarthquakeAdapter(getContext(),earthquakeList);
    }

    private void setupSpinners(){
        provinceSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (provinceSpinner.getItems() == null){
                    getProvinceList();
                }
            }
        });

        provinceSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                getRegionList(position + 1);
                provinceID = position + 1;
                updateList();
            }
        });
        regionSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                String s = (String) item;
                regionID = Integer.parseInt(s.split("-",2)[0]);
                updateList();
            }
        });

        dateSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PersianCalendar persianCalendar = new PersianCalendar();
                persianCalendar.setTimeInMillis(Converter.toTimestampLong((int) timestamp));
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext());
                datePickerDialog.setSelectionMode(DateRangeCalendarView.SelectionMode.Single);
                datePickerDialog.setDisableDaysAgo(false);
                datePickerDialog.setMaxDate(new PersianCalendar());
                datePickerDialog.setCurrentDate(persianCalendar);
                datePickerDialog.setAcceptButtonColor(getContext().getResources().getColor(R.color.primary));
                datePickerDialog.setHeaderBackgroundColor(getContext().getResources().getColor(R.color.primary));
                datePickerDialog.setSelectedDateColor(getContext().getResources().getColor(R.color.accent));
                datePickerDialog.setOnSingleDateSelectedListener(new DatePickerDialog.OnSingleDateSelectedListener() {
                    @Override
                    public void onSingleDateSelected(PersianCalendar date) {
                        timestamp = Converter.toShortTimestamp(date.getTimeInMillis());
                        updateList();
                    }
                });
                datePickerDialog.showDialog();
            }
        });
    }

    private void getProvinceList(){
        apiService.getProvincesList().enqueue(new Callback<List<Province>>() {
            @Override
            public void onResponse(Call<List<Province>> call, Response<List<Province>> response) {
                for (Province province : response.body()) {
                    provinceList.add(province.getFaTitle());
                }
                provinceSpinner.setItems(provinceList);
                getRegionList(1);
            }

            @Override
            public void onFailure(Call<List<Province>> call, Throwable t) {

            }
        });
    }

    private void getRegionList(int province_id){
        apiService.getRegionsList(province_id).enqueue(new Callback<RegionList>() {
            @Override
            public void onResponse(Call<RegionList> call, Response<RegionList> response) {
                regionList.clear();
                for (Region region : response.body().getRegions()) {
                    regionList.add(region.getId()+"-"+region.getFaTitle());
                }
                regionSpinner.setItems(regionList);
            }

            @Override
            public void onFailure(Call<RegionList> call, Throwable t) {
                Log.d("Chart", t.getMessage());
            }
        });
    }

    private void updateList(){
        PersianCalendar persianCalendar = new PersianCalendar();
        persianCalendar.setTimeInMillis(Converter.toTimestampLong((int) timestamp));
        textView.setText(" "+persianCalendar.getPersianLongDate());
        textView.setText(Converter.toFaNum(textView.getText().toString()));
        apiService.getEarthquakesCustomList(timestamp,provinceID,regionID).enqueue(new Callback<List<Earthquake>>() {
            @Override
            public void onResponse(Call<List<Earthquake>> call, Response<List<Earthquake>> response) {
                adapter = new EarthquakeAdapter(getContext(),response.body());
                recyclerView.setAdapter(adapter);
                Log.i(TAG, "onResponse: "+response.body().size());
                Log.i(TAG, "onResponse: "+call.request().url());
            }

            @Override
            public void onFailure(Call<List<Earthquake>> call, Throwable t) {

            }
        });
    }
}
