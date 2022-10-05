package com.abdelrahman.rafaat.weatherapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.abdelrahman.rafaat.weatherapp.databinding.ActivityIntializaionScreenBinding
import com.abdelrahman.rafaat.weatherapp.utils.ConnectionLiveData
import com.abdelrahman.rafaat.weatherapp.utils.ConstantsValue
import com.abdelrahman.rafaat.weatherapp.utils.connectInternet
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*


class InitializationScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIntializaionScreenBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val PERMISSION_ID_LOCATION = 0
    private var isInternet = false
    private var isFirstTime = true

    override fun onCreate(savedInstanceState: Bundle?) {
        Locale.setDefault(Locale.forLanguageTag(ConstantsValue.language))
        super.onCreate(savedInstanceState)
        binding = ActivityIntializaionScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (checkPermission()) {
            getLastLocation()
        } else {
            checkInternet()
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
        }
        return isGranted
    }

    private fun checkInternet() {

        val snackbar = showSnackBar(getString(R.string.enable_internet))
        snackbar.setAction(getString(R.string.enable)) {
            connectInternet(this)
        }

        ConnectionLiveData.getInstance(this).observe(this) {
            if (it) {
                isInternet = true
                requestPermission()
                snackbar.dismiss()
            }
        }
        CoroutineScope(Dispatchers.Main).launch {
            delay(1000)
            if (!isInternet) {
                snackbar.show()
            }
        }

    }

    private fun requestPermission() {
        setVisibility(View.GONE, "")
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ), PERMISSION_ID_LOCATION
        )
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
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this, Manifest.permission.ACCESS_COARSE_LOCATION,
                    ) || ActivityCompat.shouldShowRequestPermissionRationale(
                        this, Manifest.permission.ACCESS_FINE_LOCATION,
                    )
                ) {
                    requestPermission()
                } else {
                    val snackbar = showSnackBar(getString(R.string.open_setting))
                    snackbar.setAction(R.string.action_settings) {
                        val intent = Intent()
                        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        val uri = Uri.fromParts(
                            "package",
                            this.packageName,
                            null
                        )
                        intent.data = uri
                        startActivity(intent)
                    }
                    snackbar.show()
                    isFirstTime = false
                    setVisibility(View.VISIBLE, getString(R.string.wait))
                }
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
                    ConstantsValue.longitude = location.longitude.toString()
                    ConstantsValue.latitude = location.latitude.toString()
                    goToNextActivity()
                }
            }
        } else {
            setVisibility(View.VISIBLE, getString(R.string.waiting_enable_gps))
            val snackbar = showSnackBar(getString(R.string.enable_gps))
            snackbar.setAction(getString(R.string.enable)) {
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
            snackbar.show()
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun showSnackBar(message: String): Snackbar {
        val snackBar = Snackbar.make(
            binding.root,
            message,
            Snackbar.LENGTH_INDEFINITE
        ).setActionTextColor(Color.WHITE)
        snackBar.view.setBackgroundColor(getColor(R.color.colorLightBlue))
        return snackBar
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
            ConstantsValue.longitude = mLastLocation?.longitude.toString()
            ConstantsValue.latitude = mLastLocation?.latitude.toString()
            finish()
            goToNextActivity()
        }
    }

    private fun setVisibility(visibility: Int, text: String) {
        binding.waitingAnimation.visibility = visibility
        binding.givePermission.visibility = visibility
        binding.givePermission.text = text
    }

    override fun onResume() {
        super.onResume()
        if (!checkPermission()) {
            if (!isFirstTime) {
                val snackbar = showSnackBar(getString(R.string.open_setting))
                snackbar.setAction(R.string.action_settings) {
                    val intent = Intent()
                    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    val uri = Uri.fromParts(
                        "package",
                        this.packageName,
                        null
                    )
                    intent.data = uri
                    startActivity(intent)
                }
                snackbar.show()
                setVisibility(View.VISIBLE, getString(R.string.wait))
            }
        }

    }

}