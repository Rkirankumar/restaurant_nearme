package retrofitdemo.com.restaurantsnearme.network

import retrofitdemo.com.restaurantsnearme.network.APIService.Companion.BASE_URL
import retrofitdemo.com.restaurantsnearme.App
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class APIClient {
    private var retrofit: Retrofit? = null

    private fun getClient(): Retrofit {
        val cacheSize = 10 * 1024 * 1024L // 10 MB
        val cache = Cache(App.instance.cacheDir, cacheSize)

        val client = OkHttpClient.Builder()
                .cache(cache)  // Enabling API cache
                .connectTimeout(3, TimeUnit.MINUTES)
                .writeTimeout(3, TimeUnit.MINUTES)
                .readTimeout(3, TimeUnit.MINUTES)
                .build()

        if (null == retrofit) {
            retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(client)
                    .build()
        }

        return retrofit!!
    }

    fun getAPIService() = getClient().create(APIService::class.java)
}