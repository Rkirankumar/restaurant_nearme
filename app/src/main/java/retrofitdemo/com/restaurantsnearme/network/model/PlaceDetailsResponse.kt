package retrofitdemo.com.restaurantsnearme.network.model

import java.io.Serializable

data class PlaceDetailsResponse(
        var status: String,
        var result: results) {

    inner class results : Serializable {
        var formatted_address: String = ""
        var geometry: Geometry? = null
    }

    inner class Geometry : Serializable {
        var location: LocationA? = null
    }

    inner class LocationA : Serializable {
        var lat: Double = 0.toDouble()
        var lng: Double = 0.toDouble()
    }


}