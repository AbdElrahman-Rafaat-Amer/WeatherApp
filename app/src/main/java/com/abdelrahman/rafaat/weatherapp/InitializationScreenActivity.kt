package com.abdelrahman.rafaat.weatherapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import java.io.IOException
import java.util.*

class InitializationScreenActivity : AppCompatActivity() {

    private val TAG = "InitializationScreenActivity"
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val PERMISSION_ID_LOCATION = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intializaion_screen)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (isInternetAvailable()) {
            Log.i(TAG, "getLocationButton: isInternetAvailable + true")
            if (checkPermission()) {
                Log.i(TAG, "getLocationButton: checkPermission + true")
                getLastLocation()
            } else {
                Log.i(TAG, "getLocationButton: requestPermission + false")
                requestPermission()
            }
        } else {
            Log.i(TAG, "getLocationButton: isInternetAvailable + false")
            Toast.makeText(
                this,
                "Please turn on Wifi or mobile Data",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun checkPermission(): Boolean {
        var isGranted = false
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            isGranted = true
            Log.i("TAG", "checkPermission: true ")
        } else {
            Log.i("TAG", "checkPermission: false ")
        }
        return isGranted
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ), PERMISSION_ID_LOCATION
        )
        Log.i("TAG", "requestPermission: ")
    }

    private fun isInternetAvailable(): Boolean {
        var isAvailable = false
        val manager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        if (manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)!!.isConnected || manager.getNetworkInfo(
                ConnectivityManager.TYPE_MOBILE)!!.isConnected)
            isAvailable = true
        return isAvailable
    }

    private fun getAddress(latitude: Double, longitude: Double) {
        val result = StringBuilder()
        try {
            val geocoder = Geocoder(this, Locale.getDefault())
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            if (addresses.size > 0) {
                val address = addresses[0]
                result.append(address.locality).append("\n")
                result.append(address.countryName)
            }
            Log.i(TAG, "getAddress: " + addresses)
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("ADDRESS", addresses[0].toString())
            intent.putExtra("LATITUDE", latitude)
            intent.putExtra("LONGITUDE", longitude)
            startActivity(intent)
            finish()
        } catch (e: IOException) {
            Log.e("TAG", e.localizedMessage)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_ID_LOCATION -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (isLocationEnabled()) {
            fusedLocationClient.lastLocation.addOnCompleteListener { task ->
                val location = task.result
                if (location == null) {
                    requestNewLocationData()
                } else {
                    Log.i(TAG, "getLastLocation: getLatitude---> " + location.getLatitude())
                    Log.i(TAG, "getLastLocation: getLongitude--> " + location.getLongitude())
                    getAddress(location.getLatitude(), location.getLongitude())
                    // requestNewLocationData()
                }
            }
        } else {
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        val locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 10000
        locationRequest.fastestInterval = 5000
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.requestLocationUpdates(
            locationRequest, mLocationCallback,
            Looper.myLooper()!!
        )
    }

    private val mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation = locationResult.lastLocation
            Log.i(TAG, "mLocationCallback: getLatitude---> " + mLastLocation.getLatitude())
            Log.i(TAG, "mLocationCallback: getLongitude--> " + mLastLocation.getLongitude())
            getAddress(mLastLocation.getLatitude(), mLastLocation.getLongitude())
        }
    }


    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }
}