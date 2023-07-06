package com.kinopio.eatgo.presentation.store


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kinopio.eatgo.databinding.ItemTodayOpenStoreBinding
import com.kinopio.eatgo.domain.store.PopularStoreResponseDto
import com.kinopio.eatgo.domain.store.TodayOpenStoreResponseDto
import com.kinopio.eatgo.domain.store.ui_model.OpenInfo
import com.kinopio.eatgo.domain.store.ui_model.Store

class TodayOpenStoreAdapter(private val storeList: List<TodayOpenStoreResponseDto>, private val onStoreClickListener: OnStoreClickListener) :
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

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val storeId = storeList[position].storeId
                    onStoreClickListener.onStoreClick(storeId)
                }
            }
        }

        fun bind(store: TodayOpenStoreResponseDto) {
            binding.apply {
                binding.storeName.text = store.storeName
                binding.storeDesc.text = store.info
                binding.tags.text = store.tags.map { " # " + it.tagName }.joinToString(separator = " ").toString()
                Glide.with(itemView)
                    .load(store.thumbnail)
                    .into(storeImg)

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