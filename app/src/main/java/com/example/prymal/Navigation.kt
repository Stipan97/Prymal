package com.example.prymal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.prymal.view.fragments.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_navigation.*

class Navigation : AppCompatActivity() {

    private var firebase: Firebase = Firebase()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)

        showFragment(Home())

        nav_navBar.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.nav_home -> {
                    showFragment(Home())
                }

                R.id.nav_search -> {
                    showFragment(Search())
                }

                R.id.nav_add -> {
                    showFragment(Add())
                }

                R.id.nav_notifications -> {
                    showFragment(Notifications())
                }

                R.id.nav_profile -> {
                    showFragment(Profile())
                }
            }

            true
        }
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.lay_fragments, fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.logout, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_logout) {
            firebase.auth = FirebaseAuth.getInstance()
            firebase.logout(this)
        }
        return true
    }
}