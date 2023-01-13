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
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
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
import eu.tutorials.projemanag.utils.Constants
import java.io.IOException

class MyProfileActivity : BaseActivity()  {

    private var binding : ActivityMyProfileBinding? = null
    private var mSelectedImageFileUri : Uri? = null
    private var mProfileImageURL : String = ""
    private lateinit var mUserDetails : User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyProfileBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setupActionBar()
        FirestoreClass().loadUserData(this)

        binding?.ivUserImage?.setOnClickListener{
            Constants.choosePhotoFromGallery(this)
        }
        binding?.btnUpdate?.setOnClickListener{
            if(mSelectedImageFileUri != null){
                uploadUserImage()
            } else{
                showProgressDialog(resources.getString(R.string.please_wait))
                updateUserProfileData()
            }
        }

    }

    private fun setupActionBar() {
        val toolbar: Toolbar = findViewById(R.id.toolbar_my_profile_activity)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        actionBar?.title = resources.getString(R.string.my_profile_title)
        toolbar.setNavigationOnClickListener { finish() }

    }

    fun setUserDataInUI(user : User){
        mUserDetails = user
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
            if (requestCode == Constants.GALLERY_CODE) {
                if (data != null) {
                    mSelectedImageFileUri = data.data!!
                    try {
                        Glide
                            .with(this@MyProfileActivity)
                            .load(Uri.parse(mSelectedImageFileUri.toString())) // URI of the image
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

    private fun uploadUserImage(){
        showProgressDialog(resources.getString(R.string.please_wait))

        if(mSelectedImageFileUri != null){
            val sRef : StorageReference = FirebaseStorage
                .getInstance()
                .reference.child("USER_IMAGE"+System.currentTimeMillis()+"."+ Constants.getFileExtension(
                    mSelectedImageFileUri!!,this@MyProfileActivity
                ))

            sRef.putFile(mSelectedImageFileUri!!).addOnSuccessListener {
                taskSnapShot ->
                Log.i("Firebase Image URL",
                    taskSnapShot.metadata!!.reference!!.downloadUrl.toString())

                taskSnapShot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                    uri ->
                    Log.i("Downloadable Image URL",uri.toString())
                    mProfileImageURL = uri.toString()
                    updateUserProfileData()

                }
            }.addOnFailureListener{
                exception ->
                Toast.makeText(this@MyProfileActivity,exception.message,Toast.LENGTH_LONG)
                    .show()
                hideProgressDialog()
            }
        }
    }

    fun profileUpdateSuccess(){
        hideProgressDialog()
        setResult(Activity.RESULT_OK)
        finish()
    }

    private fun updateUserProfileData(){
        val userHashMap = HashMap<String,Any>()
        var anyChangesMade = false

        if(mProfileImageURL.isNotEmpty() && mProfileImageURL != mUserDetails.image){
            userHashMap[Constants.IMAGE] = mProfileImageURL
            anyChangesMade = true
        }
        if(binding?.etName?.text.toString() != mUserDetails.name){
            userHashMap[Constants.NAME] = binding?.etName?.text.toString()
            anyChangesMade = true

        }
        if(binding?.etMobile?.text.toString() != mUserDetails.mobile.toString()){
            userHashMap[Constants.MOBILE] = binding?.etMobile?.text.toString().toLong()
            anyChangesMade = true

        }
        if (anyChangesMade) {
            FirestoreClass().updateUserProfileData(this, userHashMap)
        }

    }

}

