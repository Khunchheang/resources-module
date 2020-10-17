package com.domrey.resourcesmodule.base.fragment

import android.content.Context
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import com.domrey.resourcesmodule.R
import com.domrey.resourcesmodule.app.ViewModelFactory
import com.domrey.resourcesmodule.data.type.Resource
import com.domrey.resourcesmodule.dialog.LoadingDialog
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

open class BaseInjectFragment<T : ViewDataBinding>(@LayoutRes val layoutId: Int) :
   BaseFragment<T>(layoutId) {

   var loadingDialog: LoadingDialog? = null

   @Inject
   lateinit var factory: ViewModelFactory

   override fun onAttach(context: Context) {
      AndroidSupportInjection.inject(this)
      super.onAttach(context)
   }

   open fun handleLoadingRequest(resource: Resource<*>, isHandleDialog: Boolean = true) {
      if (resource is Resource.Loading) {
         loadingDialog = LoadingDialog.newInstance()
         loadingDialog?.show(childFragmentManager, loadingDialog?.tag)
         loadingDialog?.onDismissListener { loadingDialog = null }
         return
      } else if (resource is Resource.Error) {
         handleError(resource, isHandleDialog)
      }
      loadingDialog?.dismiss()
   }

   open fun handleError(resource: Resource<*>, isHandleDialog: Boolean = true) {
      val message = if (resource.messageDes is String) resource.messageDes
      else getString(resource.messageDes as Int)
      if (isHandleDialog) showOKDialogMessage(message)
      else showToast(message)
   }

   open fun showOKDialogMessage(message: String) {
      showDialogMessage()
         .setMessage(message)
         .setTextBtnPos(getString(R.string.ok))
         .show()
   }

}