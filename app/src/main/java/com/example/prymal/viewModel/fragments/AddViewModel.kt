package com.example.prymal.viewModel.fragments

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Looper
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import com.example.prymal.Firebase
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.add_fragment.*
import java.util.*

class AddViewModel : ViewModel() {
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val PERMISSION_ID = 13

    fun uploadImage(firebase: Firebase, imageUri: Uri, context: Context, description: String, pb_Upload: ProgressBar, location: String) {
        var locationTmp = location

        firebase.getPostName()
        if (location == "Add your location") {
            locationTmp = ""
        }
        firebase.uploadImage(imageUri, context, description, pb_Upload, locationTmp)
    }

    fun uploadImage(firebase: Firebase, imageUri: Uri, context: Context, pb_Upload: ProgressBar, whichImage: Boolean) {
        firebase.updateProfileImages(imageUri, context, pb_Upload, whichImage)
    }

    fun handleLocation(activity: Activity, context: Context) {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity)

        requestPermission(activity)

        getLastLocation(activity, context)
    }

    private fun getLastLocation(activity: Activity, context: Context) {
        if (checkPermission(context)) {
            if (isLocationEnabled(activity)) {
                fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
                    val location: Location? = task.result
                    if (location == null) {
                        newLocation(activity, context)
                    } else {
                        activity.cb_Location.text = getReadableLocation(location.latitude, location.longitude, context)
                    }
                }
            } else {
                Toast.makeText(context, "Please turn ON your device location!", Toast.LENGTH_SHORT).show()
            }
        } else {
            requestPermission(activity)
        }
    }

    private fun getReadableLocation(latitude: Double, longitude: Double, context: Context): String {
        val geoCoder = Geocoder(context, Locale.getDefault())
        val address = geoCoder.getFromLocation(latitude, longitude, 3)
        return address[0].locality + ", " + address[0].countryName
    }

    private fun newLocation(activity: Activity, context: Context) {
        val locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 1
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity)
        if (checkPermission(context)) {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback(activity, context), Looper.myLooper())
        }
    }

    private fun locationCallback(activity: Activity, context: Context): LocationCallback {
        return object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                val lastLocation: Location = p0.lastLocation
                activity.cb_Location.text = getReadableLocation(lastLocation.latitude, lastLocation.longitude, context)
            }
        }
    }

    private fun isLocationEnabled(activity: Activity) : Boolean {
        val locationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun checkPermission(context: Context) : Boolean {
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true
        }
        return false
    }

    private fun requestPermission(activity: Activity) {
        ActivityCompat.requestPermissions(activity, arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_ID)
    }
}