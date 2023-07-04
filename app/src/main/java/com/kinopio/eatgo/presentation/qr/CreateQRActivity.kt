package com.kinopio.eatgo.presentation.qr

import ToolbarUtils
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import androidx.viewbinding.ViewBindings
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.kinopio.eatgo.R
import com.kinopio.eatgo.databinding.ActivityCreateQrBinding

class CreateQRActivity : AppCompatActivity() {
    private lateinit var storeId: Integer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityCreateQrBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("toolbar", "toolbar1")
        ToolbarUtils.setupToolbar(this, binding.root.findViewById<Toolbar>(R.id.toolbar),"QR스캔", null)

        // 유저 아이디, 스토어 아이디 가져오기

        var storeId = intent.getIntExtra("storeId", 0)

        try {
            val barcodeEncoder = BarcodeEncoder()
            val bitmap = barcodeEncoder.encodeBitmap(
                storeId.toString(),
                BarcodeFormat.QR_CODE,
                400,
                400
            )
            binding.imageViewQrCode.setImageBitmap(bitmap)
        } catch (e: Exception) {
            Log.d("QR", "$e.printStackTrace()")
        }

        Log.d("toolbar", "toolbar2")
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return ToolbarUtils.handleOptionsItemSelected(this, item) // 분리된 클래스의 handleOptionsItemSelected 함수 호출
    }
}