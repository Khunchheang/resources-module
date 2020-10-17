package com.domrey.resourcesmodule.util

object Constants {

   /*Date format*/
   const val SERVER_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ"
   const val WEEK_MONTH_DAY_YEAR_FORMAT = "E, MMM d yyyy"
   const val YEAR_MONTH_DAY_FORMAT = "yyyy-MM-dd"
   const val YEAR_MONTH_DAY_DASH_HOUR_MIN_SEC = "yyyyMMdd_HHmmss"
   const val HOUR_MIN_FORMAT = "hh:mm aa"
   const val WEEK_DAY_MONTH_YEAR_HOUR_MIN_FORMAT = "E, d MMM yyyy hh:mm aa"
   const val MONTH_DAY_FORMAT = "MMM dd"
   /*const val YEAR_MONTH_NO_DASH_FORMAT = "yyyyMMdd"
   const val MONTH_DAY_ONLY_FORMAT = "MMM d"
   const val MONTH_DAY_YEAR_HOUR_FORMAT = "mm dd, yyyy HH:mm"
   const val TIME_YEAR_MONTH_DIS_FORMAT = "HH:mm - E, MMM d"
   const val TIME_YEAR_MONTH_PARSE_FORMAT = "yyyy-MM-dd HH:mm:ss"
   const val TIME_YEAR_MONTH_T_PARSE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"*/

   /*Delay bottom sheet dialog dismiss*/
   const val SORT_DELAY_BOTTOM_DISMISS = 150L

   /*Count down timer*/
   const val COUNT_DOWN_INTERVAL = 1000L
   const val ONE_SECOND = 60000L

   //Decimal format
   const val DECIMAL_FORMAT = "#,###,###.##"

   /*Change Language Key*/
   const val CHANGED_LANGUAGE = "change_language"
   const val CHANGED = 1
   const val RESET_CHANGE = 0

   /*Country Code*/
   const val DEFAULT_COUNTRY_CODE = "+855"

   /*Latlng*/
   const val DEFAULT_LATITUDE = 11.5449
   const val DEFAULT_LONGITUDE = 104.8922

   const val PASSWORD_PATTERN =
      "^(?=.*?[A-Z])(?=(.*[a-z])+)(?=(.*[\\d])+)(?=(.*[\\W])+)(?!.*\\s).{8,}\$"

}