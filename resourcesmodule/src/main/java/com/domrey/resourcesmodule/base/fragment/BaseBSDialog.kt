package com.domrey.resourcesmodule.base.fragment

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.domrey.resourcesmodule.R
import com.domrey.resourcesmodule.app.ViewModelFactory
import com.domrey.resourcesmodule.dialog.PosNegDialog
import com.domrey.resourcesmodule.dialog.PosNegSingletonDialog
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import javax.inject.Inject

open class BaseBSDialog<B : ViewDataBinding>(@LayoutRes val contentLayoutId: Int) :
   BottomSheetDialogFragment() {

   lateinit var binding: B
   var bottomSheetCallback: BottomSheetBehavior.BottomSheetCallback? = null
   val iconClose: Drawable by lazy {
      ContextCompat.getDrawable(requireContext(), R.drawable.ic_close_black) as Drawable
   }
   private var dismissListener: (() -> Unit)? = null

   fun onDismissListener(dismissListener: () -> Unit) =
      apply { this.dismissListener = dismissListener }

   override fun getTheme() = R.style.BottomSheetDialog

   override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
      return (super.onCreateDialog(savedInstanceState) as BottomSheetDialog).apply {
         theme
         window!!.attributes.windowAnimations = R.style.BottomSheetDialogSlideDown
         setOnShowListener { setupBottomSheetDialog(this) }
      }
   }

   @Suppress("DEPRECATION")
   open fun setupBottomSheetDialog(dialog: BottomSheetDialog) {
      val bottomSheet = dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet)
         as FrameLayout?
      val behavior = BottomSheetBehavior.from(bottomSheet!!)
         .apply { setBottomSheetCallback(bottomSheetCallback) }
      behavior.state = BottomSheetBehavior.STATE_EXPANDED
      dialog.setOnShowListener(null)
   }

   override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
   ): View? {
      binding = DataBindingUtil.inflate(inflater, contentLayoutId, container, false)
      binding.lifecycleOwner = viewLifecycleOwner
      return binding.root
   }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)
      setBottomSheetCallback()
   }

   override fun onDismiss(dialog: DialogInterface) {
      dismissListener?.invoke()
      dismissListener = null
      bottomSheetCallback = null
      super.onDismiss(dialog)
   }

   private fun setBottomSheetCallback() {
      bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {
         override fun onSlide(bottomSheet: View, slideOffset: Float) {
            onBottomSheetSlide(bottomSheet, slideOffset)
         }

         override fun onStateChanged(bottomSheet: View, newState: Int) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) dismiss()
         }
      }
   }

   open fun onBottomSheetSlide(bottomSheet: View, slideOffset: Float) {}

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