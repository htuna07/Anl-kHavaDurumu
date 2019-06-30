package com.example.havadurumu;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TempDetails {
    @SerializedName("temp")
    @Expose
    private float temp;

    @SerializedName("humidity")
    @Expose
    private int humidity;

    private final float SABIT_KELVIN_DEGER = 273.15F;

    public float getTemp() {
        temp = temp  - SABIT_KELVIN_DEGER;
        return temp;
    }

    public int getHumidity() {
        return humidity;
    }
}
