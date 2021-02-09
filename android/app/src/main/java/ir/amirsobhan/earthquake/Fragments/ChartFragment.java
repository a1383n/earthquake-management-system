package ir.amirsobhan.earthquake.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.List;

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
    private AnyChartView anyChartView;
    private List<DataEntry> entries = new ArrayList<>();
    private ApiService apiService;
    private List<String> provinceList = new ArrayList<>();
    private List<String> regionList = new ArrayList<>();
    private MaterialSpinner provinceSpinner;
    private MaterialSpinner regionSpinner;
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

        provinceSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                getRegionsList(position + 1);
                getChartInformation(0,position+1);
            }
        });

        regionSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                getChartInformation(0, position + 1);
            }
        });

        cartesian = AnyChart.column();
        anyChartView.setChart(cartesian);
        cartesian.title("زلزله های 3 سال اخیر بر اساس بزرگترین زلزله");

    }

    private void getProvincesList() {
        apiService.getProvincesList().enqueue(new Callback<List<Province>>() {
            @Override
            public void onResponse(Call<List<Province>> call, Response<List<Province>> response) {
                for (Province province : response.body()) {
                    provinceList.add(province.getFaTitle());
                }
                provinceSpinner.setItems(provinceList);

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
                regionList.clear();
                for (Region region : response.body().getRegions()) {
                    regionList.add(region.getFaTitle());
                }
                regionSpinner.setItems(regionList);
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

            }

            @Override
            public void onFailure(Call<List<ChartResponse>> call, Throwable t) {

            }
        });
    }
}
