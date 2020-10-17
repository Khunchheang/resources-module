package com.domrey.resourcesmodule.dialog.locationpicker

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentSender
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.domrey.resourcesmodule.R
import com.domrey.resourcesmodule.app.ViewModelFactory
import com.domrey.resourcesmodule.base.fragment.BaseDialogFragment
import com.domrey.resourcesmodule.databinding.DialogLocationPickerBinding
import com.domrey.resourcesmodule.extension.resetFullStatusBar
import com.domrey.resourcesmodule.extension.setFullStatusBar
import com.domrey.resourcesmodule.helpers.permission.Permission
import com.domrey.resourcesmodule.helpers.permission.PermissionsFactory
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnCameraMoveStartedListener.REASON_API_ANIMATION
import com.google.android.gms.maps.GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class LocationPickerDialog :
   BaseDialogFragment<DialogLocationPickerBinding>(R.layout.dialog_location_picker),
   OnMapReadyCallback {
   @Inject
   lateinit var factory: ViewModelFactory

   @Inject
   lateinit var permissionFactory: PermissionsFactory

   private val locationPermission: Permission by lazy {
      permissionFactory.getPermission(Permission.PERMISSION_LOCATION)
   }

   private val viewModel: LocationPickerViewModel by viewModels { factory }

   private val settingClient: SettingsClient by lazy {
      LocationServices.getSettingsClient(
         requireContext()
      )
   }
   private val mapFragment: SupportMapFragment by lazy { SupportMapFragment.newInstance() }
   private var doneClickListener: ((latLng: LatLng?) -> Unit)? = null
   private var map: GoogleMap? = null
   private val mapPaddingTop: Int by lazy { resources.getDimensionPixelOffset(R.dimen.padding_12x) }
   private var isUserMoveMap = false

   companion object {
      const val MY_LOCATION_ZOOM = 14.0f
      const val PICK_LOCATION_ZOOM = 17f
      const val DEFAULT_LATITUDE = 11.5449
      const val DEFAULT_LONGITUDE = 104.8922

      @Synchronized
      fun newInstance(): LocationPickerDialog {
         return LocationPickerDialog()
      }
   }

   override fun onCreate(savedInstanceState: Bundle?) {
      AndroidSupportInjection.inject(this)
      super.onCreate(savedInstanceState)
      setStyle(STYLE_NO_TITLE, R.style.FullScreenDialogStyle)
   }

   override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
      val dialog = super.onCreateDialog(savedInstanceState)
      dialog.window!!.attributes.windowAnimations = R.style.DialogFadeAnimation
      return dialog
   }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)
      setFullStatusBar()
      childFragmentManager.beginTransaction().add(R.id.rootMap, mapFragment).commit()
      mapFragment.getMapAsync(this)

      setupClickListener()
      askPermission()
      handleGpsSetting()
      handleCurrentLocation()
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

   override fun onDismiss(dialog: DialogInterface) {
      resetFullStatusBar()
      super.onDismiss(dialog)
      doneClickListener = null
   }

   override fun onMapReady(map: GoogleMap?) {
      this.map = map
      map?.uiSettings?.isMyLocationButtonEnabled = false
      map?.setMapStyle(MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style_standard))
      map?.setPadding(0, mapPaddingTop, 0, 0)
      map?.setOnCameraMoveStartedListener {
         if (it == REASON_GESTURE || it == REASON_API_ANIMATION) isUserMoveMap = true
      }

      val defaultLatLng = LatLng(DEFAULT_LATITUDE, DEFAULT_LONGITUDE)
      val defaultMove = CameraUpdateFactory.newLatLngZoom(defaultLatLng, MY_LOCATION_ZOOM)
      map?.moveCamera(defaultMove)
   }

   private fun askPermission() {
      if (locationPermission.isPermissionGranted(context!!)) {
         viewModel.checkLocationSetting(settingClient)
      } else {
         locationPermission.requestPermission(context!!) { perStrings, resultPerCode ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
               requestPermissions(perStrings, resultPerCode)
            }
         }
      }
   }

   private fun setupClickListener() {
      binding.toolbar.setNavigationOnClickListener { dismiss() }
      binding.fabCurrentLocation.setOnClickListener {
         isUserMoveMap = false
         askPermission()
      }
      binding.fabDone.setOnClickListener {
         doneClickListener?.invoke(map?.projection?.visibleRegion?.latLngBounds?.center)
         dismiss()
      }
   }

   private fun handleGpsSetting() {
      viewModel.locationSetting.observe(viewLifecycleOwner) {
         try {
            it.startResolutionForResult(activity, Permission.REQUEST_CHECK_SETTINGS)
         } catch (sendEx: IntentSender.SendIntentException) {
            sendEx.localizedMessage
         }
      }
   }

   private fun handleCurrentLocation() {
      viewModel.currentLocation.observe(viewLifecycleOwner) {
         if (!isUserMoveMap) {
            map?.animateCamera(CameraUpdateFactory.newLatLngZoom(it, PICK_LOCATION_ZOOM))
         }
         map?.isMyLocationEnabled = true
      }
   }

   fun setOnDoneClickListener(doneClickListener: ((latLng: LatLng?) -> Unit)?) =
      apply { this.doneClickListener = doneClickListener }

}