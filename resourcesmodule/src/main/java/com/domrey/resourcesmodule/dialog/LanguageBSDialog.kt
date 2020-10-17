package com.domrey.resourcesmodule.dialog

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.RadioGroup
import com.domrey.resourcesmodule.R
import com.domrey.resourcesmodule.base.fragment.BaseBSDialog
import com.domrey.resourcesmodule.databinding.DialogChangeLanguageBinding
import com.domrey.resourcesmodule.extension.recreateLanguageChanged
import com.domrey.resourcesmodule.extension.resetFullStatusBar
import com.domrey.resourcesmodule.extension.setFullStatusBar
import com.domrey.resourcesmodule.util.Constants
import com.domrey.resourcesmodule.util.LocaleHelper

class LanguageBSDialog : BaseBSDialog<DialogChangeLanguageBinding>(R.layout.dialog_change_language),
   RadioGroup.OnCheckedChangeListener {

   companion object {
      const val KHMER = "km"
      const val ENGLISH = "en"
      const val CHINESE = "zh"
      const val JAPANESE = "ja"
      const val KOREAN = "ko"
      const val INDONESIAN = "in"
      const val MALAYSIAN = "ms"
   }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)

      binding.toolbar.setNavigationOnClickListener { dismiss() }
      setCheckRadioButton()
   }

   override fun onBottomSheetSlide(bottomSheet: View, slideOffset: Float) {
      super.onBottomSheetSlide(bottomSheet, slideOffset)
      if (slideOffset == 1f) {
         setFullStatusBar()
         binding.appbarLayout.elevation = 8f
         binding.toolbar.navigationIcon = iconClose
      } else {
         resetFullStatusBar()
         binding.appbarLayout.elevation = 0f
         binding.toolbar.navigationIcon = null
      }
   }

   override fun onDestroyView() {
      binding.rgLanguage.setOnCheckedChangeListener(null)
      super.onDestroyView()
   }

   override fun onCheckedChanged(group: RadioGroup?, id: Int) {
      when (id) {
         R.id.rbKhmer -> changeLanguage(KHMER)
         R.id.rbEnglish -> changeLanguage(ENGLISH)
         R.id.rbChinese -> changeLanguage(CHINESE)
         R.id.rbJapanese -> changeLanguage(JAPANESE)
         R.id.rbKorean -> changeLanguage(KOREAN)
         R.id.rbIndonesian -> changeLanguage(INDONESIAN)
         R.id.rbMalaysian -> changeLanguage(MALAYSIAN)
      }
   }

   private fun changeLanguage(language: String) {
      Handler().postDelayed({
         LocaleHelper.setLocale(requireContext(), language)
         recreateLanguageChanged()
      }, Constants.SORT_DELAY_BOTTOM_DISMISS)
   }

   private fun setCheckRadioButton() {
      when (LocaleHelper.getLanguage(requireContext())) {
         KHMER -> binding.rbKhmer.isChecked = true
         ENGLISH -> binding.rbEnglish.isChecked = true
         CHINESE -> binding.rbChinese.isChecked = true
         JAPANESE -> binding.rbJapanese.isChecked = true
         KOREAN -> binding.rbKorean.isChecked = true
         INDONESIAN -> binding.rbIndonesian.isChecked = true
         MALAYSIAN -> binding.rbMalaysian.isChecked = true
      }
      binding.rgLanguage.setOnCheckedChangeListener(this)
   }
}