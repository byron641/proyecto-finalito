package com.example.proyectofinalv2;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApi {
    @GET("weather")
    Call<WeatherResponse> getWeather(@Query("id") String cityId, @Query("appid") String apiKey, @Query("lang") String lang
    );

    @GET("weather")
    Call<WeatherResponse> getWeatherByCoordinates(@Query("lat") double latitude, @Query("lon") double longitude, @Query("appid") String apiKey, @Query("lang") String lang
    );
}