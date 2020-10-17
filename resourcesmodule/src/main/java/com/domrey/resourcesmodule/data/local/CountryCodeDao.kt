package com.domrey.resourcesmodule.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.domrey.resourcesmodule.data.local.room.CountryCode

@Dao
interface CountryCodeDao {

   @Insert(onConflict = OnConflictStrategy.REPLACE)
   fun insert(vararg country: CountryCode)

   @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun insertAll(plants: List<CountryCode>)

   @Query("SELECT * FROM country_code")
   fun loadCountryCode(): List<CountryCode>

   @Query(
      """SELECT * FROM country_code 
      WHERE khmer LIKE :key
      OR english LIKE :key
      OR chinese LIKE :key
      OR japanese LIKE :key
      OR korean LIKE :key
      OR indonesian LIKE :key
      OR malaysian LIKE :key
      OR dialCode LIKE :key"""
   )
   fun searchCountry(key: String): List<CountryCode>

}