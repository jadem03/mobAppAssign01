package ie.wit.jk_cafe.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import ie.wit.jk_cafe.R
import ie.wit.jk_cafe.signUp_logIn.LoginActivity

class SplashScreen: AppCompatActivity()
{
    val SPLASHSCREEN_TIMER: Long = 2000

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
    setContentView(R.layout.splash_screen)

    Handler().postDelayed(
    {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }, SPLASHSCREEN_TIMER)
}
}