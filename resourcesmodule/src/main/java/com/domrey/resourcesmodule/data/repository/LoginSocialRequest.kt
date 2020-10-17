package com.domrey.resourcesmodule.data.repository

import com.google.gson.annotations.SerializedName

data class LoginSocialRequest(

	@field:SerializedName("provider")
	var provider: String? = null,

	@field:SerializedName("name")
	var name: String? = null,

	@field:SerializedName("provider_id")
	var providerId: String? = null,

	@field:SerializedName("photo")
	var photo: String? = null,

	@field:SerializedName("username")
	var username: String? = null

)