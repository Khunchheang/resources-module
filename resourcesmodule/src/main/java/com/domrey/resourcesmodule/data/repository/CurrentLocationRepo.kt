package com.domrey.resourcesmodule.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Bundle
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import javax.inject.Inject

@Suppress("DEPRECATION")
@SuppressLint("MissingPermission")
class CurrentLocationRepo @Inject constructor(private val context: Context) : LocationListener {

   private var completion: ((latLng: LatLng?) -> Unit)? = null
   private var googleApiClient: GoogleApiClient? = null
   private var fusedLocationClient: FusedLocationProviderClient? = null
   private var fuseLocationApi: FusedLocationProviderApi? = null
   private var googleClientBuilder: GoogleApiClient.Builder? = null

   override fun onLocationChanged(location: Location?) {
      if (location == null) {
         stopGetLocation()
         completion?.invoke(null)
      } else {
         if (location.accuracy < 40) stopGetLocation()
         completion?.invoke(LatLng(location.latitude, location.longitude))
      }
   }

   fun startGetLocation(locationRequest: LocationRequest, completion: (lagLng: LatLng?) -> Unit) {
      this.completion = completion

      if (fuseLocationApi == null) fuseLocationApi = LocationServices.FusedLocationApi
      if (googleClientBuilder == null) {
         googleClientBuilder = GoogleApiClient.Builder(context).addApi(LocationServices.API)
      }

      googleClientBuilder?.addConnectionCallbacks(object : GoogleApiClient.ConnectionCallbacks {
         override fun onConnected(bundle: Bundle?) {
            fuseLocationApi?.requestLocationUpdates(
               googleApiClient,
               locationRequest,
               this@CurrentLocationRepo
            )
         }

         override fun onConnectionSuspended(i: Int) {
            completion.invoke(null)
         }
      })

      if (googleApiClient == null) googleApiClient = googleClientBuilder?.build()
      googleApiClient?.connect()
   }

   fun getLastKnownLocation(completion: (lagLng: LatLng?) -> Unit) {
      this.completion = completion
      if (fusedLocationClient == null) {
         fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
      }
      fusedLocationClient?.lastLocation?.addOnSuccessListener { location ->
         onLocationChanged(location)
      }
   }

   fun stopGetLocation() {
      if (googleApiClient != null && googleApiClient?.isConnected!!) {
         fuseLocationApi?.removeLocationUpdates(googleApiClient, this)
         googleApiClient?.disconnect()
      }
   }
}