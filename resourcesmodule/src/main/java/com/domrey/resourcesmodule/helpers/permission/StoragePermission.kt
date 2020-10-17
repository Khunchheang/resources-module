package com.domrey.resourcesmodule.helpers.permission

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

class StoragePermission : Permission {

   override fun requestPermission(
      context: Context,
      startAskPer: (perStrings: Array<String>, resultPerCode: Int) -> Unit
   ) {
      startAskPer.invoke(
         arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), Permission.PERMISSION_STORAGE
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
         context,
         Manifest.permission.WRITE_EXTERNAL_STORAGE
      ) == PackageManager.PERMISSION_GRANTED
   }
}