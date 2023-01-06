package eu.tutorials.projemanag

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import eu.tutorials.projemanag.databinding.ActivityIntroBinding
import eu.tutorials.projemanag.utilis.SetFlagsFullScreen

class IntroActivity : AppCompatActivity() {
    private var binding : ActivityIntroBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        SetFlagsFullScreen().set(window)

        binding?.btnSignUpIntro?.setOnClickListener{
            startActivity(Intent(this@IntroActivity,SignUpActivity::class.java))
        }
        binding?.btnSignInIntro?.setOnClickListener{
            startActivity(Intent(this@IntroActivity,SignInActivity::class.java))
        }
    }
}