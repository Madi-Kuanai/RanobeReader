package com.mk.ranobereader.presentation


import android.os.Bundle
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationBarView
import com.mk.data.themeSharedPref.PreferenceService
import com.mk.ranobereader.R
import com.mk.ranobereader.databinding.ActivityMainBinding
import com.mk.ranobereader.presentation.downloadScreen.DownloadScreen
import com.mk.ranobereader.presentation.exploreScreen.ExploreScreen
import com.mk.ranobereader.presentation.favouritesScreen.FavouritesScreen
import com.mk.ranobereader.presentation.homeScreen.HomeScreen
import com.mk.ranobereader.presentation.settingScreen.SettingScreen

class MainActivity : AppCompatActivity() {
    var binding: ActivityMainBinding? = null
    var pref: PreferenceService? = null
    var homeScreen = HomeScreen()
    var favouritesScreen = FavouritesScreen()
    var exploreScreen = ExploreScreen()
    var downloadScreen = DownloadScreen()
    var settingScreen = SettingScreen()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        init()
    }

    private fun init() {
        pref = PreferenceService(getSharedPreferences(com.mk.core.Const.sharedTheme, MODE_PRIVATE))
        setThemeSettings()
        setNavigationSettings()
    }


    private fun setNavigationSettings() {
        supportFragmentManager.beginTransaction().replace(R.id.frameLayout, homeScreen).commit()
        binding!!.bottomNavigationView.setOnItemSelectedListener(NavigationBarView.OnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {

                    setLayoutToFrame(homeScreen)
                    return@OnItemSelectedListener true
                }
                R.id.explore -> {
                    setLayoutToFrame(exploreScreen)
                    return@OnItemSelectedListener true
                }
                R.id.favourites -> {
                    setLayoutToFrame(favouritesScreen)
                    return@OnItemSelectedListener true
                }
                R.id.downloaded -> {
                    setLayoutToFrame(downloadScreen)
                    return@OnItemSelectedListener true
                }
                R.id.settings -> {
                    setLayoutToFrame(settingScreen)
                    return@OnItemSelectedListener true
                }
            }
            false
        })
    }

    private fun setLayoutToFrame(frame: Fragment) {
        if (supportFragmentManager == frame) {
            supportFragmentManager.popBackStack()
        }
        supportFragmentManager.beginTransaction().replace(R.id.frameLayout, frame).commit()
    }

    private fun setThemeSettings() {
        binding!!.themeController.isChecked = pref!!.isNightTheme
        AppCompatDelegate.setDefaultNightMode(if (pref!!.isNightTheme) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
        binding!!.themeController.setOnCheckedChangeListener { _: CompoundButton?, b: Boolean ->
            if (b) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                pref!!.setNightTheme()
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                pref!!.setLightTheme()
            }
        }
    }
}