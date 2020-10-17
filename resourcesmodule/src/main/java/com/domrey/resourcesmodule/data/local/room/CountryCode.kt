package com.domrey.resourcesmodule.data.local.room

import androidx.room.Entity

@Entity(tableName = "country_code", primaryKeys = ["code"])
class CountryCode {

   var english: String? = null

   var chinese: String? = null

   var khmer: String? = null

   var japanese: String? = null

   var korean: String? = null

   var indonesian: String? = null

   var malaysian: String? = null

   var dialCode: String? = null

   lateinit var code: String

}