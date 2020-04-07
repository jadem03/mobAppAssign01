package ie.wit.jk_cafe.main

import android.app.Application
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.jetbrains.anko.AnkoLogger

class MainActivity : Application(), AnkoLogger {

    lateinit var database: DatabaseReference
    lateinit var auth: FirebaseAuth
    lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate() {
        super.onCreate()

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference
        Log.v("Order","JK Café App started")
    }
}