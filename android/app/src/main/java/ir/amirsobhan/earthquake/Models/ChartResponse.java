package ir.amirsobhan.earthquake.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChartResponse {

    @SerializedName("year")
    @Expose
    private Integer year;
    @SerializedName("max_mag")
    @Expose
    private Double maxMag;

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Double getMaxMag() {
        return maxMag;
    }

    public void setMaxMag(Double maxMag) {
        this.maxMag = maxMag;
    }

}