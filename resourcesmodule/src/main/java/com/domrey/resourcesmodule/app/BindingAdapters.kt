package com.domrey.resourcesmodule.app

import android.annotation.SuppressLint
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.domrey.resourcesmodule.R
import com.domrey.resourcesmodule.util.Constants
import java.text.SimpleDateFormat
import java.util.*

object BindingAdapters {
   @JvmStatic
   @BindingAdapter("visibleGone")
   fun visibleGone(view: View, show: Boolean) {
      view.visibility = if (show) View.VISIBLE else View.GONE
   }

   @JvmStatic
   @BindingAdapter("visibleInvisible")
   fun visibleInvisible(view: View, show: Boolean) {
      view.visibility = if (show) View.VISIBLE else View.INVISIBLE
   }

   @JvmStatic
   @BindingAdapter("textRes")
   fun setTextResource(textView: TextView, messageRes: Int?) {
      try {
         messageRes?.let {
            textView.text = textView.context.getString(it)
         }
      } catch (ex: Exception) {
         ex.localizedMessage
      }
   }

   @JvmStatic
   @BindingAdapter("textIntValue")
   fun setTextIntValue(textView: TextView, value: Int?) {
      value?.let {
         textView.text = value.toString()
      }
   }

   @JvmStatic
   @BindingAdapter("imageUrl")
   fun setImageUrl(view: ImageView, image: Any?) {
      image?.let {
         Glide
            .with(view.context)
            .load(it)
            .placeholder(R.drawable.img_placeholder)
            .error(R.drawable.img_placeholder)
            .into(view)
      }
   }

   @JvmStatic
   @BindingAdapter("imageRes")
   fun setImageRes(view: ImageView, imageRes: Int) {
      Glide
         .with(view.context)
         .load(imageRes)
         .into(view)
   }

   @JvmStatic
   @SuppressLint("SetJavaScriptEnabled")
   @BindingAdapter(value = ["progressBar", "url"])
   fun loadWebView(webView: WebView, progressBar: ProgressBar, url: String) {
      webView.webChromeClient = object : WebChromeClient() {
         override fun onProgressChanged(view: WebView, progress: Int) {
            progressBar.progress = progress
            if (progress == 100) progressBar.visibility = View.INVISIBLE
         }
      }
      webView.webViewClient = WebViewClient()
      webView.settings.javaScriptEnabled = true
      webView.loadUrl(url)
   }


   @JvmStatic
   @BindingAdapter("fullDate")
   fun setTextFullDate(textView: TextView, date: Date?) {
      date?.let {
         val format =
            SimpleDateFormat(Constants.WEEK_DAY_MONTH_YEAR_HOUR_MIN_FORMAT, Locale.getDefault())
         textView.text = format.format(it)
      }
   }

   @JvmStatic
   @BindingAdapter("isSelected")
   fun setSelected(view: View, selected: Boolean) {
      view.isSelected = selected
   }

   @JvmStatic
   @BindingAdapter("timeDate")
   fun setTimeText(textView: TextView, date: Date?) {
      date?.let {
         val format = SimpleDateFormat(Constants.HOUR_MIN_FORMAT, Locale.getDefault())
         textView.text =  format.format(it)
      }
   }

   @JvmStatic
   @BindingAdapter(value = ["latitude", "longitude"])
   fun setStaticMap(imageView: ImageView, latitude: Double, longitude: Double) {
      val locationBannerUrl = """https://maps.googleapis.com/maps/api/staticmap?center=&zoom=15&
         size=600x300&scale=&maptype=roadmap&markers=size:mid%7Ccolor:red%7C$latitude,$longitude
         &key=AIzaSyAE0Hur2IFrQJ70GGBJNb7I_AesUsRjAHc"""
      setImageUrl(imageView, locationBannerUrl)
   }
}