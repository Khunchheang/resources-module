package com.domrey.resourcesmodule.base.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.domrey.resourcesmodule.app.AppExecutors

abstract class BaseListAdapter<B : ViewDataBinding, T, VH : RecyclerView.ViewHolder>(
   appExecutors: AppExecutors,
   diffCallback: DiffUtil.ItemCallback<T>
) : ListAdapter<T, VH>(
   AsyncDifferConfig.Builder<T>(diffCallback)
      .setBackgroundThreadExecutor(appExecutors.diskIO())
      .build()
) {

   lateinit var binding: B

   var itemClickListener: ((view: View, pos: Int) -> Unit)? = null

   fun setOnItemClickListener(itemClicked: (view: View, pos: Int) -> Unit) =
      apply { this.itemClickListener = itemClicked }

   @LayoutRes
   abstract fun getLayout(viewType: Int): Int

   abstract fun setViewHolder(parent: ViewGroup): VH

   abstract fun onBindData(holder: VH, data: T, position: Int)

   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
      val inflater = LayoutInflater.from(parent.context)
      binding = DataBindingUtil.inflate(inflater, getLayout(viewType), parent, false)
      return setViewHolder(parent)
   }

   override fun onBindViewHolder(holder: VH, position: Int) {
      onBindData(holder, getItem(position), position)
   }
}