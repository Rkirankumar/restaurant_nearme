package retrofitdemo.com.restaurantsnearme

import java.util.concurrent.TimeUnit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Kiran on 05/04/19.
 */

object ApiClient {

    private var retrofit: Retrofit? = null


    val GOOGLE_PLACE_API_KEY = "AIzaSyApIfn_3O9zC-EHURARkFHz6wEzEz1lvtY"

    var base_url = "https://maps.googleapis.com/maps/api/"

    val client: Retrofit
        get() {

            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder().readTimeout(30, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS).addInterceptor(interceptor).build()


            retrofit = null

            retrofit = Retrofit.Builder()
                    .baseUrl(base_url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build()


            return (retrofit as Retrofit?)!!
        }
}