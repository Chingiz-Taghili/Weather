package com.cingiztagili.weather.model

import com.google.gson.annotations.SerializedName

data class Model(
    @SerializedName("name")
    val menteqe: String,
    @SerializedName("region")
    val region: String,
    @SerializedName("country")
    val olke: String,
    @SerializedName("tz_id")
    val saat_qursagi: String,
    @SerializedName("localtime")
    val yerli_vaxt: String,
    @SerializedName("last_updated")
    val son_yenilenme: String,
    @SerializedName("temp_c")
    val temperatur: Double,
    @SerializedName("text")
    val havanin_veziyyeti: String,
    @SerializedName("icon")
    val icon: String,
    @SerializedName("wind_kph")
    val kuleyin_sureti: Double,
    @SerializedName("wind_dir")
    val kuleyin_istiqameti: String,
    @SerializedName("humidity")
    val rutubet: Int,
    @SerializedName("feelslike_c")
    val real_hissetme: Double,
    @SerializedName("uv")
    val uv_indeksi: Double
    )