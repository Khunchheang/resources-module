package com.domrey.resourcesmodule.base.activity

import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import com.domrey.resourcesmodule.R
import com.domrey.resourcesmodule.app.ViewModelFactory
import com.domrey.resourcesmodule.data.type.Resource
import com.domrey.resourcesmodule.dialog.LoadingDialog
import io.reactivex.disposables.Disposable
import javax.inject.Inject

open class BaseMVVMActivity<T : ViewDataBinding>(@LayoutRes val layoutId: Int) :
   BaseInjectActivity<T>(layoutId) {

   @Inject
   lateinit var factory: ViewModelFactory

   var loadingDialog: LoadingDialog? = null
   var sessionDisposable: Disposable? = null

   override fun onPause() {
      super.onPause()
      sessionDisposable?.dispose()
   }

   override fun onDestroy() {
      super.onDestroy()
      sessionDisposable = null
   }

   open fun handleLoadingRequest(resource: Resource<*>, isHandleDialog: Boolean = true) {
      if (resource is Resource.Loading) {
         loadingDialog = LoadingDialog.newInstance()
         loadingDialog?.show(supportFragmentManager, loadingDialog?.tag)
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