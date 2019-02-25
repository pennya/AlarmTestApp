package com.duzi.locationmanagertest

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    var manager: LocationManager? = null
    var enableProviders: List<String>? = null
    var bestAccuracy: Float = 0.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 100)
        } else {
            getProviders()
            getLocation()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getProviders()
            getLocation()
        }
    }

    private fun getProviders() {
        val sb = StringBuilder()
        val providers = manager?.allProviders
        providers?.let { providers ->
            for (provider in providers) {
                sb.append("${provider},")
            }
            lab1_allProviders.text = sb.toString()
        }

        val sb2 = StringBuilder()
        enableProviders = manager?.getProviders(true)
        enableProviders?.let { providers ->
            for(provider in providers) {
                sb2.append("${provider},")
            }
            lab1_enableProviders.text = sb2.toString()
        }
    }

    private fun getLocation() {
        enableProviders?.let { providers ->
            for(provider in providers) {
                var location: Location? = null
                if(ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
                    location = manager?.getLastKnownLocation(provider)
                } else {
                    Toast.makeText(this@MainActivity, "no permission", Toast.LENGTH_SHORT).show()
                }

                if(location != null ) {
                    val accuracy = location.accuracy
                    if(bestAccuracy == 0.toFloat()) {
                        bestAccuracy = accuracy
                        setLocationInfo(provider, location)
                    } else {
                        if(accuracy < bestAccuracy) {
                            bestAccuracy = accuracy
                            setLocationInfo(provider, location)
                        }
                    }
                }
            }
        }
    }

    private fun setLocationInfo(provider: String, location: Location?) {
        if(location != null) {
            lab1_provider.text = provider
            lab1_latitude.text = location.latitude.toString()
            lab1_longitude.text = location.longitude.toString()

            val date = Date(location.time)
            val sd = SimpleDateFormat("yyyy-MM-dd HH:mm")
            lab1_time.text = sd.format(date)

            lab1_onOffView.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_on, null))
        }
    }
}
