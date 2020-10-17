package com.domrey.resourcesmodule.data.response

import com.google.gson.annotations.SerializedName

open class BaseResponse<T> {

   @field:SerializedName("data")
   var data: T? = null

   @field:SerializedName("message")
   var message: String? = null

   @field:SerializedName("status")
   var status: Int? = null
}