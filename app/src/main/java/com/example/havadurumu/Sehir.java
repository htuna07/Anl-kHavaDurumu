package com.example.havadurumu;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Sehir {
    private String ad ;
    private float sicaklik ;
    private int nemOrani ;
    private int bulutOrani;
    private float ruzgarHizi ;

    private String icontype;

    private String icon_name;

    private DecimalFormat df ;

    Sehir(CityDetails gelenOzellikler){
        ad = gelenOzellikler.getName();
        sicaklik = gelenOzellikler.getTempDetails().getTemp();
        nemOrani = gelenOzellikler.getTempDetails().getHumidity();
        bulutOrani = gelenOzellikler.getClouds().getAll();
        ruzgarHizi  = gelenOzellikler.getWind().getSpeed();
        icontype =  gelenOzellikler.getWeather().get(0).getIcon();
        df = new DecimalFormat();
        df.setMaximumFractionDigits(1);
    }

    public String getAd() {
        return ad;
    }

    public String getSicaklik() {
        return df.format(sicaklik) + " °C";
    }

    public String getNemOrani() {
        return "% " + nemOrani;
    }

    public String getBulutOrani() {
        return "% " + bulutOrani;
    }

    public String getRuzgarHizi() {
        return df.format(ruzgarHizi) + " m/s";
    }

    public String getHavaDurumu() {
        String donecek = "";
        switch (icontype.substring(0,2)){
            case "01":
                donecek = "acik";
                break;
            case "02":
                donecek = "az_bulutlu";
                break;
            case "03":
                donecek = "az_bulutlu";
                break;
            case "04":
                donecek = "bulutlu";
                break;
            case "09":
                donecek = "yagisli";
                break;
            case "10":
                donecek = "yagisli";
                break;
            case "11":
                donecek = "simsekli";
                break;
            case "13":
                donecek = "karli";
                break;
            case "50":
                donecek = "sisli";
                break;
        }
            return donecek;
    }

    public String getZaman() {
        String donecek = "";
        if (icontype.charAt(icontype.length() - 1) == 'n'){
           donecek = "gece";
        }
        else if(icontype.charAt(icontype.length() - 1) == 'd'){
            donecek = "gunduz";
        }
        return donecek;

    }

    public String getIcon_name() {
        icon_name = "ic_" + getHavaDurumu() + "_" + getZaman();
        return icon_name;
    }




}
