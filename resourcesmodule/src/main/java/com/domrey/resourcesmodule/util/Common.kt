package com.domrey.resourcesmodule.util

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.google.android.gms.maps.model.LatLng
import java.io.IOException
import java.util.*

object Common {

   fun getCompleteAddressString(context: Context, latLng: LatLng?): String? {
      latLng ?: return null
      var strAdd: String? = null
      val geoCoder = Geocoder(context, Locale.getDefault())
      try {
         val addresses = geoCoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
         if (addresses != null) {
            val returnedAddress = addresses[0]
            val strReturnedAddress = StringBuilder("")

            //val address = addresses[0].getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            //val city = addresses[0].locality
            val state = addresses[0].adminArea
            val subAdmin = addresses[0].subAdminArea
            val country = addresses[0].countryName
            //val postalCode = addresses[0].postalCode
            val knownName = addresses[0].featureName

            strAdd = if (state != null) {
               "$knownName, $subAdmin, $state, $country"
            } else {
               for (i in 0..returnedAddress.maxAddressLineIndex) {
                  strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n")
               }
               strReturnedAddress.toString()
            }
         }
      } catch (e: Exception) {
         e.printStackTrace()
      }

      return strAdd
   }

   fun getCountryName(context: Context, latitude: Double, longitude: Double): String? {
      val geoCoder = Geocoder(context, Locale.getDefault())
      val addresses: List<Address>?
      return try {
         addresses = geoCoder.getFromLocation(latitude, longitude, 1)
         if (addresses != null && addresses.isNotEmpty()) addresses[0].countryName
         else null
      } catch (ignored: IOException) {
         //do something
         null
      }
   }

   @Suppress("DEPRECATION")
   fun isNetworkAvailable(context: Context): Boolean {
      val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
      val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
      return activeNetwork?.isConnected == true
   }

}