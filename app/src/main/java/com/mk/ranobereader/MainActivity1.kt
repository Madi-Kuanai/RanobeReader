package com.mk.ranobereader


import android.content.res.Configuration
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationBarView
import com.mk.ranobereader.HomeScreen.HomeScreen
import com.mk.ranobereader.Settings.PreferenceService
import com.mk.ranobereader.databinding.ActivityMainBinding
import com.mk.ranobereader.downloadScreen.DownloadScreen
import com.mk.ranobereader.exploreScreen.ExploreScreen
import com.mk.ranobereader.favouritesScreen.FavouritesScreen
import com.mk.ranobereader.settingScreen.SettingScreen

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
        pref = PreferenceService(getSharedPreferences(Const.sharedTheme, MODE_PRIVATE))
        setThemeSettings()
        setNavigationSettings()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            val params: ViewGroup.LayoutParams = binding!!.bottomNavigationView.layoutParams;
            val displayMetrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(displayMetrics)
            val height = displayMetrics.heightPixels
            params.height = (height * 0.3).toInt();
            binding!!.bottomNavigationView.layoutParams = params;
        }
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
        supportFragmentManager.beginTransaction().replace(R.id.frameLayout, frame).commit()
    }

    private fun setThemeSettings() {
        println(pref!!.isNightTheme)
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