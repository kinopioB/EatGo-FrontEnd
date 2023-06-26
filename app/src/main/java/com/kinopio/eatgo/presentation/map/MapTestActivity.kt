package com.kinopio.eatgo.presentation.map

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import net.daum.mf.map.api.MapView

class MapTestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.kinopio.eatgo.R.layout.activity_map_test)
        val mapView = MapView(this)

        val mapViewContainer = findViewById<View>(com.kinopio.eatgo.R.id.map_view) as ViewGroup
        mapViewContainer.addView(mapView)

    }
}