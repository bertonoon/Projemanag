package eu.tutorials.projemanag.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import eu.tutorials.projemanag.R
import eu.tutorials.projemanag.databinding.ActivitySignInBinding
import eu.tutorials.projemanag.firebase.FirestoreClass
import eu.tutorials.projemanag.models.User
import eu.tutorials.projemanag.utils.SetFlagsFullScreen

class SignInActivity : BaseActivity() {
    private var binding : ActivitySignInBinding? = null
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        auth = Firebase.auth
        setContentView(binding?.root)
        SetFlagsFullScreen().set(window)
        setupActionBar()
    }

    override fun onResume() {
        super.onResume()

        binding?.btnSignIn?.setOnClickListener{
            login()
        }
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
            reload();
        }
    }

    private fun login(){
        val email = binding?.etEmail?.text.toString()
        val password = binding?.etPassword?.text.toString()

        if(validateForm(email,password)){
            showProgressDialog(resources.getString(R.string.please_wait))
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    hideProgressDialog()
                    if (task.isSuccessful) {
                        FirestoreClass().loadUserData(this@SignInActivity)
                    } else {
                        Toast.makeText(this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                    }
                }
        }

    }

    private fun reload() {
        val user = auth.currentUser?.email
        Toast.makeText(this, "$user logged",Toast.LENGTH_SHORT).show()
        startActivity(Intent(this,MainActivity::class.java))
    }

    private fun setupActionBar(){
        setSupportActionBar(binding?.toolbarSignInActivity)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        binding?.toolbarSignInActivity?.setNavigationOnClickListener { finish() }
    }

    fun signInSuccess(user: User){
        hideProgressDialog()
        startActivity(Intent(this,MainActivity::class.java))
        finish()
    }


    private fun validateForm(email: String, password: String):Boolean{
        return when{
            TextUtils.isEmpty(email)->{
                showErrorSnackBar("Please enter an email address")
                false
            }
            TextUtils.isEmpty(password)->{
                showErrorSnackBar("Please enter a password")
                false
            } else->{
                true
            }
        }
    }


}