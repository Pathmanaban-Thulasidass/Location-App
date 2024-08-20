package com.example.mylocationapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Looper
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import java.util.Locale

class LocationUtils(val context : Context){


    //The FusedLocationProviderClient uses a combination of different
    // sources to provide the location information (e.g., GPS, Wi-Fi, cellular networks).
    private val _fusedLocationClient : FusedLocationProviderClient
        = LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    fun requestLocationUpdates123(viewModal: LocationViewModal){
        // Defines how to handle location updates. => locationCallback variable
        val locationCallback = object : LocationCallback(){
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                locationResult.lastLocation?.let {
                    val location = LocationData(latitude = it.latitude, longitude = it.longitude)
                    viewModal.updateLocation(location)
                }
            }
        }
        val locationrequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY,1000).build()
                                //locationrequest variable =>  Ensures the callbacks are handled on the main thread.
        _fusedLocationClient.requestLocationUpdates(locationrequest,locationCallback, Looper.getMainLooper())

    }

    fun hasLocationPermission(context: Context):Boolean
    {
        return ContextCompat.checkSelfPermission(context,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context,Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    fun reverseGeoCodeLocation(location: LocationData) : String {
        val geocoder = Geocoder(context, Locale.getDefault())
//        val coordinate = LatLng(location.longitude,location.latitude)
        val addresses : MutableList<Address>? = geocoder.getFromLocation(location.latitude,location.longitude,1)
        return if(addresses?.isNotEmpty() == true){
            addresses[0].getAddressLine(0)
        }
        else{
           "Oops Not Found Your Location!!!"
        }
    }
}


