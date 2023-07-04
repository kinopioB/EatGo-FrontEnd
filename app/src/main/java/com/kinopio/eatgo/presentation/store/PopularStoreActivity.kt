package com.kinopio.eatgo.presentation.store

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kinopio.eatgo.MainActivity
import com.kinopio.eatgo.R
import com.kinopio.eatgo.RetrofitClient
import com.kinopio.eatgo.User
import com.kinopio.eatgo.data.map.LoginService
import com.kinopio.eatgo.data.store.StoreService
import com.kinopio.eatgo.databinding.ActivityPopularStoreBinding
import com.kinopio.eatgo.domain.map.LoginResponseDto
import com.kinopio.eatgo.domain.store.PopularStoreResponseDto
import com.kinopio.eatgo.domain.store.TodayOpenStoreResponseDto
import com.kinopio.eatgo.domain.store.ui_model.Store
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PopularStoreActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPopularStoreBinding

    private  var popularStoreList : MutableList<PopularStoreResponseDto> = mutableListOf()
    private lateinit var popularStoreAdpater: PopularStoreAdapter

    private var todayOpenStoreList = mutableListOf<TodayOpenStoreResponseDto>()
    private lateinit var todayOpenStoreAdapter : TodayOpenStoreAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_popular_store)
        binding = ActivityPopularStoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ToolbarUtils.setupToolbar(
            this,
            binding.root.findViewById<Toolbar>(R.id.toolbar),
            "인기 푸드 트럭",
            null
        )

        // retrofit 연결
        val retrofit = RetrofitClient.getRetrofit2()
        val storeService = retrofit.create(StoreService::class.java)

        // 인기 푸드 트럭 데이터 추출
        storeService.getPopularStore().enqueue(object : Callback<List<PopularStoreResponseDto>> {
            override fun onFailure(call: Call<List<PopularStoreResponseDto>>, t: Throwable) {
                Log.d("fail", "인기 푸드트럭 가져오기 실패 !:) ")
                Log.d("fail", "$t")
            }
            override fun onResponse(call: Call<List<PopularStoreResponseDto>>, response: Response<List<PopularStoreResponseDto>>) {
                response.body()?.let {
                    popularStoreList = it.toMutableList()
                    popularStoreAdpater = PopularStoreAdapter(popularStoreList)

                    binding.popularRv.apply{
                        adapter= popularStoreAdpater
                        layoutManager = LinearLayoutManager(this@PopularStoreActivity, RecyclerView.HORIZONTAL, false)
                    }
                }
            }
        })

        // 새로 오픈한 푸드 트럭 가져오기
        storeService.getTodayOpenStores().enqueue(object : Callback<List<TodayOpenStoreResponseDto>> {
            override fun onFailure(call: Call<List<TodayOpenStoreResponseDto>>, t: Throwable) {
                Log.d("fail", "인기 푸드트럭 가져오기 실패 !:) ")
                Log.d("fail", "$t")
            }
            override fun onResponse(call: Call<List<TodayOpenStoreResponseDto>>, response: Response<List<TodayOpenStoreResponseDto>>) {
                response.body()?.let {
                    todayOpenStoreList = it.toMutableList()
                    todayOpenStoreAdapter = TodayOpenStoreAdapter(todayOpenStoreList)

                    binding.todayOpenRv.apply{
                        adapter= todayOpenStoreAdapter
                        layoutManager = LinearLayoutManager(this@PopularStoreActivity)
                    }
                }
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return ToolbarUtils.handleOptionsItemSelected(
            this,
            item
        )
    }
}