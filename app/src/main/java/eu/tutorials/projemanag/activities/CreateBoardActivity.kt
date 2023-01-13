package eu.tutorials.projemanag.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import eu.tutorials.projemanag.R
import eu.tutorials.projemanag.databinding.ActivityCreateBoardBinding
import eu.tutorials.projemanag.firebase.FirestoreClass
import eu.tutorials.projemanag.models.Board
import eu.tutorials.projemanag.utils.Constants
import java.io.IOException

class CreateBoardActivity : BaseActivity() {
    private var mSelectedImageFileUri : Uri? = null
    private lateinit var binding : ActivityCreateBoardBinding
    private lateinit var mUserName : String
    private var mBoardImageURL : String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActionBar()

        if(intent.hasExtra(Constants.NAME)){
            mUserName = intent.getStringExtra(Constants.NAME).toString()
        }

        binding.ivBoardImage.setOnClickListener{
            Constants.choosePhotoFromGallery(this)
        }

        binding.btnCreate.setOnClickListener {
            if (mSelectedImageFileUri != null) {
                uploadBoardImage()
            } else {
                createBoard()
            }
        }
    }

    private fun setupActionBar() {
        val toolbar: Toolbar = findViewById(R.id.toolbar_create_board_activity)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        supportActionBar?.title = resources.getString(R.string.create_board_title)
        toolbar.setNavigationOnClickListener { finish() }
    }

    fun boardCreatedSuccessfully(){
        Toast.makeText(this,
            "You have successfully created a board.",
            Toast.LENGTH_SHORT)
            .show()
        hideProgressDialog()
        finish()
    }

    private fun createBoard(){
        val name: String = binding.etBoardName.text.toString()
        val assignedUsersArrayList : ArrayList<String> = ArrayList()
        assignedUsersArrayList.add(getCurrentUserID())

        if(validateForm(name)) {
            showProgressDialog(resources.getString(R.string.please_wait))
            val board = Board(name, mBoardImageURL, mUserName,assignedUsersArrayList)
            FirestoreClass().createBoard(this, board)
        }
    }

    private fun uploadBoardImage(){
            showProgressDialog(resources.getString(R.string.please_wait))
            val sRef : StorageReference = FirebaseStorage
                .getInstance()
                .reference.child("BOARD_IMAGE"+System.currentTimeMillis()+"."+ Constants.getFileExtension(
                    mSelectedImageFileUri!!,this@CreateBoardActivity
                ))

            sRef.putFile(mSelectedImageFileUri!!).addOnSuccessListener {
                    taskSnapShot ->
                Log.i("Firebase Image URL",
                    taskSnapShot.metadata!!.reference!!.downloadUrl.toString())

                taskSnapShot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                        uri ->
                    Log.i("Downloadable Image URL",uri.toString())
                    mBoardImageURL = uri.toString()
                    createBoard()
                }
            }.addOnFailureListener{
                    exception ->
                Toast.makeText(this@CreateBoardActivity,exception.message,Toast.LENGTH_LONG)
                    .show()
                hideProgressDialog()
            }
    }

    private fun validateForm(name: String):Boolean {
        return when {
            TextUtils.isEmpty(name) -> {
                showErrorSnackBar("Please enter a name")
                false
            }
            /*
            mSelectedImageFileUri == null -> {
                showErrorSnackBar("Please choose image")
                false
            }
            */
            else -> {
                true
            }
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
                            .with(this@CreateBoardActivity)
                            .load(Uri.parse(mSelectedImageFileUri.toString())) // URI of the image
                            .centerCrop() // Scale type of the image.
                            .placeholder(R.drawable.ic_user_place_holder) // A default place holder
                            .into(binding.ivBoardImage) // the view in which the image will be loaded.
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(
                            this@CreateBoardActivity,
                            "Failed to load the image from gallery",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }


}

