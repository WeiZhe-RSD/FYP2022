package com.example.fyp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import com.example.fyp.Entity.Cafeteria
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlin.math.ln

class Map : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var map: MapView
    private lateinit var mMap: GoogleMap
    private var lat: Double = 0.0
    private var lng: Double = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val cafeteria  = intent.getParcelableExtra<Cafeteria>("cafeteria")

        if(cafeteria!= null){
            lat = cafeteria.latitude!!
            lng = cafeteria.longtitude!!
        }

        map = findViewById<MapView>(R.id.gMap)

        map.getMapAsync(this)
        map.onCreate(savedInstanceState)

    }

    override fun onResume() {
        super.onResume()
        map.onResume()
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }

    override fun onStop() {
        super.onStop()
        map.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        map.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        map.onSaveInstanceState(outState)
    }
    override fun onMapReady(p0: GoogleMap) {
        mMap = p0;
        mMap.uiSettings.isZoomControlsEnabled = true
        val location = LatLng(lat,lng)
        mMap.addMarker(
            MarkerOptions()
                .position(location)
                .title("Marker in Sydney"))
        mMap.setMinZoomPreference(15.0f)
        mMap.setMaxZoomPreference(20.0f)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location))

    }
}