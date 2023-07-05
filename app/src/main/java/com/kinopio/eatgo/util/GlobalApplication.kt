package com.kinopio.eatgo.util

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.kinopio.eatgo.R

class GlobalApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(this, this.getString(R.string.kakao_native_key))
    }
}