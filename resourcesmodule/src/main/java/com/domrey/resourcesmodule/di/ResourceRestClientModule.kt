package com.domrey.resourcesmodule.di

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import com.domrey.resourcesmodule.BuildConfig
import com.domrey.resourcesmodule.data.CredentialSharePref
import dagger.Module
import dagger.Provides
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

//Request Header Params
const val AUTHORIZATION = "Authorization"
const val BEARER = "Bearer"
const val LOCALIZATION = "X-Localization"
const val APP_ID = "X-AppId"
const val APP_TYPE = "X-AppType"
const val DEVICE_ID = "X-DeviceId"
const val PLAYER_ID = "X-PlayerId"

//Value Header
const val APP_TYPE_VALUE = "Android"
const val APP_ID_VALUE = "12345678-1234-1234-1234-123456789012"
const val BASIC_AUTH_USERNAME = "WeTraceCambodia"
const val BASIC_AUTH_PASSWORD = "WTC8@#2%8998^&\$"

@Module
class ResourceRestClientModule {

   @Provides
   @Singleton
   @SuppressLint("HardwareIds")
   fun provideHeaderInterceptor(
      context: Context,
      credentialPref: CredentialSharePref
   ): Interceptor {
      val basicAuth = Credentials.basic(BASIC_AUTH_USERNAME, BASIC_AUTH_PASSWORD)
      val deviceId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)

      return Interceptor(fun(chain: Interceptor.Chain): Response {
         val tokenBearer = "$BEARER ${credentialPref.accessToken.value}"
         val locale = Locale.getDefault().language
         val playerId = credentialPref.playerId ?: ""

         val request = chain.request()
            .newBuilder()
            .header(APP_ID, APP_ID_VALUE)
            .header(APP_TYPE, APP_TYPE_VALUE)
            .header(DEVICE_ID, deviceId)
            .header(LOCALIZATION, locale)
            .header(PLAYER_ID, playerId)
         if (credentialPref.accessToken.value != null) {
            request.header(AUTHORIZATION, tokenBearer)
         } else {
            request.header(AUTHORIZATION, basicAuth)
         }
         return chain.proceed(request.build())
      })
   }

   @Provides
   @Singleton
   fun provideLoggingHttp(): HttpLoggingInterceptor {
      val logging = HttpLoggingInterceptor()
      if (BuildConfig.DEBUG) logging.level = HttpLoggingInterceptor.Level.BODY
      return logging
   }

   @Provides
   @Singleton
   fun provideHttpClient(
      logging: HttpLoggingInterceptor,
      header: Interceptor
   ): OkHttpClient {
      return OkHttpClient().newBuilder()
         .addInterceptor(header)
         .addInterceptor(logging)
         .retryOnConnectionFailure(true)
         .readTimeout(TIMEOUT.toLong(), TimeUnit.SECONDS)
         .writeTimeout(TIMEOUT.toLong(), TimeUnit.SECONDS)
         .build()
   }

   @Provides
   @Singleton
   fun provideTimeZone(): TimeZone = TimeZone.getTimeZone("UTC")

   companion object {
      private const val TIMEOUT = 10
   }

}