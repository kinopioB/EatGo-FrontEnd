package com.kinopio.eatgo.presentation.store

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.kinopio.eatgo.R
import com.kinopio.eatgo.RetrofitClient
import com.kinopio.eatgo.data.store.StoreService
import com.kinopio.eatgo.databinding.ActivityManageBinding
import com.kinopio.eatgo.domain.map.StoreHistoryRequestDto
import com.kinopio.eatgo.domain.templates.ApiResultDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

class ManageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityManageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ToolbarUtils.setupToolbar(
            this,
            binding.root.findViewById<Toolbar>(R.id.toolbar),
            "가게 관리",
            null
        )

        var startStatus = findViewById<TextView>(R.id.startStatus)
        var endStatus = findViewById<TextView>(R.id.endStatus)
        startStatus.visibility = View.GONE
        endStatus.visibility = View.GONE


        binding.startBtn.setOnClickListener {
            Log.d("store start retrofit", "store retrofit1")

            val retrofit = RetrofitClient.getRetrofit2()
            val storeService = retrofit.create(StoreService::class.java)

            Log.d("store start retrofit", "store retrofit2")

            var storeId = 1
            var address = "대한민국 서울특별시 종로구 대명1길 16-2"
            var positionX = "37.58296105306665"
            var positionY = "127.00062394329927"

            var storeHistoryRequestDto: StoreHistoryRequestDto = StoreHistoryRequestDto(
                address = address,
                storeId = storeId,
                positionX = positionX,
                positionY = positionY
            )

            storeService.changeStoreStatusOpen(storeId, storeHistoryRequestDto)
                .enqueue(object : Callback<ApiResultDto> {
                    override fun onFailure(call: Call<ApiResultDto>, t: Throwable) {
                        Log.d("fail", "실패")
                        Log.d("fail", "$t")
                    }
                    override fun onResponse(
                        call: Call<ApiResultDto>,
                        response: Response<ApiResultDto>
                    ) {
                        if (response.isSuccessful.not()) {
                            Log.d("retrofit", "retrofit4")
                            return
                        }
                        Log.d("retrofit", "통신 + ${response?.body()}")
                    }
                })

            endStatus.visibility = View.GONE
            startStatus.visibility = View.VISIBLE

        }

        binding.closeBtn.setOnClickListener {
            Log.d("store close retrofit", "store retrofit1")

            val retrofit = RetrofitClient.getRetrofit2()
            val storeService = retrofit.create(StoreService::class.java)

            Log.d("store close retrofit", "store retrofit2")

            var storeId = 1
            storeService.changeStoreStatusClose(storeId)
                .enqueue(object : Callback<ApiResultDto> {
                    override fun onFailure(call: Call<ApiResultDto>, t: Throwable) {
                        Log.d("fail", "실패")
                        Log.d("fail", "$t")
                    }
                    override fun onResponse(
                        call: Call<ApiResultDto>,
                        response: Response<ApiResultDto>
                    ) {
                        if (response.isSuccessful.not()) {
                            Log.d("retrofit", "retrofit4")
                            return
                        }
                        Log.d("retrofit", "통신 + ${response?.body()}")
                    }
                })
            endStatus.visibility = View.VISIBLE
            startStatus.visibility = View.GONE
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return ToolbarUtils.handleOptionsItemSelected(
            this,
            item
        ) // 분리된 클래스의 handleOptionsItemSelected 함수 호출
    }


}