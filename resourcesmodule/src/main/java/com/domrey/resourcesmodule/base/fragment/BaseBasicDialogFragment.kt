package com.domrey.resourcesmodule.base.fragment

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.domrey.resourcesmodule.R

open class BaseBasicDialogFragment : DialogFragment() {

   private var dismissListener: (() -> Unit)? = null

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      setStyle(STYLE_NO_TITLE, R.style.AppTheme_DialogStyle)
   }

   override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
      val dialog = super.onCreateDialog(savedInstanceState)
      dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
      dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
      dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
      return dialog
   }

   override fun show(manager: FragmentManager, tag: String?) {
      manager.executePendingTransactions()
      val fragmentTransaction = manager.beginTransaction()
      if (!this.isAdded) fragmentTransaction.add(this, this.tag)
      fragmentTransaction.commitAllowingStateLoss()
   }

   override fun dismiss() {
      this.dismissAllowingStateLoss()
      super.dismiss()
   }

   override fun onDismiss(dialog: DialogInterface) {
      dismissListener?.invoke()
      dismissListener = null
      super.onDismiss(dialog)
   }

   fun onDismissListener(dismissListener: () -> Unit) =
      apply { this.dismissListener = dismissListener }
}