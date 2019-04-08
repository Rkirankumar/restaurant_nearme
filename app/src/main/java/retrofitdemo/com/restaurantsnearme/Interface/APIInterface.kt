package retrofitdemo.com.restaurantsnearme.Interface

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofitdemo.com.restaurantsnearme.Responsc.DistanceResponse
import retrofitdemo.com.restaurantsnearme.Responsc.PlacesResponse
import retrofitdemo.com.restaurantsnearme.Responsc.Places_details
import retrofitdemo.com.restaurantsnearme.Responsc.Places_details_list

/**
 * Created by Kiran on 05/04/19.
 */

interface APIInterface {

    @GET("place/nearbysearch/json?")
    fun doPlaces(@Query(value = "location", encoded = true) location: String, @Query(value = "radius", encoded = true) radius: Long, @Query(value = "type", encoded = true) type: String, @Query(value = "key", encoded = true) key: String): Call<PlacesResponse.Root>

    @GET("geocode/json?")
    fun getCurrentAddress(@Query(value = "latlng", encoded = true) latlng: String, @Query(value = "key", encoded = true) key: String): Call<Places_details>

    @GET("distancematrix/json?") // origins/destinations:  LatLng as string
    fun getDistance(@Query(value = "origins", encoded = true) origins: String, @Query(value = "destinations", encoded = true) destinations: String, @Query(value = "key", encoded = true) key: String): Call<DistanceResponse>

    @GET("place/details/json?")
    fun getPlaceDetails(@Query(value = "placeid", encoded = true) placeid: String, @Query(value = "key", encoded = true) key: String): Call<Places_details_list>


}
