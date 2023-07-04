package com.kinopio.eatgo

import android.content.Intent
import android.os.Build.VERSION_CODES.O
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.annotation.UiThread
import com.kinopio.eatgo.data.map.StoreLocationService
import com.kinopio.eatgo.databinding.ActivityMainBinding
import com.kinopio.eatgo.domain.map.StoreLocationDto
import com.kinopio.eatgo.presentation.qr.CustomQRScannerActivity
import com.kinopio.eatgo.presentation.qr.ScanQRActivity
import com.kinopio.eatgo.presentation.store.SummaryInfomationFragment
import com.kinopio.eatgo.presentation.templates.NavigationFragment
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.btnCustomScanMain.setOnClickListener {
            Log.d("qr", "커스텀 스캔 클릭1")
            val intent = Intent( this, ScanQRActivity::class.java )
            startActivity(intent)
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
                                        var infomationFragment:SummaryInfomationFragment = SummaryInfomationFragment()
                                        val storeId = markerList[i].id
                                        val bundle = Bundle()
                                        bundle.putInt("storeId", storeId)
                                        infomationFragment.arguments = bundle
                                        transaction.add(R.id.frameLayout, infomationFragment)
                                        transaction.commit()
                                        true
                                    }
                                    marker.position = LatLng(markerList[i].positionX, markerList[i].positionY)
                                    marker.width = 100
                                    marker.height = 110

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
        val transaction = supportFragmentManager.beginTransaction()


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
                            var infomationFragment:SummaryInfomationFragment = SummaryInfomationFragment()
                            val storeId = markerList[i].id
                            val bundle = Bundle()
                            Log.d("fragment", "$storeId")
                            bundle.putInt("storeId", storeId)
                            infomationFragment.arguments = bundle
                            transaction.add(R.id.frameLayout, infomationFragment)
                            transaction.commit()
                            true
                        }
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

    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }
}