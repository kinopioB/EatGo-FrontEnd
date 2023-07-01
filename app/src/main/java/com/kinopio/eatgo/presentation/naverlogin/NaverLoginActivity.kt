package com.kinopio.eatgo.presentation.naverlogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import com.kinopio.eatgo.MainActivity
import com.kinopio.eatgo.databinding.ActivityMainBinding
import com.kinopio.eatgo.R
import com.kinopio.eatgo.RetrofitClient
import com.kinopio.eatgo.User
import com.kinopio.eatgo.data.map.LoginService
import com.kinopio.eatgo.databinding.ActivityNaverLoginBinding
import com.kinopio.eatgo.domain.map.LoginDto
import com.kinopio.eatgo.domain.map.LoginResponseDto
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NaverLoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNaverLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNaverLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        /** Naver Login Module Initialize */
        val naverClientId = getString(R.string.social_login_info_naver_client_id)
        val naverClientSecret = getString(R.string.social_login_info_naver_client_secret)
        val naverClientName = getString(R.string.social_login_info_naver_client_name)
        NaverIdLoginSDK.initialize(this, naverClientId, naverClientSecret , naverClientName)

        setLayoutState(false)

        binding.tvNaverLogin.setOnClickListener {
            val radioGroup = binding.loginRadioGroup

            if (radioGroup.checkedRadioButtonId == -1){
                Toast.makeText(this, "로그인 유형을 선택하세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val checkedId : String = resources.getResourceEntryName(radioGroup.checkedRadioButtonId)

            Log.d("checked", "$checkedId")
            if (checkedId == "boss"){
                startNaverLogin(1)
            }
            else if (checkedId == "normal") {
                startNaverLogin(2)
            }

        }

//        binding.tvNaverLogin.setOnClickListener {
//
//            startNaverLogin()
//
//        }
        binding.tvNaverLogout.setOnClickListener {
            startNaverLogout()
        }
        binding.tvNaverDeleteToken.setOnClickListener {
            startNaverDeleteToken()
        }
    }

    /**
     * 로그인
     * authenticate() 메서드를 이용한 로그인 */
    private fun startNaverLogin(role:Int){
        var naverToken :String? = ""

        val profileCallback = object : NidProfileCallback<NidProfileResponse> {
            override fun onSuccess(response: NidProfileResponse) {
                val userId = response.profile?.id
                val userName = response.profile?.name

                val retrofit = RetrofitClient.getRetrofit2()
                val loginService = retrofit.create(LoginService::class.java)

                val loginInfo = LoginDto(
                    userSocialId = userId.toString(),
                    userName = userName.toString(),
                    LoginType = 2,
                    role = role
                )
                loginService.login(loginInfo).enqueue(object : Callback<LoginResponseDto> {

                    override fun onFailure(call: Call<LoginResponseDto>, t: Throwable) {
                        Log.d("fail", "로그인 실패")
                        Log.d("fail", "$t")
                    }

                    override fun onResponse(call: Call<LoginResponseDto>, response: Response<LoginResponseDto>) {
                        Log.d("success", "로그인 성공")
                        response.body()?.let {
                            User.setUserId(it.userId)
                            User.setUserName(it.userName)
                            User.setUserSocialId(it.userSocialId)
                            User.setRole(it.role)
                            User.setSocialToken(naverToken.toString())
                            User.setJwt(it.jwt)
                            User.setLoginType(it.loginType)
                        }
                        Log.d("success", "${User.getJwt()}")
                        Log.d("success", "${User.getUserSocialId()}")
                        Log.d("success", "${User.getSocialToken()}")
                        val intent:Intent = Intent(applicationContext, MainActivity::class.java)
                        startActivity(intent)
                    }
                })

//                setLayoutState(true)
            }
            override fun onFailure(httpStatus: Int, message: String) {

                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                Toast.makeText(this@NaverLoginActivity, "errorCode: ${errorCode}\n" +
                        "errorDescription: ${errorDescription}", Toast.LENGTH_SHORT).show()
            }
            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }
        }

        /** OAuthLoginCallback을 authenticate() 메서드 호출 시 파라미터로 전달하거나 NidOAuthLoginButton 객체에 등록하면 인증이 종료되는 것을 확인할 수 있습니다. */
        val oauthLoginCallback = object : OAuthLoginCallback {

            override fun onSuccess() {
                // 네이버 로그인 인증이 성공했을 때 수행할 코드 추가
                naverToken = NaverIdLoginSDK.getAccessToken()
//                var naverRefreshToken = NaverIdLoginSDK.getRefreshToken()
//                var naverExpiresAt = NaverIdLoginSDK.getExpiresAt().toString()
//                var naverTokenType = NaverIdLoginSDK.getTokenType()
//                var naverState = NaverIdLoginSDK.getState().toString()

                //로그인 유저 정보 가져오기
                NidOAuthLogin().callProfileApi(profileCallback)
            }
            override fun onFailure(httpStatus: Int, message: String) {
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                Toast.makeText(this@NaverLoginActivity, "errorCode: ${errorCode}\n" +
                        "errorDescription: ${errorDescription}", Toast.LENGTH_SHORT).show()
            }
            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }
        }
        Log.d("naver", "123123 ${NaverIdLoginSDK.getAccessToken()}")
        NaverIdLoginSDK.authenticate(this, oauthLoginCallback)

    }

    /**
     * 로그아웃
     * 애플리케이션에서 로그아웃할 때는 다음과 같이 NaverIdLoginSDK.logout() 메서드를 호출합니다. */
    private fun startNaverLogout(){
        NaverIdLoginSDK.logout()
        setLayoutState(false)
        Toast.makeText(this@NaverLoginActivity, "네이버 아이디 로그아웃 성공!", Toast.LENGTH_SHORT).show()
    }

    /**
     * 연동해제
     * 네이버 아이디와 애플리케이션의 연동을 해제하는 기능은 다음과 같이 NidOAuthLogin().callDeleteTokenApi() 메서드로 구현합니다.
    연동을 해제하면 클라이언트에 저장된 토큰과 서버에 저장된 토큰이 모두 삭제됩니다.
     */
    private fun startNaverDeleteToken(){
        NidOAuthLogin().callDeleteTokenApi(this, object : OAuthLoginCallback {
            override fun onSuccess() {
                //서버에서 토큰 삭제에 성공한 상태입니다.
                setLayoutState(false)
                Toast.makeText(this@NaverLoginActivity, "네이버 아이디 토큰삭제 성공!", Toast.LENGTH_SHORT).show()
            }
            override fun onFailure(httpStatus: Int, message: String) {
                // 서버에서 토큰 삭제에 실패했어도 클라이언트에 있는 토큰은 삭제되어 로그아웃된 상태입니다.
                // 클라이언트에 토큰 정보가 없기 때문에 추가로 처리할 수 있는 작업은 없습니다.
                Log.d("naver", "errorCode: ${NaverIdLoginSDK.getLastErrorCode().code}")
                Log.d("naver", "errorDesc: ${NaverIdLoginSDK.getLastErrorDescription()}")
            }
            override fun onError(errorCode: Int, message: String) {
                // 서버에서 토큰 삭제에 실패했어도 클라이언트에 있는 토큰은 삭제되어 로그아웃된 상태입니다.
                // 클라이언트에 토큰 정보가 없기 때문에 추가로 처리할 수 있는 작업은 없습니다.
                onFailure(errorCode, message)
            }
        })
    }

    private fun setLayoutState(login: Boolean){
        if(login){
            binding.tvNaverLogin.visibility = View.GONE
            binding.tvNaverLogout.visibility = View.VISIBLE
            binding.tvNaverDeleteToken.visibility = View.VISIBLE
        }else{
            binding.tvNaverLogin.visibility = View.VISIBLE
            binding.tvNaverLogout.visibility = View.GONE
            binding.tvNaverDeleteToken.visibility = View.GONE
//            binding.tvResult.text = ""
        }
    }
}