package com.kinopio.eatgo.presentation.store


import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kinopio.eatgo.databinding.ItemMypageReviewBinding
import com.kinopio.eatgo.databinding.ItemReviewBinding
import com.kinopio.eatgo.domain.map.StoreMyPageResponseDto
import com.kinopio.eatgo.domain.store.ui_model.Review

class ReviewAdapter(private val reviewList: List<Review>?) :
    RecyclerView.Adapter<ReviewAdapter.Holder>() {

    // ViewHolder 객체를 생성하고 초기화
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemReviewBinding.inflate(inflater, parent, false)
        Log.d("store start retrofit", "onCreateViewHolder 진입")

        return Holder(binding)
    }

    // 데이터를 가져와 ViewHolder 안의 내용을 채워줌
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val review = reviewList?.get(position)
        Log.d("store", "$review")
        if (review != null) {
            holder.bind(review)
        }
    }

    // 총 데이터 갯수 반환
    override fun getItemCount(): Int {
        Log.d("store start retrofit", "${reviewList?.size ?:0}")
        return reviewList?.size ?: 0
    }

    inner class Holder(private val binding: ItemReviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(review: Review) {
            binding.apply {
                Log.d("store start retrofit", "Holder 진입")

                user.text = review.userName
                ratingStar.rating = review.rating.toFloat()
                content.text = review.content
                createdAt.text = review.createdAt
            }
        }
    }
}
