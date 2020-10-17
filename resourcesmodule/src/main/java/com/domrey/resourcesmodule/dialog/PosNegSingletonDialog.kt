package com.domrey.resourcesmodule.dialog

import android.content.DialogInterface
import androidx.fragment.app.FragmentManager

class PosNegSingletonDialog : PosNegDialog() {

   data class Builder(private val fragmentManager: FragmentManager) :
      PosNegDialog.Builder(fragmentManager) {

      companion object {
         @Volatile
         var INSTANCE: PosNegSingletonDialog? = null
      }

      override fun show(isCancelable: Boolean) {
         synchronized(this) {
            buildPosNegSingleton()
            INSTANCE?.isCancelable = isCancelable
            fragmentManager.let {
               if (!it.isDestroyed) {
                  it.executePendingTransactions()
                  val fragmentTransaction = it.beginTransaction()
                  if (!INSTANCE?.isAdded!!) fragmentTransaction.add(INSTANCE!!, INSTANCE!!.tag)
                  fragmentTransaction.commitAllowingStateLoss()
               }
            }
         }
      }

      override fun buildPosNegSingleton(): PosNegSingletonDialog {
         if (INSTANCE != null) return INSTANCE!!
         return synchronized(this) {
            INSTANCE = PosNegSingletonDialog()
            INSTANCE = super.buildPosNegSingleton()
            INSTANCE!!
         }
      }
   }

   override fun dismiss() {
      super.dismiss()
      Builder.INSTANCE = null
   }

   override fun onDismiss(dialog: DialogInterface) {
      super.onDismiss(dialog)
      Builder.INSTANCE = null
   }

   override fun onDetach() {
      super.onDetach()
      Builder.INSTANCE = null
   }
}