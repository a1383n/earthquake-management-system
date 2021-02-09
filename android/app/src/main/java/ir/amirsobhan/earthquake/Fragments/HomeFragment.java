package ir.amirsobhan.earthquake.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import ir.amirsobhan.earthquake.Adapters.EarthquakeAdapter;
import ir.amirsobhan.earthquake.Helper.Converter;
import ir.amirsobhan.earthquake.Helper.EndlessRecyclerViewScrollListener;
import ir.amirsobhan.earthquake.Helper.Utils.PersianCalendar;
import ir.amirsobhan.earthquake.Models.Earthquake;
import ir.amirsobhan.earthquake.R;
import ir.amirsobhan.earthquake.Retrofit.ApiService;
import ir.amirsobhan.earthquake.Retrofit.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private ApiService apiService;
    private List<Earthquake> earthquakeList;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private EarthquakeAdapter adapter;
    private ProgressBar progressBar;
    private TextView rowHead;
    private SwipeRefreshLayout refreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        Initialization(view);

        //Set EndLess mode
        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                getEarthquakesList(page,earthquakeList.size());
                Log.d("EndLess",page+"");
            }
        });

        return view;
    }
    private void getEarthquakesList(int page,int lat_index){
        apiService.getEarthquakesList(page).enqueue(new Callback<List<Earthquake>>() {
            @Override
            public void onResponse(Call<List<Earthquake>> call, Response<List<Earthquake>> response) {
                        adapter.addViewType(response.body());
                earthquakeList.addAll(lat_index,response.body());
                adapter.notifyItemRangeChanged(adapter.getItemCount(),earthquakeList.size() - 1);
                progressBar.setVisibility(View.GONE);
                rowHead.setVisibility(View.VISIBLE);
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<Earthquake>> call, Throwable t) {
                Log.d("JSON", t.toString());
            }
        });
    }

    private void setRowHead(Earthquake earthquake) {
        PersianCalendar calendar = new PersianCalendar();
        calendar.setTimeInMillis(earthquake.getTimestamp());
        rowHead.setText(Converter.toFaNum(calendar.getPersianLongDate()));
    }

    private void Initialization(View view) {
        apiService = RetrofitClient.getApiService();
        recyclerView = view.findViewById(R.id.recyclerView_home);
        progressBar = view.findViewById(R.id.progressBar_home);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        rowHead = view.findViewById(R.id.eq_row_head_date);
        refreshLayout = view.findViewById(R.id.home_refresh);

        earthquakeList = new ArrayList<>();
        adapter = new EarthquakeAdapter(getContext(),earthquakeList);

        recyclerView.setAdapter(adapter);

        getEarthquakesList(0,0);

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                View first = recyclerView.getChildAt(0);
                if (recyclerView.getChildViewHolder(first) instanceof EarthquakeAdapter.StickyDateViewHolder) {
                    rowHead.setText(((EarthquakeAdapter.StickyDateViewHolder) recyclerView.getChildViewHolder(first)).head_date.getText());
                } else {
                    rowHead.setText(Converter.toFaNum(((EarthquakeAdapter.ViewHolder) recyclerView.getChildViewHolder(first)).head_date));
                }
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                linearLayoutManager = new LinearLayoutManager(getContext());
                earthquakeList = new ArrayList<>();
                adapter = new EarthquakeAdapter(getContext(),earthquakeList);
                recyclerView.setAdapter(adapter);
                getEarthquakesList(0,0);
            }
        });
    }
}
