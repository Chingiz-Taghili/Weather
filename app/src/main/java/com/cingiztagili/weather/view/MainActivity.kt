package com.cingiztagili.weather.view

import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
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
        println(ex.localizedMessage)
    }

    private fun handleResponse(weather: Model) {
        weather.let {
            binding.progressBar.visibility = View.GONE
            binding.nameText.visibility = View.VISIBLE
            binding.nameText.text = it.location.menteqe
            binding.currentTemperaturText.text = "${it.current.temperatur.roundToInt()}°"
            binding.currentConditionText.text = it.current.condition.havanin_veziyyeti
            binding.dailyMaxMinTemp.text =
                "${it.forecast.gunluk_proqnoz.get(0).gunluk_umumi.maks_temp.roundToInt()}° / ${
                    it.forecast.gunluk_proqnoz.get(0).gunluk_umumi.min_temp.roundToInt()
                }°"
            Picasso.get().load("https:${it.current.condition.icon}").into(binding.currentIcon)

            val saatliqList = it.forecast.gunluk_proqnoz.get(0).saatliq_proqnoz
            binding.saatliqRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            val adapter = SaatliqAdapter(saatliqList)
            binding.saatliqRecyclerView.adapter = adapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable?.clear()
    }
}