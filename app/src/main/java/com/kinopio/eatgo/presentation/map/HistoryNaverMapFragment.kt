package com.kinopio.eatgo.presentation.map

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.UiThread
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.kinopio.eatgo.R
import com.kinopio.eatgo.databinding.FragmentHistoryNaverMapBinding
import com.kinopio.eatgo.databinding.FragmentStoreMangeNaverMapBinding
import com.kinopio.eatgo.domain.store.StoreHistory
import com.kinopio.eatgo.presentation.store.StoreInfoFragment
import com.kinopio.eatgo.util.OnMapTouchListener
import com.kinopio.eatgo.util.TouchableWrapper
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource


class HistoryNaverMapFragment : Fragment(R.layout.fragment_history_naver_map), OnMapReadyCallback {
    private lateinit var binding: FragmentHistoryNaverMapBinding
    private lateinit var locationSource: FusedLocationSource
    private lateinit var naverMap: NaverMap
    private var listener: OnMapTouchListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = requireActivity() as? OnMapTouchListener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_history_naver_map, container, false)
        binding.lifecycleOwner = this

        val mapFragment = childFragmentManager.findFragmentById(R.id.historyNaverMap)  as? MapFragment?
        mapFragment?.getMapAsync(this)

        // 지도 전체를 덮어씌우도록 TouchableWrapper를 추가
        val frameLayout = TouchableWrapper(requireActivity(), null, 0, listener)
        frameLayout.setBackgroundColor(ContextCompat.getColor(requireContext(), android.R.color.transparent))
        (binding.root as? ViewGroup)?.addView(
            frameLayout,
            ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        )

        locationSource =
            FusedLocationSource(this, HistoryNaverMapFragment.LOCATION_PERMISSION_REQUEST_CODE)

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

        val histories = arguments?.getSerializable(ARG_HISTORY_LIST) as? List<StoreHistory>

        Log.d("history", "${histories}")

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

        marker.width = 100
        marker.height = 110
        marker.icon = OverlayImage.fromResource(R.drawable.store_location)
        marker.map = naverMap

    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000

        private const val ARG_HISTORY_LIST = "history_list"

        fun newInstance(historyList: List<StoreHistory>?): HistoryNaverMapFragment {
            val fragment = HistoryNaverMapFragment()
            val args = Bundle()
            args.putSerializable(ARG_HISTORY_LIST, ArrayList(historyList))
            fragment.arguments = args
            return fragment
        }
    }


}