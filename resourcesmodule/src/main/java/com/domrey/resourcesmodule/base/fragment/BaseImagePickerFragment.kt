package com.domrey.resourcesmodule.base.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import com.domrey.resourcesmodule.R
import com.domrey.resourcesmodule.dialog.OptionPickImageBSDialog
import com.domrey.resourcesmodule.extension.getImageRealPath
import com.domrey.resourcesmodule.extension.pickImageFromGallery
import com.domrey.resourcesmodule.extension.takeImageCamera
import com.domrey.resourcesmodule.helpers.permission.Permission
import com.domrey.resourcesmodule.helpers.permission.PermissionsFactory
import javax.inject.Inject

abstract class BaseImagePickerFragment<T : ViewDataBinding>(
   private val applicationId: String,
   @LayoutRes val contentId: Int
) : BaseInjectFragment<T>(contentId) {

   @Inject
   lateinit var permissionsFactory: PermissionsFactory

   private val cameraStoragePermission: Permission by lazy {
      permissionsFactory.getPermission(Permission.PERMISSION_CAMERA_STORAGE)
   }
   private var optionCode = OptionPickImageBSDialog.PICK_IMAGE_FROM_CAMERA_CODE
   private var cameraImageUri: Uri? = null

   abstract fun onImagePicked(uri: Uri?)

   override fun onRequestPermissionsResult(
      requestCode: Int,
      permissions: Array<out String>,
      grantResults: IntArray
   ) {
      super.onRequestPermissionsResult(requestCode, permissions, grantResults)
      cameraStoragePermission.permissionResult(grantResults, {
         if (requestCode == Permission.PERMISSION_CAMERA_STORAGE) startActionImagePicker()
      }, {
         showToast(R.string.permissions_denied)
      })
   }

   override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
      super.onActivityResult(requestCode, resultCode, data)
      if ((requestCode == OptionPickImageBSDialog.PICK_IMAGE_IN_GALLERY_CODE ||
            requestCode == OptionPickImageBSDialog.PICK_IMAGE_FROM_CAMERA_CODE) &&
         resultCode == Activity.RESULT_OK
      ) {
         val uri = if (data?.data != null) Uri.parse(data.data?.getImageRealPath(context))
         else cameraImageUri
         onImagePicked(uri)
      }
   }

   fun showImagePickerDialog() {
      val imagePickerDialog = OptionPickImageBSDialog.newInstance()
      imagePickerDialog.show(childFragmentManager, imagePickerDialog.tag)
      imagePickerDialog.setOnPickerOptionSelectedListener { askPermissions(it) }
   }

   private fun askPermissions(optionCode: Int) {
      this.optionCode = optionCode
      if (cameraStoragePermission.isPermissionGranted(requireContext())) {
         startActionImagePicker()
      } else {
         cameraStoragePermission.requestPermission(requireContext()) { perStrings, resultPerCode ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
               requestPermissions(perStrings, resultPerCode)
            }
         }
      }
   }

   private fun startActionImagePicker() {
      if (optionCode == OptionPickImageBSDialog.PICK_IMAGE_FROM_CAMERA_CODE) {
         cameraImageUri = takeImageCamera(context, applicationId)
      } else {
         pickImageFromGallery()
      }
   }

}