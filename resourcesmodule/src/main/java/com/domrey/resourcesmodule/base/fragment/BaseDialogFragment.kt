package com.domrey.resourcesmodule.base.fragment

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentManager
import com.domrey.resourcesmodule.R

open class BaseDialogFragment<T : ViewDataBinding>(@LayoutRes val contentLayoutId: Int) :
   BaseBasicDialogFragment() {

   lateinit var binding: T
   private var inflatedView: View? = null
   private var dismissListener: (() -> Unit)? = null

   override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
   ): View? {
      if (inflatedView != null) return inflatedView
      binding = DataBindingUtil.inflate(inflater, contentLayoutId, container, false)
      inflatedView = binding.root
      return inflatedView
   }

   override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
      val dialog = super.onCreateDialog(savedInstanceState)
      dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
      dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
      dialog.window!!.attributes.windowAnimations = R.style.DialogScaleAnimation
      return dialog
   }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)
      binding.lifecycleOwner = viewLifecycleOwner
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
}