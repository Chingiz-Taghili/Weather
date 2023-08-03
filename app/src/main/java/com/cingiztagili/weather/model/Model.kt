package com.cingiztagili.weather.model

import com.google.gson.annotations.SerializedName

data class Model(
    @SerializedName("location")
    val location: Location,
    @SerializedName("current")
    val current: Current,
    @SerializedName("forecast")
    val forecast: Forecast
)

data class Location(
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
)

data class Current(
    @SerializedName("last_updated")
    val son_yenilenme: String,
    @SerializedName("temp_c")
    val temperatur: Double,
    @SerializedName("condition")
    val condition: Condition,
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

data class Condition(
    @SerializedName("text")
    val havanin_veziyyeti: String,
    @SerializedName("icon")
    val icon: String
)

data class Forecast(
    @SerializedName("forecastday")
    val gunluk_proqnoz: List<BirGunUcun>
)

data class BirGunUcun(
    @SerializedName("date")
    val tarix: String,
    @SerializedName("day")
    val gunluk_umumi: GunlukUmumi,
    @SerializedName("astro")
    val astronomik: Astronomik,
    @SerializedName("hour")
    val saatliq_proqnoz: List<HerSaatUcun>
)

data class GunlukUmumi(
    @SerializedName("maxtemp_c")
    val maks_temp: Double,
    @SerializedName("mintemp_c")
    val min_temp: Double,
    @SerializedName("avghumidity")
    val ortalama_rutubet: Double,
    @SerializedName("condition")
    val condition: Condition
)

data class Astronomik(
    @SerializedName("sunrise")
    val sefeq: String,
    @SerializedName("sunset")
    val qurub: String
)

data class HerSaatUcun(
    @SerializedName("time")
    val tarix_ve_vaxt: String,
    @SerializedName("temp_c")
    val temperatur: Double,
    @SerializedName("condition")
    val condition: Condition,
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