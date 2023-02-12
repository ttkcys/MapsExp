package com.yusudhanlates.ekipprojesi

import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.yusudhanlates.ekipprojesi.databinding.ActivityMapsBinding
import java.util.*

class Maps : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnMapLongClickListener(dinleyici)

        // Latitude --> Enlem
        // Longitude --> Boylam

        /*val ankara = LatLng(39.921603, 32.854440)
        mMap.addMarker(MarkerOptions().position(ankara).title("Marker in Ankara"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ankara,15f))*/

        //casting --> as
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationListener = object : LocationListener {
            override fun onLocationChanged(p0: Location) {
                //lokasyon yada konunm değişince yapılacak işlemler
               // println(p0.latitude)
               // println(p0.longitude)
                mMap.clear()
                val guncelKonum = LatLng(p0.latitude,p0.longitude)
                mMap.addMarker(MarkerOptions().position(guncelKonum).title("Guncel Konumunuz")) // isteğe bağlı eklenebilir.
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(guncelKonum,15f))

                //adrese çevirme işlmei için gerekli
                val geocoder = Geocoder(this@Maps, Locale.getDefault())
                try{

                    val adresListesi = geocoder.getFromLocation(p0.latitude,p0.longitude,1)

                    /*if(adresListesi.size > 0){
                            println(adresListesi.get(0).toString())
                    }*/

                }catch (e: Exception){
                    e.printStackTrace()
                }

            }

        }

        if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            //izin verilmemiş
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),1)

        }else{
            //izin zaten verilmiş
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1,1f,locationListener)
            val sonBilinenKonum = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if(sonBilinenKonum != null){
                val sonBilinenLatLng = LatLng(sonBilinenKonum.latitude,sonBilinenKonum.longitude)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sonBilinenLatLng,15f))
            }
        }



    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == 1){
            if(grantResults.size > 0){
                if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    // izin verildi
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1,1f,locationListener)
                }
            }

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    val dinleyici = object : GoogleMap.OnMapLongClickListener{
        override fun onMapLongClick(p0: LatLng) {
            mMap.clear()
            val geocoder = Geocoder(this@Maps, Locale.getDefault())

            if(p0 != null){

                var adres = ""

                try {

                    val adresListesi = geocoder.getFromLocation(p0.latitude,p0.longitude,1)
                    if(adresListesi.size > 0){
                        if(adresListesi.get(0).thoroughfare != null){
                            adres += adresListesi.get(0).thoroughfare

                            if(adresListesi.get(0).subThoroughfare != null){
                                adres += adresListesi.get(0).subThoroughfare

                            }
                        }
                    }

                }catch (e : Exception){
                    e.printStackTrace()
                }
                mMap.addMarker(MarkerOptions().position(p0).title(adres))
            }
        }
    }

}