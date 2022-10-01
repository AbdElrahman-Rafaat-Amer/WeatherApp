package com.abdelrahman.rafaat.weatherapp.maps.view

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.abdelrahman.rafaat.weatherapp.MainActivity
import com.abdelrahman.rafaat.weatherapp.R
import com.abdelrahman.rafaat.weatherapp.database.ConcreteLocaleSource
import com.abdelrahman.rafaat.weatherapp.maps.viewmodel.MapViewModel
import com.abdelrahman.rafaat.weatherapp.maps.viewmodel.MapViewModelFactory
import com.abdelrahman.rafaat.weatherapp.utils.ConstantsValue
import com.abdelrahman.rafaat.weatherapp.model.FavoritePlaces
import com.abdelrahman.rafaat.weatherapp.model.Repository
import com.abdelrahman.rafaat.weatherapp.network.WeatherClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class GoogleMapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private val TAG = "GoogleMapsActivity"
    private lateinit var mMap: GoogleMap
    private lateinit var latitude: String
    private lateinit var longitude: String
    private lateinit var selectedPlace: String
    private lateinit var selectedDate: String
    private lateinit var mapDoneFloatingActionButton: FloatingActionButton
    private lateinit var showSelectedLocation: TextView
    private lateinit var viewModelFactory: MapViewModelFactory
    private lateinit var viewModel: MapViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        supportActionBar?.hide()

        val destination = intent.getStringExtra("FROM")

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        viewModelFactory = MapViewModelFactory(
            Repository.getInstance(
                this,
                WeatherClient.getInstance(),
                ConcreteLocaleSource.getInstance(this)
            )
        )


        viewModel = ViewModelProvider(
            this, viewModelFactory
        ).get(MapViewModel::class.java)


        showSelectedLocation = findViewById(R.id.show_selected_location_textView)
        mapDoneFloatingActionButton = findViewById(R.id.map_done_FloatingButton)

        mapDoneFloatingActionButton.setOnClickListener {

            if (destination.equals("FAVORITE")) {
                //Save to FAVORITE
                Log.i(TAG, "onCreate: back to favorite")
                selectedDate = SimpleDateFormat("EE dd-M-yyyy").format(Date())
                Log.i(TAG, "mapDoneFloatingActionButton: longitude---------------> $longitude")
                Log.i(TAG, "mapDoneFloatingActionButton: latitude----------------> $latitude")
                Log.i(TAG, "mapDoneFloatingActionButton: selectedPlace-----------> $selectedPlace")
                Log.i(TAG, "mapDoneFloatingActionButton: selectedDate------------> $selectedDate")
                viewModel.insertToFavorite(
                    FavoritePlaces(
                        latitude.toDouble(),
                        longitude.toDouble(),
                        selectedPlace,
                        selectedDate
                    )
                )
            } else {
                //Save to HOME
                Log.i(TAG, "onCreate: go to home")
                ConstantsValue.latitude = latitude
                ConstantsValue.longitude = longitude
                startActivity(Intent(this, MainActivity::class.java))
            }
            finish()
        }

    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(ConstantsValue.latitude.toDouble(), ConstantsValue.longitude.toDouble())

        val marker = mMap.addMarker(
            MarkerOptions().position(sydney).draggable(true).title("Marker in Sydney")
        )

        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        mMap.moveCamera(CameraUpdateFactory.zoomTo(5f))



        mMap.setOnMapClickListener(object : GoogleMap.OnMapClickListener {
            override fun onMapClick(p0: LatLng) {
                Log.i("TAG", "onMapClick: ")
                if (marker == null) return

                marker.position = p0

                val geocoder = Geocoder(this@GoogleMapsActivity, Locale.getDefault())
                val addresses: List<Address>?
                try {
                    addresses = geocoder.getFromLocation(p0.latitude, p0.longitude, 1)
                    if (!addresses.isNullOrEmpty()) {
                        showSelectedLocation.text = addresses[0].adminArea
                        marker.title = addresses[0].adminArea

                        latitude = marker.position.latitude.toString()
                        longitude = marker.position.longitude.toString()
                        /*  if (addresses[0].locality != null) {
                              selectedPlace = addresses[0].locality
                          } else {*/
                        selectedPlace = addresses[0].adminArea
                        //   }

                        Log.i(
                            TAG,
                            "mapDoneFloatingActionButton: longitude---------------> $longitude"
                        )
                        Log.i(
                            TAG,
                            "mapDoneFloatingActionButton: latitude----------------> $latitude"
                        )
                        Log.i(
                            TAG,
                            "mapDoneFloatingActionButton: selectedPlace-----------> $selectedPlace"
                        )
                        Log.i(TAG, "getLocation: addOnCompleteListener ${addresses[0].locality}")

                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

        })

        mMap.setOnMarkerDragListener(object : OnMarkerDragListener {
            override fun onMarkerDragStart(marker: Marker) {
            }

            override fun onMarkerDrag(marker: Marker) {

            }

            override fun onMarkerDragEnd(marker: Marker) {

                if (marker == null) return

                val geocoder = Geocoder(this@GoogleMapsActivity, Locale.getDefault())
                val addresses: List<Address>?
                try {
                    addresses = geocoder.getFromLocation(
                        marker.position.latitude,
                        marker.position.longitude,
                        1
                    )
                    if (!addresses.isNullOrEmpty()) {
                        showSelectedLocation.text = addresses[0].adminArea
                        marker.title = addresses[0].subAdminArea
                        latitude = marker.position.latitude.toString()
                        longitude = marker.position.longitude.toString()
                        selectedPlace = addresses[0].adminArea
                        Log.i(
                            TAG,
                            "mapDoneFloatingActionButton: longitude---------------> $longitude"
                        )
                        Log.i(
                            TAG,
                            "mapDoneFloatingActionButton: latitude----------------> $latitude"
                        )
                        Log.i(
                            TAG,
                            "mapDoneFloatingActionButton: selectedPlace-----------> $selectedPlace"
                        )
                    }
                } catch (e: IOException) {
                    Log.i(TAG, "onMarkerDragEnd: geocoder IOException ")
                    finish()
                }
                Log.i(TAG, "onMarkerDragEnd: ${marker.position}")
            }
        })

    }


}
