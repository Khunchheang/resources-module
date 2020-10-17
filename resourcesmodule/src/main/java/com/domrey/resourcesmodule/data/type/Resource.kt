/**
 * Copyright (c) SkyBooking Co,. Ltd
 * SkyOwner
 * Created by Khunchheang on  18/1/2020.
 */

package com.domrey.resourcesmodule.data.type

import com.domrey.resourcesmodule.data.type.Status.*

sealed class Resource<out T>(
   val status: Status,
   val data: T?,
   val messageTitle: Int?,
   val messageDes: Any?,
   val errorCode: Int?
) {

   data class Unauthorized<out T>(
      private val msgTitle: Int,
      private val msgDes: Int,
      private val resource: T? = null
   ) : Resource<T>(ERROR, resource, msgTitle, msgDes, null)

   data class Loading<out T>(
      private val resource: T? = null
   ) : Resource<T>(LOADING, resource, null, null, null)

   data class Success<out T>(
      private val resource: T?
   ) : Resource<T>(SUCCESS, resource, null, null, null)

   data class Error<out T>(
      private val msgTitle: Int,
      private val msgDes: Any,
      private val code: Int? = null,
      private val resource: T? = null
   ) : Resource<T>(ERROR, resource, msgTitle, msgDes, code)

}