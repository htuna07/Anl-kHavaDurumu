package com.example.havadurumu;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

class CityDetails {
        @SerializedName("main")
        @Expose
        private TempDetails tempDetails;

        @SerializedName("name")
        @Expose
        private String name;

        @SerializedName("clouds")
        @Expose
        private Clouds clouds;

        @SerializedName("wind")
        @Expose
        private Wind wind;

        @SerializedName("weather")
        @Expose
        private List<Weather> weather = null;

        public TempDetails getTempDetails() {
                return tempDetails;
        }

        public String getName() {
                return name;
        }

        public Clouds getClouds() {
                return clouds;
        }

        public Wind getWind() {
                return wind;
        }


        public List<Weather> getWeather() {
                return weather;
        }


}
