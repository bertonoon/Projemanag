package eu.tutorials.projemanag.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import eu.tutorials.projemanag.databinding.ActivityIntroBinding
import eu.tutorials.projemanag.utils.SetFlagsFullScreen

class IntroActivity : BaseActivity() {
    private var binding : ActivityIntroBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        SetFlagsFullScreen().set(window)

        binding?.btnSignUpIntro?.setOnClickListener{
            startActivity(Intent(this@IntroActivity, SignUpActivity::class.java))
        }
        binding?.btnSignInIntro?.setOnClickListener{
            startActivity(Intent(this@IntroActivity, SignInActivity::class.java))
        }
    }
}