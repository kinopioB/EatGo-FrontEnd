package com.kinopio.eatgo.presentation.map

import android.content.Context
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.UiThread
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.analytics.FirebaseAnalytics
import com.kinopio.eatgo.R
import com.kinopio.eatgo.databinding.FragmentNaverMapBinding
import com.kinopio.eatgo.databinding.FragmentStoreMangeNaverMapBinding
import com.kinopio.eatgo.util.OnMapTouchListener
import com.kinopio.eatgo.util.TouchableWrapper
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [StoreMangeNaverMapFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StoreMangeNaverMapFragment : Fragment(R.layout.fragment_store_mange_naver_map) , OnMapReadyCallback{
    private lateinit var binding: FragmentStoreMangeNaverMapBinding
    private lateinit var locationSource: FusedLocationSource
    private lateinit var naverMap: NaverMap

    private var listener: OnMapTouchListener? = null


    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = requireActivity() as? OnMapTouchListener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_store_mange_naver_map, container, false)
        binding.lifecycleOwner = this

        // MapFragment 객체 초기화
        val mapFragment = childFragmentManager.findFragmentById(R.id.fragment_map) as? MapFragment?
        mapFragment?.getMapAsync(this)

        // 지도 전체를 덮어씌우도록 TouchableWrapper를 추가
        val frameLayout = TouchableWrapper(requireActivity(), null, 0, listener)
        frameLayout.setBackgroundColor(ContextCompat.getColor(requireContext(), android.R.color.transparent))
        (binding.root as? ViewGroup)?.addView(
            frameLayout,
            ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        )

//        mapFragment.getMapAsync(this)
        locationSource =
            FusedLocationSource(this, StoreMangeNaverMapFragment.LOCATION_PERMISSION_REQUEST_CODE)

        return binding.root
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


        this.naverMap = naverMap
        // 현재 위치
        naverMap.locationSource = locationSource
        // 현재 위치 버튼 기능
        naverMap.uiSettings.isLocationButtonEnabled = false
        naverMap.uiSettings.isZoomControlEnabled = false
        naverMap.uiSettings.isLogoClickEnabled = false
        naverMap.uiSettings.isCompassEnabled = false



        val marker = Marker()

        arguments?.let {
            val cameraUpdate = CameraUpdate.scrollTo(LatLng(it.getDouble("positionX"), it.getDouble("positionY")))
            naverMap.moveCamera(cameraUpdate)
            marker.position = LatLng(
                it.getDouble("positionX"),
                it.getDouble("positionY")
            )
            getAddress(it.getDouble("positionX"), it.getDouble("positionY"))
        }
        marker.width = 100
        marker.height = 110
        marker.icon = OverlayImage.fromResource(R.drawable.store_location)
        marker.map = naverMap
        naverMap.addOnCameraChangeListener { reason, animated ->

            when (reason) {
                CameraUpdate.REASON_GESTURE -> {
                    marker.position = LatLng(
                        // 현재 보이는 네이버맵의 정중앙 가운데로 마커 이동
                        naverMap.cameraPosition.target.latitude,
                        naverMap.cameraPosition.target.longitude
                    )
                    requireActivity().findViewById<TextView>(R.id.manageAddress).text = "이동중..."
                }
            }
        }

        naverMap.addOnCameraIdleListener {
            marker.position = LatLng(
                naverMap.cameraPosition.target.latitude,
                naverMap.cameraPosition.target.longitude
            )
            // 좌표 -> 주소 변환 텍스트 세팅, 버튼 활성화
            getAddress(naverMap.cameraPosition.target.latitude, naverMap.cameraPosition.target.longitude)
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }

    private fun getAddress(latitude: Double, longitude: Double) {
//        Log.d("aaa", "this")
        // Geocoder 선언
        val geocoder = Geocoder(requireContext().applicationContext, Locale.KOREAN)

        // 안드로이드 API 레벨이 33 이상인 경우
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            geocoder.getFromLocation(
                latitude, longitude, 1
            ) { address ->
                if (address.size != 0) {
                    // 반환 값에서 전체 주소만 사용한다.
                    Log.d("aaa", "'${address[0].getAddressLine(0)}', ${latitude}, ${longitude}")
                    requireActivity().findViewById<TextView>(R.id.manageAddress).text = "${address[0].getAddressLine(0)}"
                }
            }
        } else { // API 레벨이 33 미만인 경우
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            if (addresses != null) {
                Log.d("aaa", "${addresses[0].getAddressLine(0)}, ${latitude}, ${longitude}")
                requireActivity().findViewById<TextView>(R.id.manageAddress).text = "${addresses[0].getAddressLine(0)}"

            }
        }
    }

}