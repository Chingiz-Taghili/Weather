package com.cingiztagili.weather.view

import android.opengl.Visibility
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.cingiztagili.weather.R
import com.cingiztagili.weather.adapter.GunlukAdapter
import com.cingiztagili.weather.adapter.SaatliqAdapter
import com.cingiztagili.weather.databinding.ActivityMainBinding
import com.cingiztagili.weather.model.HerSaatUcun
import com.cingiztagili.weather.model.Model
import com.cingiztagili.weather.service.ServiceAPI
import com.google.android.material.elevation.SurfaceColors
import com.squareup.picasso.Picasso
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.sql.Time
import java.util.Calendar
import java.util.Date
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val BASE_URL = "https://api.weatherapi.com/"

    private var compositeDisposable: CompositeDisposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(R.color.blue)
        }

        compositeDisposable = CompositeDisposable()

        loadData()
    }

    private fun loadData() {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build().create(ServiceAPI::class.java)

        compositeDisposable?.add(
            retrofit.getData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(this::handleError)
                .subscribe(this::handleResponse) { e -> println("onError ${e.localizedMessage}") }
        )
    }

    private fun handleError(ex: Throwable) {
        Toast.makeText(this@MainActivity, ex.localizedMessage, Toast.LENGTH_SHORT).show()
    }

    private fun handleResponse(weather: Model) {
        weather.let {
            binding.progressBar.visibility = View.GONE

            binding.nameText.visibility = View.VISIBLE
            binding.nameText.text = it.location.menteqe

            binding.currentTemperaturText.visibility = View.VISIBLE
            binding.currentTemperaturText.text = "${it.current.temperatur.roundToInt()}°"

            binding.currentConditionText.visibility = View.VISIBLE
            binding.currentConditionText.text = it.current.condition.havanin_veziyyeti

            binding.dailyMaxMinTemp.visibility = View.VISIBLE
            binding.dailyMaxMinTemp.text =
                "${it.forecast.gunluk_proqnoz.get(0).gunluk_umumi.maks_temp.roundToInt()}° / ${
                    it.forecast.gunluk_proqnoz.get(0).gunluk_umumi.min_temp.roundToInt()
                }°"

            binding.currentIcon.visibility = View.VISIBLE
            Picasso.get().load("https:${it.current.condition.icon}").into(binding.currentIcon)

            binding.scrollView.visibility = View.VISIBLE

            binding.saatliqRecyclerView.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            binding.saatliqRecyclerView.adapter =
                SaatliqAdapter(it.forecast.gunluk_proqnoz.get(0).saatliq_proqnoz)

            binding.gunlukRecyclerView.layoutManager = LinearLayoutManager(this)
            binding.gunlukRecyclerView.adapter = GunlukAdapter(it.forecast.gunluk_proqnoz)
            binding.gunlukRecyclerView.isNestedScrollingEnabled = false


            binding.feelsLikeDegree.text = "${it.current.real_hissetme.roundToInt()}°"

            binding.humidityDegree.text = "${it.current.rutubet}%"

            var uvIndexDegree = "Low"
            if (it.current.uv_indeksi.roundToInt() >= 3 && it.current.uv_indeksi.roundToInt() <= 5) {
                uvIndexDegree = "Moderate"
            } else if (it.current.uv_indeksi.roundToInt() >= 6 && it.current.uv_indeksi.roundToInt() <= 7) {
                uvIndexDegree = "High"
            } else if (it.current.uv_indeksi.roundToInt() >= 8 && it.current.uv_indeksi.roundToInt() <= 10) {
                uvIndexDegree = "Very High"
            } else if (it.current.uv_indeksi.roundToInt() >= 11) {
                uvIndexDegree = "Extreme"
            }
            binding.uvIndexDegree.text = uvIndexDegree

            binding.windDegree.text = "${it.current.kuleyin_sureti.roundToInt()} km/h"

            binding.sunriseTime.text = it.forecast.gunluk_proqnoz.get(0).astronomik.sefeq

            binding.sunsetTime.text = it.forecast.gunluk_proqnoz.get(0).astronomik.qurub

            Glide.with(this).asGif().load("https://i.pinimg.com/originals/9d/72/5f/9d725fe7ec601fb2744d8d704f402a10.gif").into(binding.sunriseIcon)
            //https://usagif.com/wp-content/uploads/gifs/sun-58.gif
            //https://i.pinimg.com/originals/9d/72/5f/9d725fe7ec601fb2744d8d704f402a10.gif
            Glide.with(this).asGif().load("https://media.tenor.com/-RNOB5xDZ5gAAAAC/sunset.gif").into(binding.sunsetIcon)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable?.clear()
    }
}