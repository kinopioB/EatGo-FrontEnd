package com.kinopio.eatgo.presentation.qr

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.kinopio.eatgo.R
import com.kinopio.eatgo.databinding.ActivityQrBinding
import com.kinopio.eatgo.presentation.store.ReviewFragment

class MainQRActivity : AppCompatActivity() {

    private lateinit var binding : ActivityQrBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityQrBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCreate.setOnClickListener{
            Log.d("QR", "QR코드 생성")
            val intent = Intent( this, CreateQRActivity::class.java )
            startActivity(intent)
        }

        binding.btnScan.setOnClickListener{
            Log.d("QR", "QR코드 인식")
            val intent = Intent( this, ScanQRActivity::class.java )
            startActivity(intent)
        }
        binding.btnReview.setOnClickListener {
            ReviewFragment().show(
                supportFragmentManager, "Review"
            )
        }
    }
}