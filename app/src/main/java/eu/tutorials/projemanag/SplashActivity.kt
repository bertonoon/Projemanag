package eu.tutorials.projemanag

import android.content.Intent
import android.graphics.Typeface
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowInsets
import android.view.WindowManager
import eu.tutorials.projemanag.databinding.ActivitySplashBinding
import eu.tutorials.projemanag.utilis.SetFlagsFullScreen

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
            startActivity(
                Intent(this@SplashActivity
                    ,IntroActivity::class.java))
        }, 2500)
    }
}