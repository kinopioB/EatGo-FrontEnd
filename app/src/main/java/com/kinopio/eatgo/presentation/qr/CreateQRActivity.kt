package com.kinopio.eatgo.presentation.qr

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.kinopio.eatgo.R
import com.kinopio.eatgo.databinding.ActivityCreateQrBinding

class CreateQRActivity : AppCompatActivity() {
    private lateinit var storeId : Integer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityCreateQrBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbarBodyTemplate = binding.toolbar
        setSupportActionBar(toolbarBodyTemplate)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼 활성화 (화살표)
        supportActionBar?.setDisplayShowTitleEnabled(false) //액션바에 표시되는 제목의 표시유무를 설정합니다. false로 해야 custom한 툴바의 이름이 화면에 보이게 됩니다.
        // 유저 아이디 값은 세션에서 가져오기
        // 스토어 아이디

        try {
            val barcodeEncoder = BarcodeEncoder()
            val bitmap = barcodeEncoder.encodeBitmap("https://www.naver.com/", BarcodeFormat.QR_CODE, 400, 400)
            binding.imageViewQrCode.setImageBitmap(bitmap)
        } catch (e: Exception) {
            Log.d("QR", "$e.printStackTrace()")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(
            R.menu.template_toolbar_menu,
            menu
        )       // main_menu 메뉴를 toolbar 메뉴 버튼으로 설정
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> { //뒤로 가기 버튼
                finish()
            }
            /*R.id.toolbar_info -> {// 툴팁
                val view = findViewById<View>(R.id.toolbar_info) //툴팁을 띄우기 위해서는 view가 필요함
                balloon.showAlignBottom(view)
            }*/
        }
        return super.onOptionsItemSelected(item)
    }
}