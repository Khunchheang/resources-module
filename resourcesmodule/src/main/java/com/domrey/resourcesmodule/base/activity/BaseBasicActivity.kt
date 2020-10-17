package com.domrey.resourcesmodule.base.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.domrey.resourcesmodule.dialog.PosNegDialog
import com.domrey.resourcesmodule.dialog.PosNegSingletonDialog
import com.domrey.resourcesmodule.util.LocaleHelper

open class BaseBasicActivity : AppCompatActivity() {

   override fun attachBaseContext(newBase: Context?) {
      super.attachBaseContext(LocaleHelper.onAttach(newBase!!))
   }

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      //supportActionBar?.elevation = 0f
      Log.d("ActivityLifecycle", String.format("onCreate (%s)", this.javaClass.simpleName))
   }

   override fun onResume() {
      super.onResume()
      Log.d("ActivityLifecycle", String.format("onResume (%s)", this.javaClass.simpleName))
   }

   override fun onPause() {
      super.onPause()
      Log.d("ActivityLifecycle", String.format("onPause (%s)", this.javaClass.simpleName))
   }

   override fun onRestart() {
      super.onRestart()
      Log.d("ActivityLifecycle", String.format("onRestart (%s)", this.javaClass.simpleName))
   }

   override fun onStop() {
      super.onStop()
      Log.d("ActivityLifecycle", String.format("onStop (%s)", this.javaClass.simpleName))
   }

   override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
      super.onActivityResult(requestCode, resultCode, data)
      Log.d(
         "ActivityLifecycle",
         String.format("onActivityResult (%s)", this.javaClass.simpleName)
      )
   }

   override fun onDestroy() {
      super.onDestroy()
      Log.d("ActivityLifecycle", String.format("onDestroy (%s)", this.javaClass.simpleName))
   }

   fun showDialogMessage(): PosNegDialog.Builder {
      return PosNegDialog.Builder(supportFragmentManager)
   }

   fun showDialogMessageSingleton(): PosNegSingletonDialog.Builder {
      return PosNegSingletonDialog.Builder(supportFragmentManager)
   }

   fun showToast(msg: String) {
      Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
   }

   fun showToast(msg: Int) {
      showToast(getString(msg))
   }
}