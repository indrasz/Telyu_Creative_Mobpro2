package org.d3ifcool.telcrav.ui.mainscreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.d3ifcool.telcrav.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.bottomNavigationView)

        val navController = findNavController(R.id.navHostFragment)
        AppBarConfiguration.Builder(
            R.id.navigation_home,
            R.id.navigation_wishlist,
            R.id.navigation_search,
            R.id.navigation_account
        ).build()

        navView.setupWithNavController(navController)
    }
}