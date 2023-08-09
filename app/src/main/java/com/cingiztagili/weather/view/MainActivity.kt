package com.cingiztagili.weather.view

import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.ActionBarContainer
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.cingiztagili.weather.R
import com.cingiztagili.weather.adapter.GunlukAdapter
import com.cingiztagili.weather.adapter.SaatliqAdapter
import com.cingiztagili.weather.databinding.ActivityMainBinding
import com.cingiztagili.weather.model.Model
import com.cingiztagili.weather.service.ServiceAPI
import com.squareup.picasso.Picasso
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val BASE_URL = "https://api.weatherapi.com/"

    private var compositeDisposable: CompositeDisposable? = null


    lateinit var drawerLayout: DrawerLayout
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //Status bar-in rengini app-a uygunlasdiririq:
        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.getColor(R.color.day)
        }


        //Navigation view-i tanidiriq:
        drawerLayout = binding.drawerLayout
        actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close)
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        compositeDisposable = CompositeDisposable()

        loadData()
    }


    private fun menuButtonClicked(view: View) {
        drawerLayout.openDrawer(Gravity.LEFT)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)
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

            binding.locationImage.visibility = View.VISIBLE

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
            //RecyclerView-in daxili scrollingini baglayiriq:
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

            binding.sunriseTime.text =
                it.forecast.gunluk_proqnoz.get(0).astronomik.sefeq //"05:41 AM"

            binding.sunsetTime.text = it.forecast.gunluk_proqnoz.get(0).astronomik.qurub

            Glide.with(this).asGif()
                .load("https://i.pinimg.com/originals/9d/72/5f/9d725fe7ec601fb2744d8d704f402a10.gif")
                .into(binding.sunriseIcon)

            Glide.with(this).asGif().load("https://media.tenor.com/-RNOB5xDZ5gAAAAC/sunset.gif")
                .into(binding.sunsetIcon)


             if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                 val APIupdateTime = LocalDateTime.parse(
                     it.current.son_yenilenme,
                     DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm") //"2023-08-09 14:00"
                 )
                 val updatedTime = DateTimeFormatter.ofPattern("dd.MM HH:mm").format(APIupdateTime)
                 binding.lastUpdatedTime.text = "Updated: ${updatedTime}"
             }



            //App-in background-unu gundelik vaxta uygunlasdiririq:
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val APIsunriseTime = LocalTime.parse(
                    it.forecast.gunluk_proqnoz.get(0).astronomik.sefeq,
                    DateTimeFormatter.ofPattern("hh:mm a")
                )
                val sunriseTime = DateTimeFormatter.ofPattern("HH:mm").format(APIsunriseTime)

                val APIsunsetTime = LocalTime.parse(
                    it.forecast.gunluk_proqnoz.get(0).astronomik.qurub,
                    DateTimeFormatter.ofPattern("hh:mm a")
                )
                val sunsetTime = DateTimeFormatter.ofPattern("HH:mm").format(APIsunsetTime)

                val nowTime = DateTimeFormatter.ofPattern("HH:mm").format(LocalTime.now())

                if (nowTime > sunriseTime && nowTime < sunsetTime) {
                    window.statusBarColor = this.getColor(R.color.day)
                    val listDayBackground = R.drawable.list_day_background
                    binding.mainConstraintLayout.setBackgroundResource(R.drawable.day_background)
                    binding.saatliqLinearLayout.setBackgroundResource(listDayBackground)
                    binding.gunlukLinearLayout.setBackgroundResource(listDayBackground)
                    binding.feelsLikeLinearLayout.setBackgroundResource(listDayBackground)
                    binding.humidityLinearLayout.setBackgroundResource(listDayBackground)
                    binding.uvIndexLinearLayout.setBackgroundResource(listDayBackground)
                    binding.windLinearLayout.setBackgroundResource(listDayBackground)
                    binding.sunriseLinearLayout.setBackgroundResource(listDayBackground)
                    binding.sunsetLinearlayout.setBackgroundResource(listDayBackground)
                } else {
                    window.statusBarColor = this.getColor(R.color.night)
                    val listNightBackground = R.drawable.list_night_background
                    binding.mainConstraintLayout.setBackgroundResource(R.drawable.night_background)
                    binding.saatliqLinearLayout.setBackgroundResource(listNightBackground)
                    binding.gunlukLinearLayout.setBackgroundResource(listNightBackground)
                    binding.feelsLikeLinearLayout.setBackgroundResource(listNightBackground)
                    binding.humidityLinearLayout.setBackgroundResource(listNightBackground)
                    binding.uvIndexLinearLayout.setBackgroundResource(listNightBackground)
                    binding.windLinearLayout.setBackgroundResource(listNightBackground)
                    binding.sunriseLinearLayout.setBackgroundResource(listNightBackground)
                    binding.sunsetLinearlayout.setBackgroundResource(listNightBackground)
                }
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable?.clear()
    }
}