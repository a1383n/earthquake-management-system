package ir.amirsobhan.earthquake.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

import ir.amirsobhan.earthquake.Models.Earthquake;
import ir.amirsobhan.earthquake.R;
import ir.amirsobhan.earthquake.Retrofit.ApiService;
import ir.amirsobhan.earthquake.Retrofit.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    ApiService apiService;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);

        Initialization();


        apiService.getLast20Earthquakes().enqueue(new Callback<List<Earthquake>>() {
            @Override
            public void onResponse(Call<List<Earthquake>> call, Response<List<Earthquake>> response) {
                Log.d("JSON",response.body().size()+"");
            }

            @Override
            public void onFailure(Call<List<Earthquake>> call, Throwable t) {
                Log.d("JSON",t.toString());
            }
        });

        return view;
    }

    private void Initialization(){
        apiService = RetrofitClient.getApiService();
    }
}
