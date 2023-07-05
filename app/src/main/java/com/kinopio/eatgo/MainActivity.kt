package com.kinopio.eatgo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.UiThread
import com.google.firebase.messaging.FirebaseMessaging
import com.kinopio.eatgo.data.map.StoreLocationService
import com.kinopio.eatgo.databinding.ActivityMainBinding
import com.kinopio.eatgo.domain.map.StoreLocationDto
import com.kinopio.eatgo.presentation.qr.ScanQRActivity
import com.kinopio.eatgo.presentation.store.SummaryInfomationFragment
import com.kinopio.eatgo.presentation.templates.NavigationFragment
import com.kinopio.eatgo.util.setNaverMapMarkerIcon
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
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



class MainActivity : AppCompatActivity() , OnMapReadyCallback {
    private lateinit var locationSource: FusedLocationSource
    private lateinit var naverMap: NaverMap
    private var searchFilter:String = ""
    private val binding : ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private lateinit var markerList : List<StoreLocationDto>;
    var markers = mutableListOf<Marker>()
    private val retrofit = RetrofitClient.getRetrofit()
    private val storeLocationService = retrofit?.create(StoreLocationService::class.java)
    private val TAG = "FirebaseService"


    // 파이어베이스 디바이스에 부여된 토큰값 알아내기

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()

        // 스캔 버튼 클릭
        binding.btnCustomScanMain.setOnClickListener {
            Log.d("qr", "커스텀 스캔 클릭1")
            val intent = Intent( this, ScanQRActivity::class.java )
            startActivity(intent)
        }

        // 파이어베이스 디바이스에 부여된 토큰값 알아내기
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.d(TAG, "토큰 가져오기 실패", task.exception)
            }
            val token = task.result
            Log.d(TAG, "토큰 값 : ${token}")
            // Toast.makeText(baseContext, token, Toast.LENGTH_SHORT).show()
        }

        val fm = supportFragmentManager
        val transaction = fm.beginTransaction()
        var navigationFragment:NavigationFragment = NavigationFragment()

        transaction.add(R.id.bottomBar, navigationFragment)
        transaction.commit()
        val mapFragment = fm.findFragmentById(R.id.map) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map, it).commit()
            }

        mapFragment.getMapAsync(this)

        locationSource =
            FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)


        binding.customCurLocationBtn.setOnClickListener {
            User.setPositionX(naverMap.cameraPosition.target.latitude)
            User.setPositionY(naverMap.cameraPosition.target.longitude)
            naverMap.locationTrackingMode = LocationTrackingMode.Follow
        }

        binding.searchEditText.setOnEditorActionListener(object : TextView.OnEditorActionListener{
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchFilter = binding.searchEditText.text.toString()

                    for (i in 0 .. markers.size - 1) {
                        markers[i].map = null;
                    }
                    markers = mutableListOf<Marker>()

                    storeLocationService?.getFilterStores(searchFilter)?.enqueue(object : Callback<List<StoreLocationDto>> {
                        override fun onFailure(call: Call<List<StoreLocationDto>>, t: Throwable) {
                            Log.d("fail", "실패")
                            Log.d("fail", "$t")
                        }

                        override fun onResponse(
                            call: Call<List<StoreLocationDto>>,
                            response: Response<List<StoreLocationDto>>
                        ) {
                            if(response.isSuccessful.not()){
                                return
                            }
                            response.body()?.let{
                                markerList = it

                                for (i in 0 .. markerList.size - 1) {

                                    val marker = Marker()
                                    marker.setOnClickListener {overlay ->

                                        val cameraUpdate = CameraUpdate.scrollTo(LatLng(markerList[i].positionX, markerList[i].positionY))
                                        naverMap.moveCamera(cameraUpdate)

                                        val transaction = supportFragmentManager.beginTransaction()
                                        var infomationFragment:SummaryInfomationFragment = SummaryInfomationFragment()
                                        val prevFrameLayout = supportFragmentManager.findFragmentById(R.id.MainSummaryframeLayout)
                                        if (prevFrameLayout != null) {
                                            transaction.remove(prevFrameLayout)
                                        }
                                        val storeId = markerList[i].id
                                        val bundle = Bundle()
                                        bundle.putInt("storeId", storeId)
                                        bundle.putDouble("posX", markerList[i].positionX)
                                        bundle.putDouble("posY", markerList[i].positionY)
                                        infomationFragment.arguments = bundle
                                        transaction.add(R.id.MainSummaryframeLayout, infomationFragment)
                                        transaction.commit()
                                        true
                                    }
                                    marker.position = LatLng(markerList[i].positionX, markerList[i].positionY)
                                    marker.width = 100
                                    marker.height = 110

                                    setNaverMapMarkerIcon(markerList[i], marker)
                                    marker.map = naverMap
                                }
                                searchFilter = ""
                            }
                        }
                    })
                    // 현재 위치
                    naverMap.locationSource = locationSource
                    // 현재 위치 버튼 기능
                    naverMap.uiSettings.isLocationButtonEnabled = false
                    naverMap.uiSettings.isZoomControlEnabled = false
                    naverMap.uiSettings.isLogoClickEnabled = false
                    // 위치를 추적하면서 카메라도 따라 움직인다.
                    naverMap.locationTrackingMode = LocationTrackingMode.Follow

                    return true
                }
                return false
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
        val retrofit = RetrofitClient.getRetrofit()

        storeLocationService?.getStores()?.enqueue(object : Callback<List<StoreLocationDto>> {
            override fun onFailure(call: Call<List<StoreLocationDto>>, t: Throwable) {
                Log.d("fail", "실패")
                Log.d("fail", "$t")
            }

            override fun onResponse(
                call: Call<List<StoreLocationDto>>,
                response: Response<List<StoreLocationDto>>
            ) {
                if(response.isSuccessful.not()){
                    return
                }
                response.body()?.let{
                    markerList = it
                    for (i in 0 .. markerList.size - 1) {
                        val marker = Marker()
                        marker.setOnClickListener {overlay ->
                            val cameraUpdate = CameraUpdate.scrollTo(LatLng(markerList[i].positionX, markerList[i].positionY))
                            naverMap.moveCamera(cameraUpdate)
                            val transaction = supportFragmentManager.beginTransaction()
                            val prevFrameLayout = supportFragmentManager.findFragmentById(R.id.MainSummaryframeLayout)
                            if (prevFrameLayout != null) {
                                transaction.remove(prevFrameLayout)
                            }

                            var infomationFragment:SummaryInfomationFragment = SummaryInfomationFragment()
                            val storeId = markerList[i].id
                            val bundle = Bundle()
                            bundle.putInt("storeId", storeId)
                            bundle.putDouble("posX", markerList[i].positionX)
                            bundle.putDouble("posY", markerList[i].positionY)
                            infomationFragment.arguments = bundle
                            transaction.add(R.id.MainSummaryframeLayout, infomationFragment)
                            transaction.commit()
                            true
                        }
                        marker.position = LatLng(markerList[i].positionX, markerList[i].positionY)
                        marker.width = 100
                        marker.height = 110
                        setNaverMapMarkerIcon(markerList[i], marker)

                        markers.add(marker)
                        marker.map = naverMap
                    }

                    searchFilter = ""
                }

            }
        })
        this.naverMap = naverMap
        // 현재 위치
        naverMap.locationSource = locationSource
        // 현재 위치 버튼 기능
        naverMap.uiSettings.isLocationButtonEnabled = false
        naverMap.uiSettings.isZoomControlEnabled = false
        naverMap.uiSettings.isLogoClickEnabled = false
        // 위치를 추적하면서 카메라도 따라 움직인다.
        naverMap.locationTrackingMode = LocationTrackingMode.Follow
        User.setPositionX(naverMap.cameraPosition.target.latitude)
        User.setPositionY(naverMap.cameraPosition.target.longitude)
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }
}