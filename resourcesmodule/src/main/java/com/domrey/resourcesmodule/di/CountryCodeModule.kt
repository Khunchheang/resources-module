package com.domrey.resourcesmodule.di

import android.content.Context
import androidx.lifecycle.ViewModel
import com.domrey.resourcesmodule.data.local.CountryCodeDao
import com.domrey.resourcesmodule.data.local.ResourceDatabase
import com.domrey.resourcesmodule.dialog.countrycode.CountryCodeBSDialog
import com.domrey.resourcesmodule.dialog.countrycode.CountryCodeViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import javax.inject.Singleton

/**
 * Created by : khunchheang
 * Date  : 10/18/20
 * Time  : 9:59 AM
 */
@Module(includes = [CountryCodeDaoModule::class])
interface CountryCodeModule {

    @ContributesAndroidInjector
    fun bindCountryCodeDialog(): CountryCodeBSDialog

    @Binds
    @IntoMap
    @ViewModelKey(CountryCodeViewModel::class)
    fun bindCountryCodeViewModel(countryCodeViewModel: CountryCodeViewModel): ViewModel
}

@Module
class CountryCodeDaoModule {
    @Singleton
    @Provides
    fun provideCountryCodeDao(context: Context): CountryCodeDao {
        return ResourceDatabase.getInstance(context).countryCodeDao()
    }
}