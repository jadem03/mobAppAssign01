package ie.wit.jk_cafe.main

import android.app.Application
import android.location.Location
import android.net.Uri
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import org.jetbrains.anko.AnkoLogger

class MainActivity : Application(), AnkoLogger {

    lateinit var database: DatabaseReference
    lateinit var auth: FirebaseAuth
    lateinit var googleSignInClient: GoogleSignInClient
    lateinit var storage: StorageReference
    lateinit var userImage: Uri

    lateinit var mMap : GoogleMap
    lateinit var marker : Marker

    lateinit var currentLocation : Location
    lateinit var locationClient : FusedLocationProviderClient

    override fun onCreate() {
        super.onCreate()

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference
        storage = FirebaseStorage.getInstance().reference

        Log.v("Order","JK Café App started")
    }
}