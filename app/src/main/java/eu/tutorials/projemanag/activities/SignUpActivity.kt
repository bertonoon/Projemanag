package eu.tutorials.projemanag.activities

import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
import eu.tutorials.projemanag.R
import eu.tutorials.projemanag.databinding.ActivitySignUpBinding
import eu.tutorials.projemanag.utils.SetFlagsFullScreen
import com.google.firebase.auth.ktx.auth
import eu.tutorials.projemanag.firebase.FirestoreClass
import eu.tutorials.projemanag.models.User


class SignUpActivity : BaseActivity() {
    private var binding : ActivitySignUpBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)

        setContentView(binding?.root)


        SetFlagsFullScreen().set(window)
        setupActionBar()

    }

    private fun setupActionBar(){
        setSupportActionBar(binding?.toolbarSignUpActivity)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        binding?.toolbarSignUpActivity?.setNavigationOnClickListener { finish() }

        binding?.btnSignUp?.setOnClickListener{
            registerUser()
        }
    }

    fun userRegisteredSuccess(){
        Toast.makeText(this,
            "you have successfully registered.",
            Toast.LENGTH_SHORT)
            .show()
        hideProgressDialog()
        FirebaseAuth.getInstance().signOut()
        finish()
    }

    private fun registerUser(){
        val name: String = binding?.etName?.text.toString().trim{it < ' '}
        val email: String = binding?.etEmail?.text.toString().trim{it < ' '}
        val password: String = binding?.etPassword?.text.toString().trim{it < ' '}

        if(validateForm(name,email,password)){
            showProgressDialog(resources.getString(R.string.please_wait))
            FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email,password).addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        val firebaseUser : FirebaseUser = task.result.user!!
                        val registeredEmail = firebaseUser.email!!
                        val user = User(firebaseUser.uid,name,registeredEmail)
                        FirestoreClass().registerUser(this,user)
                    } else {
                        hideProgressDialog()
                        Toast.makeText(this,
                            task.exception!!.message,
                            Toast.LENGTH_SHORT)
                            .show()
                    }
                }
        }

    }

    private fun validateForm(name: String, email: String, password: String):Boolean{
        return when{
            TextUtils.isEmpty(name)->{
                showErrorSnackBar("Please enter a name")
                false
            }
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