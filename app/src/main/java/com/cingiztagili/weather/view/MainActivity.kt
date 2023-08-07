package com.cingiztagili.weather.view

import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.cingiztagili.weather.adapter.GunlukAdapter
import com.cingiztagili.weather.adapter.SaatliqAdapter
import com.cingiztagili.weather.databinding.ActivityMainBinding
import com.cingiztagili.weather.model.HerSaatUcun
import com.cingiztagili.weather.model.Model
import com.cingiztagili.weather.service.ServiceAPI
import com.squareup.picasso.Picasso
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
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


            binding.humidityDegree.text = "${it.current.rutubet}%"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable?.clear()
    }
}