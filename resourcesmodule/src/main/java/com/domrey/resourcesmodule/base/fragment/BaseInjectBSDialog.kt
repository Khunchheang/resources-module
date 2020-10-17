package com.domrey.resourcesmodule.base.fragment

import android.content.Context
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import dagger.android.support.AndroidSupportInjection

open class BaseInjectBSDialog<B : ViewDataBinding>(@LayoutRes val layoutId: Int) :
   BaseBSDialog<B>(layoutId) {

   override fun onAttach(context: Context) {
      AndroidSupportInjection.inject(this)
      super.onAttach(context)
   }

}