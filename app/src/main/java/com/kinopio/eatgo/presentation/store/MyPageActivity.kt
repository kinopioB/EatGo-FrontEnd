package com.kinopio.eatgo.presentation.store

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kinopio.eatgo.R
import com.kinopio.eatgo.databinding.ActivityMyPageBinding
import com.kinopio.eatgo.domain.store.ui_model.Review

class MyPageActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var reviewAdapter: ReviewAdapter

    private var reviewList = mutableListOf<Review>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        val binding = ActivityMyPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ToolbarUtils.setupToolbar(this, binding.root.findViewById<Toolbar>(R.id.toolbar),"제목", null)

        recyclerView = findViewById(R.id.myStoreReview)
        recyclerView.layoutManager = LinearLayoutManager(this)
        reviewAdapter = ReviewAdapter(reviewList)

        binding.myStoreReview.apply {
            adapter = reviewAdapter
            layoutManager = LinearLayoutManager(this@MyPageActivity)
        }
        reviewList.add(
            Review("Review 1", "4", "맛있네요 또 오고 싶습니다", "2023-06-02"))
        reviewList.add(
            Review("Review 2", "5", "제 인생 최고의 닭꼬치입니다", "2023-06-22"))
        reviewList.add(
            Review("Review 3", "3", "생각보다 평범한 느낌이였습니다", "2023-07-02"))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return ToolbarUtils.handleOptionsItemSelected(this, item) // 분리된 클래스의 handleOptionsItemSelected 함수 호출
    }
}