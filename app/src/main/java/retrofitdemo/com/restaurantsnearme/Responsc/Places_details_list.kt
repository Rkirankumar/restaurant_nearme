package retrofitdemo.com.restaurantsnearme.Responsc

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.ArrayList

/**
 * Created by Kiran on 05/04/19.
 */

class Places_details_list {
    @SerializedName("status")
    lateinit var status: String

    @SerializedName("result")
    lateinit var result: results

    inner class results : Serializable {
        @SerializedName("formatted_address")
        var formatted_adress: String = ""
        @SerializedName("geometry")
        lateinit var geometry: Geometry
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