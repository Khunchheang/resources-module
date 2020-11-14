package com.domrey.resourcesmodule.dialog.countrycode

import android.annotation.SuppressLint
import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.domrey.resourcesmodule.R
import com.domrey.resourcesmodule.app.AppExecutors
import com.domrey.resourcesmodule.base.adapter.BaseListAdapter
import com.domrey.resourcesmodule.data.local.room.CountryCode
import com.domrey.resourcesmodule.databinding.ItemCountryCodeBinding
import com.domrey.resourcesmodule.util.LocaleHelper
import javax.inject.Inject

class CountryCodeAdapter @Inject constructor(private val context: Context, appExecutors: AppExecutors) :
   BaseListAdapter<ItemCountryCodeBinding, CountryCode, CountryCodeAdapter.ViewHolder>(
      appExecutors,
      COUNTRY_CODE_COMPARATOR
   ) {

   var isDisplayDialCode: Boolean = true

   override fun getLayout(viewType: Int): Int {
      return R.layout.item_country_code
   }

   override fun setViewHolder(viewType: Int, parent: ViewGroup): ViewHolder {
      return ViewHolder(binding)
   }

   override fun onBindData(holder: ViewHolder, data: CountryCode, position: Int) {
      holder.binding.displayCountry = getCountryNameByLocale(context, data)
      holder.binding.dialCode = if (isDisplayDialCode) " (${data.dialCode})" else ""
   }

   inner class ViewHolder(val binding: ItemCountryCodeBinding) :
      RecyclerView.ViewHolder(binding.root) {
      init {
         binding.root.setOnClickListener {
            itemClickListener?.invoke(it, adapterPosition)
         }
      }
   }

   companion object {
      const val KHMER = "km"
      const val ENGLISH = "en"
      const val CHINESE = "zh"
      const val JAPANESE = "ja"
      const val KOREAN = "ko"
      const val INDONESIAN = "in"
      const val MALAYSIAN = "ms"

      private val COUNTRY_CODE_COMPARATOR = object : DiffUtil.ItemCallback<CountryCode>() {
         override fun areItemsTheSame(oldItem: CountryCode, newItem: CountryCode): Boolean {
            return oldItem.code == newItem.code
         }

         @SuppressLint("DiffUtilEquals")
         override fun areContentsTheSame(oldItem: CountryCode, newItem: CountryCode): Boolean {
            return oldItem == newItem
         }
      }

      fun getCountryNameByLocale(context: Context, countryCode: CountryCode?): String? {
         return when (LocaleHelper.getLanguage(context)) {
            KHMER -> countryCode?.khmer
            ENGLISH -> countryCode?.english
            CHINESE -> countryCode?.chinese
            JAPANESE -> countryCode?.japanese
            KOREAN -> countryCode?.korean
            INDONESIAN -> countryCode?.indonesian
            MALAYSIAN -> countryCode?.malaysian
            else -> countryCode?.english
         }
      }
   }
}