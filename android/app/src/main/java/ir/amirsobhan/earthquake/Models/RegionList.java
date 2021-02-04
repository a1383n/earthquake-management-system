package ir.amirsobhan.earthquake.Models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegionList {

    @SerializedName("province")
    @Expose
    private Province province;
    @SerializedName("regions")
    @Expose
    private List<Region> regions = null;

    public Province getProvince() {
        return province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    public List<Region> getRegions() {
        return regions;
    }

    public void setRegions(List<Region> regions) {
        this.regions = regions;
    }

}