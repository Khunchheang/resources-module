package com.domrey.resourcesmodule.animator

import android.view.View
import androidx.core.view.ViewPropertyAnimatorListener
import androidx.recyclerview.widget.RecyclerView

abstract class AnimateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

   fun preAnimateAddImpl() {}

   fun preAnimateRemoveImpl() {}

   abstract fun animateAddImpl(listener: ViewPropertyAnimatorListener)

   abstract fun animateRemoveImpl(listener: ViewPropertyAnimatorListener)
}
