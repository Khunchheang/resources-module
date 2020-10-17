package com.domrey.resourcesmodule.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.domrey.resourcesmodule.data.LocalDatabaseWorker
import com.domrey.resourcesmodule.data.local.room.CountryCode

@Database(
   entities = [
      CountryCode::class
   ],
   version = 1,
   exportSchema = false
)
abstract class ResourceDatabase : RoomDatabase() {

   abstract fun countryCodeDao(): CountryCodeDao

   companion object {

      // For Singleton instantiation
      @Volatile
      private var instance: ResourceDatabase? = null

      fun getInstance(context: Context): ResourceDatabase {
         return instance ?: synchronized(this) {
            instance ?: buildDatabase(context).also { instance = it }
         }
      }

      private fun buildDatabase(context: Context): ResourceDatabase {
         return Room.databaseBuilder(context, ResourceDatabase::class.java, "resource.db")
            .addCallback(object : RoomDatabase.Callback() {
               override fun onCreate(db: SupportSQLiteDatabase) {
                  super.onCreate(db)
                  val request = OneTimeWorkRequestBuilder<LocalDatabaseWorker>().build()
                  WorkManager.getInstance(context).enqueue(request)
               }
            })
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
      }
   }

}