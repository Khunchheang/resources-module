package com.domrey.resourcesmodule.data.type

import com.domrey.resourcesmodule.R

sealed class GenderType(val genderTypeRes: Int, val code: String) {

   companion object {
      private const val MALE_CODE = "MALE"
      private const val FEMALE_CODE = "FEMALE"
      private const val UNKNOWN_CODE = ""

      fun fromCodeToGenderType(code: String): GenderType {
         return when (code) {
            FEMALE_CODE -> Female()
            MALE_CODE -> Male()
            else -> Unknown()
         }
      }
   }

   class Female : GenderType(R.string.female, FEMALE_CODE)

   class Male : GenderType(R.string.male, MALE_CODE)

   class Unknown : GenderType(R.string.unknown, UNKNOWN_CODE)

}