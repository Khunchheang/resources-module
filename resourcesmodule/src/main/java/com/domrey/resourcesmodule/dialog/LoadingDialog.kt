package com.domrey.resourcesmodule.dialog

import android.os.Bundle
import android.view.View
import com.domrey.resourcesmodule.R
import com.domrey.resourcesmodule.base.fragment.BaseDialogFragment
import com.domrey.resourcesmodule.databinding.LoadingDialogBinding

class LoadingDialog private constructor() : BaseDialogFragment<LoadingDialogBinding>(R.layout.loading_dialog) {

   companion object {
      private const val MASSAGE = "msg"

      @Synchronized
      fun newInstance(msg: Int = R.string.loading): LoadingDialog {
         return LoadingDialog().apply {
            arguments = Bundle().apply {
               putInt(MASSAGE, msg)
            }
         }
      }
   }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)
      if (savedInstanceState != null) arguments = savedInstanceState
      val msg = arguments?.getInt(MASSAGE, 0)
      binding.message = msg?.let { getString(it) }
   }
}