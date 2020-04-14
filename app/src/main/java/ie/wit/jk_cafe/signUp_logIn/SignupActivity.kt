package ie.wit.jk_cafe.signUp_logIn

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import ie.wit.jk_cafe.R
import ie.wit.jk_cafe.activities.Home
import ie.wit.jk_cafe.main.MainActivity
import kotlinx.android.synthetic.main.signup_activity.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import java.util.regex.Pattern

class SignUpActivity: AppCompatActivity(), AnkoLogger {

    lateinit var app: MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_activity)

        app = application as MainActivity

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        app.googleSignInClient = GoogleSignIn.getClient(this, gso)

        app.storage = FirebaseStorage.getInstance().reference

        signUpBtn.setOnClickListener {
            signUp()
        }

        google_button.setOnClickListener{
           googleSignIn()
        }

        loginTxtBtn.setOnClickListener {
            startActivityForResult(intentFor<LoginActivity>(), 0)
        }
    }

    companion object {
        private const val RC_SIGN_IN = 9001
    }

    private fun signUp() {
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

    private fun googleSignIn() {
        val signInIntent = app.googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {

            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        app.auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = app.auth.currentUser
                    app.database = FirebaseDatabase.getInstance().reference
                    startActivity<Home>()
                } else {
                    toast("Fail")
                }
            }
    }
}