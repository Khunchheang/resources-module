package com.domrey.resourcesmodule.helpers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status
import java.util.regex.Pattern

class SMSBroadcastReceiver : BroadcastReceiver() {

   private var receiveSMSListener: ((sms: String?) -> Unit)? = null

   override fun onReceive(context: Context, intent: Intent) {
      if (SmsRetriever.SMS_RETRIEVED_ACTION == intent.action) {
         val extras = intent.extras
         val status = extras!!.get(SmsRetriever.EXTRA_STATUS) as Status

         when (status.statusCode) {
            CommonStatusCodes.SUCCESS -> {
               val message = extras.get(SmsRetriever.EXTRA_SMS_MESSAGE) as String
               val pattern = Pattern.compile("\\d{6}")
               val matcher = pattern.matcher(message)
               if (matcher.find()) receiveSMSListener?.invoke(matcher.group(0))
            }
            CommonStatusCodes.TIMEOUT -> {
               Log.d("SMS_TIMEOUT", "timeout")
            }
         }
      }
   }

   fun onReceiveSMSListener(receiveSMSListener: ((sms: String?) -> Unit)) = apply {
      this.receiveSMSListener = receiveSMSListener
   }
}