package com.domrey.resourcesmodule.helpers.permission

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

class StorageLocationPermission : Permission {

   override fun requestPermission(
      context: Context,
      startAskPer: (perStrings: Array<String>, resultPerCode: Int) -> Unit
   ) {
      val permissions = arrayOf(
         Manifest.permission.WRITE_EXTERNAL_STORAGE,
         Manifest.permission.ACCESS_FINE_LOCATION
      )
      startAskPer.invoke(permissions, Permission.PERMISSION_STORAGE_LOCATION)
   }

   override fun permissionResult(
      grantResults: IntArray,
      onPerGranted: () -> Unit,
      onPerDenied: () -> Unit
   ) {
      if (grantResults.isNotEmpty()) {
         val permissionsNotGranted = grantResults.filter { it != PackageManager.PERMISSION_GRANTED }
         if (permissionsNotGranted.isEmpty()) {
            onPerGranted.invoke()
         } else {
            onPerDenied.invoke()
         }
      }
   }

   override fun isPermissionGranted(context: Context): Boolean {
      val permissions = arrayOf(
         Manifest.permission.WRITE_EXTERNAL_STORAGE,
         Manifest.permission.ACCESS_FINE_LOCATION
      )
      val permissionsNotGranted =
         permissions.filter {
            ContextCompat.checkSelfPermission(
               context,
               it
            ) != PackageManager.PERMISSION_GRANTED
         }
      return permissionsNotGranted.isEmpty()
   }
}