package com.kinopio.eatgo.presentation.store

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.kinopio.eatgo.MainActivity
import com.kinopio.eatgo.R
import com.kinopio.eatgo.RetrofitClient
import com.kinopio.eatgo.User
import com.kinopio.eatgo.data.store.StoreService
import com.kinopio.eatgo.databinding.ActivityStoreDetailBinding

import com.kinopio.eatgo.domain.store.Menu
import com.kinopio.eatgo.domain.store.ReviewDto
import com.kinopio.eatgo.domain.store.StoreDetailResponseDto
import com.kinopio.eatgo.domain.store.StoreHistory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class StoreDetailActivity : AppCompatActivity() {

    private var menuList :List<Menu> = mutableListOf()
    private var reviewList : List<ReviewDto> = mutableListOf()
    private var historyList : List<StoreHistory> = mutableListOf()
    private var info : String =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityStoreDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val receivedIntent = intent // 현재 Activity의 Intent 가져오기
        val storeId = receivedIntent.getIntExtra("storeId",-1)
       // 카테고리 아이디 넘겨줘야 함
        ToolbarUtils.setupToolbar(
            this,
            binding.root.findViewById<Toolbar>(R.id.toolbar),
            "카테고리",
            null
        )



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

                            // 가게 이름 세팅
                            binding.storeName.text = data.storeName

                            // 가게 리뷰 평점 세팅
                            binding.reviewAverage.text = data.ratingAverage.toString()

                            // 영업일 정보 세팅
                            var openDayInfo =""
                            for (i in 0..data.openInfos.size-1){
                                openDayInfo += (data.openInfos.get(i).day + " : " + data.openInfos.get(i).openTime +" - " + data.openInfos.get(i).closeTime + "\n")
                            }
                            binding.openDay.text = openDayInfo

                            // 영업 위치 세팅
                            binding.address.text = data.address

                            if(data.menus.size !=0){
                                menuList = data.menus
                                Log.d("StoreDetail", "메뉴 사이즈 ${data.menus.size}")
                            }
                            if(data.reviews.size!=0){
                                reviewList = data.reviews
                            }
                            info = data.info
                            historyList = data.storeHistories
                            var categoryId =data.categoryId
                            binding.pager.adapter = StoreDetailTabAdapter(this@StoreDetailActivity, menuList, reviewList, categoryId, info, historyList)
                           // loadingAnimDialog.hide()
                        }
                    }
                })


        }

//        // 탭 설정
//        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
//            override fun onTabSelected(tab: TabLayout.Tab?) {
//                val selectedData = tab?.tag
//                val selectedPosition = tab?.position
//            }
//
//            override fun onTabUnselected(tab: TabLayout.Tab?) {
//                // 탭이 선택되지 않은 상태로 변경 되었을 때
//            }
//
//            override fun onTabReselected(tab: TabLayout.Tab?) {
//                // 이미 선택된 탭이 다시 선택 되었을 때
//            }
//        })

        binding.pager.adapter = StoreDetailTabAdapter(this, menuList, reviewList, 0, info, historyList)

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
        val bundle = Bundle()
        bundle.putInt("storeId", receivedIntent.getIntExtra("storeId", -1))

        bundle.putInt("userId", User.getUserId()!!)


        if(fragmentClassName!= null) {
            try {
                val fragment = ReviewFragment()
                fragment.arguments = bundle
                val transaction = supportFragmentManager.beginTransaction()
                transaction.add(R.id.detailReview, fragment)
                transaction.commit()

//                val fragment = Class.forName("com.kinopio.eatgo.presentation.store.ReviewFragment")
//                    .newInstance() as Fragment
//                supportFragmentManager.beginTransaction()
//                    .add(R.id.detailReview, fragment)
//                    .commit()
            } catch (e: Exception) {
                e.printStackTrace()
            }
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