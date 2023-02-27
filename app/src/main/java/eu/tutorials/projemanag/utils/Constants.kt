package eu.tutorials.projemanag.utils

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat.startActivity
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import eu.tutorials.projemanag.activities.MyProfileActivity

object Constants {
    const val USERS: String = "users"
    const val MOBILE: String = "mobile"
    const val IMAGE : String = "image"
    const val NAME : String = "name"
    const val GALLERY_CODE = 1
    const val BOARDS : String = "boards"
    const val CREATED_BY : String = "createdBy"
    const val ASSIGNED_TO : String = "assignedTo"



fun getFileExtension(uri: Uri, activity: Activity) : String?{
    return MimeTypeMap
        .getSingleton()
        .getExtensionFromMimeType(
            activity.contentResolver.getType(uri))
}

fun choosePhotoFromGallery(activity: Activity){
    Dexter.withContext(activity)
        .withPermission(
            Manifest.permission.READ_EXTERNAL_STORAGE
        ).withListener(object: PermissionListener {
            override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activity.startActivityForResult(galleryIntent, Constants.GALLERY_CODE)
            }
            override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                Toast.makeText(
                    activity,
                    "Permission denied.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            override fun onPermissionRationaleShouldBeShown(
                p0: PermissionRequest?,
                p1: PermissionToken?
            ) {
                showRationaleDialogForPermissions(activity)
            }
        }).onSameThread().check()
}

private fun showRationaleDialogForPermissions(activity: Activity) {
    AlertDialog.Builder(activity).setMessage(
        "It looks like you have turned off permission required for this feature." +
                "It can be enabled under the Application Settings.")
        .setPositiveButton("Go to settings"){
                _,_ ->
            try {
                val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package",activity.packageName,null)
                intent.data = uri
                activity.startActivity(intent)
            } catch (e: ActivityNotFoundException){
                e.printStackTrace()
            }
        }.setNegativeButton("Cancel"){
                dialog,_ ->
            dialog.dismiss()
        }.show()
}

}