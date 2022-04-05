package com.abdelrahman.rafaat.weatherapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.abdelrahman.rafaat.weatherapp.model.ConstantsValue
import com.google.android.gms.location.*
import java.io.IOException
import java.util.*


class InitializationScreenActivity : AppCompatActivity() {

    private val TAG = "InitializationScreenActivity"
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val PERMISSION_ID_LOCATION = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(TAG, "onCreate: " + ConstantsValue.language)
        Locale.setDefault(Locale.forLanguageTag(ConstantsValue.language))
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intializaion_screen)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (checkPermission()) {
            Log.i(TAG, "onCreate: checkPermission will get location")
            getLastLocation()
            ConstantsValue.locationMethod = "G"
        } else {
            Log.i(TAG, "onCreate: requestPermission  will request permission")
            requestPermission()
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
                    Log.i(TAG, "getLastLocation: getLatitude---> " + location.latitude)
                    Log.i(TAG, "getLastLocation: getLongitude--> " + location.longitude)
                    getAddress(location.latitude, location.longitude)

                    // requestNewLocationData()
                }
            }
        } else {
             val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
             startActivity(intent)
            Toast.makeText(this, "Please enable GPS", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
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
            Log.i(TAG, "mLocationCallback: getLatitude---> " + mLastLocation.latitude)
            Log.i(TAG, "mLocationCallback: getLongitude--> " + mLastLocation.longitude)
            getAddress(mLastLocation.latitude, mLastLocation.longitude)
        }
    }


    private fun getAddress(latitude: Double, longitude: Double) {
        Log.i(TAG, "getAddress: begin of method")
        val result = StringBuilder()
        try {
            val geocoder = Geocoder(this, Locale.getDefault())
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            if (addresses.size > 0) {
                val address = addresses[0]
                result.append(address.locality).append("\n")
                result.append(address.countryName)
            }
            Log.i(TAG, "getAddress: $addresses")

            ConstantsValue.address = addresses[0]
            ConstantsValue.longitude = longitude.toString()
            ConstantsValue.latitude = latitude.toString()
            goToNextActivity()
        } catch (e: IOException) {
            Log.e("TAG", e.localizedMessage)
        }
    }

    private fun goToNextActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onRestart() {
        super.onRestart()
        Log.i("TAG", "onRestart: ")
        getLastLocation()
    }
/*    private fun isInternetAvailable(): Boolean {

        var isAvailable = false
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                isAvailable = when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        true
                    }
                    else -> false
                }
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            isAvailable = activeNetworkInfo != null && activeNetworkInfo.isConnected
        }
        return isAvailable

    }*/
}