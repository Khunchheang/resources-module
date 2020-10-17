package com.domrey.resourcesmodule.base.fragment

import android.content.Intent
import android.content.IntentSender
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.domrey.resourcesmodule.R
import com.domrey.resourcesmodule.dialog.locationpicker.LocationPickerViewModel
import com.domrey.resourcesmodule.helpers.permission.Permission
import com.domrey.resourcesmodule.helpers.permission.PermissionsFactory
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.maps.model.MapStyleOptions
import javax.inject.Inject

open class BaseCurrentLocationFragment<V : ViewDataBinding>(@LayoutRes val contentId: Int) :
   BaseInjectFragment<V>(contentId) {

   @Inject
   lateinit var permissionFactory: PermissionsFactory

   private val locationPermission: Permission by lazy {
      permissionFactory.getPermission(Permission.PERMISSION_LOCATION)
   }
   private val settingClient: SettingsClient by lazy {
      LocationServices.getSettingsClient(requireContext())
   }
   val mapStyle:MapStyleOptions by lazy {
      MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style_standard)
   }
   val viewModel: LocationPickerViewModel by viewModels { factory }

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      askPermission()
      handleGpsSetting()
   }

   override fun onPause() {
      super.onPause()
      viewModel.stopGetLocation()
   }

   override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
      super.onActivityResult(requestCode, resultCode, data)
      if (requestCode == Permission.REQUEST_CHECK_SETTINGS) {
         viewModel.startGetLocation(resultCode)
      }
   }

   override fun onRequestPermissionsResult(
      requestCode: Int,
      permissions: Array<out String>,
      grantResults: IntArray
   ) {
      super.onRequestPermissionsResult(requestCode, permissions, grantResults)
      locationPermission.permissionResult(grantResults, {
         if (requestCode == Permission.PERMISSION_LOCATION) {
            viewModel.checkLocationSetting(settingClient)
         }
      }, {
         Toast.makeText(context, R.string.permissions_denied, Toast.LENGTH_SHORT).show()
      })
   }

   fun askPermission() {
      if (locationPermission.isPermissionGranted(requireContext())) {
         viewModel.checkLocationSetting(settingClient)
      } else {
         locationPermission.requestPermission(requireContext()) { perStrings, resultPerCode ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
               requestPermissions(perStrings, resultPerCode)
            }
         }
      }
   }

   private fun handleGpsSetting() {
      viewModel.locationSetting.observe(this) {
         try {
            it.startResolutionForResult(activity, Permission.REQUEST_CHECK_SETTINGS)
         } catch (sendEx: IntentSender.SendIntentException) {
            sendEx.localizedMessage
         }
      }
   }
}