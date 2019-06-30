package com.example.havadurumu;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Wind {

    @SerializedName("speed")
    @Expose
    private float speed;


    public float getSpeed() {
        return speed;
    }


}
