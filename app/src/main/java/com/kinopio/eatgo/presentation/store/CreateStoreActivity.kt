package com.kinopio.eatgo.presentation.store

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kinopio.eatgo.databinding.ActivityCreateStoreBinding


class CreateStoreActivity : AppCompatActivity() {
    private val binding : ActivityCreateStoreBinding by lazy {
        ActivityCreateStoreBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    }
}