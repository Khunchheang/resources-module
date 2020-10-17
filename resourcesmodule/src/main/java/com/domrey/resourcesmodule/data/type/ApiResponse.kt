/**
 * Copyright (c) SkyBooking Co,. Ltd
 * SkyOwner
 * Created by Khunchheang on  18/1/2020.
 */

package com.domrey.resourcesmodule.data.type

sealed class ApiResponse<out T> {

   class ApiEmptyResponse<T> : ApiResponse<T>()

   data class ApiSuccessResponse<T>(val body: T) : ApiResponse<T>()

   data class ApiErrorResponse<T>(val throwable: Throwable) : ApiResponse<T>()

   data class ApiExceptionResponse<T>(val code: Int, val msg: String? = null) : ApiResponse<T>()

}