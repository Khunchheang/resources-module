package com.domrey.resourcesmodule.dialog.countrycode

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import com.domrey.resourcesmodule.base.BaseViewModel
import com.domrey.resourcesmodule.data.local.room.CountryCode
import com.domrey.resourcesmodule.data.repository.CountryCodeRepo
import java.util.*
import javax.inject.Inject

class CountryCodeViewModel @Inject constructor(private val countryCodeRepo: CountryCodeRepo) :
   BaseViewModel() {

   private val _query = MutableLiveData<String>().apply { value = "" }
   val countryCodes: LiveData<List<CountryCode>> = _query.switchMap {
      liveData {
         val nationalities = countryCodeRepo.getCountryCode(it)
         emitSource(nationalities)
      }
   }

   fun search(query: String) {
      val input = query.toLowerCase(Locale.getDefault()).trim()
      if (input == _query.value) return
      _query.value = input
   }
}