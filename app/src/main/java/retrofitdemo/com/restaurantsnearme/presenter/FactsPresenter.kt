package retrofitdemo.com.restaurantsnearme.presenter

import android.util.Log
import retrofitdemo.com.restaurantsnearme.R
import retrofitdemo.com.restaurantsnearme.contract.FactsContract
import retrofitdemo.com.restaurantsnearme.model.FactsModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import retrofitdemo.com.restaurantsnearme.App
import retrofitdemo.com.restaurantsnearme.network.APIService
import retrofitdemo.com.restaurantsnearme.network.model.FactResponse

class FactsPresenter(factView: FactsContract.View) : FactsContract.Presenter {


    private var mFactView: FactsContract.View = factView
    private var mFactsModel = FactsModel()
    private var mCityFactsList: ArrayList<FactResponse.CustomA> = ArrayList()
    lateinit var photourl: String
    lateinit var distance_latLng: String

    init {
        mFactView?.init()
    }


    override fun getFetchCurrentAddress(latlong: String) {
        if (!App.isNetworkAvailable()) {
            mFactView?.onError(App.instance.resources.getString(R.string.network_error))
            return
        }
        mFactsModel
                .getaddress(latlong)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = { response ->
                            mFactView?.onSuccessAddress(response)
                        },
                        onError = { e ->
                            e.printStackTrace()
                            mFactView?.onError(App.instance.resources.getString(R.string.response_error))
                        }
                )
    }

    override fun getFactsList(latlong: String, radius: Long, type: String) {
        if (!App.isNetworkAvailable()) {
            mFactView?.onError(App.instance.resources.getString(R.string.network_error))
            return
        }

        mFactsModel
                .getFacts(latlong, radius, type)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = { response ->
                            mFactView?.onSuccessResturant(response)
                            mCityFactsList.clear()
                            for (item in response.results) {
                                if (!item.place_id.isNullOrEmpty()) {
                                    mCityFactsList.add(item)
                                }
                            }
                            for (i in mCityFactsList.indices) {
                                val info = mCityFactsList[i] as FactResponse.CustomA
                                val place_id = mCityFactsList[i].place_id
                                if (mCityFactsList[i].photos != null) {
                                    val photo_reference = mCityFactsList[i].photos[0].photo_reference
                                    photourl = APIService.BASE_URL + "place/photo?maxwidth=100&photoreference=" + photo_reference +
                                            "&key=" + APIService.GOOGLE_PLACE_API_KEY

                                } else {
                                    photourl = "NA"
                                }
                                fetchDistance(info.geometry.location.lat.toString(), info.geometry.location.lng.toString(), place_id, photourl, latlong, info.name)

                            }
                        },
                        onError = { e ->
                            e.printStackTrace()
                            mFactView?.onError(App.instance.resources.getString(R.string.response_error))
                        }
                )
    }

    override fun fetchDistance(lat: String, lng: String, place_id: String, photourl: String, latlong: String, name: String) {
        if (!App.isNetworkAvailable()) {
            mFactView?.onError(App.instance.resources.getString(R.string.network_error))
            return
        }
        distance_latLng = lat + "," + lng
        mFactsModel
                .Distance(latlong, distance_latLng)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = { response ->
                            mFactView?.onSuccessDistance(response)
                            Log.i("details : ", response.toString())
                            val row1 = response!!.rows.get(0)
                            val element1 = row1.elements.get(0)
                            if ("OK".equals(element1.status, ignoreCase = true)) {
                                val itemDistance = element1.distance
                                val total_distance = itemDistance.text
                                fetchPlace_details(place_id, total_distance, name, photourl)
                            }
                        },
                        onError = { e ->
                            e.printStackTrace()
                            mFactView?.onError(App.instance.resources.getString(R.string.response_error))
                        }
                )
    }

    override fun fetchPlace_details(place_id: String, total_distance: String, name: String, photourl: String) {
        if (!App.isNetworkAvailable()) {
            mFactView?.onError(App.instance.resources.getString(R.string.network_error))
            return
        }
        mFactsModel
                .getPlaceDetails(place_id)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = { response ->
                            mFactView?.onSuccessPlaceDetails(response, total_distance, name, photourl, mCityFactsList.size)
                        },
                        onError = { e ->
                            e.printStackTrace()
                            mFactView?.onError(App.instance.resources.getString(R.string.response_error))
                        }
                )
    }

}