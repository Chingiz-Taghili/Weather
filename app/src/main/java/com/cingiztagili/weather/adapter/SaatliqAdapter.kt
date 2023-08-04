package com.cingiztagili.weather.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cingiztagili.weather.databinding.SaatliqRowBinding
import com.cingiztagili.weather.model.HerSaatUcun
import com.cingiztagili.weather.model.Model
import com.squareup.picasso.Picasso
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

    override fun onBindViewHolder(holder: SaatliqHolder, position: Int) {
        holder.binding.saatSaatliqRecyclerView.text = saatliq_proqnoz.get(position).tarix_ve_vaxt
        holder.binding.tempSaatliqRecyclerView.text = "${saatliq_proqnoz.get(position).temperatur.roundToInt().toString()}°"
        holder.binding.rutubetSaatliqRecyclerView.text = "${saatliq_proqnoz.get(position).rutubet}%"
        Picasso.get().load("https:${saatliq_proqnoz.get(position).condition.icon}").into(holder.binding.iconSaatliqRecyclerView)
    }
}