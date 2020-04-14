package ie.wit.jk_cafe.activities

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import ie.wit.jk_cafe.R
import ie.wit.jk_cafe.fragments.*
import ie.wit.jk_cafe.helpers.*
import ie.wit.jk_cafe.main.MainActivity
import ie.wit.jk_cafe.signUp_logIn.LoginActivity
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.home.*
import kotlinx.android.synthetic.main.nav_header_home.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.toast
import java.lang.Exception
import java.time.LocalTime

class Home : AppCompatActivity(), AnkoLogger,

    NavigationView.OnNavigationItemSelectedListener
    {
        lateinit var ft: FragmentTransaction
        lateinit var app: MainActivity

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.home)
            setSupportActionBar(this.toolbar)

            app = application as MainActivity
            app.storage = FirebaseStorage.getInstance().reference
            checkExistingPhoto(app,this)

            navView.setNavigationItemSelectedListener(this)
            navView.getHeaderView(0).headerEmail.text = app.auth.currentUser?.email
            navView.getHeaderView(0).imageView.setOnClickListener {
                showImagePicker(this, 1)
            }

            val toggle = ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
            )
            drawerLayout.addDrawerListener(toggle)
            toggle.syncState()

            ft = supportFragmentManager.beginTransaction()
            val fragment = HomeFragment.newInstance()
            ft.replace(R.id.homeFrame, fragment)
            ft.commit()
        }

        override fun onNavigationItemSelected(item: MenuItem): Boolean {

            when (item.itemId) {
                R.id.nav_home -> navigateTo(HomeFragment.newInstance())
                R.id.nav_receipts -> navigateTo(ReceiptsFragment.newInstance())
                R.id.nav_about -> navigateTo(AboutFragment.newInstance())
                R.id.nav_logout -> onLogOut()

                R.id.nav_order ->{
                    val currentTime = LocalTime.now()
                    val openTime = LocalTime.of(7, 0, 10)
                    val closeTime = LocalTime.of(18, 0, 10)
                    if (currentTime > openTime && currentTime < closeTime) {
                        navigateTo(OrderFragment.newInstance())
                    }
                    else {
                        val dialogBox = AlertDialog.Builder(this)
                        dialogBox.setTitle("Sorry!")
                        dialogBox.setMessage(
                            "We're Closed!" +
                                    "Our open times are 07am - 06pm Monday - Sunday."
                        )
                        dialogBox.setNeutralButton("OK") { dialog, which ->
                            dialog.cancel()
                        }
                        val myDialog = dialogBox.create()
                        myDialog.show()
                    }
                }

                else -> toast("You Selected Something Else")
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            return true
        }

        override fun onCreateOptionsMenu(menu: Menu): Boolean {
            menuInflater.inflate(R.menu.menu_home, menu)
            return true
        }
        override fun onOptionsItemSelected(item: MenuItem): Boolean {

            when (item.itemId) {
                R.id.action_home -> navigateTo(HomeFragment.newInstance())

                R.id.action_order -> {
                    val currentTime = LocalTime.now()
                    val openTime = LocalTime.of(7, 0, 10)
                    val closeTime = LocalTime.of(18, 0, 10)
                    if (currentTime > openTime && currentTime < closeTime) {

                        navigateTo(OrderFragment.newInstance())
                    } else {
                        val dialogBox = AlertDialog.Builder(this)
                        dialogBox.setTitle("Sorry!")
                        dialogBox.setMessage(
                            "We're Closed!" +
                                    "Our open times are 07am - 06pm Monday - Sunday."
                        )

                        dialogBox.setNeutralButton("OK") { dialog, which ->
                            dialog.cancel()
                        }
                        val myDialog = dialogBox.create()
                        myDialog.show()
                    }
                }

                R.id.action_receipts -> navigateTo(ReceiptsFragment.newInstance())
                else -> toast("You Selected Something Else")
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            return true
        }

        private fun onLogOut(){
            FirebaseAuth.getInstance().signOut()
            app.googleSignInClient.signOut()
            toast("Logged Out")
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            when (requestCode) {
                1 -> {
                    if (data != null) {
                        writeImageRef(app,readImageUri(resultCode, data).toString())
                        Picasso.get().load(readImageUri(resultCode, data).toString())
                            .resize(180, 180)
                            .transform(CropCircleTransformation())
                            .into(navView.getHeaderView(0).imageView, object : Callback {
                                override fun onSuccess() {
                                    // Drawable is ready
                                    uploadImageView(app,navView.getHeaderView(0).imageView)
                                }
                                override fun onError(e: Exception) {}
                            })
                    }
                }
            }
        }

        override fun onBackPressed() {
            if (drawerLayout.isDrawerOpen(GravityCompat.START))
                drawerLayout.closeDrawer(GravityCompat.START)
            else
                super.onBackPressed()
        }

        private fun navigateTo(fragment: Fragment) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.homeFrame, fragment)
                .addToBackStack(null)
                .commit()
        }
    }