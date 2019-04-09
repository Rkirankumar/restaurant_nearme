package retrofitdemo.com.restaurantsnearme.network.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AddressResponse(

        var status: String,
        var results: ArrayList<Results> = ArrayList()) {
    inner class Results : Serializable {
        lateinit var formatted_address: String
    }

}