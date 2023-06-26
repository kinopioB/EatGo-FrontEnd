package com.kinopio.eatgo.store

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kinopio.eatgo.databinding.ActivityCreateStoreBinding
import com.kinopio.eatgo.databinding.ActivityNaverMapBinding

class CreateStoreActivity : AppCompatActivity() {
    private val binding : ActivityCreateStoreBinding by lazy {
        ActivityCreateStoreBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    }
}