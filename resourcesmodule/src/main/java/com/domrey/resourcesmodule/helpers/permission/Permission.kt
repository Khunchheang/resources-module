package com.domrey.resourcesmodule.helpers.permission

import android.content.Context

interface Permission {

   fun requestPermission(
      context: Context,
      startAskPer: (perStrings: Array<String>, resultPerCode: Int) -> Unit
   )

   fun permissionResult(
      grantResults: IntArray,
      onPerGranted: () -> Unit,
      onPerDenied: () -> Unit
   )

   fun isPermissionGranted(context: Context): Boolean

   companion object {
      //Permissions
      const val PERMISSION_CAMERA_STORAGE = 312
      const val PERMISSION_STORAGE = 132
      const val PERMISSION_STORAGE_LOCATION = 321
      const val PERMISSION_LOCATION = 123
      const val REQUEST_CHECK_SETTINGS = 2
   }

}