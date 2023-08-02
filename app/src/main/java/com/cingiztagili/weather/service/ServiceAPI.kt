package com.cingiztagili.weather.service

import com.cingiztagili.weather.model.Model
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET

interface ServiceAPI {
    @GET("v1/current.json?key=bd64a2ded339460d9b364123230208&q=Baku&aqi=yes")
    fun getData(): Observable<Model>
}