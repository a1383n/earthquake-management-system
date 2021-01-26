package ir.amirsobhan.earthquake.Retrofit;

import java.util.List;

import ir.amirsobhan.earthquake.Models.Earthquake;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("earthquakes")
    Call<List<Earthquake>> getEarthquakesList(@Query("page") int pageNumber);
}
