package retrofitdemo.com.restaurantsnearme.Responsc

import com.google.gson.annotations.SerializedName

import java.io.Serializable
import java.util.ArrayList

/**
 * Created by Kiran on 05/04/19.
 */

class PlacesResponse {
    inner class Root : Serializable {
        @SerializedName("results")
        var customA: ArrayList<CustomA> = ArrayList()
        @SerializedName("status")
        lateinit var status: String
    }

    inner class CustomA : Serializable {
        @SerializedName("geometry")
        lateinit var geometry: Geometry
        @SerializedName("vicinity")
        lateinit var vicinity: String
        @SerializedName("name")
        lateinit var name: String
        @SerializedName("photos")
        var photos: ArrayList<Photos> = ArrayList()
        @SerializedName("place_id")
        lateinit var place_id: String
    }

    inner class Photos : Serializable {
        @SerializedName("photo_reference")
        lateinit var photo_reference: String
    }

    inner class Geometry : Serializable {
        @SerializedName("location")
        lateinit var locationA: LocationA
    }

    inner class LocationA : Serializable {
        @SerializedName("lat")
        var lat: Double = 0.toDouble()
        @SerializedName("lng")
        var lng: Double = 0.toDouble()
    }
}