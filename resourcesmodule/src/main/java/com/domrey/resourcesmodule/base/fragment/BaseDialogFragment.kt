package com.domrey.resourcesmodule.base.fragment

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import com.domrey.resourcesmodule.R
import com.domrey.resourcesmodule.data.type.Resource
import com.domrey.resourcesmodule.dialog.LoadingDialog
import com.domrey.resourcesmodule.dialog.PosNegDialog
import com.domrey.resourcesmodule.dialog.PosNegSingletonDialog

open class BaseDialogFragment<T : ViewDataBinding>(@LayoutRes val contentLayoutId: Int) :
   DialogFragment() {

   lateinit var binding: T
   private var inflatedView: View? = null
   private var dismissListener: (() -> Unit)? = null

   fun onDismissListener(dismissListener: () -> Unit) =
      apply { this.dismissListener = dismissListener }

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      setStyle(STYLE_NO_TITLE, R.style.AppTheme_DialogStyle)
   }

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
      dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
      return dialog
   }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)
      binding.lifecycleOwner = viewLifecycleOwner
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

   fun showDialogMessage(): PosNegDialog.Builder {
      return PosNegDialog.Builder(childFragmentManager)
   }

   fun showDialogMessageSingleton(): PosNegSingletonDialog.Builder {
      return PosNegSingletonDialog.Builder(childFragmentManager)
   }

   fun showToast(msg: String) {
      Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
   }

   fun showToast(msg: Int) {
      showToast(getString(msg))
   }
}