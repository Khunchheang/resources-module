package com.domrey.resourcesmodule.customview

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt

/**
 * Created by : khunchheang
 * Date  : 6/13/20
 * Time  : 11:13 AM
 */
class SpanningLinearLayoutManager(context: Context?, orientation: Int, reverseLayout: Boolean) :
   LinearLayoutManager(
      context,
      orientation,
      reverseLayout
   ) {

   override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
      return spanLayoutSize(super.generateDefaultLayoutParams())
   }

   override fun generateLayoutParams(c: Context?, attrs: AttributeSet?): RecyclerView.LayoutParams {
      return spanLayoutSize(super.generateLayoutParams(c, attrs))
   }

   override fun generateLayoutParams(lp: ViewGroup.LayoutParams?): RecyclerView.LayoutParams {
      return spanLayoutSize(super.generateLayoutParams(lp))
   }

   private fun spanLayoutSize(layoutParams: RecyclerView.LayoutParams): RecyclerView.LayoutParams {
      if (orientation == HORIZONTAL) {
         layoutParams.width = (getHorizontalSpace() / (itemCount.toDouble() + 0.4)).roundToInt()
      } else if (orientation == VERTICAL) {
         layoutParams.height = (getVerticalSpace() / itemCount.toDouble()).roundToInt()
      }
      return layoutParams
   }

   override fun canScrollVertically(): Boolean {
      return false
   }

   override fun canScrollHorizontally(): Boolean {
      return false
   }

   private fun getHorizontalSpace(): Int {
      return width - paddingRight - paddingLeft
   }

   private fun getVerticalSpace(): Int {
      return height - paddingBottom - paddingTop
   }
}