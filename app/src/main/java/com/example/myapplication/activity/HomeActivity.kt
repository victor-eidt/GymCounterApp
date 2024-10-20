package com.example.myapplication.activity

import android.content.Intent
import android.content.res.Configuration
import android.nfc.Tag
import android.os.Bundle
import android.os.PersistableBundle
import android.renderscript.ScriptGroup.Binding
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityHomeBinding
import com.example.myapplication.global.DB
import com.example.myapplication.manager.SessionManager
import com.google.android.material.navigation.NavigationView
import fragment.FragmentAddMember
import fragment.FragmentAddUpdateFee
import fragment.FragmentAllMember
import fragment.FragmentChangePassword
import fragment.FragmentFeePending


class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private val TAG = "homeActivity"
    var session: SessionManager? = null
    var db: DB? = null
    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = DB(this)
        session = SessionManager(this)
        setSupportActionBar(binding.homeInclude.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.navView.setNavigationItemSelectedListener(this)
        drawer = binding.drawerLayout

        toggle = ActionBarDrawerToggle(
            this,
            drawer,
            binding.homeInclude.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_Closed
        )
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        val fragment = FragmentAllMember()
        loadFragment(fragment)


    }

    override fun onPostCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onPostCreate(savedInstanceState, persistentState)
        toggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        toggle.onConfigurationChanged(newConfig)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                val fragment = FragmentAllMember()
                loadFragment(fragment)

                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START)
                }
            }

            R.id.nav_add -> {
                val fragment = FragmentAddMember()
                loadFragment(fragment)

                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START)
                }
            }

            R.id.nav_nav_fee_pending -> {
                val fragment = FragmentFeePending()
                loadFragment(fragment)

                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START)
                }
            }

            R.id.nav_update_fee -> {
                val fragment = FragmentAddUpdateFee()
                loadFragment(fragment)

                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START)
                }
            }

            R.id.nav_change_password -> {
                val fragment = FragmentChangePassword()
                loadFragment(fragment)


                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START)
                }
            }

            R.id.nav_log_out -> {
                logOut()

                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START)
                }
            }

        }
        return true
    }

    private fun logOut(){
        session?.setLogin(false)
        val intent = Intent(this,LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun loadFragment(fragment:Fragment){
        var fragmentManager: FragmentManager?=null
        fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.frame_container,fragment,"Home").commit()
    }

}