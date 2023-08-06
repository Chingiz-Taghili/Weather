package com.cingiztagili.weather.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.cingiztagili.weather.databinding.GunlukRowBinding
import com.cingiztagili.weather.model.BirGunUcun
import com.squareup.picasso.Picasso
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

class GunlukAdapter(val gunluk_proqnoz: List<BirGunUcun>) :
    RecyclerView.Adapter<GunlukAdapter.GunlukHolder>() {

    class GunlukHolder(val binding: GunlukRowBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GunlukHolder {
        val binding = GunlukRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GunlukHolder(binding)
    }

    override fun getItemCount(): Int {
        return gunluk_proqnoz.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: GunlukHolder, position: Int) {
        val dateString = gunluk_proqnoz.get(position).tarix //2023-08-07
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val date = LocalDate.parse(dateString, formatter)
        val week = date.dayOfWeek
        holder.binding.weekDayGunlukRecyclerView.text = week.toString()

        holder.binding.rainGunlukRecyclerView.text = "${gunluk_proqnoz.get(position).gunluk_umumi.yagis_ehtimali}%"

        Picasso.get().load("https:${gunluk_proqnoz.get(position).gunluk_umumi.condition.icon}").into(holder.binding.iconGunlukRecyclerView)

        holder.binding.maxMinTempGunlukRecyclerView.text = "${gunluk_proqnoz.get(position).gunluk_umumi.maks_temp.roundToInt()}°/ ${gunluk_proqnoz.get(position).gunluk_umumi.min_temp.roundToInt()}°"
    }
}