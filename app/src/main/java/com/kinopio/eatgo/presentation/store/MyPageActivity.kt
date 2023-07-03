package com.kinopio.eatgo.presentation.store

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kinopio.eatgo.R
import com.kinopio.eatgo.RetrofitClient
import com.kinopio.eatgo.data.store.StoreService
import com.kinopio.eatgo.databinding.ActivityMyPageBinding
import com.kinopio.eatgo.domain.map.StoreMyPageResponseDto
import com.kinopio.eatgo.domain.store.ui_model.Review
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.properties.Delegates

class MyPageActivity : AppCompatActivity() {
    private lateinit var reviewAdapter: ReviewAdapter
    private lateinit var reviewList: List<Review>
    private lateinit var storeMyPageResponseDto : StoreMyPageResponseDto

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

        // 상태 버튼
        var startStatus = findViewById<TextView>(R.id.startStatus)
        var endStatus = findViewById<TextView>(R.id.endStatus)
        startStatus.visibility = View.GONE
        endStatus.visibility = View.GONE

        // 레트로핏
        val retrofit = RetrofitClient.getRetrofit2()
        val storeService = retrofit.create(StoreService::class.java)

        Log.d("store start retrofit", "store retrofit2")

        var storeId = 1
        storeService.getReviews(storeId)
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
                    // isOpen = response.body()!!.isOpen
                    // 리뷰 값 불러오기
                    response.body()?.let{ storeMyPageResponseDto ->
                        Log.d("store start retrofit", "리사이클러뷰 진입")

                        reviewList = storeMyPageResponseDto.reviews

                        Log.d("store start retrofit", "${reviewList}")
                        reviewAdapter = ReviewAdapter(reviewList)

                        binding.myStoreReview.apply {
                            adapter = reviewAdapter
                            this.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                        }
                    }
                }
            })

        /*// 나머지 부분 세팅
        Log.d("store start retrofit", "isOpen :  ${storeMyPageResponseDto.isOpen}")
        // 상태 바 표시
        // 바로 적용이 되지 않음
        if(storeMyPageResponseDto.isOpen == 1){
            startStatus.visibility = View.VISIBLE
            endStatus.visibility = View.GONE
        }else{
            startStatus.visibility = View.GONE
            endStatus.visibility = View.VISIBLE
        }
        // val imageUrl = "https://example.com/image.jpg"
        binding.categoryName.text = storeMyPageResponseDto.categoryName
        binding.categoryIcon.text = storeMyPageResponseDto.categoryId.toString()
        binding.thumbnail.text = storeMyPageResponseDto.thumbNail
        // binding.likeImage.setImageURI()
        binding.ratingStar.text = storeMyPageResponseDto.ratingAverage.toString()
        // binding.reviewNum.text = storeMyPageResponseDto.reviewNum*/


        /*binding.manageBtn.setOnClickListener {
            Log.d("mypage", "manage page 이동")
            val intent = Intent(this, ManageActivity::class.java)
            startActivity(intent)
        }*/


        binding.storeCard.setOnClickListener{
            val intent = Intent(this, ManageActivity::class.java)
            intent.putExtra("storeId", storeId)

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