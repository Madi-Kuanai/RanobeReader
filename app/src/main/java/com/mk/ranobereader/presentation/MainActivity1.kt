package com.mk.ranobereader.presentation


import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.widget.CompoundButton
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationBarView
import com.mk.data.themeSharedPref.PreferenceService
import com.mk.domain.Const
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
    lateinit var lastFrame: Fragment

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

        if (networkInfo != null && networkInfo.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding!!.root)
            init()
        } else {
            setContentView(R.layout.internet_error)
        }

    }

    private fun init() {
        pref = PreferenceService(getSharedPreferences(Const.sharedTheme, MODE_PRIVATE))
    }

    override fun onStart() {
        super.onStart()
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
        lastFrame = frame

    }

    private fun setThemeSettings() {
        binding!!.themeController.isChecked = pref?.isNightTheme ?: false
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