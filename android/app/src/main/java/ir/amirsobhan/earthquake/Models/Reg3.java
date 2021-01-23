
package ir.amirsobhan.earthquake.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Reg3 {

    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("city")
    @Expose
    private String city;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

}
