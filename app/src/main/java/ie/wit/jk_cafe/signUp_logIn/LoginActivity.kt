package ie.wit.jk_cafe.signUp_logIn

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
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

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        app.googleSignInClient = GoogleSignIn.getClient(this, gso)

        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            startActivityForResult(intentFor<Home>(), 0)
        } else {
            login()
        }

        loginBtn.setOnClickListener {
            login()
        }

        google_button.setOnClickListener {
            googleSignIn()
        }

        signUpTxtBtn.setOnClickListener {
            startActivityForResult(intentFor<SignUpActivity>(), 0)
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

    companion object {
        private const val RC_SIGN_IN = 9001
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
                } else {
                    toast("Fail")
                }
            }
    }
}