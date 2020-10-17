
package com.domrey.resourcesmodule.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import com.domrey.resourcesmodule.R
import com.domrey.resourcesmodule.base.fragment.BaseBSDialog
import com.domrey.resourcesmodule.databinding.DialogPickImageBinding

class OptionPickImageBSDialog : BaseBSDialog<DialogPickImageBinding>(R.layout.dialog_pick_image) {

   private var pickerOptionListener: ((optionCode: Int) -> Unit)? = null

   companion object {
      const val PICK_IMAGE_IN_GALLERY_CODE = 1000
      const val PICK_IMAGE_FROM_CAMERA_CODE = 1002

      @Synchronized
      fun newInstance(): OptionPickImageBSDialog {
         return OptionPickImageBSDialog()
      }
   }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)
      binding.tvCamera.setOnClickListener {
         pickerOptionListener?.invoke(PICK_IMAGE_FROM_CAMERA_CODE)
         dismiss()
      }
      binding.tvGallery.setOnClickListener {
         pickerOptionListener?.invoke(PICK_IMAGE_IN_GALLERY_CODE)
         dismiss()
      }
      binding.tvCancel.setOnClickListener {
         dismiss()
      }
   }

   override fun onDismiss(dialog: DialogInterface) {
      super.onDismiss(dialog)
      pickerOptionListener = null
   }

   fun setOnPickerOptionSelectedListener(pickerOptionListener: ((optionCode: Int) -> Unit)?) =
      apply { this.pickerOptionListener = pickerOptionListener }

}