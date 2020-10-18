package com.domrey.resourcesmodule.di

import androidx.paging.PagedList
import com.domrey.resourcesmodule.helpers.permission.PermissionsFactory
import com.domrey.resourcesmodule.util.Constants
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import dagger.Module
import dagger.Provides
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Named
import javax.inject.Singleton

@Module
class ResourceModule {

   @Provides
   fun provideCalendar(): Calendar {
      val calendar = Calendar.getInstance()
      val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
      val month = calendar.get(Calendar.MONTH)
      val year = calendar.get(Calendar.YEAR)
      return GregorianCalendar(year, month, dayOfMonth, 0, 0)
   }

   @Provides
   @Named(Constants.YEAR_MONTH_DAY_FORMAT)
   fun provideYearMonthDayFormat() =
      SimpleDateFormat(Constants.YEAR_MONTH_DAY_FORMAT, Locale.getDefault())

   @Provides
   @Named(Constants.MONTH_DAY_FORMAT)
   fun provideMonthDayFormat() =
      SimpleDateFormat(Constants.MONTH_DAY_FORMAT, Locale.getDefault())

   @Provides
   @Singleton
   fun provideDecimalFormat() = DecimalFormat(Constants.DECIMAL_FORMAT)

   @Provides
   @Singleton
   fun provideAppPermissionFactory() = PermissionsFactory()

   @Provides
   @Singleton
   fun providePageListConfigBuilder() = PagedList.Config.Builder()
      .setPageSize(10)
      .setInitialLoadSizeHint(10)
      .setPrefetchDistance(10)
      .setEnablePlaceholders(false)
      .build()

   @Provides
   @Named(DOB_CALENDAR_CONSTRAINT_BUILDER)
   fun provideDobCalendarConstraint(calendar: Calendar): CalendarConstraints.Builder {
      val dobCalendarConstraints = CalendarConstraints.Builder()
      dobCalendarConstraints.setEnd(calendar.timeInMillis)
      dobCalendarConstraints.setValidator(DateValidatorPointBackward.now())
      return dobCalendarConstraints
   }

   companion object {
      const val DOB_CALENDAR_CONSTRAINT_BUILDER = "dob_calendar_constraint"
   }
}