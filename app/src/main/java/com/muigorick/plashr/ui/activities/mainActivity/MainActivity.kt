package com.muigorick.plashr.ui.activities.mainActivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.muigorick.plashr.R
import com.muigorick.plashr.databinding.ActivityMainBinding
import com.muigorick.plashr.ui.activities.settings.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var settingsViewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initToolbar()

        setupNavigation()

        settingsViewModel = ViewModelProvider(this)[SettingsViewModel::class.java]
        settingsViewModel.readThemeFromDataStore.observe(this) {
            when (it) {
                "Dark" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                "Light" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                "System" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
        }
    }

    /**
     * Sets up the toolbar as a support action bar and gets rid of the app title.
     */
    private fun initToolbar() {
        setSupportActionBar(binding.mainActivityToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    /**
     * Sets up navigation for the Bottom Navigation View.
     * */
    private fun setupNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavView.apply {
            setupWithNavController(navController = navController)
            setOnItemReselectedListener { }
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.mainActivityToolbar.title = when (destination.id) {
                R.id.navigation_home -> getString(R.string.home)
                R.id.navigation_search -> getString(R.string.search)
                R.id.navigation_collections -> getString(R.string.collections)
                R.id.navigation_profile -> getString(R.string.profile)
                else -> ""
            }
        }
    }
}