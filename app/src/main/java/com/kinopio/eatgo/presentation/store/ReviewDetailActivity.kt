package com.kinopio.eatgo.presentation.store

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import com.kinopio.eatgo.R
import com.kinopio.eatgo.databinding.ActivityReviewDetailBinding

class ReviewDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityReviewDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragmentClassName = intent.getStringExtra("fragmentToOpen")

        Log.d("review", " review fragment : $fragmentClassName" )

        if (fragmentClassName != null) {
            try {
                val fragment = Class.forName(fragmentClassName).newInstance() as Fragment
                supportFragmentManager.beginTransaction()
                    .add(R.id.detailReview, fragment)
                    .commit()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}