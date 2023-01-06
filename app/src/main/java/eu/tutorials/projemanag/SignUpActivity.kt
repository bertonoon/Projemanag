package eu.tutorials.projemanag

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import eu.tutorials.projemanag.databinding.ActivitySignUpBinding
import eu.tutorials.projemanag.utilis.SetFlagsFullScreen


class SignUpActivity : AppCompatActivity() {
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
    }


}