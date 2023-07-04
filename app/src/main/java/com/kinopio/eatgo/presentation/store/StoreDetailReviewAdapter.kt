package com.kinopio.eatgo.presentation.store


import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kinopio.eatgo.databinding.ItemReviewBinding
import com.kinopio.eatgo.databinding.ItemStoreBinding
import com.kinopio.eatgo.domain.map.ReviewResponseDto
import com.kinopio.eatgo.domain.store.ReviewDto
import com.kinopio.eatgo.domain.store.ui_model.Store

class StoreDetailReviewAdapter(private val reviewList: List<ReviewDto>) :
    RecyclerView.Adapter<StoreDetailReviewAdapter.Holder>() {

    // ViewHolder 객체를 생성하고 초기화
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemReviewBinding.inflate(inflater, parent, false)
        return Holder(binding)
    }

    // 데이터를 가져와 ViewHolder 안의 내용을 채워줌
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val review = reviewList[position]
        Log.d("review", "${review}")
        holder.bind(review)
    }

    // 총 데이터 갯수 반환
    override fun getItemCount(): Int {
        Log.d("store", "${reviewList.size}")
        return reviewList.size
    }

    inner class Holder(private val binding: ItemReviewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(review: ReviewDto) {
            binding.apply {
                ratingStar.rating = review.rating.toFloat()
                user.text = review.userName
                content.text = review.content
            }
        }
    }
}