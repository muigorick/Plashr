package com.muigorick.plashr.ui.activities.settings

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.window.OnBackInvokedDispatcher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.NavUtils
import androidx.fragment.app.activityViewModels
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.muigorick.plashr.R
import com.muigorick.plashr.databinding.ActivitySettingsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initToolbar()

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, SettingsFragment())
            .commit()


        settingsViewModel.getRestartMainActivity().observe(this) {
            Log.i("SETTINGS ACTIVITY", "restart activity: $it ")
        }
    }

    override fun onBackPressed() {
        if (settingsViewModel.getRestartMainActivity().value == true) {
            NavUtils.navigateUpFromSameTask(this)
        } else {
            super.onBackPressed()
        }
    }


    private fun initToolbar() {
        setSupportActionBar(binding.settingsActivityToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.settingsActivityToolbar.apply {
            title = resources.getString(R.string.settings)
            setNavigationOnClickListener { onBackPressed() }
        }
    }
}

@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {
    private val settingsViewModel: SettingsViewModel by activityViewModels()
    // private val settingsActivityViewModel: SettingsActivityViewModel by viewModels()

    companion object {
        const val PREF_APP_THEME = "key_app_theme"
        const val PREF_IMAGE_LAYOUT = "key_image_layout"
        const val PREF_LOAD_QUALITY = "key_load_quality"
        const val PREF_RATE_APP = "key_rate_app"
        const val PREF_FEEDBACK = "key_feedback"
        const val PREF_PRIVACY_POLICY = "key_app_privacy_policy"
        const val PREF_ABOUT_US = "key_about_us"
        const val CLEAR_CACHE_SIZE = "key_clear_cache"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // settingsViewModel = ViewModelProvider(requireActivity())[SettingsViewModel::class.java]
    }

    // @DelicateCoroutinesApi
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings_preferences, rootKey)

        val darkModePreference: ListPreference? = findPreference(PREF_APP_THEME)
        darkModePreference?.summaryProvider = appThemeSummaryProvider
        darkModePreference?.setOnPreferenceChangeListener { preference, newValue ->
            when (newValue) {
                "Dark" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                "Light" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                "System" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
            settingsViewModel.saveThemeToDataStore(newValue.toString())

            true
        }

        val imageLayoutPreferences: ListPreference? = findPreference(PREF_IMAGE_LAYOUT)
        imageLayoutPreferences?.summaryProvider = imageLayoutSummaryProvider
        imageLayoutPreferences?.setOnPreferenceChangeListener { preference, newValue ->
            settingsViewModel.saveImageLayoutToDataStore(newValue.toString())
            true
        }

    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences?.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences
            ?.unregisterOnSharedPreferenceChangeListener(this)
    }


    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key.equals(PREF_IMAGE_LAYOUT)) {
            settingsViewModel.setRestartMainActivity(restartMainActivity = true)
        }
    }


    private val appThemeSummaryProvider =
        Preference.SummaryProvider<ListPreference> { preference ->
            when (preference.value) {
                "Light" -> "Light"
                "Dark" -> "Dark"
                "System" -> "System"
                else -> ""
            }
        }

    private val imageLayoutSummaryProvider =
        Preference.SummaryProvider<ListPreference> { preference ->
            when (preference.value) {
                "List" -> "List"
                "Grid" -> "Grid"
                "Cards" -> "Cards"
                else -> ""
            }
        }


}
