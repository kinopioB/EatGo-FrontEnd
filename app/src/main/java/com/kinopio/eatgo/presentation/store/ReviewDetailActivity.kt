package com.kinopio.eatgo.presentation.store

import StoreMenuFragment
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.kinopio.eatgo.R
import com.kinopio.eatgo.databinding.ActivityReviewDetailBinding
import com.kinopio.eatgo.domain.store.ui_model.Menu
import com.naver.maps.map.e
import java.io.Serializable


class ReviewDetailActivity : AppCompatActivity() {

    private lateinit var menuList :List<Menu>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityReviewDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

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