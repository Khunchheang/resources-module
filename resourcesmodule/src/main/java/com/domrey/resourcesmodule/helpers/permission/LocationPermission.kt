package com.domrey.resourcesmodule.helpers.permission

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

class LocationPermission : Permission {

   override fun requestPermission(
      context: Context,
      startAskPer: (perStrings: Array<String>, resultPerCode: Int) -> Unit
   ) {
      startAskPer.invoke(
         arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
         Permission.PERMISSION_LOCATION
      )
   }

   override fun permissionResult(
      grantResults: IntArray,
      onPerGranted: () -> Unit,
      onPerDenied: () -> Unit
   ) {
      if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
         onPerGranted.invoke()
      } else {
         onPerDenied.invoke()
      }
   }

   override fun isPermissionGranted(context: Context): Boolean {
      return ContextCompat.checkSelfPermission(
         context, Manifest.permission.ACCESS_FINE_LOCATION
      ) == PackageManager.PERMISSION_GRANTED
   }

}