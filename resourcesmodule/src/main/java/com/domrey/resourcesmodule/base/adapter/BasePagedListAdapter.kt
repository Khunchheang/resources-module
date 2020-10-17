package com.domrey.resourcesmodule.base.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.domrey.resourcesmodule.app.AppExecutors
import com.domrey.resourcesmodule.data.type.Resource
import com.domrey.resourcesmodule.data.type.Status
import com.domrey.resourcesmodule.databinding.LoadingMorePgBinding
import com.domrey.resourcesmodule.listener.RetryCallback

abstract class BasePagedListAdapter<B : ViewDataBinding, T, VH : RecyclerView.ViewHolder>(
   appExecutors: AppExecutors,
   diffCallback: DiffUtil.ItemCallback<T>,
   private val handleErrorCenter: Boolean = true
) : PagedListAdapter<T, RecyclerView.ViewHolder>(
   AsyncDifferConfig.Builder<T>(diffCallback)
      .setBackgroundThreadExecutor(appExecutors.diskIO())
      .build()
) {

   lateinit var binding: B
   private var retryListener: (() -> Unit)? = null
   private var networkState: Resource<List<T>>? = null
   var itemClickListener: ((view: View, pos: Int) -> Unit)? = null

   @LayoutRes
   abstract fun getLayout(viewType: Int): Int

   abstract fun setViewHolder(viewType: Int, parent: ViewGroup): VH

   abstract fun onBindData(holder: VH, data: T?, position: Int)

   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
      return when {
         viewType == VIEW_LOADING -> {
            val inflater = LayoutInflater.from(parent.context)
            val binding = LoadingMorePgBinding.inflate(inflater, parent, false)
            LoadingSatePagingViewHolder(binding, retryListener)
         }
         viewType >= VIEW_ITEM -> {
            val inflater = LayoutInflater.from(parent.context)
            binding = DataBindingUtil.inflate(inflater, getLayout(viewType), parent, false)
            setViewHolder(viewType, parent)
         }
         else -> throw IllegalArgumentException("unknown view type $viewType")
      }
   }

   @Suppress("UNCHECKED_CAST")
   override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
      val viewType = getItemViewType(position)
      when {
         viewType == VIEW_LOADING -> (holder as BasePagedListAdapter<*, *, *>.LoadingSatePagingViewHolder).bindItem()
         viewType >= VIEW_ITEM -> onBindData((holder as VH), getItem(position), position)
      }
   }

   private fun hasExtraRow() = networkState != null && networkState?.status != Status.SUCCESS

   fun setNetworkState(newNetworkState: Resource<List<T>>?) {
      val previousState = this.networkState
      val hadExtraRow = hasExtraRow()
      this.networkState = newNetworkState
      val hasExtraRow = hasExtraRow()
      if (hadExtraRow != hasExtraRow) {
         if (hadExtraRow) {
            notifyItemRemoved(super.getItemCount())
         } else {
            notifyItemInserted(super.getItemCount())
         }
      } else if (hasExtraRow && previousState != newNetworkState) {
         notifyItemChanged(itemCount - 1)
      }
   }

   override fun getItemViewType(position: Int): Int {
      return if (hasExtraRow() && position == itemCount - 1) VIEW_LOADING
      else VIEW_ITEM
   }

   override fun getItemCount(): Int {
      return super.getItemCount() + if (hasExtraRow()) 1 else 0
   }

   fun setOnRetryListener(retryListener: (() -> Unit)) =
      apply { this.retryListener = retryListener }

   fun setOnItemClickListener(itemClicked: (view: View, pos: Int) -> Unit) =
      apply { this.itemClickListener = itemClicked }

   inner class LoadingSatePagingViewHolder(
      val binding: LoadingMorePgBinding,
      val retryListener: (() -> Unit)?
   ) : RecyclerView.ViewHolder(binding.root) {

      init {
         binding.callback = object : RetryCallback {
            override fun retry() {
               retryListener?.invoke()
            }
         }
      }

      fun bindItem() {
         binding.resource = networkState
         binding.itemCount = itemCount

         if ((handleErrorCenter && itemCount == 1 && networkState?.status == Status.LOADING) ||
            (handleErrorCenter && itemCount == 1 && networkState?.status == Status.ERROR)
         ) {
            val params = LinearLayout.LayoutParams(
               LinearLayout.LayoutParams.MATCH_PARENT,
               LinearLayout.LayoutParams.MATCH_PARENT
            )
            binding.rootLoading.layoutParams = params
         } else {
            val params = LinearLayout.LayoutParams(
               LinearLayout.LayoutParams.MATCH_PARENT,
               LinearLayout.LayoutParams.WRAP_CONTENT
            )
            binding.rootLoading.layoutParams = params
         }
      }
   }

   companion object {
      const val VIEW_LOADING = 0
      const val VIEW_ITEM = 1
   }
}