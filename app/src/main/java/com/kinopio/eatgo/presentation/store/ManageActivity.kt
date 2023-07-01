package com.kinopio.eatgo.presentation.store

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.kinopio.eatgo.R
import com.kinopio.eatgo.databinding.ActivityManageBinding

class ManageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityManageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ToolbarUtils.setupToolbar(this, binding.root.findViewById<Toolbar>(R.id.toolbar),"제목", null)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return ToolbarUtils.handleOptionsItemSelected(this, item) // 분리된 클래스의 handleOptionsItemSelected 함수 호출
    }
}