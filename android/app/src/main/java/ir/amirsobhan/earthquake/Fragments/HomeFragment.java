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

import java.util.List;

import ir.amirsobhan.earthquake.Adapters.EarthquakeAdapter;
import ir.amirsobhan.earthquake.Helper.Converter;
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
    private RecyclerView recyclerView;
    private EarthquakeAdapter adapter;
    private ProgressBar progressBar;
    private TextView rowHead;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Initialization(view);


        apiService.getLast20Earthquakes().enqueue(new Callback<List<Earthquake>>() {
            @Override
            public void onResponse(Call<List<Earthquake>> call, Response<List<Earthquake>> response) {
                adapter = new EarthquakeAdapter(getContext(), response.body());
                recyclerView.setAdapter(adapter);
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);

                Log.d("List",response.body().size()+"");
            }

            @Override
            public void onFailure(Call<List<Earthquake>> call, Throwable t) {
                Log.d("JSON", t.toString());
            }
        });
        return view;
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
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        rowHead = view.findViewById(R.id.eq_row_head_date);


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
    }
}
