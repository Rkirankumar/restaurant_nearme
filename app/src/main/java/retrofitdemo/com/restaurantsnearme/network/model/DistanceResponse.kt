package retrofitdemo.com.restaurantsnearme.network.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class DistanceResponse(

        var status: String,
        var rows: ArrayList<InfoDistance> = ArrayList()) {

    inner class InfoDistance {
        var elements: ArrayList<DistanceElement> = ArrayList()

        inner class DistanceElement {
            lateinit var status: String
            lateinit var duration: ValueItem
            lateinit var distance: ValueItem
        }

        inner class ValueItem {

            var value: Long = 0
            lateinit var text: String
        }
    }

}