@file:Suppress("DEPRECATION")

package com.domrey.resourcesmodule.animator

import android.annotation.TargetApi
import android.os.Build
import android.view.View
import androidx.core.view.ViewCompat

object ViewHelper {
   @TargetApi(Build.VERSION_CODES.HONEYCOMB)
   fun clear(v: View) {
      ViewCompat.setAlpha(v, 1f)
      ViewCompat.setScaleY(v, 1f)
      ViewCompat.setScaleX(v, 1f)
      ViewCompat.setTranslationY(v, 0f)
      ViewCompat.setTranslationX(v, 0f)
      ViewCompat.setRotation(v, 0f)
      ViewCompat.setRotationY(v, 0f)
      ViewCompat.setRotationX(v, 0f)
      //ViewCompat.setPivotY(v, v.getMeasuredHeight() / 2);
      v.pivotY = (v.measuredHeight / 2).toFloat()
      ViewCompat.setPivotX(v, (v.measuredWidth / 2).toFloat())
      ViewCompat.animate(v).interpolator = null
   }
}
