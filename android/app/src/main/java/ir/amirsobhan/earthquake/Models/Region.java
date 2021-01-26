
package ir.amirsobhan.earthquake.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Region {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("fa_title")
    @Expose
    private String faTitle;
    @SerializedName("en_title")
    @Expose
    private String enTitle;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFaTitle() {
        return faTitle;
    }

    public void setFaTitle(String faTitle) {
        this.faTitle = faTitle;
    }

    public String getEnTitle() {
        return enTitle;
    }

    public void setEnTitle(String enTitle) {
        this.enTitle = enTitle;
    }

}
