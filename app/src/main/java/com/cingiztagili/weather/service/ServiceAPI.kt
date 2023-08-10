package com.cingiztagili.weather.service

import com.cingiztagili.weather.model.Model
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface ServiceAPI {

    //https://api.weatherapi.com/v1/forecast.json?key=bd64a2ded339460d9b364123230208&q=Baku&days=7&aqi=yes&alerts=yes

    @GET("v1/forecast.json")
    fun getData(
        @Query("key") key: String, @Query("q") location: String, @Query("days") days: Int,
        @Query("aqi") aqi: String, @Query("alerts") alerts: String, @Query("lang") language: String
    ): Observable<Model>
}