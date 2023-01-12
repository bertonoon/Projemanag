package eu.tutorials.projemanag.activities

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import com.bumptech.glide.Glide
import com.karumi.dexter.Dexter
import com.karumi.dexter.DexterBuilder.SinglePermissionListener
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import de.hdodenhof.circleimageview.CircleImageView
import eu.tutorials.projemanag.R
import eu.tutorials.projemanag.databinding.ActivityMyProfileBinding
import eu.tutorials.projemanag.firebase.FirestoreClass
import eu.tutorials.projemanag.models.User
import java.io.IOException

class MyProfileActivity : BaseActivity()  {

    private var binding : ActivityMyProfileBinding? = null

    companion object {
        private const val GALLERY_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyProfileBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setupActionBar()
        FirestoreClass().loadUserData(this)

        binding?.ivUserImage?.setOnClickListener{
            choosePhotoFromGallery()
        }

    }

    private fun choosePhotoFromGallery(){
        Dexter.withContext(this@MyProfileActivity)
            .withPermission(
                Manifest.permission.READ_EXTERNAL_STORAGE
            ).withListener(object: PermissionListener{
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        startActivityForResult(galleryIntent, GALLERY_CODE)
                }
                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                    Toast.makeText(
                        this@MyProfileActivity,
                        "Permission denied.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                override fun onPermissionRationaleShouldBeShown(
                    p0: PermissionRequest?,
                    p1: PermissionToken?
                ) {
                    showRationaleDialogForPermissions()
                }
            }).onSameThread().check()
    }

    private fun setupActionBar() {
        val toolbar: Toolbar = findViewById(R.id.toolbar_my_profile_activity)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        actionBar?.title = resources.getString(R.string.my_profile_title)
        toolbar.setNavigationOnClickListener { finish() }

    }

    private fun showRationaleDialogForPermissions() {
        AlertDialog.Builder(this@MyProfileActivity).setMessage(
            "It looks like you have turned off permission required for this feature." +
                    "It can be enabled under the Application Settings.")
            .setPositiveButton("Go to settings"){
                    _,_ ->
                try {
                    val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package",packageName,null)
                    intent.data = uri
                    startActivity(intent)
                } catch (e: ActivityNotFoundException){
                    e.printStackTrace()
                }
            }.setNegativeButton("Cancel"){
                    dialog,_ ->
                dialog.dismiss()
            }.show()
    }

    fun setUserDataInUI(user : User){
        Glide
            .with(this@MyProfileActivity)
            .load(user.image)
            .fitCenter()
            .placeholder(R.drawable.ic_user_place_holder)
            .into(binding?.ivUserImage!!)

        binding?.etName?.setText(user.name)
        binding?.etEmail?.setText(user.email)
        if(user.mobile != 0L){
            binding?.etMobile?.setText(user.mobile.toString())
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GALLERY_CODE) {
                if (data != null) {
                    val contentURI = data.data
                    try {
                        Glide
                            .with(this@MyProfileActivity)
                            .load(Uri.parse(contentURI.toString())) // URI of the image
                            .centerCrop() // Scale type of the image.
                            .placeholder(R.drawable.ic_user_place_holder) // A default place holder
                            .into(binding?.ivUserImage!!) // the view in which the image will be loaded.
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(
                            this@MyProfileActivity,
                            "Failed to load the image from gallery",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }
}

