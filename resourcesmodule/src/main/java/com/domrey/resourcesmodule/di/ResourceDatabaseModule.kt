package com.domrey.resourcesmodule.di

import android.content.Context
import com.domrey.resourcesmodule.data.local.ResourceDatabase
import com.domrey.resourcesmodule.data.local.CountryCodeDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ResourceDatabaseModule {

   @Singleton
   @Provides
   fun provideDatabase(context: Context): ResourceDatabase {
      return ResourceDatabase.getInstance(context)
   }

   @Singleton
   @Provides
   fun provideCountryCodeDao(db: ResourceDatabase): CountryCodeDao {
      return db.countryCodeDao()
   }

}