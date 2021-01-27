package ir.amirsobhan.earthquake.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApiResponse {
    @SerializedName("data")
    @Expose
    private Earthquake earthquake;
    @SerializedName("metadata")
    @Expose
    private Metadata metadata;

    public Earthquake getData() {
        return earthquake;
    }

    public void setData(Earthquake earthquake) {
        this.earthquake = earthquake;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }
}
