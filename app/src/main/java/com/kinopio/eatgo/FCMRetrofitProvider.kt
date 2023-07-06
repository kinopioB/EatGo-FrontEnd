import android.content.res.Resources
import com.kinopio.eatgo.NotificationInterface
import com.kinopio.eatgo.R
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object FCMRetrofitProvider {
    private var token: String = Resources.getSystem().getString(R.string.firebase_key)
    //    R.string.firebase_server_key.toString()
    private const val BASE_URL = "https://fcm.googleapis.com/"

    private var retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(createOkHttpClient())
        .build()

    private fun createOkHttpClient(): OkHttpClient {
        val httpClient = OkHttpClient.Builder()

        httpClient.addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
            chain.proceed(request)
        }

        return httpClient.build()
    }

//    fun setToken(token: String) {
//        this.token = token
//        // Rebuild the Retrofit instance with the updated token
//        retrofit = retrofit.newBuilder()
//            .client(createOkHttpClient())
//            .build()
//    }

    fun getRetrofit(): NotificationInterface {
        return retrofit.create(NotificationInterface::class.java)
    }
}