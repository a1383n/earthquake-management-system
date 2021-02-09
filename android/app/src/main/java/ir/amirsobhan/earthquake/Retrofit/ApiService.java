package ir.amirsobhan.earthquake.Retrofit;

import java.util.List;

import ir.amirsobhan.earthquake.Models.ApiResponse;
import ir.amirsobhan.earthquake.Models.ChartResponse;
import ir.amirsobhan.earthquake.Models.Earthquake;
import ir.amirsobhan.earthquake.Models.Province;
import ir.amirsobhan.earthquake.Models.Region;
import ir.amirsobhan.earthquake.Models.RegionList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @GET("earthquakes")
    Call<List<Earthquake>> getEarthquakesList(@Query("page") int pageNumber);

    @GET("earthquakes/nearby")
    Call<ApiResponse> getNearbyEarthquakes(@Query("lat") double lat, @Query("long") double _long);

    @GET("provinces")
    Call<List<Province>> getProvincesList();

    @GET("provinces/{id}")
    Call<RegionList> getRegionsList(@Path("id") int province_id);

    @GET("chart")
    Call<List<ChartResponse>> getChartInformation(@Query("province") int province,@Query("region") int region);

    @GET("earthquakes")
    Call<List<Earthquake>> getEarthquakesCustomList(
            @Query("timestamp") long timestamp,
            @Query("province") int provinceID,
            @Query("region") int regionID
            );
}
