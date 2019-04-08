package retrofitdemo.com.restaurantsnearme.Activity

import android.Manifest
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.location.Location
import android.net.ConnectivityManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnSuccessListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofitdemo.com.restaurantsnearme.Adapter.Resturant_adapter
import retrofitdemo.com.restaurantsnearme.ApiClient
import retrofitdemo.com.restaurantsnearme.Interface.APIInterface
import retrofitdemo.com.restaurantsnearme.R
import retrofitdemo.com.restaurantsnearme.Responsc.*
import java.util.*

class MainActivity : AppCompatActivity(), GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private val TAG = "MainActivity"
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
    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null
    private val radius = (3 * 1000).toLong()// resturant near by radius
    private val MY_PERMISION_CODE = 10
    private var Permission_is_granted = false
    var mAddressOutput: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.i("On Create", "true")
        progress = findViewById<ProgressBar>(R.id.progress)
        rl_layout = findViewById<RelativeLayout>(R.id.rl_layout)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            progress.setProgress(0, true)
        } else
            progress.progress = 0


        apiService = ApiClient.client.create(APIInterface::class.java)

        recyclerView = findViewById<View>(R.id.recyclerView) as RecyclerView?
        recyclerView!!.setNestedScrollingEnabled(false)
        recyclerView!!.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this)
        recyclerView!!.setLayoutManager(layoutManager)

        mGoogleApiClient = GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)


        // Manual check internet conn. on activity start
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        if (networkInfo != null && networkInfo.isConnected) {
            checkinternet_connection(true)
        } else {
            progress.visibility = View.GONE
            checkinternet_connection(false)
        }


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

    fun checkinternet_connection(isConnected: Boolean) {
        if (isConnected) {
            getUserLocation()

        } else {
            Toast.makeText(applicationContext,"No internet connection",Toast.LENGTH_SHORT).show()
        }
    }

    private fun getUserLocation() {
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) !== PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) !== PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                                ACCESS_FINE_LOCATION) && ActivityCompat.shouldShowRequestPermissionRationale(this,
                                ACCESS_COARSE_LOCATION)) {
                    showAlert()
                } else {
                    if (isFirstTimeAskingPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                        firstTimeAskingPermission(this,
                                Manifest.permission.ACCESS_FINE_LOCATION, false)
                        ActivityCompat.requestPermissions(this,
                                arrayOf<String>(ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION),
                                MY_PERMISION_CODE)
                    } else {
                        Toast.makeText(this, "You won't be able to access the features of this App", Toast.LENGTH_LONG).show()
                        progress.visibility = View.GONE
                        //Permission disable by device policy or user denied permanently. Show proper error message
                    }
                }

            } else
                Permission_is_granted = true
        } else {
            mFusedLocationProviderClient?.getLastLocation()!!.addOnSuccessListener(this, object : OnSuccessListener<Location> {
                override fun onSuccess(location: Location?) {
                    if (location != null) {
                        mLastLocation = location
                        source_lat = location.latitude
                        source_long = location.longitude
                        latLngString = location.latitude.toString() + "," + location.longitude
                        fetchCurrentAddress(latLngString)
                        Log.i(TAG, latLngString + "")
                        fetchStores("restaurant")
                    } else {
                        progress.visibility = View.GONE
                        Toast.makeText(applicationContext, "Error in fetching the location", Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }
    }

    private fun showAlert() {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings are OFF \nPlease Enable Location")
                .setPositiveButton("Allow", DialogInterface.OnClickListener { paramDialogInterface, paramInt ->
                    ActivityCompat.requestPermissions(this@MainActivity,
                            arrayOf(ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION),
                            MY_PERMISION_CODE)
                })
                .setNegativeButton("Cancel", DialogInterface.OnClickListener { paramDialogInterface, paramInt -> })
        dialog.show()
    }

    fun firstTimeAskingPermission(context: Context, permission: String, isFirstTime: Boolean) {
        val sharedPreference = context.getSharedPreferences(PREFS_FILE_NAME, MODE_PRIVATE)
        sharedPreference.edit().putBoolean(permission, isFirstTime).apply()
    }

    fun isFirstTimeAskingPermission(context: Context, permission: String): Boolean {
        return context.getSharedPreferences(PREFS_FILE_NAME, MODE_PRIVATE).getBoolean(permission, true)
    }


    private fun fetchCurrentAddress(latLngString: String) {

        val call = apiService!!.getCurrentAddress(latLngString, ApiClient.GOOGLE_PLACE_API_KEY)
        call.enqueue(object : Callback<Places_details> {


            override fun onResponse(call: Call<Places_details>, response: Response<Places_details>) {

                val details = response.body() as Places_details?

                if ("OK".equals(details!!.status, ignoreCase = true)) {

                    mAddressOutput = details!!.results.get(0).formatted_adress
                    Log.i("Addr Current and coord.", mAddressOutput + latLngString)
                }

            }

            override fun onFailure(call: Call<Places_details>?, t: Throwable?) {
                call!!.cancel()
            }

        })

    }
    /*
    fetch Resturant near by response
     */
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


    }
   /*
     To featch destination Addresses and origin Addresses , distence between them
    */
    private fun fetchDistance(info: PlacesResponse.CustomA, place_id: String, photourl: String) {
        Log.i(TAG, "Distance API call start")
        val call = apiService!!.getDistance(latLngString, info.geometry.locationA.lat.toString() + "," + info.geometry.locationA.lng, ApiClient.GOOGLE_PLACE_API_KEY)
        call.enqueue(object : Callback<DistanceResponse> {
            override fun onResponse(call: Call<DistanceResponse>, response: Response<DistanceResponse>) {
                val resultDistance = response.body() as DistanceResponse?
                if (response.isSuccessful()) {
                    Log.i(TAG, resultDistance!!.status)
                    if ("OK".equals(resultDistance!!.status, ignoreCase = true)) {
                        val row1 = resultDistance!!.rows.get(0)
                        val element1 = row1.elements.get(0)
                        if ("OK".equals(element1.status, ignoreCase = true)) {
                            val itemDistance = element1.distance
                            val total_distance = itemDistance.text
                            fetchPlace_details(info, place_id, total_distance, info.name, photourl)
                        }
                    }
                } else if (response.code() != 200) {
                    Toast.makeText(applicationContext, "Error " + response.code() + " found.", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<DistanceResponse>?, t: Throwable?) {
                Toast.makeText(applicationContext, "Error in Fetching Details,Please Refresh", Toast.LENGTH_SHORT).show()
                if (call != null) {
                    call.cancel()
                }
            }
        })

    }

    /*
     Featch resturant details like address, ratting, opening and closing time ,etc
     */
    private fun fetchPlace_details(info: PlacesResponse.CustomA, place_id: String, totaldistance: String, name: String, photourl: String) {
        val call = apiService!!.getPlaceDetails(place_id, ApiClient.GOOGLE_PLACE_API_KEY)
        call.enqueue(object : Callback<Places_details_list> {
            override fun onResponse(call: Call<Places_details_list>, response: Response<Places_details_list>) {
                val details = response.body()
                if ("OK".equals(details!!.status, ignoreCase = true)) {
                    val address = details.result.formatted_adress
                    val lat = details.result.geometry.locationA.lat
                    val lng = details.result.geometry.locationA.lng
                    // Add details in Array
                    details_modal.add(PlacesDetails_Modal(address, totaldistance, name, photourl, lat, lng))
                    Log.i("details : ", info.name + "  " + address)
                    if (details_modal.size == results.size) {
                        Collections.sort(details_modal) { lhs, rhs -> lhs.distance.compareTo(rhs.distance) }
                        progress.visibility = View.GONE
                        val adapterStores = Resturant_adapter(applicationContext, details_modal, mAddressOutput, source_lat, source_long)
                        recyclerView!!.setAdapter(adapterStores)
                        adapterStores.notifyDataSetChanged()
                    }
                }
            }
            override fun onFailure(call: Call<Places_details_list>?, t: Throwable?) {
                if (call != null) {
                    call.cancel()
                }
            }
        })
    }
}
