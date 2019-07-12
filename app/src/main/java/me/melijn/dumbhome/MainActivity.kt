package me.melijn.dumbhome

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import me.melijn.dumbhome.database.Database
import me.melijn.dumbhome.utils.ExtensionFunctions


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        Database().initLiveData(applicationContext)
        ExtensionFunctions().init(applicationContext)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_devices, R.id.navigation_rules
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setOnNavigationItemSelectedListener { item ->
            onNavDestinationSelected(item, navController)
        }
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.overflow_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_item_settings -> {
                findNavController(R.id.nav_host_fragment).navigate(R.id.settingsActivity)
            }
            R.id.menu_item_sync -> {
                findNavController(R.id.nav_host_fragment).navigate(R.id.syncActivity)
            }
            android.R.id.home -> {
                onBackPressed()
            }
        }

        return true
    }
}
