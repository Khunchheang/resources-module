package com.domrey.resourcesmodule.data.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.domrey.resourcesmodule.data.type.ApiResponse
import org.json.JSONObject
import retrofit2.Response
import java.net.HttpURLConnection

object RetrofitRequest {

   suspend fun <T> handleNetworkBoundRequest(call: suspend () -> Response<T>): LiveData<ApiResponse<T>> {
      val apiResponse: ApiResponse<T>
      apiResponse = try {
         val response = call.invoke()
         if (response.isSuccessful) {
            val body = response.body()
            if (body == null || response.code() == HttpURLConnection.HTTP_NO_CONTENT) {
               ApiResponse.ApiEmptyResponse()
            } else {
               ApiResponse.ApiSuccessResponse(body)
            }
         } else {
            var msg: String? = null
            var errorCode: Int = response.code()
            val jsonObject = JSONObject(response.errorBody()?.string())
            try {
               errorCode = jsonObject.getInt("status")
               msg = jsonObject.getString("error")
            } catch (e: Exception) {
               e.printStackTrace()
            }
            ApiResponse.ApiExceptionResponse(errorCode, msg)
         }
      } catch (throwable: Throwable) {
         ApiResponse.ApiErrorResponse(throwable)
      }
      return MutableLiveData(apiResponse)
   }

   suspend fun <T> handleNetworkRequest(call: suspend () -> Response<T>): ApiResponse<T> {
      val apiResponse: ApiResponse<T>
      apiResponse = try {
         val response = call.invoke()
         if (response.isSuccessful) {
            val body = response.body()
            if (body == null) {
               ApiResponse.ApiExceptionResponse(response.code())
            } else {
               ApiResponse.ApiSuccessResponse(body)
            }
         } else {
            var msg: String? = null
            var errorCode: Int = response.code()
            val jsonObject = JSONObject(response.errorBody()?.string())
            try {
               errorCode = jsonObject.getInt("status")
               msg = jsonObject.getString("message")
            } catch (e: Exception) {
               e.printStackTrace()
            }
            ApiResponse.ApiExceptionResponse(errorCode, msg)
         }
      } catch (throwable: Throwable) {
         ApiResponse.ApiErrorResponse(throwable)
      }
      return apiResponse
   }
}