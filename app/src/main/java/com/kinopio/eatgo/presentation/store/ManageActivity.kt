package com.kinopio.eatgo.presentation.store

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.kinopio.eatgo.R
import com.kinopio.eatgo.RetrofitClient
import com.kinopio.eatgo.data.store.StoreService
import com.kinopio.eatgo.databinding.ActivityManageBinding
import com.kinopio.eatgo.domain.map.StoreHistoryRequestDto
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
            "제목",
            null
        )


        binding.startBtn.setOnClickListener {
            Log.d("store start retrofit", "store retrofit1")

            val retrofit = RetrofitClient.getRetrofit2()
            val storeService = retrofit.create(StoreService::class.java)

            Log.d("store start retrofit", "store retrofit2")

            var storeId = 1
            var address = ""
            var positionX = ""
            var positionY = ""

            var storeHistoryRequestDto: StoreHistoryRequestDto = StoreHistoryRequestDto(
                address = address,
                storeId = storeId,
                positionX = positionX,
                positionY = positionY
            )

            storeService.changeStoreStatusOpen(storeId, storeHistoryRequestDto)
                .enqueue(object : Callback<StoreHistoryRequestDto> {
                    override fun onFailure(call: Call<StoreHistoryRequestDto>, t: Throwable) {
                        Log.d("fail", "실패")
                        Log.d("fail", "$t")
                    }
                    override fun onResponse(
                        call: Call<StoreHistoryRequestDto>,
                        response: Response<StoreHistoryRequestDto>
                    ) {
                        TODO("Not yet implemented")
                        if (response.isSuccessful.not()) {
                            Log.d("retrofit", "retrofit4")
                            return
                        }
                        Log.d("retrofit", "통신 + ${response?.body()}")
                    }
                })
        }

        binding.closeBtn.setOnClickListener {
            Log.d("store close retrofit", "store retrofit1")

            val retrofit = RetrofitClient.getRetrofit2()
            val storeService = retrofit.create(StoreService::class.java)

            Log.d("store close retrofit", "store retrofit2")

            var storeId = 1
            storeService.changeStoreStatusClose(storeId)
                .enqueue(object : Callback<StoreHistoryRequestDto> {
                    override fun onFailure(call: Call<StoreHistoryRequestDto>, t: Throwable) {
                        Log.d("fail", "실패")
                        Log.d("fail", "$t")
                    }
                    override fun onResponse(
                        call: Call<StoreHistoryRequestDto>,
                        response: Response<StoreHistoryRequestDto>
                    ) {
                        TODO("Not yet implemented")
                        if (response.isSuccessful.not()) {
                            Log.d("retrofit", "retrofit4")
                            return
                        }
                        Log.d("retrofit", "통신 + ${response?.body()}")
                    }
                })
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return ToolbarUtils.handleOptionsItemSelected(
            this,
            item
        ) // 분리된 클래스의 handleOptionsItemSelected 함수 호출
    }


}