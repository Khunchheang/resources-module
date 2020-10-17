package com.domrey.resourcesmodule.base.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import dagger.android.AndroidInjection

open class BaseInjectActivity<T : ViewDataBinding>(@LayoutRes val contentLayoutId: Int) :
   BaseBasicActivity() {

   lateinit var binding: T

   override fun onCreate(savedInstanceState: Bundle?) {
      AndroidInjection.inject(this)
      super.onCreate(savedInstanceState)
      performDataBinding()
   }

   override fun onOptionsItemSelected(item: MenuItem): Boolean {
      val id = item.itemId
      if (id == android.R.id.home) {
         super.onBackPressed()
         return true
      }
      return super.onOptionsItemSelected(item)
   }

   fun setSupportToolbar() {
      supportActionBar?.setDisplayHomeAsUpEnabled(true)
      supportActionBar?.setDisplayShowTitleEnabled(true)
   }

   private fun performDataBinding() {
      binding = DataBindingUtil.setContentView(this, contentLayoutId)
      binding.lifecycleOwner = this
      binding.executePendingBindings()
   }

   companion object {
      var IS_CANCEL_UPDATE = false
   }
}