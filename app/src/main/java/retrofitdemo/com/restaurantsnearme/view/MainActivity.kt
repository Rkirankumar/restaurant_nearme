package retrofitdemo.com.restaurantsnearme.view

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.pm.PackageManager
import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnSuccessListener
import kotlinx.android.synthetic.main.activity_main.*
import retrofitdemo.com.restaurantsnearme.App
import retrofitdemo.com.restaurantsnearme.R
import retrofitdemo.com.restaurantsnearme.adapters.Resturant_adapter
import retrofitdemo.com.restaurantsnearme.contract.FactsContract
import retrofitdemo.com.restaurantsnearme.network.model.*
import retrofitdemo.com.restaurantsnearme.presenter.FactsPresenter
import java.util.*

class MainActivity : AppCompatActivity(), GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener , FactsContract.View{



    private val TAG = "MainActivity"
    private var mPresenter: FactsPresenter? = null

    private var mGoogleApiClient: GoogleApiClient? = null
    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null
    var mCityFactsList: ArrayList<PlacesDetails_Modal> = ArrayList()
    private var maddress: String = ""
    var source_lat: Double = 0.toDouble()
    var source_long: Double = 0.toDouble()
    private var mAdapter: Resturant_adapter? = null
    private var latLngString: String = ""
    private val radius = (3 * 1000).toLong()// resturant near by radius
/*

    private var recyclerView: RecyclerView? = null
    private lateinit var progress: ProgressBar
    private lateinit var rl_layout: RelativeLayout
    val PREFS_FILE_NAME = "sharedPreferences"
    private var apiService: APIInterface? = null
    private var latLngString: String = ""
    var source_lat: Double = 0.toDouble()
    var source_long: Double = 0.toDouble()
    var results = arrayListOf<PlacesResponse.CustomA>()
    protected var mLastLocation: Location? = null
    private var mGoogleApiClient: GoogleApiClient? = null
    var details_modal = arrayListOf<PlacesDetails_Modal>()
    private val radius = (3 * 1000).toLong()// resturant near by radius
    private val MY_PERMISION_CODE = 10
    private var Permission_is_granted = false
    var mAddressOutput: String = ""

*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.i("On Create", "true")
        if (App.isNetworkAvailable() && App.checkAndRequestPermissions(this@MainActivity)) {
            mGoogleApiClient = GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build()
            mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
            mPresenter = FactsPresenter(this)
            getUserLocation()


        }else{
            Toast.makeText(applicationContext, getString(R.string.network_error), Toast.LENGTH_SHORT).show()
        }

    }
    override fun init() {
        mAdapter = Resturant_adapter(applicationContext, mCityFactsList, maddress, source_lat, source_long)
        rvCityFactsList.layoutManager = LinearLayoutManager(this)
        rvCityFactsList.adapter = mAdapter


    }
    override fun onSuccessResturant(response: FactResponse) {
        println(response)
    }
    override fun onSuccessDistance(response: DistanceResponse) {
        println(response)
    }
    override fun onSuccessAddress(response: AddressResponse) {
        maddress= response!!.results.get(0).formatted_address
    }
    override fun onSuccessPlaceDetails(response: PlaceDetailsResponse, totaldistance: String, name: String, photourl: String, size: Int) {
        val address =response.result.formatted_address
        val lat = response.result.geometry?.location?.lat
        val lng = response.result.geometry?.location?.lng
        // Add details in Array
        mCityFactsList.add(PlacesDetails_Modal(address, totaldistance, name, photourl, lat!!, lng!!))
        if (mCityFactsList.size == size) {
            rvProgress.visibility = View.GONE
            Collections.sort(mCityFactsList) { lhs, rhs -> lhs.distance.compareTo(rhs.distance) }
            mAdapter = Resturant_adapter(applicationContext, mCityFactsList, maddress, source_lat, source_long)
            rvCityFactsList.layoutManager = LinearLayoutManager(this)
            rvCityFactsList.adapter = mAdapter
            rvCityFactsList.adapter!!.notifyDataSetChanged()
        }
    }

    override fun onError(error: String) {
        println(error)
    }
    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onConnected(p0: Bundle?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onConnectionSuspended(p0: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    private fun getUserLocation() {
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) !== PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) !== PackageManager.PERMISSION_GRANTED) {

        } else {
            mFusedLocationProviderClient?.getLastLocation()!!.addOnSuccessListener(this, object : OnSuccessListener<Location> {
                override fun onSuccess(location: Location?) {
                    if (location != null) {
                        source_lat = location.latitude
                        source_long = location.longitude
                        latLngString = location.latitude.toString() + "," + location.longitude
                        mPresenter?.getFetchCurrentAddress(latLngString)
                        Log.i(TAG, latLngString + "")
                        mPresenter?.getFactsList(latLngString, radius, "restaurant")

                    } else {
                       Toast.makeText(applicationContext, getString(R.string.error_in_location), Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }
    }







   /* *//*
    fetch Resturant near by response
     *//*
    private fun fetchStores(placeType: String) {
        val call = apiService!!.doPlaces(latLngString, radius, placeType, ApiClient.GOOGLE_PLACE_API_KEY)
        call.enqueue(object : Callback<PlacesResponse.Root> {
            override fun onResponse(call: Call<PlacesResponse.Root>, response: Response<PlacesResponse.Root>) {
                val root = response.body() as PlacesResponse.Root?
                if (response.isSuccessful) {
                    when (root!!.status) {
                        "OK" -> {
                            results = root!!.customA
                            details_modal = ArrayList()
                            var photourl: String
                            Log.i(TAG, "fetch stores")
                            for (i in results.indices) {
                                val info = results[i] as PlacesResponse.CustomA
                                val place_id = results[i].place_id
                                if (results[i].photos != null) {
                                    val photo_reference = results[i].photos[0].photo_reference
                                    photourl = ApiClient.base_url + "place/photo?maxwidth=100&photoreference=" + photo_reference +
                                            "&key=" + ApiClient.GOOGLE_PLACE_API_KEY

                                } else {
                                    photourl = "NA"
                                }
                                fetchDistance(info, place_id, photourl)
                                Log.i("Names  ", info.name)
                            }
                        }
                        "ZERO_RESULTS" -> {
                            Toast.makeText(applicationContext, "No matches found near you", Toast.LENGTH_SHORT).show()
                            progress.visibility = View.GONE
                        }
                        "OVER_QUERY_LIMIT" -> {
                            Toast.makeText(applicationContext, "You have reached the Daily Quota of Requests", Toast.LENGTH_SHORT).show()
                            progress.visibility = View.GONE
                        }
                        else -> {
                            Toast.makeText(applicationContext, "Error", Toast.LENGTH_SHORT).show()
                            progress.visibility = View.GONE
                        }
                    }
                } else if (response.code() != 200) {
                    Toast.makeText(applicationContext, "Error " + response.code() + " found.", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<PlacesResponse.Root>?, t: Throwable?) {
                Toast.makeText(applicationContext, "Error in Fetching Details,Please Refresh", Toast.LENGTH_SHORT).show()
                if (call != null) {
                    call.cancel()
                }
            }
        })


    }*/



}
