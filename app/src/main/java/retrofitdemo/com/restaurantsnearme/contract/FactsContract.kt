package retrofitdemo.com.restaurantsnearme.contract

import retrofitdemo.com.restaurantsnearme.network.model.FactResponse
import io.reactivex.Observable
import retrofitdemo.com.restaurantsnearme.network.model.AddressResponse
import retrofitdemo.com.restaurantsnearme.network.model.DistanceResponse
import retrofitdemo.com.restaurantsnearme.network.model.PlaceDetailsResponse

interface FactsContract {

    interface Model {
        fun getFacts(latlong: String, radius: Long, type: String): Observable<FactResponse>
        fun Distance(lat: String, latlong: String): Observable<DistanceResponse>
        fun getPlaceDetails(placeid: String): Observable<PlaceDetailsResponse>
        fun getaddress(latlong: String): Observable<AddressResponse>
    }

    interface View {
        fun init()
        fun onSuccessResturant(response: FactResponse)
        fun onSuccessDistance(response: DistanceResponse)
        fun onSuccessPlaceDetails(response: PlaceDetailsResponse, totaldistance: String, name: String, photourl: String, size: Int)
        fun onSuccessAddress(response: AddressResponse)
        fun onError(error: String)
    }

    interface Presenter {
        fun getFactsList(latlong: String, radius: Long, type: String)
         fun fetchDistance(lat: String, lng: String, place_id: String, photourl: String, latlong: String, name: String)
         fun fetchPlace_details(place_id: String, total_distance: String, name: String, photourl: String)
        fun getFetchCurrentAddress(latlong: String)
    }
}