package com.kinopio.eatgo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.UiThread
import com.kinopio.eatgo.data.map.StoreLocationService
import com.kinopio.eatgo.databinding.ActivityMainBinding
import com.kinopio.eatgo.domain.map.StoreLocationDto
import com.kinopio.eatgo.domain.map.StoreLocationListDto
import com.kinopio.eatgo.databinding.ActivityNaverMapBinding
import com.kinopio.eatgo.presentation.map.NaverMapActivity
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.naver.maps.map.widget.LocationButtonView
import java.util.Locale


class MainActivity : AppCompatActivity() , OnMapReadyCallback {
    private lateinit var locationSource: FusedLocationSource
    private lateinit var naverMap: NaverMap
    private val binding : ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private lateinit var markerList : List<StoreLocationDto>;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map, it).commit()
            }

        mapFragment.getMapAsync(this)

        locationSource =
            FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)

        binding.customCurLocationBtn.setOnClickListener {
            naverMap.locationTrackingMode = LocationTrackingMode.Follow
        }
        val retrofit = RetrofitClient.getRetrofit()
        val storeLocationService = retrofit?.create(StoreLocationService::class.java)

        storeLocationService?.getStores()?.enqueue(object : Callback<StoreLocationListDto> {
            override fun onFailure(call: Call<StoreLocationListDto>, t: Throwable) {
                Log.d("fail", "실패")
                Log.d("fail", "$t")
            }

            override fun onResponse(
                call: Call<StoreLocationListDto>,
                response: Response<StoreLocationListDto>
            ) {
                if(response.isSuccessful.not()){
                    return
                }
                response.body()?.let{
                    markerList = it.res
                }
            }
        })
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        if (locationSource.onRequestPermissionsResult(requestCode, permissions,
                grantResults)) {
            if (!locationSource.isActivated) { // 권한 거부됨
                naverMap.locationTrackingMode = LocationTrackingMode.None
            }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    @UiThread
    override fun onMapReady(naverMap: NaverMap) {

        for (i in 0 .. markerList.size - 1) {
            val marker = Marker()

            marker.position = LatLng(markerList[i].positionX, markerList[i].positionY)
            marker.width = 100
            marker.height = 110
//            marker.icon = OverlayImage.fromResource(R.mipmap.yakitori)

            when(markerList[i].categoryId) {
                1 -> marker.icon = OverlayImage.fromResource(R.drawable.yakitori_open)
                2 -> marker.icon = OverlayImage.fromResource(R.drawable.snackbar_open)
                3 -> marker.icon = OverlayImage.fromResource(R.drawable.fishbread_open)
                4 -> marker.icon = OverlayImage.fromResource(R.drawable.sundae_open)
                5 -> marker.icon = OverlayImage.fromResource(R.drawable.takoyaki_open)
                6 -> marker.icon = OverlayImage.fromResource(R.drawable.toast_open)
                7 -> marker.icon = OverlayImage.fromResource(R.drawable.chicken_open)
                8 -> marker.icon = OverlayImage.fromResource(R.drawable.hotdog_open)
                else -> marker.icon = OverlayImage.fromResource(R.drawable.yakitori_open)
            }

            marker.map = naverMap
        }

        this.naverMap = naverMap
        // 현재 위치
        naverMap.locationSource = locationSource
        // 현재 위치 버튼 기능
        naverMap.uiSettings.isLocationButtonEnabled = false
        naverMap.uiSettings.isZoomControlEnabled = false
        naverMap.uiSettings.isLogoClickEnabled = false
        // 위치를 추적하면서 카메라도 따라 움직인다.
        naverMap.locationTrackingMode = LocationTrackingMode.Follow
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }
}