package com.kinopio.eatgo.presentation.store

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kinopio.eatgo.databinding.ItemOpenInfoBinding
import com.kinopio.eatgo.domain.store.ui_model.OpenInfo

class OpenInfoAdapter(private val openInfoList: List<OpenInfo>) :
    RecyclerView.Adapter<OpenInfoAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OpenInfoAdapter.Holder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemOpenInfoBinding.inflate(inflater, parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val openInfo = openInfoList[position]
        holder.bind(openInfo)
    }

    override fun getItemCount(): Int {
        return openInfoList.size
    }

    inner class Holder(private val binding: ItemOpenInfoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(openInfo: OpenInfo) {
            binding.apply {
                 day.text = openInfo.day
                 startTime.text = openInfo.startTime
                 endTime.text = openInfo.endTime

            }
        }
    }
}