package com.kinopio.eatgo.store

import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.UiThread
import androidx.appcompat.app.AppCompatActivity
import com.kinopio.eatgo.R
import com.kinopio.eatgo.databinding.ActivityNaverMapBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import java.util.Locale

//class NaverMapActivity : AppCompatActivity(), OnMapReadyCallback {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        val binding = ActivityNaverMapBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as MapFragment?
//            ?: MapFragment.newInstance().also {
//                supportFragmentManager.beginTransaction().add(R.id.map, it).commit()
//            }
//        mapFragment.getMapAsync(this)
//    }
//    override fun onMapReady(naverMap: NaverMap) {
//        Toast.makeText(this, "지도 준비 완료", Toast.LENGTH_SHORT).show()
//    }
//}

class NaverMapActivity : AppCompatActivity() , OnMapReadyCallback{
    private lateinit var locationSource: FusedLocationSource
    private lateinit var naverMap: NaverMap
    private val binding : ActivityNaverMapBinding by lazy {
        ActivityNaverMapBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val binding = ActivityNaverMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map, it).commit()
            }

        mapFragment.getMapAsync(this)

        locationSource =
            FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
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
        var markerList = arrayOf(doubleArrayOf(37.58360643235775, 127.00287162295791), doubleArrayOf(37.57634704780042, 127.00014662083021), doubleArrayOf(37.57437921990977, 127.00597769025228), doubleArrayOf(37.57545705952983, 127.00408207401604))

        for (i in 0 .. 3) {
            val marker = Marker()
            marker.position = LatLng(markerList[i][0], markerList[i][1])
            marker.width = 50
            marker.height = 70
            marker.map = naverMap
        }

        this.naverMap = naverMap
        // 현재 위치
        naverMap.locationSource = locationSource
        // 현재 위치 버튼 기능
        naverMap.uiSettings.isLocationButtonEnabled = true
        naverMap.uiSettings.isZoomControlEnabled = false
        naverMap.uiSettings.isLogoClickEnabled = false
        // 위치를 추적하면서 카메라도 따라 움직인다.
        naverMap.locationTrackingMode = LocationTrackingMode.Follow

//        naverMap.addOnLocationChangeListener { location ->
//            Toast.makeText(this, "${location.latitude}, ${location.longitude}",
//                Toast.LENGTH_SHORT).show()
//        }
        // 빨간색 표시 마커 (네이버맵 현재 가운데에 항상 위치)

        val marker = Marker()
        marker.icon = OverlayImage.fromResource(R.mipmap.marker)
        naverMap.addOnLocationChangeListener { location ->
            val cameraPosition: CameraPosition = naverMap.cameraPosition
//            val cameraUpdate = CameraUpdate.toCameraPosition(cameraPosition)
//            naverMap.moveCamera(cameraUpdate)
//            val cameraUpdate = CameraUpdate.scrollTo(LatLng(location.latitude, location.longitude))
//            naverMap.moveCamera(cameraUpdate)
            marker.position = LatLng(
                cameraPosition.target.latitude,
                cameraPosition.target.longitude
            )

            marker.map = naverMap
//            Log.d("hong", "$cameraPosition")
            getAddress(cameraPosition.target.latitude, cameraPosition.target.longitude)
        }


    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }

    private fun getAddress(latitude: Double, longitude: Double) {
//        Log.d("aaa", "this")
        // Geocoder 선언
        val geocoder = Geocoder(applicationContext, Locale.KOREAN)

        // 안드로이드 API 레벨이 33 이상인 경우
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            geocoder.getFromLocation(
                latitude, longitude, 1
            ) { address ->
                if (address.size != 0) {
                    // 반환 값에서 전체 주소만 사용한다.
                    // getAddressLine(0)
//                    toast(address[0].getAddressLine(0))
//                    Log.d("hong", "${address[0].getAddressLine(0)}")
                    binding.selectKoreaAddress.text = "${address[0].getAddressLine(0)}"
//                    binding.selectKoreaAddress.width = "wrap_content"
                }
            }
        } else { // API 레벨이 33 미만인 경우
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            if (addresses != null) {
//                toast(addresses[0].getAddressLine(0))
//                Log.d("hong", "${addresses[0].getAddressLine(0)}")
                binding.selectKoreaAddress.text = "${addresses[0].getAddressLine(0)}"

            }
        }
    }
}