package com.domrey.resourcesmodule.dialog.locationpicker

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.domrey.resourcesmodule.base.BaseViewModel
import com.domrey.resourcesmodule.data.repository.CurrentLocationRepo
import com.domrey.resourcesmodule.extension.toSingleEvent
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
import javax.inject.Inject

class LocationPickerViewModel @Inject constructor(
   private val currentLocationRepo: CurrentLocationRepo
) : BaseViewModel() {

   private val locationRequest: LocationRequest by lazy {
      LocationRequest.create().apply {
         interval = UPDATE_INTERVAL_IN_MILLISECONDS
         fastestInterval = FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS
         priority = LocationRequest.PRIORITY_HIGH_ACCURACY
      }
   }

   private val _locationSetting = MutableLiveData<ResolvableApiException>()
   val locationSetting: LiveData<ResolvableApiException> = _locationSetting.toSingleEvent()

   private val _currentLocation = MutableLiveData<LatLng?>()
   val currentLocation: LiveData<LatLng?> = _currentLocation.toSingleEvent()

   fun checkLocationSetting(client: SettingsClient) {
      val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
      val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())
      task.addOnSuccessListener { startGetLocation(Activity.RESULT_OK) }
      task.addOnFailureListener { exception ->
         if (exception is ResolvableApiException) _locationSetting.value = exception
      }
   }

   fun startGetLocation(resultCode: Int) {
      if (resultCode == Activity.RESULT_OK) {
         currentLocationRepo.startGetLocation(locationRequest) {
            _currentLocation.value = it
         }
      } else {
         currentLocationRepo.getLastKnownLocation {
            _currentLocation.value = it
         }
      }
   }

   fun stopGetLocation() {
      currentLocationRepo.stopGetLocation()
   }

   override fun onCleared() {
      super.onCleared()
      currentLocationRepo.stopGetLocation()
   }

   companion object {
      const val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 10000
      const val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS: Long = 5000
   }

}