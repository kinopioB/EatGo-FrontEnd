package com.kinopio.eatgo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kinopio.eatgo.databinding.ActivityNaverMapBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityNaverMapBinding.inflate(layoutInflater)

        setContentView(binding.root)
    }
}