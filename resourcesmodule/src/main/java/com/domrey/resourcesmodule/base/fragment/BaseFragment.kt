package com.domrey.resourcesmodule.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.domrey.resourcesmodule.dialog.PosNegDialog
import com.domrey.resourcesmodule.dialog.PosNegSingletonDialog

open class BaseFragment<T : ViewDataBinding>(@LayoutRes val contentLayoutId: Int) : Fragment() {

   lateinit var binding: T

   override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
   ): View? {
      binding = DataBindingUtil.inflate(inflater, contentLayoutId, container, false)
      return binding.root
   }

   override fun onActivityCreated(savedInstanceState: Bundle?) {
      super.onActivityCreated(savedInstanceState)
      binding.lifecycleOwner = viewLifecycleOwner
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