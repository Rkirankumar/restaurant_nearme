package retrofitdemo.com.restaurantsnearme.model

import retrofitdemo.com.restaurantsnearme.contract.FactsContract
import retrofitdemo.com.restaurantsnearme.network.APIClient
import retrofitdemo.com.restaurantsnearme.network.model.FactResponse
import io.reactivex.Observable
import retrofitdemo.com.restaurantsnearme.network.APIService
import retrofitdemo.com.restaurantsnearme.network.model.AddressResponse
import retrofitdemo.com.restaurantsnearme.network.model.DistanceResponse
import retrofitdemo.com.restaurantsnearme.network.model.PlaceDetailsResponse

class FactsModel : FactsContract.Model {
    override fun getaddress(latlong: String): Observable<AddressResponse> {
        return APIClient().getAPIService().getCurrentAddress(latlong, APIService.GOOGLE_PLACE_API_KEY)
    }

    override fun getPlaceDetails(placeid: String): Observable<PlaceDetailsResponse> {
        return APIClient().getAPIService().getPlaceDetails(placeid, APIService.GOOGLE_PLACE_API_KEY)
    }

    override fun Distance(lat: String, latlong: String): Observable<DistanceResponse> {
        return APIClient().getAPIService().getDistance(lat, latlong, APIService.GOOGLE_PLACE_API_KEY)
    }

    override fun getFacts(latlong: String, radius: Long, type: String): Observable<FactResponse> {
        return APIClient().getAPIService().doPlaces(latlong, radius, type, APIService.GOOGLE_PLACE_API_KEY)
    }
}