
package ir.amirsobhan.earthquake.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Earthquake {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("reg1")
    @Expose
    private Reg1 reg1;
    @SerializedName("dis1")
    @Expose
    private String dis1;
    @SerializedName("reg2")
    @Expose
    private Reg2 reg2;
    @SerializedName("dis2")
    @Expose
    private String dis2;
    @SerializedName("reg3")
    @Expose
    private Reg3 reg3;
    @SerializedName("dis3")
    @Expose
    private String dis3;
    @SerializedName("mag")
    @Expose
    private String mag;
    @SerializedName("dep")
    @Expose
    private String dep;
    @SerializedName("lon")
    @Expose
    private String lon;
    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("date")
    @Expose
    private Date date;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Reg1 getReg1() {
        return reg1;
    }

    public void setReg1(Reg1 reg1) {
        this.reg1 = reg1;
    }

    public String getDis1() {
        return dis1;
    }

    public void setDis1(String dis1) {
        this.dis1 = dis1;
    }

    public Reg2 getReg2() {
        return reg2;
    }

    public void setReg2(Reg2 reg2) {
        this.reg2 = reg2;
    }

    public String getDis2() {
        return dis2;
    }

    public void setDis2(String dis2) {
        this.dis2 = dis2;
    }

    public Reg3 getReg3() {
        return reg3;
    }

    public void setReg3(Reg3 reg3) {
        this.reg3 = reg3;
    }

    public String getDis3() {
        return dis3;
    }

    public void setDis3(String dis3) {
        this.dis3 = dis3;
    }

    public String getMag() {
        return mag;
    }

    public void setMag(String mag) {
        this.mag = mag;
    }

    public String getDep() {
        return dep;
    }

    public void setDep(String dep) {
        this.dep = dep;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}
