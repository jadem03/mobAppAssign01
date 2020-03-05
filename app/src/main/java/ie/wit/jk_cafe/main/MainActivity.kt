package ie.wit.jk_cafe.main

import android.app.Application
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import org.jetbrains.anko.AnkoLogger

class MainActivity : Application(), AnkoLogger {

    lateinit var database: DatabaseReference
    lateinit var auth: FirebaseAuth

    override fun onCreate() {
        super.onCreate()

        auth = FirebaseAuth.getInstance()
        Log.v("Order","JK Café App started")

    }
}