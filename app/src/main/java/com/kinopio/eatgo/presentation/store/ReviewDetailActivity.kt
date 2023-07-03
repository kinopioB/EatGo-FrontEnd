package com.kinopio.eatgo.presentation.store

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.kinopio.eatgo.R
import com.kinopio.eatgo.RetrofitClient
import com.kinopio.eatgo.data.store.StoreService
import com.kinopio.eatgo.databinding.ActivityReviewDetailBinding
import com.kinopio.eatgo.domain.store.CreateStoreResponseDto
import com.kinopio.eatgo.domain.store.Menu
import com.kinopio.eatgo.domain.store.StoreDetailResponseDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ReviewDetailActivity : AppCompatActivity() {

    private var menuList :List<Menu> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityReviewDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val receivedIntent = intent // 현재 Activity의 Intent 가져오기
        val storeId = receivedIntent.getIntExtra("storeId",-1)

        if (storeId != -1) {
            val retrofit = RetrofitClient.getRetrofit2()
            val storeService = retrofit.create(StoreService::class.java)

            storeService.getStoreDetail(storeId)
                .enqueue(object : Callback<StoreDetailResponseDto> {
                    override fun onFailure(call: Call<StoreDetailResponseDto>, t: Throwable) {
                        Log.d("image", "errorororor:) ")
                        Log.d("fail", "$t")
                    }

                    override fun onResponse(
                        call: Call<StoreDetailResponseDto>,
                        response: Response<StoreDetailResponseDto>
                    ) {
                        response.body()?.let {
                            // 응답 성공
                            Log.d("StoreId", "Store Created Success")
                            Log.d("StoreId", "${response.body()}")
                            var data = response.body()!!
                            if(data.menus.size !=0){
                                menuList = data.menus
                            }
                            binding.pager.adapter = StoreDetailTabAdapter(this@ReviewDetailActivity, menuList)

                        }
                    }
                })


        }

        // 탭 설정
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val selectedData = tab?.tag
                val selectedPosition = tab?.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // 탭이 선택되지 않은 상태로 변경 되었을 때
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // 이미 선택된 탭이 다시 선택 되었을 때
            }
        })


        menuList = listOf(
            Menu("Menu 1", 10,10,"test","test"),
            Menu("Menu 2", 10,10,"test","test"),
            Menu("Menu 3", 10,10,"test","test"),
        )
        binding.pager.adapter = StoreDetailTabAdapter(this, menuList)

        /* 탭과 뷰페이저를 연결, 여기서 새로운 탭을 다시 만드므로 레이아웃에서 꾸미지말고 여기서 꾸며야함
        * 여기서 데이터 세팅 */
        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "가게 메뉴"
                    val data ="test"
                    tab.tag = data
                }
                1 -> tab.text = "가게 정보"
                2 -> tab.text = "가게 리뷰"
            }
        }.attach()

        // 리뷰 창
        val fragmentClassName = intent.getStringExtra("fragmentToOpen")

        Log.d("review", " review fragment : $fragmentClassName" )

            try {
                val fragment = Class.forName("com.kinopio.eatgo.presentation.store.ReviewFragment").newInstance() as Fragment
                supportFragmentManager.beginTransaction()
                    .add(R.id.detailReview, fragment)
                    .commit()
            } catch (e: Exception) {
                e.printStackTrace()
            }

    }
}