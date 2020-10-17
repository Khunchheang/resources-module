package com.domrey.resourcesmodule.di

import android.content.Context
import android.os.Handler
import androidx.paging.PagedList
import com.domrey.resourcesmodule.R
import com.domrey.resourcesmodule.app.AppExecutors
import com.domrey.resourcesmodule.customview.DividerItemDecoration
import com.domrey.resourcesmodule.customview.ItemOffsetDecoration
import com.domrey.resourcesmodule.dialog.countrycode.CountryCodeAdapter
import com.domrey.resourcesmodule.helpers.permission.PermissionsFactory
import com.domrey.resourcesmodule.util.Constants
import com.google.android.gms.maps.MapView
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
   fun provideMapView(context: Context): MapView {
      val mapView = MapView(context)
      mapView.onCreate(null)
      mapView.onPause()
      mapView.onDestroy()
      return mapView
   }

   @Provides
   fun provideHandler() = Handler()

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
   fun provideItemOffsetDecorator(context: Context): ItemOffsetDecoration {
      val space = context.resources.getDimensionPixelOffset(R.dimen.default_spacing_small)
      return ItemOffsetDecoration(space)
   }

   @Provides
   @Singleton
   fun provideDividerDecorator(context: Context) = DividerItemDecoration(context)

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

   @Provides
   fun provideCountryCodeAdapter(context: Context, appExecutors: AppExecutors) =
      CountryCodeAdapter(context, appExecutors)

   companion object {
      const val DOB_CALENDAR_CONSTRAINT_BUILDER = "dob_calendar_constraint"
   }
}