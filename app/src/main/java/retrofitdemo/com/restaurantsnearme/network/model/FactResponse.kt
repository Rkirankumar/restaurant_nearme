package retrofitdemo.com.restaurantsnearme.network.model

import java.io.Serializable

data class FactResponse(
        var status: String,
        var results: ArrayList<CustomA> = ArrayList()) {


    inner class CustomA : Serializable {
        lateinit var geometry: Geometry
        lateinit var vicinity: String
        lateinit var name: String
        var photos: ArrayList<Photos> = ArrayList()
        lateinit var place_id: String

    }

    inner class Photos : Serializable {
        lateinit var photo_reference: String
    }

    inner class Geometry : Serializable {
        lateinit var location: LocationA
    }

    inner class LocationA : Serializable {
        var lat: Double = 0.toDouble()
        var lng: Double = 0.toDouble()
    }

}