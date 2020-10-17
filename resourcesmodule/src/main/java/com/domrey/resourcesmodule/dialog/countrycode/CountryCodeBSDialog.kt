package com.domrey.resourcesmodule.dialog.countrycode

import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.RecyclerView
import com.domrey.resourcesmodule.R
import com.domrey.resourcesmodule.base.fragment.BaseInjectBSDialog
import com.domrey.resourcesmodule.data.local.room.CountryCode
import com.domrey.resourcesmodule.databinding.DialogCountryCodeBinding
import com.domrey.resourcesmodule.extension.dismissKeyboard
import com.domrey.resourcesmodule.extension.resetFullStatusBar
import com.domrey.resourcesmodule.extension.setFullStatusBar
import com.domrey.resourcesmodule.listener.SimpleTextWatcher
import com.domrey.resourcesmodule.util.Constants
import com.google.android.material.bottomsheet.BottomSheetBehavior
import javax.inject.Inject

class CountryCodeBSDialog :
   BaseInjectBSDialog<DialogCountryCodeBinding>(R.layout.dialog_country_code) {

   @Inject
   lateinit var countryCodeAdapter: CountryCodeAdapter

   private val viewModel: CountryCodeViewModel by viewModels { factory }
   private var countrySelectListener: ((country: CountryCode?) -> Unit)? = null
   private var behavior: BottomSheetBehavior<View>? = null

   companion object {
      private const val IS_DISPLAY_DIAL_CODE = "is_display_dial_code"

      fun newInstance(isDisplayDialCode: Boolean = true): CountryCodeBSDialog {
         return CountryCodeBSDialog().apply {
            arguments = Bundle().apply {
               putBoolean(IS_DISPLAY_DIAL_CODE, isDisplayDialCode)
            }
         }
      }
   }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)
      initCountryCodeList()
      initEdtSearch()
      binding.toolbar.setNavigationOnClickListener { dismiss() }
   }

   override fun onDismiss(dialog: DialogInterface) {
      super.onDismiss(dialog)
      behavior = null
      countrySelectListener = null
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

   private fun initCountryCodeList() {
      val isDisplayDialCode = arguments?.getBoolean(IS_DISPLAY_DIAL_CODE)
      countryCodeAdapter.isDisplayDialCode = isDisplayDialCode ?: true
      binding.resource = viewModel.countryCodes
      binding.rvCountryCode.adapter = countryCodeAdapter
      viewModel.countryCodes.observe(viewLifecycleOwner) {
         countryCodeAdapter.submitList(it)
      }
      countryCodeAdapter.setOnItemClickListener { _, pos ->
         val itemPos = countryCodeAdapter.currentList[pos]
         countrySelectListener?.invoke(itemPos)
         Handler().postDelayed({ dismiss() }, Constants.SORT_DELAY_BOTTOM_DISMISS)
      }
      binding.rvCountryCode.addOnScrollListener(object : RecyclerView.OnScrollListener() {
         override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (dy > 0) dismissKeyboard(binding.edtSearch.windowToken)
         }
      })
   }

   private fun initEdtSearch() {
      binding.edtSearch.isCursorVisible = true
      binding.edtSearch.requestFocus()
      binding.edtSearch.addTextChangedListener(object : SimpleTextWatcher {
         override fun afterTextChanged(s: Editable?) {
            if (behavior?.state != BottomSheetBehavior.STATE_EXPANDED) {
               behavior?.state = BottomSheetBehavior.STATE_EXPANDED
            }
            viewModel.search(s.toString())
         }
      })
      binding.edtSearch.setOnClickListener {
         if (behavior == null) behavior = BottomSheetBehavior.from((view?.parent) as View)
         behavior?.state = BottomSheetBehavior.STATE_EXPANDED
      }
   }

   fun onCountrySelected(countrySelectListener: (country: CountryCode?) -> Unit) =
      apply { this.countrySelectListener = countrySelectListener }

}