package retrofitdemo.com.restaurantsnearme.Activity

import android.graphics.Color
import android.support.v4.app.FragmentActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.maps.CameraUpdateFactory

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.DirectionsApi
import com.google.maps.GeoApiContext
import retrofitdemo.com.restaurantsnearme.R

import java.util.ArrayList

class MapsActivity : FragmentActivity(), OnMapReadyCallback {
    private val TAG = "MapsActivity"
    private var mMap: GoogleMap? = null
    var lat: Double? = null
    var lng: Double? = null
    var currentlat: Double? = null
    var currentlng: Double? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        lat = intent.getDoubleExtra("LAT", 0.0)
        lng = intent.getDoubleExtra("LNG", 0.0)
        currentlat = intent.getDoubleExtra("CURRENT_LAT", 0.0)
        currentlng = intent.getDoubleExtra("CURRENT_LNG", 0.0)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val source = LatLng(this!!.lat!!, this!!.lng!!)
        mMap!!.addMarker(MarkerOptions().position(source).title("Marker in source"))
        val destination = LatLng(this!!.currentlat!!, this!!.currentlng!!)
        mMap!!.addMarker(MarkerOptions().position(destination).title("Marker in destination"))
        var path: ArrayList<LatLng> = ArrayList()
        val context = GeoApiContext.Builder()
                .apiKey("AIzaSyApIfn_3O9zC-EHURARkFHz6wEzEz1lvtY")
                .build()
        var origin: String? = lat.toString() + "," + lng.toString()
        var destinatio: String? = currentlat.toString() + "," + currentlat.toString()
        val req = DirectionsApi.getDirections(context, origin, destinatio)
        try {
            val res = req.await()
            //Loop through legs and steps to get encoded polylines of each step
            if (res.routes != null && res.routes.size > 0) {
                val route = res.routes[0]
                if (route.legs != null) {
                    for (i in 0 until route.legs.size) {
                        val leg = route.legs[i]
                        if (leg.steps != null) {
                            for (j in 0 until leg.steps.size) {
                                val step = leg.steps[j]
                                if (step.steps != null && step.steps.size > 0) {
                                    for (k in 0 until step.steps.size) {
                                        val step1 = step.steps[k]
                                        val points1 = step1.polyline
                                        if (points1 != null) {
                                            //Decode polyline and add points to list of route coordinates
                                            val coords1 = points1.decodePath()
                                            for (coord1 in coords1) {
                                                path.add(LatLng(coord1.lat, coord1.lng))
                                            }
                                        }
                                    }
                                } else {
                                    val points = step.polyline
                                    if (points != null) {
                                        //Decode polyline and add points to list of route coordinates
                                        val coords = points.decodePath()
                                        for (coord in coords) {
                                            path.add(LatLng(coord.lat, coord.lng))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (ex: Exception) {
            Log.e(TAG, ex.getLocalizedMessage())
        }
        if (path.size > 0) {
            val opts = PolylineOptions().addAll(path).color(Color.BLUE).width(5F)
            mMap!!.addPolyline(opts)
        }
        mMap!!.getUiSettings().setZoomControlsEnabled(true)
        mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(destination, 14F))
    }


}
