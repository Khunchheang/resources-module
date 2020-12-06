package com.domrey.resourcesmodule.util

object Constants {

   /*Date format*/
   const val SERVER_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ"
   const val YEAR_MONTH_DAY_DASH_HOUR_MIN_SEC = "yyyyMMdd_HHmmss"
   const val HOUR_MIN_FORMAT = "hh:mm aa"
   const val WEEK_DAY_MONTH_YEAR_HOUR_MIN_FORMAT = "E, d MMM yyyy hh:mm aa"

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

   const val PASSWORD_PATTERN =
      "^(?=.*?[A-Z])(?=(.*[a-z])+)(?=(.*[\\d])+)(?=(.*[\\W])+)(?!.*\\s).{8,}\$"

}