package ir.amirsobhan.earthquake.Fragments;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.charts.TagCloud;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.List;

import ir.amirsobhan.earthquake.Adapters.ProvinceSpinnerAdapter;
import ir.amirsobhan.earthquake.Adapters.RegionSpinnerAdapter;
import ir.amirsobhan.earthquake.Helper.Converter;
import ir.amirsobhan.earthquake.Models.ChartResponse;
import ir.amirsobhan.earthquake.Models.Province;
import ir.amirsobhan.earthquake.Models.Region;
import ir.amirsobhan.earthquake.Models.RegionList;
import ir.amirsobhan.earthquake.R;
import ir.amirsobhan.earthquake.Retrofit.ApiService;
import ir.amirsobhan.earthquake.Retrofit.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChartFragment extends Fragment {
    private static final String TAG = "ChartFragment";
    private AnyChartView anyChartView;
    private List<DataEntry> entries = new ArrayList<>();
    private ApiService apiService;
    private List<Province> provinceList;
    private List<Region> regionList;
    private Spinner provinceSpinner;
    private Spinner regionSpinner;
    private Cartesian cartesian;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chart, container, false);
        Initialization(view);

        getProvincesList();

        return view;
    }

    private void Initialization(View view) {
        apiService = RetrofitClient.getApiService();

        anyChartView = view.findViewById(R.id.chart_view);
        provinceSpinner = view.findViewById(R.id.province_spinner);
        regionSpinner = view.findViewById(R.id.region_spinner);

        provinceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getRegionsList((int) id);
                getChartInformation(0, (int) id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        regionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getChartInformation(0, (int) id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        cartesian = AnyChart.column();
        anyChartView.setChart(cartesian);
        cartesian.title("زلزله های 3 سال اخیر بر اساس بزرگترین زلزله");
        cartesian.background("#"+Integer.toHexString(getContext().getResources().getColor(R.color.backgroundColor)).substring(2));
        anyChartView.addFont("Vazir","file:///android_asset/fonts/Vazir.ttf");
    }

    private void getProvincesList() {
        apiService.getProvincesList().enqueue(new Callback<List<Province>>() {
            @Override
            public void onResponse(Call<List<Province>> call, Response<List<Province>> response) {
                provinceList = response.body();
                provinceSpinner.setAdapter(new ProvinceSpinnerAdapter(getContext(),provinceList));
                getChartInformation(1,0);
            }

            @Override
            public void onFailure(Call<List<Province>> call, Throwable t) {

            }
        });
    }

    private void getRegionsList(int region_id) {
        apiService.getRegionsList(region_id).enqueue(new Callback<RegionList>() {
            @Override
            public void onResponse(Call<RegionList> call, Response<RegionList> response) {
                regionList = response.body().getRegions();
                regionSpinner.setAdapter(new RegionSpinnerAdapter(getContext(),regionList));
            }

            @Override
            public void onFailure(Call<RegionList> call, Throwable t) {
                Log.d("Chart", t.getMessage());
            }
        });
    }

    private void getChartInformation(int province_id, int region_id) {
        apiService.getChartInformation(province_id, region_id).enqueue(new Callback<List<ChartResponse>>() {
            @Override
            public void onResponse(Call<List<ChartResponse>> call, Response<List<ChartResponse>> response) {
                entries.clear();
                for (ChartResponse chartResponse : response.body()) {
                    entries.add(new ValueDataEntry(Converter.toFaNum(chartResponse.getYear() + ""), (chartResponse.getMaxMag() == null) ? 0 : chartResponse.getMaxMag()));
                }
                cartesian.data(entries);
                anyChartView.invalidate();
                Log.i(TAG, "onResponse: "+ call.request().url());

            }

            @Override
            public void onFailure(Call<List<ChartResponse>> call, Throwable t) {

            }
        });
    }
}
