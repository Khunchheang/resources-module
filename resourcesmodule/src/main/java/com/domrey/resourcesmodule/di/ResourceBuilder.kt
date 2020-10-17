package com.domrey.resourcesmodule.di

import androidx.lifecycle.ViewModel
import com.domrey.resourcesmodule.dialog.countrycode.CountryCodeBSDialog
import com.domrey.resourcesmodule.dialog.countrycode.CountryCodeViewModel
import com.domrey.resourcesmodule.dialog.locationpicker.LocationPickerDialog
import com.domrey.resourcesmodule.dialog.locationpicker.LocationPickerViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module(
   includes = [
      ResourceDatabaseModule::class,
      ResourceModule::class,
      ResourceRestClientModule::class
   ]
)
interface ResourceBuilder {

   @ContributesAndroidInjector
   fun bindCountryCodeDialog(): CountryCodeBSDialog

   @Binds
   @IntoMap
   @ViewModelKey(CountryCodeViewModel::class)
   fun bindCountryCodeViewModel(countryCodeViewModel: CountryCodeViewModel): ViewModel

   @ContributesAndroidInjector
   fun bindLocationPickerDialog(): LocationPickerDialog

   @Binds
   @IntoMap
   @ViewModelKey(LocationPickerViewModel::class)
   fun bindLocationPickerViewModel(locationPickerViewModel: LocationPickerViewModel): ViewModel

}