package com.kinopio.eatgo.presentation.naverlogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.common.model.AuthErrorCause.*
import com.kakao.sdk.common.util.Utility
import com.kinopio.eatgo.MainActivity
import com.kinopio.eatgo.R

class KakaoLoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kakao_login)
        UserApiClient.instance.unlink { error ->
                    if (error != null) {
                        Log.d("카카오로그인","회원 탈퇴 실패")
                    }else {
                        Log.d("카카오로그인","회원 탈퇴 성공")
                    }
                }
        // 로그인 정보 확인
//        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
//            if (error != null) {
//                Toast.makeText(this, "토큰 정보 보기 실패", Toast.LENGTH_SHORT).show()
//            }
//            else if (tokenInfo != null) {
//                Toast.makeText(this, "토큰 정보 보기 성공", Toast.LENGTH_SHORT).show()
//                Log.d("kakao", "$tokenInfo")
////                Log.d("kakao","로그인에 성공하였습니다.\n${token.accessToken}")
//                UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
//                    UserApiClient.instance.me { user, error ->
//                        Log.d("kakao", "닉네임: ${user?.kakaoAccount?.profile?.nickname}")
//                        Log.d("kakao", "이메일: ${user?.kakaoAccount?.email}")
//                    }
//                }
//                val intent = Intent(this, MainActivity::class.java)
//                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
//                UserApiClient.instance.logout { error ->
//                    if (error != null) {
//                        Log.d("kakao","카카오 로그아웃 실패")
//                    }else {
//                        Log.d("kakao","카카오 로그아웃 성공!")
//                    }
//                }
//                UserApiClient.instance.unlink { error ->
//                    if (error != null) {
//                        Log.d("카카오로그인","회원 탈퇴 실패")
//                    }else {
//                        Log.d("카카오로그인","회원 탈퇴 성공")
//                    }
//                }
//                finish()
//            }
//        }



//        val keyHash = Utility.getKeyHash(this)
//        Log.d("Hash", "${keyHash}")

        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->

            UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
                UserApiClient.instance.me { user, error ->
                    Log.d("kakao", "닉네임: ${user?.kakaoAccount?.profile?.nickname}")
                }
            }

            UserApiClient.instance.me { user, error ->
                Log.d("kakao", "닉네임: ${user?.kakaoAccount?.profile?.nickname}")
                Log.d("kakao", "이메일: ${user?.kakaoAccount?.email}")

                if (user?.kakaoAccount?.email == null) {
                    UserApiClient.instance.unlink { error ->
                        if (error != null) {
                            Log.d("kakao","회원 탈퇴 실패")
                        }else {
                            Log.d("kakao","회원 탈퇴 성공")
                        }
                    }
                    Log.d("kakao", "1111111")
                    Toast.makeText(this, "이메일 동의 필수입니다.", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, KakaoLoginActivity::class.java)
                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))

                }
            }
            if (error != null) {
                when {
                    error.toString() == AccessDenied.toString() -> {
                        Toast.makeText(this, "접근이 거부 됨(동의 취소)", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == InvalidClient.toString() -> {
                        Toast.makeText(this, "유효하지 않은 앱", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == InvalidGrant.toString() -> {
                        Toast.makeText(this, "인증 수단이 유효하지 않아 인증할 수 없는 상태", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == InvalidRequest.toString() -> {
                        Toast.makeText(this, "요청 파라미터 오류", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == InvalidScope.toString() -> {
                        Toast.makeText(this, "유효하지 않은 scope ID", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == Misconfigured.toString() -> {
                        Toast.makeText(this, "설정이 올바르지 않음(android key hash)", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == ServerError.toString() -> {
                        Toast.makeText(this, "서버 내부 에러", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == Unauthorized.toString() -> {
                        Toast.makeText(this, "앱이 요청 권한이 없음", Toast.LENGTH_SHORT).show()
                    }
                    else -> { // Unknown
                        Toast.makeText(this, "기타 에러", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            else if (token != null) {
                Toast.makeText(this, "로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show()
                Log.d("kakao","로그인에 성공하였습니다.\n${token.accessToken}")
                Log.d("kakao","로그인에 성공하였습니다.\n${token}")
                UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
                    UserApiClient.instance.me { user, error ->
                        Log.d("kakao", "닉네임: ${user?.kakaoAccount?.profile?.nickname}")
                    }
                }
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                finish()
            }
        }


        val kakao_login_button = findViewById<ImageButton>(R.id.kakao_login_button) // 로그인 버튼

        kakao_login_button.setOnClickListener {
            if(UserApiClient.instance.isKakaoTalkLoginAvailable(this)){
                UserApiClient.instance.loginWithKakaoTalk(this, callback = callback)
//                UserApiClient.instance.me { user, error ->
//                    Log.d("kakao", "닉네임: ${user?.kakaoAccount?.profile?.nickname}")
//                    Log.d("kakao", "이메일: ${user?.kakaoAccount?.email}")
//
//                    if (user?.kakaoAccount?.email == null) {
//                        UserApiClient.instance.unlink { error ->
//                            if (error != null) {
//                                Log.d("카카오로그인","회원 탈퇴 실패")
//                            }else {
//                                Log.d("카카오로그인","회원 탈퇴 성공")
//                            }
//                        }
//                        Toast.makeText(this, "이메일 동의 필수입니다.", Toast.LENGTH_SHORT).show()
//                        return@me
//                    }
//                }
//                UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
//                    UserApiClient.instance.me { user, error ->
//                        Log.d("kakao", "닉네임: ${user?.kakaoAccount?.profile?.nickname}")
//                        Log.d("kakao", "이메일: ${user?.kakaoAccount?.email}")
//
//                        if (user?.kakaoAccount?.email == null) {
//                            UserApiClient.instance.unlink { error ->
//                                if (error != null) {
//                                    Log.d("카카오로그인","회원 탈퇴 실패")
//                                }else {
//                                    Log.d("카카오로그인","회원 탈퇴 성공")
//                                }
//                            }
//                            Toast.makeText(this, "이메일 동의 필수입니다.", Toast.LENGTH_SHORT).show()
//                            return@me
//                        }
//                    }
//                }
                
            }else{
                UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)

//                UserApiClient.instance.me { user, error ->
//                    Log.d("kakao", "닉네임: ${user?.kakaoAccount?.profile?.nickname}")
//                    Log.d("kakao", "이메일: ${user?.kakaoAccount?.email}")
//
//                    if (user?.kakaoAccount?.email == null) {
//                        UserApiClient.instance.unlink { error ->
//                            if (error != null) {
//                                Log.d("카카오로그인","회원 탈퇴 실패")
//                            }else {
//                                Log.d("카카오로그인","회원 탈퇴 성공")
//                            }
//                        }
//                        Toast.makeText(this, "이메일 동의 필수입니다.", Toast.LENGTH_SHORT).show()
//                        return@me
//                    }
//                }

//                UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
//                    UserApiClient.instance.me { user, error ->
//                        Log.d("kakao", "닉네임: ${user?.kakaoAccount?.profile?.nickname}")
//                        Log.d("kakao", "이메일: ${user?.kakaoAccount?.email}")
//
//                        if (user?.kakaoAccount?.email == null) {
//                            UserApiClient.instance.unlink { error ->
//                                if (error != null) {
//                                    Log.d("카카오로그인","회원 탈퇴 실패")
//                                }else {
//                                    Log.d("카카오로그인","회원 탈퇴 성공")
//                                }
//                            }
//                            Toast.makeText(this, "이메일 동의 필수입니다.", Toast.LENGTH_SHORT).show()
//                            return@me
//                        }
//                    }
//                }
            }
        }
    }
}