
package ir.amirsobhan.earthquake.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Date {

    @SerializedName("date")
    @Expose
    private Date_ date;
    @SerializedName("time")
    @Expose
    private Time time;

    public Date_ getDate() {
        return date;
    }

    public void setDate(Date_ date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

}
