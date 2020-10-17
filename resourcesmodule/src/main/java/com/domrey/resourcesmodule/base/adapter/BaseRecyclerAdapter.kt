package com.domrey.resourcesmodule.base.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerAdapter<B : ViewDataBinding, T, VH : RecyclerView.ViewHolder> :
   RecyclerView.Adapter<VH>() {

   var items = ArrayList<T>()
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
      onBindData(holder, items[position], position)
   }

   override fun getItemCount(): Int {
      return items.size
   }

   open fun setItemPosition(pos: Int, data: T) {
      items[pos] = data
      notifyItemChanged(pos)
   }

   open fun removeItem(pos: Int) {
      items.removeAt(pos)
      notifyDataSetChanged()
   }

   open fun setItems(data: List<T>?) {
      if (data == null) return
      items.clear()
      items.addAll(data)
      this.notifyItemInserted(itemCount)
   }

   open fun addItem(data: T) {
      items.add(data)
      this.notifyItemInserted(itemCount)
   }

   fun clearItems() {
      items.clear()
      this.notifyDataSetChanged()
   }

   fun getItemPosition(position: Int): T {
      return items[position]
   }
}
