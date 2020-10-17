package com.domrey.resourcesmodule.helpers.permission

class PermissionsFactory {
   fun getPermission(permissionCode: Int): Permission {
      return when (permissionCode) {
         Permission.PERMISSION_CAMERA_STORAGE -> CameraStoragePermission()
         Permission.PERMISSION_STORAGE -> StoragePermission()
         Permission.PERMISSION_LOCATION -> LocationPermission()
         Permission.PERMISSION_STORAGE_LOCATION -> StorageLocationPermission()
         else -> StoragePermission()
      }
   }
}