package retrofitdemo.com.restaurantsnearme

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import java.util.ArrayList


/**
 * Created by Kiran on 05/04/19.
 */

class App : Application() {
    companion object {
        lateinit var instance: App
            private set
        val REQUEST_ID_MULTIPLE_PERMISSIONS = 1
        fun isNetworkAvailable(): Boolean {
            val cm = instance.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return cm.activeNetworkInfo?.isConnectedOrConnecting == true
        }

        fun checkAndRequestPermissions(mContext: Context): Boolean {

            val location = ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
            val coarselocation = ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_COARSE_LOCATION)

            val listPermissionsNeeded = ArrayList<String>()
            if (location != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION)
            }
            if (coarselocation != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(android.Manifest.permission.ACCESS_COARSE_LOCATION)
            }
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(mContext as Activity, listPermissionsNeeded.toTypedArray(), REQUEST_ID_MULTIPLE_PERMISSIONS)
                return false
            }
            return true
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }


}