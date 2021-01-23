
package ir.amirsobhan.earthquake.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Time {

    @SerializedName("hour")
    @Expose
    private String hour;
    @SerializedName("second")
    @Expose
    private String second;
    @SerializedName("millisecond")
    @Expose
    private String millisecond;

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getSecond() {
        return second;
    }

    public void setSecond(String second) {
        this.second = second;
    }

    public String getMillisecond() {
        return millisecond;
    }

    public void setMillisecond(String millisecond) {
        this.millisecond = millisecond;
    }

}
