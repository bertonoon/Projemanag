package eu.tutorials.projemanag

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import eu.tutorials.projemanag.databinding.ActivityMainBinding
import eu.tutorials.projemanag.databinding.ActivitySignInBinding
import eu.tutorials.projemanag.utilis.SetFlagsFullScreen

class SignInActivity : AppCompatActivity() {
    private var binding : ActivitySignInBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySignInBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding?.root)
        SetFlagsFullScreen().set(window)
        setupActionBar()
    }

    private fun setupActionBar(){
        setSupportActionBar(binding?.toolbarSignInActivity)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        binding?.toolbarSignInActivity?.setNavigationOnClickListener { finish() }
    }
}