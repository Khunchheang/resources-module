package com.domrey.resourcesmodule.data.repository

import android.content.ContentValues
import android.content.Intent
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class GoogleAuthRepo @Inject constructor() {

   fun getLoginSocialRequest(
      scope: CoroutineScope,
      dataIntent: Intent,
      completion: ((loginSocialRequest: LoginSocialRequest?) -> Unit)
   ) {
      scope.launch {
         val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(dataIntent)
         try {
            val account = task.getResult(ApiException::class.java)
            val loginSocialRequest = LoginSocialRequest().also {
               val firstName = account?.familyName
               val lastName = account?.givenName
               it.name = "$firstName $lastName"
               it.providerId = account?.id
               it.provider = PROVIDER
               it.username = account?.email
               it.photo = account?.photoUrl.toString()
            }
            completion.invoke(loginSocialRequest)
         } catch (e: ApiException) {
            Log.w(ContentValues.TAG, "signInResult:failed code=" + e.statusCode)
            completion.invoke(null)
         }
      }
   }

   companion object {
      private const val PROVIDER = "google"
   }

}