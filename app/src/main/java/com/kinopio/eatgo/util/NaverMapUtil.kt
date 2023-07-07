package com.kinopio.eatgo.util

import com.kinopio.eatgo.R
import com.kinopio.eatgo.domain.map.StoreLocationDto
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage

fun setNaverMapMarkerIcon(mL : StoreLocationDto, marker:Marker) {
    when(mL.createdType) {
        0 -> when(mL.categoryId) {
            1 -> marker.icon = OverlayImage.fromResource(R.drawable.sweet_potato_report)
            2 -> marker.icon = OverlayImage.fromResource(R.drawable.takoyaki_report)
            3 -> marker.icon = OverlayImage.fromResource(R.drawable.snackbar_report)
            4 -> marker.icon = OverlayImage.fromResource(R.drawable.fishbread_report)
            5 -> marker.icon = OverlayImage.fromResource(R.drawable.takoyaki_report)
            6 -> marker.icon = OverlayImage.fromResource(R.drawable.toast_report)
            else -> marker.icon = OverlayImage.fromResource(R.drawable.yakitori_report)
        }
        1 -> when(mL.isOpen) {
            0 -> when(mL.categoryId) {
                1 -> marker.icon = OverlayImage.fromResource(R.drawable.sweet_potato_close)
                2 -> marker.icon = OverlayImage.fromResource(R.drawable.takoyaki_close)
                3 -> marker.icon = OverlayImage.fromResource(R.drawable.snackbar_close)
                4 -> marker.icon = OverlayImage.fromResource(R.drawable.fishbread_close)
                5 -> marker.icon = OverlayImage.fromResource(R.drawable.takoyaki_close)
                6 -> marker.icon = OverlayImage.fromResource(R.drawable.toast_close)
                else -> marker.icon = OverlayImage.fromResource(R.drawable.yakitori_close)
            }
            1 -> when(mL.categoryId) {
                1 -> marker.icon = OverlayImage.fromResource(R.drawable.sweet_potato_open)
                2 -> marker.icon = OverlayImage.fromResource(R.drawable.yakitori_open)
                3 -> marker.icon = OverlayImage.fromResource(R.drawable.snackbar_open)
                4 -> marker.icon = OverlayImage.fromResource(R.drawable.fishbread_open)
                5 -> marker.icon = OverlayImage.fromResource(R.drawable.takoyaki_open)
                6 -> marker.icon = OverlayImage.fromResource(R.drawable.toast_open)
                else -> marker.icon = OverlayImage.fromResource(R.drawable.yakitori_open)
            }
        }
    }
}

fun calDist(lat1:Double, lon1:Double, lat2:Double, lon2:Double) : Long{
    val EARTH_R = 6371000.0
    val rad = Math.PI / 180
    val radLat1 = rad * lat1
    val radLat2 = rad * lat2
    val radDist = rad * (lon1 - lon2)

    var distance = Math.sin(radLat1) * Math.sin(radLat2)
    distance = distance + Math.cos(radLat1) * Math.cos(radLat2) * Math.cos(radDist)
    val ret = EARTH_R * Math.acos(distance)

    return Math.round(ret) // 미터 단위
}