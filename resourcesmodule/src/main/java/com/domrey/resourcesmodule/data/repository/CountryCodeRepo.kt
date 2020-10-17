package com.domrey.resourcesmodule.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.domrey.resourcesmodule.data.local.CountryCodeDao
import com.domrey.resourcesmodule.data.local.room.CountryCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CountryCodeRepo @Inject constructor(private val countryCodeDao: CountryCodeDao) {

   suspend fun getCountryCode(query: String): LiveData<List<CountryCode>> {
      val countryCode = MutableLiveData<List<CountryCode>>()
      withContext(Dispatchers.Main) {
         countryCode.value = if (query.isEmpty()) countryCodeDao.loadCountryCode()
         else countryCodeDao.searchCountry("%$query%")
      }
      return countryCode
   }

}