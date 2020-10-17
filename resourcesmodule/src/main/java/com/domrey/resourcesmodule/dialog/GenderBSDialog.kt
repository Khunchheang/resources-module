package com.domrey.resourcesmodule.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import com.domrey.resourcesmodule.R
import com.domrey.resourcesmodule.base.fragment.BaseBSDialog
import com.domrey.resourcesmodule.data.type.GenderType
import com.domrey.resourcesmodule.databinding.DialogGenderBinding

class GenderBSDialog : BaseBSDialog<DialogGenderBinding>(R.layout.dialog_gender) {

   private var selectGenderListener: ((genderType: GenderType) -> Unit)? = null

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)
      binding.tvMale.setOnClickListener {
         selectGenderListener?.invoke(GenderType.Male())
         dismiss()
      }
      binding.tvFemale.setOnClickListener {
         selectGenderListener?.invoke(GenderType.Female())
         dismiss()
      }
      binding.tvCancel.setOnClickListener {
         dismiss()
      }
   }

   override fun onDismiss(dialog: DialogInterface) {
      super.onDismiss(dialog)
      selectGenderListener = null
   }

   fun onGenderSelected(selectGenderListener: ((genderType: GenderType) -> Unit)?) =
      apply { this.selectGenderListener = selectGenderListener }
}