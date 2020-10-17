package com.domrey.resourcesmodule.data

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject

class CredentialSharePref @Inject constructor(context: Context) {
   private var sharedPref: SharedPreferences? = null
   private val editor: SharedPreferences.Editor

   val accessToken = MutableLiveData<String?>()
   val playerId: String? get() = sharedPref?.getString(PLAYER_ID, null)

   init {
      sharedPref = context.getSharedPreferences(CREDENTIAL_PREF, Context.MODE_PRIVATE)
      editor = sharedPref!!.edit()
      editor.apply()

      accessToken.value = sharedPref!!.getString(USER_TOKEN, null)
   }

   fun storeAccessToken(token: String?) {
      token ?: return
      accessToken.value = token
      editor.putString(USER_TOKEN, token)
      editor.commit()
   }

   fun resetAccessToken() {
      accessToken.value = null
      editor.putString(USER_TOKEN, null)
      editor.commit()
   }

   fun storePlayerId(playerId: String?) {
      editor.putString(PLAYER_ID, playerId).commit()
   }

   companion object {
      private const val CREDENTIAL_PREF = "credential_pref"
      private const val USER_TOKEN = "user_token"
      private const val PLAYER_ID = "player_id"
   }

}