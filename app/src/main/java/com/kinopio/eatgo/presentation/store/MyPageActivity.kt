package com.kinopio.eatgo.presentation.store

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import com.kinopio.eatgo.MainActivity
import com.kinopio.eatgo.R
import com.kinopio.eatgo.RetrofitClient
import com.kinopio.eatgo.User
import com.kinopio.eatgo.data.store.StoreService
import com.kinopio.eatgo.databinding.ActivityMyPageBinding
import com.kinopio.eatgo.domain.map.StoreMyPageResponseDto
import com.kinopio.eatgo.presentation.qr.CreateQRActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyPageActivity : AppCompatActivity() {
    private lateinit var reviewAdapter: ReviewAdapter

    // private lateinit var reviewList: List<Review>
    private lateinit var storeMyPageResponseDto: StoreMyPageResponseDto

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

        var userId = User.getUserId()

        Log.d("store start retrofit", "$userId")

        // 분기 처리 - 사용자일 때, 아닐 때
        if (userId != null) {
            storeService.getMypage(userId)
                .enqueue(object : Callback<StoreMyPageResponseDto> {
                    override fun onFailure(call: Call<StoreMyPageResponseDto>, t: Throwable) {
                        Log.d("store start retrofit", "실패")
                        Log.d("store start retrofit", "$t")
                    }

                    override fun onResponse(
                        call: Call<StoreMyPageResponseDto>,
                        response: Response<StoreMyPageResponseDto>
                    ) {
                        Log.d("store start retrofit", "통신 + ${response?.body()}")
                        // isOpen = response.body()!!.isOpen
                        // 리뷰 값 불러오기
                        response.body()?.let {
                            storeMyPageResponseDto = it
                            reviewAdapter = ReviewAdapter(storeMyPageResponseDto.reviews)
                            binding.myStoreReview.apply {
                                adapter = reviewAdapter
                                this.layoutManager = LinearLayoutManager(
                                    context,
                                    LinearLayoutManager.VERTICAL,
                                    false
                                )
                            }
                            Glide.with(binding.root)
                                .load(storeMyPageResponseDto.thumbNail)
                                .into(binding.thumbnailId)
                            // binding.categoryName.text = storeMyPageResponseDto.categoryName
                            // binding.categoryIcon.text = storeMyPageResponseDto.categoryId.toString()
                            binding.starRating.text =
                                storeMyPageResponseDto.ratingAverage.toString()
                            binding.reviewNum.text = storeMyPageResponseDto.reviewNum.toString()
                            binding.storeName.text = storeMyPageResponseDto.storeName
                            binding.category.text = storeMyPageResponseDto.categoryName
                            val linearContainer = findViewById<LinearLayout>(R.id.linear_container)

                            for (item in storeMyPageResponseDto.tags) {
                                val textView = TextView(this@MyPageActivity)
                                textView.layoutParams = LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                                )
                                textView.setPadding(10,10,10,10)
                                textView.setBackgroundResource(R.drawable.button_radius)
                                textView.text = item.tagName.toString()

                                linearContainer.addView(textView)
                            }
                            if(storeMyPageResponseDto.isOpen==1){
                                binding.startStatus.visibility = View.VISIBLE
                            }else{
                                binding.endStatus.visibility = View.VISIBLE
                            }



                        }

                    }
                })
        }

//        // 나머지 부분 세팅
//        Log.d("store start retrofit", "isOpen :  ${storeMyPageResponseDto.isOpen}")
//        // 상태 바 표시
//        // 바로 적용이 되지 않음
//        if(storeMyPageResponseDto.isOpen == 1){
//            startStatus.visibility = View.VISIBLE
//            endStatus.visibility = View.GONE
//        }else{
//            startStatus.visibility = View.GONE
//            endStatus.visibility = View.VISIBLE
//        }
//
//
//        /*binding.manageBtn.setOnClickListener {
//            Log.d("mypage", "manage page 이동")
//            val intent = Intent(this, ManageActivity::class.java)
//            startActivity(intent)
//        }*/
//
//
        binding.storeCard.setOnClickListener {
            val intent = Intent(this, ManageActivity::class.java)
            intent.putExtra("storeId", storeMyPageResponseDto.storeId)
            startActivity(intent)
        }

        binding.qr.setOnClickListener {
            Log.d("QR", "QR코드 생성")
            val intent = Intent(this, CreateQRActivity::class.java)
            intent.putExtra("storeId", storeMyPageResponseDto.storeId)
            startActivity(intent)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}