package com.domrey.resourcesmodule.di

import androidx.lifecycle.ViewModel
import com.domrey.resourcesmodule.dialog.locationpicker.LocationPickerDialog
import com.domrey.resourcesmodule.dialog.locationpicker.LocationPickerViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

/**
 * Created by : khunchheang
 * Date  : 10/18/20
 * Time  : 10:02 AM
 */
@Module
interface LocationPickerModule {

    @ContributesAndroidInjector
    fun bindLocationPickerDialog(): LocationPickerDialog

    @Binds
    @IntoMap
    @ViewModelKey(LocationPickerViewModel::class)
    fun bindLocationPickerViewModel(locationPickerViewModel: LocationPickerViewModel): ViewModel

}