package eu.tutorials.projemanag.activities

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import eu.tutorials.projemanag.databinding.ActivitySplashBinding
import eu.tutorials.projemanag.firebase.FirestoreClass
import eu.tutorials.projemanag.utils.SetFlagsFullScreen

class SplashActivity : AppCompatActivity() {
    private var binding : ActivitySplashBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        SetFlagsFullScreen().set(window)


        val typeFace : Typeface = Typeface.createFromAsset(assets,"carbon bl.ttf")
        binding?.tvAppName?.typeface = typeFace

        Handler(Looper.getMainLooper()).postDelayed({

            var currentUserID = FirestoreClass().getCurrentUserId()

            if(currentUserID.isNotEmpty()){
                startActivity(Intent(this,
                    MainActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this@SplashActivity,
                    IntroActivity::class.java))
                finish()
            }
        }, 2500)
    }
}