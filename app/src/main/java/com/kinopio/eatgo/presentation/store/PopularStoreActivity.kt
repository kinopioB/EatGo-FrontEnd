package com.kinopio.eatgo.presentation.store

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kinopio.eatgo.databinding.ActivityPopularStoreBinding
import com.kinopio.eatgo.domain.store.ui_model.Store


class PopularStoreActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPopularStoreBinding

    private val popularStoreList = mutableListOf<Store>()
    private lateinit var popularStoreAdpater: PopularStoreAdapter

    private val todayOpenStoreList = mutableListOf<Store>()
    private lateinit var todayOpenStoreAdapter : TodayOpenStoreAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_popular_store)
        binding = ActivityPopularStoreBinding.inflate(layoutInflater)
        setContentView(binding.root)



        // popular store data
        popularStoreList.add(Store("test","떡볶이", "test",null))
        popularStoreList.add(Store("test","떡볶이","안녕하세요",null))
        popularStoreList.add(Store("test","떡볶이", "test",null))
        popularStoreList.add(Store("test","떡볶이","안녕하세요",null))
        popularStoreList.add(Store("test","떡볶이", "test",null))
        popularStoreList.add(Store("test","떡볶이","안녕하세요",null))
        popularStoreList.add(Store("test","떡볶이", "test",null))
        popularStoreList.add(Store("test","떡볶이","안녕하세요",null))

        todayOpenStoreList.add(Store("test","떡볶이", "test",null))
        todayOpenStoreList.add(Store("test","떡볶이","안녕하세요",null))
        todayOpenStoreList.add(Store("test","떡볶이", "test",null))
        todayOpenStoreList.add(Store("test","떡볶이","안녕하세요",null))
        todayOpenStoreList.add(Store("test","떡볶이", "test",null))
        todayOpenStoreList.add(Store("test","떡볶이","안녕하세요",null))
        todayOpenStoreList.add(Store("test","떡볶이", "test",null))
        todayOpenStoreList.add(Store("test","떡볶이","안녕하세요",null))



        popularStoreAdpater = PopularStoreAdapter(popularStoreList)

        binding.popularRv.apply{
            adapter= popularStoreAdpater
            layoutManager = LinearLayoutManager(this@PopularStoreActivity, RecyclerView.HORIZONTAL, false)
        }

        todayOpenStoreAdapter = TodayOpenStoreAdapter(todayOpenStoreList)

        binding.todayOpenRv.apply{
            adapter= todayOpenStoreAdapter
            layoutManager = LinearLayoutManager(this@PopularStoreActivity)
        }

    }
}