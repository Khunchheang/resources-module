package com.domrey.resourcesmodule.extension

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.IBinder
import android.provider.MediaStore
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import com.domrey.resourcesmodule.R
import com.domrey.resourcesmodule.dialog.OptionPickImageBSDialog
import com.domrey.resourcesmodule.dialog.locationpicker.LocationPickerDialog
import com.domrey.resourcesmodule.util.Constants
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

fun String?.parseDateFromServer(timeZone: TimeZone): Date? {
   val utcFormat = SimpleDateFormat(Constants.SERVER_DATE_FORMAT, Locale.getDefault())
   utcFormat.timeZone = timeZone
   return try {
      if (this != null && this.isNotEmpty()) utcFormat.parse(this)
      else null
   } catch (e: ParseException) {
      null
   }
}

fun <T> LiveData<T>.toSingleEvent(): LiveData<T> {
   val result = SingleLiveEvent<T>()
   result.addSource(this) {
      result.value = it
   }
   return result
}

fun Fragment.recreateLanguageChanged() {
   activity?.intent?.let {
      activity?.finish()
      it.putExtra(Constants.CHANGED_LANGUAGE, Constants.CHANGED)
      it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_ANIMATION)
      activity?.overridePendingTransition(0, 0)
      activity?.overridePendingTransition(0, 0)
      startActivity(it)
   }
}

fun String?.isValidEmail(): Boolean {
   if (this == null || this.isEmpty()) return false
   val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
   val pattern: Pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
   val matcher: Matcher = pattern.matcher(this.toString())
   return matcher.matches()
}

fun String.isValidPassword(): Boolean {
   val pattern: Pattern = Pattern.compile(Constants.PASSWORD_PATTERN)
   val matcher: Matcher = pattern.matcher(this)
   return matcher.matches()
}

fun Fragment.getVersionName(): String? {
   return try {
      val pInfo = context!!.packageManager.getPackageInfo(context!!.packageName, 0)
      pInfo.versionName
   } catch (e: Throwable) {
      e.printStackTrace()
      null
   }
}

fun View.showSoftKeyboard() {
   this.requestFocus()
   val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
   imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

fun Fragment.dismissKeyboard(windowToken: IBinder) {
   val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
   imm?.hideSoftInputFromWindow(windowToken, 0)
}

fun DialogFragment.setFullStatusBar(dialog: Dialog? = this.dialog) {
   dialog?.window?.statusBarColor = Color.WHITE
   dialog?.window?.setDimAmount(0f)
   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      dialog?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
   }
}

fun DialogFragment.resetFullStatusBar() {
   dialog?.window?.statusBarColor = Color.TRANSPARENT
   dialog?.window?.setDimAmount(0.6f)
   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      dialog?.window?.decorView?.systemUiVisibility = 0
   }
}

fun AppCompatActivity.statusBarTransparentFullScreen() {
   window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
   window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
   window.statusBarColor = Color.TRANSPARENT
   window.decorView.systemUiVisibility =
      View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
}

fun Fragment.clearLightStatusBar() {
   activity!!.window.apply {
      clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
      addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
         decorView.systemUiVisibility = 0
      }
      statusBarColor = ContextCompat.getColor(activity!!, R.color.colorPrimaryDark)
   }
}

fun Fragment.setLightStatusBar() {
   activity!!.window.apply {
      clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
      addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
         decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
      }
      statusBarColor = Color.WHITE
   }
}

fun Long.getCountDownTimerString(): String {
   val minute = (this / Constants.ONE_SECOND)
   val second = (this % Constants.ONE_SECOND / Constants.COUNT_DOWN_INTERVAL)
   return String.format("%02d : %02d", minute, second)
}

fun Uri.getMultiPartBody(fieldName: String): MultipartBody.Part {
   val file = File(this.path!!)
   val requestFile = file.asRequestBody(MultipartBody.FORM)
   return MultipartBody.Part.createFormData(fieldName, file.name, requestFile)
}

fun Uri.getImageRealPath(context: Context?): String {
   var cursor: Cursor? = null
   return try {
      val pro = arrayOf(MediaStore.Images.Media.DATA)
      cursor = context?.contentResolver?.query(this, pro, null, null, null)
      val columnIndex = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
      cursor.moveToFirst()
      cursor.getString(columnIndex)
   } finally {
      cursor?.close()
   }
}

fun Fragment.pickImageFromGallery() {
   val intent = Intent(Intent.ACTION_PICK)
   intent.type = "image/*"
   startActivityForResult(intent, OptionPickImageBSDialog.PICK_IMAGE_IN_GALLERY_CODE)
}

fun Fragment.takeImageCamera(context: Context?, applicationId: String): Uri? {
   context ?: return null
   var currentImagePath: String? = null

   @Throws(IOException::class)
   fun createImageFile(context: Context): File {
      // Create an image file name
      val format = Constants.YEAR_MONTH_DAY_DASH_HOUR_MIN_SEC
      val timeStamp: String = SimpleDateFormat(format, Locale.getDefault()).format(Date())
      val storageDir: File = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
      return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir).apply {
         currentImagePath = absolutePath
      }
   }

   val authority = "$applicationId.provider"
   val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
      takePictureIntent.resolveActivity(context.packageManager)?.also {
         val photoFile: File? = try {
            createImageFile(context)
         } catch (ex: IOException) {
            null
         }
         photoFile?.also {
            val photoURI: Uri = FileProvider.getUriForFile(context, authority, it)
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
         }
      }
   }
   startActivityForResult(intent, OptionPickImageBSDialog.PICK_IMAGE_FROM_CAMERA_CODE)
   return Uri.parse(currentImagePath)
}

fun String.loadJsonFromAsset(context: Context): String? {
   val json: String
   try {
      val inputStream = context.assets.open(this)
      val size = inputStream.available()
      val buffer = ByteArray(size)
      inputStream.read(buffer)
      inputStream.close()
      json = String(buffer, StandardCharsets.UTF_8)
   } catch (ex: IOException) {
      ex.printStackTrace()
      return null
   }
   return json
}

fun AppCompatActivity.startIntentUpdateApp(url: String) {
   val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
   intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
   startActivity(intent)
   finish()
}

fun LatLng?.getCameraUpdate(): CameraUpdate {
   return CameraUpdateFactory.newLatLngZoom(this, LocationPickerDialog.MY_LOCATION_ZOOM)
}

fun Fragment.showDatePickerDialog(
   titleRes: Int? = null,
   currentSelected: Long? = null,
   calendarConstraintsBuilder: CalendarConstraints.Builder? = null
): MaterialDatePicker<Long> {
   val datePickerDialogBuilder = MaterialDatePicker.Builder.datePicker()
   titleRes?.let { datePickerDialogBuilder.setTitleText(it) }
   currentSelected?.let { datePickerDialogBuilder.setSelection(it) }
   calendarConstraintsBuilder?.let { datePickerDialogBuilder.setCalendarConstraints(it.build()) }
   val datePickerDialog = datePickerDialogBuilder.build()
   datePickerDialog.show(childFragmentManager, null)
   return datePickerDialog
}