package com.kinopio.eatgo.presentation.qr

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.journeyapps.barcodescanner.CaptureManager
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.kinopio.eatgo.R
import com.kinopio.eatgo.databinding.ActivityCustomBarcodeScannerBinding

class CustomQRScannerActivity : AppCompatActivity() {

    private lateinit var captureManager: CaptureManager
    private lateinit var decoratedBarcodeView: DecoratedBarcodeView
    private lateinit var btnFlash : FloatingActionButton

    private var isFlash : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityCustomBarcodeScannerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 뒤로가기 툴바
/*        val ab = supportActionBar
        ab!!.title = "코드 스캔"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)*/

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar) // 액티비티의 앱바로 툴바를 지정합니다
        val actionBar : ActionBar? = supportActionBar
        actionBar!!.title = "코드 스캔"
        actionBar?.setDisplayHomeAsUpEnabled(true) // 앱바에 뒤로가기 버튼을 추가합니다


        // 바코드 코드
        decoratedBarcodeView = binding.decoratedBarcodeView
        btnFlash = binding.decoratedBarcodeView.findViewById(R.id.btnFlash)

        captureManager = CaptureManager( this, decoratedBarcodeView)
        captureManager.initializeFromIntent( intent, savedInstanceState )
        captureManager.setShowMissingCameraPermissionDialog(true,"카메라 권한 요청")	// 권한요청 다이얼로그 보이게 할 지 말 지
        captureManager.decode()		// decoding 시작

        // 플래시 버튼 클릭 ?
        btnFlash.setOnClickListener {
            Log.d("qr", "qr코드 플래시")
            if (!isFlash) {
                // 플래시가 현재 꺼져있을 때 버튼이 눌렸다면 플래시를 켠다.
                btnFlash.tag = "Flash OFF"
                isFlash = true
                decoratedBarcodeView.setTorchOn()
            } else {
                // 플래시가 현재 켜져있을 때 버튼이 눌렸다면 플래시를 끈다.
                btnFlash.tag = "Flash ON"
                isFlash = false
                decoratedBarcodeView.setTorchOff()
            }
        }
    }

    // 뒤로가기 버튼을 눌렀을 때 동작
    @SuppressLint("NonConstantResourceId")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> { // 툴바의 뒤로가기 버튼이 눌렸을 때 동작합니다
                val intent = Intent(this, ScanQRActivity::class.java)
                startActivity(intent)
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    // LifeCycle 에 따라 CaptureManager 또한 처리해주어야 한다.
    override fun onResume() {
        super.onResume()
        captureManager.onResume()
    }

    override fun onPause() {
        super.onPause()
        captureManager.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        captureManager.onDestroy()
    }
    // onSaveInstanceState ? 또한 처리해주어야 한다.
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        captureManager.onSaveInstanceState(outState)
    }
    // 카메라 권한을 요청할 수 있기 때문에
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        captureManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


}