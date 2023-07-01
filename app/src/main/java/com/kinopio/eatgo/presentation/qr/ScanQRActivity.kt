package com.kinopio.eatgo.presentation.qr

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import com.kinopio.eatgo.databinding.ActivityScanQrBinding
import com.kinopio.eatgo.presentation.store.ReviewDetailActivity
import com.kinopio.eatgo.presentation.store.ReviewFragment


class ScanQRActivity : AppCompatActivity() {

    private lateinit var txtResult : TextView

    // 스캐너 설정
    private val barcodeLauncher = registerForActivityResult(
        ScanContract()
    ) { result: ScanIntentResult ->
        // result : 스캔된 결과
        // 내용이 없다면
        if (result.contents == null) {
            Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
        }
        else { // 내용이 있다면

            // 1. Toast 메시지 출력.
            // 주석처리
            Toast.makeText(
                this,
                "Scanned: " + result.contents,
                Toast.LENGTH_LONG
            ).show()

            Log.d("review", result.formatName)

            result.contents
            var userId : Int = 0
            var storeId : Int = 0

            Log.d("review", "프레그먼트 실행 전")
            val intent = Intent(this, ReviewDetailActivity::class.java)
            intent.putExtra("userId", userId)
            intent.putExtra("storeId", storeId)
            intent.putExtra("fragmentToOpen", ReviewFragment::class.java.name)
            startActivity(intent)
            Log.d("review", "프레그먼트 실행 후")
            //txtResult.text = result.contents.toString()
        }
    }

    fun onScanButtonClicked() {
        // Launch ( SCAN 실행 )
        // barcodeLauncher.launch(ScanOptions())

        val options = ScanOptions()
        options.setOrientationLocked(false)
        barcodeLauncher.launch(options)
    }

    // 커스텀 스캐너 실행하기
    // Custom SCAN - onClick
    private fun onCustomScanButtonClicked() {
        // Custom Scan Layout -> Activity

        // Intent ? -> 맞는 방법일까 ?
        // val intent = Intent( this, CustomBarcodeScannerActivity::class.java)
        // startActivity(intent)

        // ScanOptions + captureActivity(CustomScannerActivity)
        val options = ScanOptions()
        options.setOrientationLocked(false)
        // options.setCameraId(1)          // 0 : 후면(default), 1 : 전면,
        options.setBeepEnabled(true)
        // options.setTorchEnabled(true)      // true : 실행되자마자 플래시가 켜진다.
        options.setPrompt("커스텀 QR 스캐너 창")
        options.setDesiredBarcodeFormats( ScanOptions.QR_CODE )
        options.captureActivity = CustomQRScannerActivity::class.java
        barcodeLauncher.launch(options)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityScanQrBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val btnCustomScan : Button = binding.btnCustomScan
        // Custom Scan 버튼 클릭
        btnCustomScan.setOnClickListener {
            Log.d("qr", "커스텀 스캔 클릭")
            onCustomScanButtonClicked()
        }
    }
}