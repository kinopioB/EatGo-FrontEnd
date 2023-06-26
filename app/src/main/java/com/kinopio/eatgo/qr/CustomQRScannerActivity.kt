package com.kinopio.eatgo.qr

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.journeyapps.barcodescanner.CaptureManager
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.kinopio.eatgo.R
import com.kinopio.eatgo.databinding.ActivityCustomBarcodeScannerBinding

class CustomQRScannerActivity : AppCompatActivity() {

    private lateinit var captureManager: CaptureManager
    private lateinit var decoratedBarcodeView: DecoratedBarcodeView
    private lateinit var btnFlash : Button

    private var isFlash : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityCustomBarcodeScannerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        decoratedBarcodeView = binding.decoratedBarcodeView
        btnFlash = binding.btnFlash

        captureManager = CaptureManager(this, decoratedBarcodeView)
        captureManager.initializeFromIntent(intent, savedInstanceState)
        captureManager.setShowMissingCameraPermissionDialog(true, "카메라 권한 요청")
        captureManager.decode()

        


    }
}