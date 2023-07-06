import com.kinopio.eatgo.NotificationInterface
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object FCMRetrofitProvider {
    private var token: String = "AAAACHpPMKM:APA91bGZiS3L29ohVlv1qIljbKvG_eVQF4-UF7YcAJVaLdQRGUZbI9UU0MUlSsdNB_hxWIKEk1a9ICZhUWeTVFJ8Lrkqg15dWQ1FqqD0n5cZ_eciVy9GythXBHLa33Q3LSuGCSaZP3Jw"
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