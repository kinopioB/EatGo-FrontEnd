package com.kinopio.eatgo.presentation.store


import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kinopio.eatgo.databinding.ItemOpenInfoBinding
import com.kinopio.eatgo.databinding.ItemPopularStoreBinding
import com.kinopio.eatgo.domain.store.PopularStoreResponseDto
import com.kinopio.eatgo.domain.store.ui_model.OpenInfo
import com.kinopio.eatgo.domain.store.ui_model.Store
import kotlinx.coroutines.NonDisposableHandle.parent


class PopularStoreAdapter(private val storeList: List<PopularStoreResponseDto>, private val onStoreClickListener: OnStoreClickListener) :

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
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val storeId = storeList[position].storeId
                    onStoreClickListener.onStoreClick(storeId)
                }
            }
        }

        fun bind(popularStore: PopularStoreResponseDto) {
            binding.apply {
                binding.popularStoreName.text = popularStore.storeName
                binding.popularStoreCate.text = popularStore.categoryName
                var grade = position.toInt()+1
                binding.grade.text = grade.toString()+"등"

                Glide.with(itemView)
                    .load(popularStore.thumbnail)
                    .into(popularStoreImg)
//
            }
        }
    }
}