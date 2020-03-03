package ie.wit.jk_cafe.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import ie.wit.jk_cafe.R
import ie.wit.jk_cafe.adapters.DeleteListener
import ie.wit.jk_cafe.adapters.OrderAdapter
import ie.wit.jk_cafe.fragments.*
import ie.wit.jk_cafe.main.MainActivity
import ie.wit.jk_cafe.models.OrderModel
import ie.wit.jk_cafe.signUp_logIn.LoginActivity
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.fragment_receipts.*
import kotlinx.android.synthetic.main.home.*
import org.jetbrains.anko.toast

class Home : AppCompatActivity(),

    NavigationView.OnNavigationItemSelectedListener
    {
        lateinit var ft: FragmentTransaction
        lateinit var app: MainActivity

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.home)
            setSupportActionBar(this.toolbar)

            app = application as MainActivity

            navView.setNavigationItemSelectedListener(this)

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
                R.id.nav_order -> navigateTo(OrderFragment.newInstance())
                R.id.nav_receipts -> navigateTo(ReceiptsFragment.newInstance())
                R.id.nav_about -> navigateTo(AboutFragment.newInstance())
                R.id.nav_logout -> onLogOut()
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
                R.id.action_order -> navigateTo(OrderFragment.newInstance())
                R.id.action_receipts -> navigateTo(ReceiptsFragment.newInstance())
                else -> toast("You Selected Something Else")
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            return true
        }

        private fun onLogOut(){
            FirebaseAuth.getInstance().signOut()
            toast("Logged Out")
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
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