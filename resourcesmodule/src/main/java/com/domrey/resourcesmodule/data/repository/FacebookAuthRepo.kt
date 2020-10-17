package com.domrey.resourcesmodule.data.repository

import android.os.Bundle
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.json.JSONException
import javax.inject.Inject

class FacebookAuthRepo @Inject constructor() {

   fun getLoginSocialRequest(
      scope: CoroutineScope,
      callbackManager: CallbackManager,
      completion: ((loginSocialRequest: LoginSocialRequest?) -> Unit)
   ) {
      scope.launch {
         LoginManager.getInstance()
            .registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {
               override fun onSuccess(result: LoginResult?) {
                  val request = GraphRequest.newMeRequest(result?.accessToken) { jsonObject, _ ->
                     try {
                        val loginSocialRequest = LoginSocialRequest().also {
                           val id = jsonObject.getString(ID)
                           val firstName = jsonObject.getString(FIRST_NAME)
                           val lastName = jsonObject.getString(LAST_NAME)
                           it.name = "$firstName $lastName"
                           it.providerId = id
                           it.provider = PROVIDER
                           it.username = jsonObject.getString(EMAIL)
                           it.photo = "$PREFIX_PHOTO_URL$id$POSTFIX_PHOTO_URL"
                        }
                        completion.invoke(loginSocialRequest)
                     } catch (e: JSONException) {
                        e.localizedMessage
                        completion.invoke(null)
                     }
                  }
                  val parameters = Bundle()
                  parameters.putString("fields", "id,first_name,last_name,email")
                  request.parameters = parameters
                  request.executeAsync()
               }

               override fun onCancel() {}

               override fun onError(exception: FacebookException) {
                  completion.invoke(null)
               }
            })
      }
   }

   companion object {
      private const val ID = "id"
      const val EMAIL = "email"
      const val PUBLIC_PROFILE = "public_profile"
      private const val FIRST_NAME = "first_name"
      private const val LAST_NAME = "last_name"
      private const val PROVIDER = "facebook"

      private const val PREFIX_PHOTO_URL = "http://graph.facebook.com/"
      private const val POSTFIX_PHOTO_URL = "/picture?width=400&heigth=400"
   }

}