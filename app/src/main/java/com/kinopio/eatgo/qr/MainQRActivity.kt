package com.kinopio.eatgo.qr

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.kinopio.eatgo.databinding.ActivityQrBinding

class MainQRActivity : AppCompatActivity() {
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
    }
}