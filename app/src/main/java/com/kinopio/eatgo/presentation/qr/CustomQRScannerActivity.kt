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
import com.kinopio.eatgo.MainActivity
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

        // Toolbar 적용
        ToolbarUtils.setupToolbar(this, binding.root.findViewById<Toolbar>(R.id.toolbar),"제목", null)

        // 바코드 코드
        decoratedBarcodeView = binding.decoratedBarcodeView
        btnFlash = binding.decoratedBarcodeView.findViewById(R.id.btnFlash)

        captureManager = CaptureManager( this, decoratedBarcodeView)
        captureManager.initializeFromIntent( intent, savedInstanceState )
        captureManager.setShowMissingCameraPermissionDialog(true,"카메라 권한 요청")	// 권한요청 다이얼로그 보이게 할 지 말 지
        captureManager.decode()		// decoding 시작

        // 플래시 버튼 클릭
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

    // 뒤로가기 동작 버튼
    // 분리된 클래스의 handleOptionsItemSelected 함수 호출
    /*override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return ToolbarUtils.handleOptionsItemSelected(this, item)
    }*/

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            return true
        }
        return super.onOptionsItemSelected(item)
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