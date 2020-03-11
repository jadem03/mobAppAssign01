package ie.wit.jk_cafe.signUp_logIn

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import ie.wit.jk_cafe.R
import ie.wit.jk_cafe.activities.Home
import ie.wit.jk_cafe.main.MainActivity
import kotlinx.android.synthetic.main.login_activity.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast

class LoginActivity: AppCompatActivity(), AnkoLogger {

    lateinit var app: MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        app = application as MainActivity

        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            startActivityForResult(intentFor<Home>(), 0)
        } else {
            login()
        }

        val loginBtn = findViewById<View>(R.id.loginBtn)
        loginBtn.setOnClickListener(View.OnClickListener { view ->
            login()
        })

        signupTxtBtn.setOnClickListener {
            startActivityForResult(intentFor<SignupActivity>(), 0)
        }
    }

    private fun verifyEmail()
    {
        val user = app.auth.currentUser
        if(user!=null) {
            if (user.isEmailVerified) {

                startActivity(Intent(this, Home::class.java))
                toast("Log in success")
            } else {
                toast("email not verified")
                app.auth.signOut()
            }
        }
    }

    private fun login() {
        val emailText = findViewById<View>(R.id.emailText) as EditText
        val email = emailText.text.toString()
        val passwordText = findViewById<View>(R.id.passwordText) as EditText
        val password = passwordText.text.toString()

        app.auth.currentUser?.reload()

        if (email.isNotEmpty() || password.isNotEmpty()) {
            app.auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->

                    if (task.isSuccessful) {
                        verifyEmail()
                    } else {
                        toast("Log in failed")
                    }

                }
        }
        else {
            toast("Enter email and password")
        }
    }
}