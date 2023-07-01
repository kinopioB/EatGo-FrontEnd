package com.kinopio.eatgo.presentation.store


import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kinopio.eatgo.databinding.ItemStoreBinding
import com.kinopio.eatgo.domain.store.ui_model.Review

class ReviewAdapter(private val reviewList: List<Review>) :
    RecyclerView.Adapter<ReviewAdapter.Holder>() {

    // ViewHolder 객체를 생성하고 초기화
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemStoreBinding.inflate(inflater, parent, false)
        return Holder(binding)
    }

    // 데이터를 가져와 ViewHolder 안의 내용을 채워줌
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val store = reviewList[position]
        Log.d("store", "$store")
        //holder.bind(store)
    }

    // 총 데이터 갯수 반환
    override fun getItemCount(): Int {
        Log.d("store", "${reviewList.size}")
        return reviewList.size
    }

    inner class Holder(private val binding: ItemStoreBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /*fun bind(store: Store) {
            binding.apply {
                text1.text = store.name
                text2.text = store.startTime
                text3.text = store.endTime
                text4.text = store.location
            }
        }*/
    }
}