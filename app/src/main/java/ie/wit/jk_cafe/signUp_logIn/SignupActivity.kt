package ie.wit.jk_cafe.signUp_logIn

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import ie.wit.jk_cafe.R
import ie.wit.jk_cafe.main.MainActivity
import kotlinx.android.synthetic.main.signup_activity.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast
import java.util.regex.Pattern

class SignupActivity: AppCompatActivity(), AnkoLogger {

    lateinit var app: MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_activity)

        app = application as MainActivity
        val signupBtn = findViewById<View>(R.id.signUpBtn)

        signupBtn.setOnClickListener(View.OnClickListener { view ->
            signUp()
        })

        loginTxtBtn.setOnClickListener {
            startActivityForResult(intentFor<LoginActivity>(), 0)
        }
    }

    private fun signUp() {

        val emailText = findViewById<View>(R.id.emailText) as EditText
        val passwordText = findViewById<View>(R.id.passwordText) as EditText

        val email = emailText.text.toString()
        val password = passwordText.text.toString()

        fun passwordCheck(): Boolean {
            var exp = ".*[0-9].*"
            var pattern = Pattern.compile(exp, Pattern.CASE_INSENSITIVE)
            var matcher = pattern.matcher(password)
            if (password.length < 8 && !matcher.matches()) {
                return false
            }

            exp = ".*[A-Z].*"
            pattern = Pattern.compile(exp)
            matcher = pattern.matcher(password)
            if (!matcher.matches()) {
                return false
            }

            // Password should contain at least one small letter
            exp = ".*[a-z].*"
            pattern = Pattern.compile(exp)
            matcher = pattern.matcher(password)
            if (!matcher.matches()) {
                return false
            }
            return true
        }

        if (email.isNotEmpty() && passwordCheck()) {
            app.auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, OnCompleteListener { task ->
                    if (task.isSuccessful) {

                        val user = app.auth.currentUser
                        val uid = user!!.uid

                        startActivity(Intent(this, LoginActivity::class.java))
                        Toast.makeText(this, "You have successfully signed up", Toast.LENGTH_LONG)
                            .show()

                        user.sendEmailVerification()
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    toast("Verification Email sent")
                                }
                            }
                    }
                    else {
                        Toast.makeText(this, "Error. Please try again", Toast.LENGTH_LONG).show()
                    }
                })

        } else {
            toast("Please complete all fields correctly")
        }

    }
}