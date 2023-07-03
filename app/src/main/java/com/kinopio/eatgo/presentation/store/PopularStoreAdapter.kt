package com.kinopio.eatgo.presentation.store


import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.kinopio.eatgo.databinding.ItemOpenInfoBinding
import com.kinopio.eatgo.databinding.ItemPopularStoreBinding
import com.kinopio.eatgo.domain.store.ui_model.OpenInfo
import com.kinopio.eatgo.domain.store.ui_model.Store

class PopularStoreAdapter(private val storeList: List<Store>) :
    RecyclerView.Adapter<PopularStoreAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularStoreAdapter.Holder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPopularStoreBinding.inflate(inflater, parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val store = storeList[position]
        holder.bind(store)
    }

    override fun getItemCount(): Int {
        return storeList.size
    }

    inner class Holder(private val binding: ItemPopularStoreBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(store: Store) {
            binding.apply {
                binding.popularStoreName.text = store.name
                binding.popularStoreCate.text = store.category

//
            }
        }
    }
}