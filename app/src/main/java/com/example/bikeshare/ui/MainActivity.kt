package com.example.bikeshare.ui

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.bikeshare.R
import com.example.bikeshare.data.Bicycle
import com.example.bikeshare.ui.fragments.BicycleFragment
import com.example.bikeshare.ui.viewmodels.BicycleViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Locale

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private val TAG: String = "TESTING"

    private lateinit var geocoder: Geocoder
    private val viewModel: BicycleViewModel by viewModels()
    private lateinit var mMap: GoogleMap
    private val markers = mutableMapOf<Long, Marker>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize geocoder
        geocoder = Geocoder(applicationContext, Locale.getDefault())

        // Set ActionBar title
        supportActionBar?.title = "Lost Bicycles App"

        // Initialize the map fragment and set up the map asynchronously
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Load the BicycleFragment into the container if this is the first creation of the activity
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, BicycleFragment())
                .commit()
        }
    }

    override fun onResume() {
        super.onResume()
        getMapLocations()
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycleScope.launch {
            viewModel.clearDatabase()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        // Initialize mMap when the map is ready
        mMap = googleMap

        // Enable map UI settings
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isZoomGesturesEnabled = true

        // Move camera to a default location (Downtown Toronto)
        val downtownToronto = LatLng(43.651070, -79.347015)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(downtownToronto, 9.5f))

        // Load map locations
        getMapLocations()
    }

    private fun getMapLocations() {
        if (::mMap.isInitialized) { // Check if mMap is initialized before using it
            lifecycleScope.launch {
                viewModel.bicycles.collect { bicycles ->
                    updateMapMarkers(bicycles)
                }
            }
        } else {
            Log.e(TAG, "getMapLocations called before mMap was initialized")
        }
    }

    private fun updateMapMarkers(bicycles: List<Bicycle>) {
        if (::mMap.isInitialized) { // Check if mMap is initialized before using it
            mMap.clear()
            markers.clear()

            bicycles.filter { !it.isFound }.forEach { bicycle ->
                val pos = LatLng(bicycle.lat, bicycle.lng)
                val marker = mMap.addMarker(
                    MarkerOptions().position(pos)
                        .title(bicycle.address)
                        .snippet(bicycle.name)
                )
                marker?.let { markers[bicycle.id] = it }
            }
        } else {
            Log.e(TAG, "updateMapMarkers called before mMap was initialized")
        }
    }

    fun updateMapMarker(bicycle: Bicycle) {
        if (::mMap.isInitialized) { // Check if mMap is initialized before using it
            if (bicycle.isFound) {
                markers[bicycle.id]?.remove()
                markers.remove(bicycle.id)
            } else {
                val pos = LatLng(bicycle.lat, bicycle.lng)
                val marker = mMap.addMarker(
                    MarkerOptions().position(pos)
                        .title(bicycle.address)
                        .snippet(bicycle.name)
                )
                marker?.let { markers[bicycle.id] = it }
            }
        } else {
            Log.e(TAG, "updateMapMarker called before mMap was initialized")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_options, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mi_report_bicycle -> {
                reportBicycle()
                true
            }
            R.id.mi_logout -> {
                finish() // Close the activity to log out
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getAddressFromLocation(latitude: Double, longitude: Double): String {
        val addresses: MutableList<Address>? = geocoder.getFromLocation(latitude, longitude, 1)
        val fullAddress = StringBuilder()

        if (addresses != null && addresses.isNotEmpty()) {
            val address = addresses[0]
            for (i in 0..address.maxAddressLineIndex) {
                fullAddress.append(address.getAddressLine(i)).append("\n")
            }
        } else {
            Log.e(TAG, "No address found for location ($latitude, $longitude)")
        }

        return fullAddress.toString()
    }

    private fun generateTorontoLat(): Double {
        val min = 43.58
        val max = 43.85
        return min + (max - min) * Math.random()
    }

    private fun generateTorontoLng(): Double {
        val min = -79.64
        val max = -79.12
        return min + (max - min) * Math.random()
    }

    private fun generateThreeDigitNumber(): Int {
        val min = 100
        val max = 999
        return (min + (Math.random() * (max - min + 1))).toInt()
    }

    private fun reportBicycle() {
        val lat_gen = generateTorontoLat()
        val lng_gen = generateTorontoLng()
        val addr = getAddressFromLocation(lat_gen, lng_gen)

        if (addr.isEmpty()) {
            Log.e(TAG, "No address found for generated location")
            return
        }

        insertBicycle(
            Bicycle(
                id = 0,
                name = "BIKE-${generateThreeDigitNumber()}",
                address = addr,
                isFound = false,
                lat = lat_gen,
                lng = lng_gen
            )
        )

    }

    fun insertBicycle(bicycle: Bicycle) {
        viewModel.insertBicycle(bicycle)
    }

}
