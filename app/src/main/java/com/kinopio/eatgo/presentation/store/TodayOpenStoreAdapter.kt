package com.kinopio.eatgo.presentation.store


import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.kinopio.eatgo.databinding.ItemOpenInfoBinding
import com.kinopio.eatgo.databinding.ItemPopularStoreBinding
import com.kinopio.eatgo.databinding.ItemTodayOpenStoreBinding
import com.kinopio.eatgo.domain.store.ui_model.OpenInfo
import com.kinopio.eatgo.domain.store.ui_model.Store

class TodayOpenStoreAdapter(private val storeList: List<Store>) :
    RecyclerView.Adapter<TodayOpenStoreAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodayOpenStoreAdapter.Holder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTodayOpenStoreBinding.inflate(inflater, parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val store = storeList[position]
        holder.bind(store)
    }

    override fun getItemCount(): Int {
        return storeList.size
    }

    inner class Holder(private val binding: ItemTodayOpenStoreBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(store: Store) {
            binding.apply {
                binding.storeName.text = store.name
                binding.storeDesc.text = store.desc

//                for (tag in store.tags) {
//                    val tagButton = Button(requireContext())
//                    tagButton.text = tag.name
//                    // 버튼 스타일, 크기, 클릭 이벤트 등 설정
//
//                    // LinearLayout에 버튼 추가
//                    val layoutParams = LinearLayout.LayoutParams(
//                        LinearLayout.LayoutParams.WRAP_CONTENT,
//                        LinearLayout.LayoutParams.WRAP_CONTENT
//                    )
//                    layoutParams.setMargins(0, 0, 8, 0) // 각 버튼 사이의 간격 설정
//
//                }
            }
        }
    }
}