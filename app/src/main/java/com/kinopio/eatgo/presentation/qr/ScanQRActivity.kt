package com.kinopio.eatgo.presentation.qr

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import com.kinopio.eatgo.R
import com.kinopio.eatgo.databinding.ActivityScanQrBinding
import com.kinopio.eatgo.presentation.store.StoreDetailActivity
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
        else {
            Log.d("review", result.formatName)

            var storeId = result.contents
            var userId = 2
            Log.d("review", "프레그먼트 실행 전")
            // Mypage 로 넘겨주기
            val intent = Intent(this, StoreDetailActivity::class.java)
            intent.putExtra("userId", userId)
            intent.putExtra("storeId", storeId)
            intent.putExtra("fragmentToOpen", ReviewFragment::class.java.name)
            startActivity(intent)
            Log.d("review", "프레그먼트 실행 후")
        }
    }

    fun onScanButtonClicked() {
        val options = ScanOptions()
        options.setOrientationLocked(false)
        barcodeLauncher.launch(options)
    }

    // 커스텀 스캐너 실행하기
    // Custom SCAN - onClick
    private fun onCustomScanButtonClicked() {
        // Custom Scan Layout -> Activity

        Log.d("qr", "커스텀 스캔 클릭3")

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
        Log.d("qr", "커스텀 스캔 클릭2")

        onCustomScanButtonClicked()

        super.onCreate(savedInstanceState)
       /* val binding = ActivityScanQrBinding.inflate(layoutInflater)
        setContentView(binding.root)*/

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return ToolbarUtils.handleOptionsItemSelected(this, item) // 분리된 클래스의 handleOptionsItemSelected 함수 호출
    }
}