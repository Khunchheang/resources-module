package com.domrey.resourcesmodule.customview

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class SquareEditText : AppCompatEditText {

   constructor(context: Context) : super(context)

   constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

   constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
      context,
      attrs,
      defStyleAttr
   )

   override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
      super.onMeasure(widthMeasureSpec, widthMeasureSpec)
   }

}