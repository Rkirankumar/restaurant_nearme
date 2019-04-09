package retrofitdemo.com.restaurantsnearme.network

import retrofitdemo.com.restaurantsnearme.network.model.FactResponse
import io.reactivex.Observable

import retrofit2.http.*
import retrofitdemo.com.restaurantsnearme.network.model.AddressResponse
import retrofitdemo.com.restaurantsnearme.network.model.DistanceResponse
import retrofitdemo.com.restaurantsnearme.network.model.PlaceDetailsResponse

interface APIService {
    companion object {
        val GOOGLE_PLACE_API_KEY = "AIzaSyApIfn_3O9zC-EHURARkFHz6wEzEz1lvtY"
        var BASE_URL = "https://maps.googleapis.com/maps/api/"
    }

    @GET("geocode/json?")
    fun getCurrentAddress(@Query(value = "latlng", encoded = true) latlng: String, @Query(value = "key", encoded = true) key: String): Observable<AddressResponse>

    @GET("place/nearbysearch/json?")
    fun doPlaces(@Query(value = "location", encoded = true) location: String, @Query(value = "radius", encoded = true) radius: Long, @Query(value = "type", encoded = true) type: String, @Query(value = "key", encoded = true) key: String): Observable<FactResponse>

    @GET("distancematrix/json?") // origins/destinations:  LatLng as string
    fun getDistance(@Query(value = "origins", encoded = true) origins: String, @Query(value = "destinations", encoded = true) destinations: String, @Query(value = "key", encoded = true) key: String): Observable<DistanceResponse>

    @GET("place/details/json?")
    fun getPlaceDetails(@Query(value = "placeid", encoded = true) placeid: String, @Query(value = "key", encoded = true) key: String): Observable<PlaceDetailsResponse>


}