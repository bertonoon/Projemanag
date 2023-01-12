package eu.tutorials.projemanag.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import eu.tutorials.projemanag.R
import eu.tutorials.projemanag.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var binding : ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
    }

    override fun onResume() {
        super.onResume()
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Firebase.auth.signOut()
                startActivity(Intent(this@MainActivity,IntroActivity::class.java))
                finish()
            }
        })

    }




}
