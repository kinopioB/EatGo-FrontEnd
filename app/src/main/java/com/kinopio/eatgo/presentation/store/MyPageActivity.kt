package com.kinopio.eatgo.presentation.store

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import com.kinopio.eatgo.R
import com.kinopio.eatgo.RetrofitClient
import com.kinopio.eatgo.data.store.StoreService
import com.kinopio.eatgo.databinding.ActivityMyPageBinding
import com.kinopio.eatgo.domain.map.StoreMyPageResponseDto
import com.kinopio.eatgo.domain.store.ui_model.Review
import com.kinopio.eatgo.presentation.qr.CreateQRActivity
import com.kinopio.eatgo.presentation.qr.CustomQRScannerActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.properties.Delegates

class MyPageActivity : AppCompatActivity() {
    private lateinit var reviewAdapter: ReviewAdapter
    // private lateinit var reviewList: List<Review>
    private lateinit var storeMyPageResponseDto : StoreMyPageResponseDto

    private val barcodeLauncher = registerForActivityResult(
        ScanContract()
    ) { result: ScanIntentResult ->
        // result : 스캔된 결과
        // 내용이 없다면
        if (result.contents == null) {
            Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
        }
        else { // 내용이 있다면
            // 1. Toast 메시지 출력.
            // 주석처리
            /* Toast.makeText(
                 this,
                 "Scanned: " + result.contents,
                 Toast.LENGTH_LONG
             ).show()*/

            Log.d("review", result.formatName)

            var storeId = result.contents
            var userId = 2
            Log.d("review", "프레그먼트 실행 전")
            val intent = Intent(this, StoreDetailActivity::class.java)
            intent.putExtra("userId", userId)
            intent.putExtra("storeId", storeId)
            intent.putExtra("fragmentToOpen", ReviewFragment::class.java.name)
            startActivity(intent)
            Log.d("review", "프레그먼트 실행 후")
            //txtResult.text = result.contents.toString()
        }
    }

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
                    response.body()?.let {
                        storeMyPageResponseDto = it
                    }
                    reviewAdapter = ReviewAdapter(storeMyPageResponseDto.reviews)
                    binding.myStoreReview.apply {
                        adapter = reviewAdapter
                        this.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    }

                    Glide.with(binding.root)
                        .load(storeMyPageResponseDto.thumbNail)
                        .into(binding.thumbnailId)
                    binding.title.text = storeMyPageResponseDto.storeName
                    binding.categoryName.text = storeMyPageResponseDto.categoryName
                    binding.categoryIcon.text = storeMyPageResponseDto.categoryId.toString()
                    binding.ratingStar.text = storeMyPageResponseDto.ratingAverage.toString()
                    binding.reviewNum.text = storeMyPageResponseDto.reviewNum.toString()
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

        binding.qr.setOnClickListener{
            Log.d("QR", "QR코드 생성")
            val intent = Intent( this, CreateQRActivity::class.java )
            intent.putExtra("storeId", storeId)
            startActivity(intent)
        }
        binding.btnCustomScan.setOnClickListener {
            Log.d("qr", "커스텀 스캔 클릭")
            onCustomScanButtonClicked()
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return ToolbarUtils.handleOptionsItemSelected(
            this,
            item
        ) // 분리된 클래스의 handleOptionsItemSelected 함수 호출
    }

    // 커스텀 스캐너 실행하기
    // Custom SCAN - onClick
    private fun onCustomScanButtonClicked() {
        // Custom Scan Layout -> Activity

        // val intent = Intent( this, CustomBarcodeScannerActivity::class.java)
        // startActivity(intent)

        // ScanOptions + captureActivity(CustomScannerActivity)
        val options = ScanOptions()
        options.setOrientationLocked(false)
        // options.setCameraId(1)          // 0 : 후면(default), 1 : 전면,
        options.setBeepEnabled(true)
        // options.setTorchEnabled(true)      // true : 실행되자마자 플래시가 켜진다.
        options.setPrompt("커스텀 QR 스캐너 창")
        options.setDesiredBarcodeFormats( ScanOptions.QR_CODE )
        options.captureActivity = CustomQRScannerActivity::class.java
        barcodeLauncher.launch(options)
    }
}