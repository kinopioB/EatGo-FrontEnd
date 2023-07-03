package com.kinopio.eatgo.presentation.store

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kinopio.eatgo.R
import com.kinopio.eatgo.RetrofitClient
import com.kinopio.eatgo.data.map.ReviewService
import com.kinopio.eatgo.databinding.ActivityMyPageBinding
import com.kinopio.eatgo.domain.map.StoreMyPageResponseDto
import com.kinopio.eatgo.domain.store.ui_model.Review
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyPageActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var reviewAdapter: ReviewAdapter
    private lateinit var reviewList: List<Review>

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        val binding = ActivityMyPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ToolbarUtils.setupToolbar(
            this,
            binding.root.findViewById<Toolbar>(R.id.toolbar),
            "마이페이지",
            null
        )

        val retrofit = RetrofitClient.getRetrofit2()
        val reviewService = retrofit.create(ReviewService::class.java)

        Log.d("store start retrofit", "store retrofit2")

        var storeId = 1
        reviewService.getReviews(storeId)
            .enqueue(object : Callback<StoreMyPageResponseDto> {
                override fun onFailure(call: Call<StoreMyPageResponseDto>, t: Throwable) {
                    Log.d("fail", "실패")
                    Log.d("fail", "$t")
                }

                override fun onResponse(
                    call: Call<StoreMyPageResponseDto>,
                    response: Response<StoreMyPageResponseDto>
                ) {
                    if (response.isSuccessful.not()) {
                        Log.d("store start retrofit", "retrofit4")
                        return
                    }
                    Log.d("store start retrofit", "통신 + ${response?.body()}")

                    // 리뷰 값 불러오기
                    response.body()?.let{ storeMyPageResponseDto ->
                        Log.d("store start retrofit", "리사이클러뷰 진입")

                        reviewList = storeMyPageResponseDto.reviews

                        Log.d("store start retrofit", "${reviewList}")
                        reviewAdapter = ReviewAdapter(reviewList)

                        binding.myStoreReview.apply {
                            Log.d("store start retrofit", "binding 시작")
                            adapter = reviewAdapter
                            Log.d("store start retrofit", "binding adapter 끝")
                            this.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                            Log.d("store start retrofit", "binding adapter 끝")
                        }
                    }
                }
            })


        binding.manageBtn.setOnClickListener {
            Log.d("mypage", "manage page 이동")
            val intent = Intent(this, ManageActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return ToolbarUtils.handleOptionsItemSelected(
            this,
            item
        ) // 분리된 클래스의 handleOptionsItemSelected 함수 호출
    }
}