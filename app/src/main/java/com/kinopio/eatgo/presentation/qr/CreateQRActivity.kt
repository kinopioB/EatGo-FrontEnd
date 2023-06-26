package com.kinopio.eatgo.presentation.qr

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.kinopio.eatgo.R
import com.kinopio.eatgo.databinding.ActivityCreateQrBinding

class CreateQRActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityCreateQrBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {
            val barcodeEncoder = BarcodeEncoder()
            val bitmap = barcodeEncoder.encodeBitmap("https://www.naver.com/", BarcodeFormat.QR_CODE, 400, 400)
            binding.imageViewQrCode.setImageBitmap(bitmap)
        } catch (e: Exception) {
            Log.d("QR", "$e.printStackTrace()")
        }
    }
}