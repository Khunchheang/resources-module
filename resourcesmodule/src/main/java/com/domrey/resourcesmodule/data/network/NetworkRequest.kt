package com.domrey.resourcesmodule.data.network

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.domrey.resourcesmodule.R
import com.domrey.resourcesmodule.data.response.BaseResponse
import com.domrey.resourcesmodule.data.type.ApiResponse
import com.domrey.resourcesmodule.data.type.Resource
import com.domrey.resourcesmodule.rxbus.RxBus
import com.domrey.resourcesmodule.rxbus.RxEvent
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.UnknownHostException

abstract class NetworkRequest<T> @MainThread constructor() {

   val result = MutableLiveData<Resource<T>>()

   @MainThread
   private fun setValue(newValue: Resource<T>) {
      if (result.value != newValue) {
         result.postValue(newValue)
      }
   }

   suspend fun fetchFromNetwork() {
      setValue(Resource.Loading())

      withContext(Dispatchers.IO) {

         when (val apiResponse = RetrofitRequest.handleNetworkRequest { createCall() }) {
            is ApiResponse.ApiSuccessResponse -> {
               setValue(Resource.Success(apiResponse.body.data))
            }
            is ApiResponse.ApiErrorResponse -> {
               var title = R.string.pls_try_again
               var msg = R.string.connection_timeout
               val throwable = apiResponse.throwable
               if (throwable is CancellationException) {
                  return@withContext
               } else {
                  if (throwable is UnknownHostException || throwable is ConnectException) {
                     title = R.string.no_network_title
                     msg = R.string.pls_check_network
                  }
                  setValue(Resource.Error(title, msg))
               }
            }
            is ApiResponse.ApiExceptionResponse -> {
               var title = R.string.pls_try_again
               var msg: Any = R.string.cannot_connect
               when (apiResponse.code) {
                  HttpURLConnection.HTTP_UNAUTHORIZED -> {
                     title = R.string.session_expired
                     msg = R.string.pls_log_in_again
                     withContext(Dispatchers.Main) {
                        RxBus.publish(RxEvent.EventSessionExpired())
                     }
                     setValue(Resource.Unauthorized(title, msg))
                  }
                  HttpURLConnection.HTTP_INTERNAL_ERROR -> {
                     setValue(Resource.Error(title, msg, apiResponse.code))
                  }
                  else -> {
                     if (apiResponse.msg != null) msg = apiResponse.msg
                     setValue(Resource.Error(title, msg, apiResponse.code))
                  }
               }
            }
         }
      }
   }

   fun asLiveData() = result as LiveData<Resource<T>>

   @MainThread
   protected abstract suspend fun createCall(): Response<BaseResponse<T>>
}