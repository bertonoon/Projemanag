package eu.tutorials.projemanag.activities

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import eu.tutorials.projemanag.R
import eu.tutorials.projemanag.databinding.ActivityBaseBinding
import eu.tutorials.projemanag.databinding.DialogProgressBinding

open class BaseActivity : AppCompatActivity() {
    private var binding : ActivityBaseBinding? = null
    private var doubleBackToExitPressedOnce = false
    private lateinit var mProgressDialog : Dialog
    private lateinit var bindingProgressDialog : DialogProgressBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBaseBinding.inflate(layoutInflater)
        setContentView(binding?.root)
    }

    fun showProgressDialog(text: String) {
        bindingProgressDialog = DialogProgressBinding.inflate(layoutInflater)
        mProgressDialog = Dialog(this)
        mProgressDialog.setContentView(bindingProgressDialog.root)
        bindingProgressDialog.tvProgressText.text = text
        mProgressDialog.show()
    }

    fun hideProgressDialog(){
        mProgressDialog.dismiss()
    }

    fun getCurrentUserID(): String {
        return FirebaseAuth.getInstance().currentUser!!.uid
    }

    fun doubleBackToExit(){
        if(doubleBackToExitPressedOnce){
            finish()
            return
        }
        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this,resources.getString(R.string.please_click_back_again_to_exit),
            Toast.LENGTH_LONG)
            .show()

        Handler(Looper.getMainLooper())
            .postDelayed({doubleBackToExitPressedOnce = false}
                ,2000)

    }

    fun showErrorSnackBar(message: String){
        val snackBar = Snackbar.make(findViewById(android.R.id.content),
            message,
            Snackbar.LENGTH_LONG
        )
        val snackBarView = snackBar.view
        snackBarView.setBackgroundColor(ContextCompat.getColor(this,R.color.snackBar_error_color))
        snackBar.show()
    }





}