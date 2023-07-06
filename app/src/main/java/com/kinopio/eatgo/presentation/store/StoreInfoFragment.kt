package com.kinopio.eatgo.presentation.store

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kinopio.eatgo.R
import com.kinopio.eatgo.User
import com.kinopio.eatgo.domain.store.ReviewDto
import com.kinopio.eatgo.domain.store.StoreHistory
import com.kinopio.eatgo.domain.store.ui_model.Store
import com.kinopio.eatgo.presentation.map.HistoryNaverMapFragment
import com.kinopio.eatgo.presentation.map.StoreMangeNaverMapFragment
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapFragment
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.NaverMapOptions
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource

class StoreInfoFragment : Fragment(), OnMapReadyCallback  {
    lateinit var mapView: MapView
    private lateinit var locationSource: FusedLocationSource
    private lateinit var naverMap: NaverMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(
            R.layout.fragment_store_info,
            container, false
        ) as ViewGroup
        mapView = rootView.findViewById<View>(R.id.testMap) as MapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        return rootView

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 데이터 전달 받기
//        val histories = arguments?.getSerializable(ARG_HISTORY_LIST) as? List<StoreHistory>
//        Log.d("history", "11111 ${histories}")
        val info = arguments?.getSerializable(ARG_INFO) as? String

//        val categoryId = arguments?.getSerializable(ARG_CATEGORY_ID) as? Int

        Log.d("Info", "${info}")

        val infoTxt = view.findViewById<TextView>(R.id.store_info_txt)
        infoTxt.text = info


//        HistoryNaverMapFragment.newInstance(info, histories)

//        val fm = parentFragmentManager
//        val transaction = fm.beginTransaction()
//
//        var mapFragment :HistoryNaverMapFragment = HistoryNaverMapFragment.newInstance(categoryId, histories)
//
//
//        transaction.add(R.id.historyNaverMapContainer, mapFragment)
//        transaction.commit()
    }
    override fun onMapReady(naverMap: NaverMap) {
        val histories = arguments?.getSerializable(ARG_HISTORY_LIST) as? List<StoreHistory>
        val categoryId = arguments?.getSerializable(ARG_CATEGORY_ID) as? Int

        var options : NaverMapOptions

        if (histories?.size!! > 0) {
            options = NaverMapOptions()
                .camera(CameraPosition(LatLng(histories[0].positionX, histories[0].positionY),  10.0))  // 카메라 위치 (위도,경도,줌)
                .mapType(NaverMap.MapType.Basic)    //지도 유형
                .enabledLayerGroups(NaverMap.LAYER_GROUP_BUILDING)

        }
        else {
            options = NaverMapOptions()
                .camera(CameraPosition(LatLng(User.getPositionX()!!, User.getPositionY()!!),  10.0))  // 카메라 위치 (위도,경도,줌)
                .mapType(NaverMap.MapType.Basic)    //지도 유형
                .enabledLayerGroups(NaverMap.LAYER_GROUP_BUILDING)  //빌딩 표시
        }

        Log.d("history", "${User.getPositionX()!!}, ${User.getPositionY()!!}")

        MapFragment.newInstance(options)
        if (histories?.size!! > 0) {
            val cameraUpdate = CameraUpdate.scrollTo(LatLng(histories[0].positionX, histories[0].positionY))
            naverMap.moveCamera(cameraUpdate)
        }
        else {
            val cameraUpdate = CameraUpdate.scrollTo(LatLng(User.getPositionX()!!, User.getPositionY()!!))
            naverMap.moveCamera(cameraUpdate)
        }
//        val marker = Marker()
//        marker.position = LatLng(37.566, 126.978)
//        marker.map = naverMap

//        this.naverMap = naverMap
        // 현재 위치
//        naverMap.locationSource = locationSource
        // 현재 위치 버튼 기능
        naverMap.uiSettings.isLocationButtonEnabled = false
        naverMap.uiSettings.isZoomControlEnabled = false
        naverMap.uiSettings.isLogoClickEnabled = false
        naverMap.uiSettings.isCompassEnabled = false
//
//        // 위치를 추적하면서 카메라도 따라 움직인다.
////        naverMap.locationTrackingMode = LocationTrackingMode.Follow
//
////        if (histories?.size!! > 0) {
////            val cameraUpdate = CameraUpdate.scrollTo(LatLng(histories[0].positionX, histories[0].positionY))
////            naverMap.moveCamera(cameraUpdate)
////        }
//
        Log.d("history", "$histories")
        Log.d("history", "${histories.size}")
        for (i in 0 .. histories?.size!! - 1) {
            val marker = Marker()
            marker.position = LatLng(
                histories[i].positionX,
                histories[i].positionY
            )
            marker.width = 100
            marker.height = 110
            when(categoryId) {
                1 -> marker.icon = OverlayImage.fromResource(R.drawable.yakitori_report)
                2 -> marker.icon = OverlayImage.fromResource(R.drawable.snackbar_report)
                3 -> marker.icon = OverlayImage.fromResource(R.drawable.fishbread_report)
                4 -> marker.icon = OverlayImage.fromResource(R.drawable.sundae_report)
                5 -> marker.icon = OverlayImage.fromResource(R.drawable.takoyaki_report)
                6 -> marker.icon = OverlayImage.fromResource(R.drawable.toast_report)
                7 -> marker.icon = OverlayImage.fromResource(R.drawable.chicken_report)
                8 -> marker.icon = OverlayImage.fromResource(R.drawable.hotdog_report)
                else -> marker.icon = OverlayImage.fromResource(R.drawable.yakitori_report)
            }
            marker.map = naverMap
        }
    }
    companion object {
        private const val ARG_HISTORY_LIST = "history_list"
        private const val ARG_INFO ="store_info"
        private const val ARG_CATEGORY_ID = "category_id"
        fun newInstance(categoryId:Int?, info:String?, historyList: List<StoreHistory>?): StoreInfoFragment {
            val fragment = StoreInfoFragment()
            val args = Bundle()
            args.putSerializable(ARG_HISTORY_LIST, ArrayList(historyList))
            args.putSerializable(ARG_INFO, info)
            args.putSerializable(ARG_CATEGORY_ID, categoryId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}