package com.domrey.resourcesmodule.data.local.converter

import androidx.room.TypeConverter
import java.util.*

/**
 * Created by : khunchheang
 * Date  : 6/16/20
 * Time  : 1:50 PM
 */
class DateConverter {

   @TypeConverter
   fun fromTimeToDate(time: Long): Date {
      return Date((time))
   }

   @TypeConverter
   fun fromDateToTime(date: Date): Long {
      return date.time
   }

}