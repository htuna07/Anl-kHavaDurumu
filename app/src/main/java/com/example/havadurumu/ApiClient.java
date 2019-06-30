package com.example.havadurumu;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiClient{
    @GET("weather")
    Call<CityDetails> getDetailsFromCityName(@Query("q") String city, @Query("APPID") String ApiKey);
}
