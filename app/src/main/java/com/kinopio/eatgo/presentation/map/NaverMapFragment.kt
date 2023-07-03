package com.kinopio.eatgo.presentation.map

import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.UiThread
import com.kinopio.eatgo.R
import com.kinopio.eatgo.databinding.FragmentNaverMapBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.NaverIdLoginSDK.applicationContext
import java.util.Locale

//// TODO: Rename parameter arguments, choose names that match
//// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NaverMapFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NaverMapFragment : Fragment(), OnMapReadyCallback {
    private lateinit var locationSource: FusedLocationSource
    private lateinit var naverMap: NaverMap
    private lateinit var binding : FragmentNaverMapBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNaverMapBinding.inflate(inflater, container, false)

        val fm = childFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map, it).commit()
            }
        mapFragment.getMapAsync(this)

        locationSource =
            FusedLocationSource(this, NaverMapFragment.LOCATION_PERMISSION_REQUEST_CODE)

        binding.selectPostiton.setOnClickListener {
            requireActivity().findViewById<TextView>(R.id.resultAddress).text = binding.selectKoreaAddress.text
            requireActivity().findViewById<FrameLayout>(R.id.createMapContainer).visibility = View.GONE
            requireActivity().findViewById<LinearLayout>(R.id.createStoreContainer).visibility = View.VISIBLE
            requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()

        }
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

        // 위치를 추적하면서 카메라도 따라 움직인다.
        naverMap.locationTrackingMode = LocationTrackingMode.Follow

        val marker = Marker()
        marker.position = LatLng(
            naverMap.cameraPosition.target.latitude,
            naverMap.cameraPosition.target.longitude
        )
        marker.icon = OverlayImage.fromResource(R.mipmap.marker)
        marker.map = naverMap
        naverMap.addOnCameraChangeListener { reason, animated ->
            Log.i("NaverMap", "카메라 변경 - reson: $reason, animated: $animated")
            marker.position = LatLng(
                // 현재 보이는 네이버맵의 정중앙 가운데로 마커 이동
                naverMap.cameraPosition.target.latitude,
                naverMap.cameraPosition.target.longitude
            )
//            getAddress(naverMap.cameraPosition.target.latitude, naverMap.cameraPosition.target.longitude)
            binding.selectKoreaAddress.text = "이동중..."
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
                    binding.selectKoreaAddress.text = "${address[0].getAddressLine(0)}"
                }
            }
        } else { // API 레벨이 33 미만인 경우
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            if (addresses != null) {
                Log.d("aaa", "${addresses[0].getAddressLine(0)}, ${latitude}, ${longitude}")
                binding.selectKoreaAddress.text = "${addresses[0].getAddressLine(0)}"

            }
        }
    }
//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment NaverMapFragment.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            NaverMapFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
}