package com.domrey.resourcesmodule.customview.indicator

import android.graphics.drawable.GradientDrawable

class DotsGradientDrawable : GradientDrawable() {

   var currentColor: Int = 0

   override fun setColor(argb: Int) {
      super.setColor(argb)
      currentColor = argb
   }
}