package com.example.popcorn.models.response;

import com.example.popcorn.models.api.Cast;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CastApiResponse {

    @Expose
    private int id;

    @SerializedName("cast")
    @Expose
    private Cast[] casts;

    public CastApiResponse(int id, Cast[] casts) {
        this.id = id;
        this.casts = casts;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Cast[] getCasts() {
        return casts;
    }

    public void setCasts(Cast[] casts) {
        this.casts = casts;
    }
}
