
package ir.amirsobhan.earthquake.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import ir.amirsobhan.earthquake.Adapters.EarthquakeAdapter;

public class Earthquake {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("region")
    @Expose
    private Region region;
    @SerializedName("province")
    @Expose
    private Province province;
    @SerializedName("lat")
    @Expose
    private Double lat;
    @SerializedName("long")
    @Expose
    private Double _long;
    @SerializedName("mag")
    @Expose
    private Double mag;
    @SerializedName("dep")
    @Expose
    private Integer dep;
    @SerializedName("timestamp")
    @Expose
    private long timestamp;

    private int viewType = EarthquakeAdapter.VIEW_TYPE_DETAIL;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public Province getProvince() {
        return province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLong() {
        return _long;
    }

    public void setLong(Double _long) {
        this._long = _long;
    }

    public Double getMag() {
        return mag;
    }

    public void setMag(Double mag) {
        this.mag = mag;
    }

    public Integer getDep() {
        return dep;
    }

    public void setDep(Integer dep) {
        this.dep = dep;
    }

    public long getTimestamp() {
        return Long.parseLong(timestamp + "000");
    }

    public void setTimestamp(long timestamp) {
        if (String.valueOf(timestamp).length() == 13){
            this.timestamp = (timestamp / 1000);
        }else{
            this.timestamp = (timestamp);
        }
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }
}
