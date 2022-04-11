package com.abdelrahman.rafaat.weatherapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.abdelrahman.rafaat.weatherapp.model.ConstantsValue
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar
import java.util.*


class InitializationScreenActivity : AppCompatActivity() {

    private val TAG = "InitializationScreenActivity"
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val PERMISSION_ID_LOCATION = 0
    private lateinit var backGround: ImageView
    private lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {

        Log.i(TAG, "onCreate: " + ConstantsValue.language)
        Locale.setDefault(Locale.forLanguageTag(ConstantsValue.language))
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intializaion_screen)

        backGround = findViewById(R.id.background_InitializationScreenActivity)
        textView = findViewById(R.id.text_InitializationScreenActivity)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (checkPermission()) {
            Log.i(TAG, "onCreate: checkPermission will get location")
            if (ConstantsValue.locationMethod.equals("M")) {
                goToNextActivity()
            } else {
                getLastLocation()
                ConstantsValue.locationMethod = "G"
            }
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
            } else {
                requestPermission()
            }
        }
    }

    private fun goToNextActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (isLocationEnabled()) {
            fusedLocationClient.lastLocation.addOnCompleteListener { task ->
                val location = task.result
                if (location == null) {
                    Toast.makeText(this, "location " + null, Toast.LENGTH_SHORT).show()
                    requestNewLocationData()
                } else {
                    Log.i(TAG, "getLastLocation: " + location.longitude)
                    Log.i(TAG, "getLastLocation: " + location.latitude)
                    ConstantsValue.longitude = location.longitude.toString()
                    ConstantsValue.latitude = location.latitude.toString()

                    finish()
                    goToNextActivity()
                }
            }
        } else {
            backGround.visibility = View.VISIBLE
            textView.visibility = View.VISIBLE
            showSnackBar()
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        var isEnabled =
            locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
            )
        Log.i(TAG, "isLocationEnabled2: isEnabled " + isEnabled)
        return isEnabled
    }

    private fun showSnackBar() {
        var snackBar = Snackbar.make(
            findViewById(R.id.ConstraintLayout_InitializationScreenActivity),
            getString(R.string.enable_gps),
            Snackbar.LENGTH_INDEFINITE
        ).setActionTextColor(Color.WHITE)

        snackBar.setAction(getString(R.string.enable)) {
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
        snackBar.view.setBackgroundColor(Color.RED)
        snackBar.show()
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
            ConstantsValue.longitude = mLastLocation.longitude.toString()
            ConstantsValue.latitude = mLastLocation.latitude.toString()
            finish()
            goToNextActivity()
        }
    }


    override fun onRestart() {
        super.onRestart()
        Log.i("TAG", "onRestart: ")
        getLastLocation()
    }
}