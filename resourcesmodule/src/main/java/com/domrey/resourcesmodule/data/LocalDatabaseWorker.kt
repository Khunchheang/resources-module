package com.domrey.resourcesmodule.data

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.domrey.resourcesmodule.data.local.ResourceDatabase
import com.domrey.resourcesmodule.data.local.room.CountryCode
import com.domrey.resourcesmodule.extension.loadJsonFromAsset
import kotlinx.coroutines.coroutineScope
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDatabaseWorker @Inject constructor(
   context: Context,
   workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

   @Suppress("BlockingMethodInNonBlockingContext")
   override suspend fun doWork(): Result = coroutineScope {
      //Country Code
      try {
         val json = COUNTRY_CODE_JSON.loadJsonFromAsset(applicationContext)
         val jsonArray = JSONArray(json)
         for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.get(i) as JSONObject
            val countryCode = CountryCode().also {
               it.english = jsonObject.getString("english")
               it.chinese = jsonObject.getString("chinese")
               it.khmer = jsonObject.getString("khmer")
               it.japanese = jsonObject.getString("japanese")
               it.korean = jsonObject.getString("korean")
               it.indonesian = jsonObject.getString("indonesian")
               it.malaysian = jsonObject.getString("malaysian")
               it.dialCode = jsonObject.getString("dial_code")
               it.code = jsonObject.getString("code")
            }
            ResourceDatabase.getInstance(applicationContext).countryCodeDao().insert(countryCode)
         }
         Result.success()
      } catch (ex: IOException) {
         ex.localizedMessage
         Result.failure()
      }
   }

   companion object {
      private const val COUNTRY_CODE_JSON = "countrycodes.json"
   }

}