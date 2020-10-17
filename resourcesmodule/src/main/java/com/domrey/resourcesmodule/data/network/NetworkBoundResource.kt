package com.domrey.resourcesmodule.data.network

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.domrey.resourcesmodule.R
import com.domrey.resourcesmodule.data.type.ApiResponse
import com.domrey.resourcesmodule.data.type.Resource
import com.domrey.resourcesmodule.rxbus.RxBus
import com.domrey.resourcesmodule.rxbus.RxEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.UnknownHostException

abstract class NetworkBoundResource<ResultType, RequestType>
@MainThread constructor(private val scope: CoroutineScope) {

   private var isSetDirectResult: Boolean = false
   val result = MediatorLiveData<Resource<ResultType>>()

   init {
      result.value = Resource.Loading(null)
      @Suppress("LeakingThis")
      val dbSource = loadFromDb()
      result.addSource(dbSource) { data ->
         result.removeSource(dbSource)
         if (shouldFetch(data)) {
            fetchFromNetwork(dbSource)
         } else {
            result.addSource(dbSource) { newData ->
               setValue(Resource.Success(newData))
            }
         }
      }
   }

   @MainThread
   fun setValue(newValue: Resource<ResultType>) {
      if (result.value != newValue) {
         result.value = newValue
      }
   }

   private fun fetchFromNetwork(dbSource: LiveData<ResultType>) {
      scope.launch(Dispatchers.IO) {
         // we re-attach dbSource as a new source, it will dispatch its latest value quickly
         setValue(Resource.Loading(null))

         val apiResponse = RetrofitRequest.handleNetworkBoundRequest { createCall() }
         scope.launch(Dispatchers.Main) {
            result.addSource(apiResponse) { response ->
               result.removeSource(apiResponse)
               result.removeSource(dbSource)
               when (response) {
                  is ApiResponse.ApiSuccessResponse -> {
                     scope.launch(Dispatchers.IO) {
                        saveCallResult(processResponse(response))
                        scope.launch(Dispatchers.Main) {
                           if (!isSetDirectResult) {
                              result.addSource(loadFromDb()) { newData ->
                                 setValue(Resource.Success(newData))
                              }
                           }
                        }
                     }
                  }
                  is ApiResponse.ApiEmptyResponse -> {
                     scope.launch(Dispatchers.IO) {
                        scope.launch(Dispatchers.Main) {
                           if (!isSetDirectResult) {
                              result.addSource(loadFromDb()) { newData ->
                                 setValue(Resource.Success(newData))
                              }
                           }
                        }
                     }
                  }
                  is ApiResponse.ApiErrorResponse -> {
                     onFetchFailed()
                     var title = R.string.pls_try_again
                     var msg = R.string.connection_timeout
                     scope.launch(Dispatchers.Main) {
                        if (response.throwable is UnknownHostException || response.throwable is ConnectException) {
                           title = R.string.no_network_title
                           msg = R.string.pls_check_network
                        }
                        setValue(Resource.Error(title, msg))
                     }
                  }
                  is ApiResponse.ApiExceptionResponse -> {
                     onFetchFailed()
                     var title = R.string.pls_try_again
                     var msg = R.string.cannot_connect
                     scope.launch(Dispatchers.Main) {
                        when (response.code) {
                           HttpURLConnection.HTTP_UNAUTHORIZED -> {
                              title = R.string.session_expired
                              msg = R.string.pls_log_in_again
                              RxBus.publish(RxEvent.EventSessionExpired())
                              setValue(Resource.Unauthorized(title, msg))
                           }
                           HttpURLConnection.HTTP_INTERNAL_ERROR -> {
                              setValue(Resource.Error(title, msg, response.code))
                           }
                           else -> {
                              val errorMessage = response.msg ?: msg
                              setValue(Resource.Error(title, errorMessage, response.code))
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   fun asLiveData() = result as LiveData<Resource<ResultType>>

   protected open fun onFetchFailed() {}

   @WorkerThread
   protected open fun processResponse(response: ApiResponse.ApiSuccessResponse<RequestType>) =
      response.body

   @WorkerThread
   protected abstract fun saveCallResult(item: RequestType)

   @MainThread
   protected abstract fun shouldFetch(data: ResultType?): Boolean

   @MainThread
   protected abstract fun loadFromDb(): LiveData<ResultType>

   @MainThread
   protected abstract suspend fun createCall(): Response<RequestType>

   //For setting results directly to view model
   fun setData(data: ResultType?) {
      isSetDirectResult = true
      scope.launch(Dispatchers.Main) {
         setValue(Resource.Success(data))
      }
   }

}