package com.example.task

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.namespace.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import io.realm.Realm
import io.realm.RealmResults

class LocationMainActivity : AppCompatActivity(), OnMapReadyCallback,
    LocationAdapter.OnItemClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: LocationAdapter
    private lateinit var realm: Realm
    private lateinit var googleMap: GoogleMap
    private var selectedMarker: Marker? = null
    private lateinit var playbackButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_list_view)

        Realm.init(this)
        val realm = Realm.getDefaultInstance()
        val locations: RealmResults<LocationData> = realm.where(LocationData::class.java).findAll()

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = LocationAdapter(locations, this)
        recyclerView.adapter = adapter

        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)


        startService(Intent(this, LocationService::class.java))
        playbackButton = findViewById(R.id.playbackButton)
        playbackButton.setOnClickListener {
            animateLocationPlayback()
        }

    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        googleMap.setInfoWindowAdapter(CustomInfoWindowAdapter(layoutInflater))
    }


    override fun onItemClick(locationData: LocationData) {
        val latLng = LatLng(locationData.latitude, locationData.longitude)
        selectedMarker?.remove()
        selectedMarker = googleMap.addMarker(
            MarkerOptions().position(latLng).title("Location at ${locationData.timestamp}")
                .snippet("Lat: ${locationData.latitude}, Lng: ${locationData.longitude}")
        )
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
        selectedMarker?.showInfoWindow()
    }


    private fun animateLocationPlayback() {


        val userLocationHistory = realm.where(LocationData::class.java)
            .equalTo("userId", "1")
            .findAll()

        if (userLocationHistory.isNotEmpty()) {
            val polylineOptions = PolylineOptions().width(5f).color(Color.BLUE)
            userLocationHistory.forEach { locationData ->
                polylineOptions.add(LatLng(locationData.latitude, locationData.longitude))
            }
            val polyline = googleMap.addPolyline(polylineOptions)
            val cameraPosition = CameraPosition.Builder()
                .target(
                    LatLng(
                        userLocationHistory.first()!!.latitude,
                        userLocationHistory.first()!!.longitude
                    )
                )
                .zoom(15f)
                .build()
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({
                polyline.remove()
            }, (userLocationHistory.size * 1000).toLong())
        } else {

            Toast.makeText(
                this,
                "No location history found for the logged-in user",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
}