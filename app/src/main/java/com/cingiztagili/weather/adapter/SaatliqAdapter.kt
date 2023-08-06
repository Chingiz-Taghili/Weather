package com.cingiztagili.weather.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.cingiztagili.weather.databinding.SaatliqRowBinding
import com.cingiztagili.weather.model.HerSaatUcun
import com.cingiztagili.weather.model.Model
import com.squareup.picasso.Picasso
import java.sql.Time
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

class SaatliqAdapter(val saatliq_proqnoz: List<HerSaatUcun>) :
    RecyclerView.Adapter<SaatliqAdapter.SaatliqHolder>() {

    class SaatliqHolder(val binding: SaatliqRowBinding) : RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SaatliqHolder {
        val binding = SaatliqRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SaatliqHolder(binding)
    }

    override fun getItemCount(): Int {
        return saatliq_proqnoz.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: SaatliqHolder, position: Int) {
        val dateString = saatliq_proqnoz.get(position).tarix_ve_vaxt //2023-08-05 00:00
        val timeString = dateString.split(" ")[1]
        val formatter24 = DateTimeFormatter.ofPattern("HH:mm")
        val time = LocalTime.parse(timeString, formatter24)
        val formatter12 = DateTimeFormatter.ofPattern("h a").format(time)
        holder.binding.saatSaatliqRecyclerView.text = formatter12.toString()

        holder.binding.tempSaatliqRecyclerView.text = "${saatliq_proqnoz.get(position).temperatur.roundToInt().toString()}°"
        holder.binding.rainSaatliqRecyclerView.text = "${saatliq_proqnoz.get(position).yagis_ehtimali}%"

        Picasso.get().load("https:${saatliq_proqnoz.get(position).condition.icon}").into(holder.binding.iconSaatliqRecyclerView)
    }
}