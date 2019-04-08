package retrofitdemo.com.restaurantsnearme.Responsc

import com.google.gson.annotations.SerializedName

import java.io.Serializable
import java.util.ArrayList


/**
 * Created by Kiran on 05/04/19.
 */

class Places_details {
    @SerializedName("results")
    var results: ArrayList<Results> = ArrayList()

    @SerializedName("status")
    lateinit var status: String

    inner class Results : Serializable {
        @SerializedName("formatted_address")
        lateinit var formatted_adress: String
        @SerializedName("international_phone_number")
        lateinit var international_phone_number: String
        @SerializedName("rating")
        var rating: Float = 0.toFloat()
    }
}