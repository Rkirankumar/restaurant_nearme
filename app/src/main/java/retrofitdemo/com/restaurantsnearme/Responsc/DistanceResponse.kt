package retrofitdemo.com.restaurantsnearme.Responsc

import com.google.gson.annotations.SerializedName

import java.util.ArrayList

/**
 * Created by Kiran on 05/04/19.
 */

class DistanceResponse {
    @SerializedName("status")
    lateinit var status: String
    @SerializedName("rows")
    var rows: ArrayList<InfoDistance> = ArrayList()

    inner class InfoDistance {
        @SerializedName("elements")
        var elements: ArrayList<DistanceElement> = ArrayList()

        inner class DistanceElement {
            @SerializedName("status")
            lateinit var status: String
            @SerializedName("duration")
            lateinit var duration: ValueItem
            @SerializedName("distance")
            lateinit var distance: ValueItem
        }

        inner class ValueItem {
            @SerializedName("value")
            var value: Long = 0
            @SerializedName("text")
            lateinit var text: String
        }
    }
}