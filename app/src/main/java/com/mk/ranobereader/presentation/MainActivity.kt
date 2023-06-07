package com.mk.ranobereader.presentation


import NetworkChangeReceiver
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.CompoundButton
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationBarView
import com.mk.data.repositories.SharedPref.ThemePreferenceService
import com.mk.domain.Const
import com.mk.domain.Const.TAG
import com.mk.ranobereader.R
import com.mk.ranobereader.databinding.ActivityMainBinding
import com.mk.ranobereader.presentation.downloadScreen.DownloadScreen
import com.mk.ranobereader.presentation.errorScreen.ErrorScreen
import com.mk.ranobereader.presentation.exploreScreen.ExploreScreen
import com.mk.ranobereader.presentation.favouritesScreen.FavouritesScreen
import com.mk.ranobereader.presentation.homeScreen.HomeScreen
import com.mk.ranobereader.presentation.settingScreen.SettingScreen


class MainActivity : AppCompatActivity(), NetworkChangeReceiver.NetworkStateListener {
    var binding: ActivityMainBinding? = null
    var pref: ThemePreferenceService? = null
    var homeScreen = HomeScreen()
    var favouritesScreen = FavouritesScreen()
    var exploreScreen = ExploreScreen()
    var downloadScreen = DownloadScreen()
    var settingScreen = SettingScreen()
    lateinit var lastFrame: Fragment
    private lateinit var networkChangeReceiver: NetworkChangeReceiver
    public var isConnected: Boolean = false

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.setDefaultUncaughtExceptionHandler { _, paramThrowable ->
            paramThrowable.localizedMessage?.let {
                Log.d(
                    TAG,
                    "Error: " + Thread.currentThread().stackTrace[2] + "/n" + it
                )
            }
        }
        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        networkChangeReceiver = NetworkChangeReceiver(this)

        if (networkInfo != null && networkInfo.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
            isConnected = true
            Log.d(TAG, "onCreate: InternetActivated")
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding!!.root)
            init()
            setThemeSettings()
            setNavigationSettings()
        } else {
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding!!.root)
            init()
            setThemeSettings()
            setNavigationSettings()
            isConnected = false
            onInternetLoss()
        }

    }

    private fun onInternetLoss() {
        isConnected = false
        Log.d(TAG, "onCreate: Connection error")
        setLayoutToFrame(ErrorScreen())
    }

    private fun onInternetActivated() {
        isConnected = true
        setNavigationSettings()
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(
            networkChangeReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )

    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(networkChangeReceiver)
    }

    private fun init() {
        pref = ThemePreferenceService(getSharedPreferences(Const.SHARED_THEME, MODE_PRIVATE))
    }

    override fun onStart() {
        super.onStart()
    }

    private fun setNavigationSettings() {
        if (!isConnected) {
            setLayoutToFrame(ErrorScreen())

        } else {
            supportFragmentManager.beginTransaction().replace(R.id.frameLayout, homeScreen).commit()
        }
        binding?.bottomNavigationView?.setOnItemSelectedListener(NavigationBarView.OnItemSelectedListener { item ->
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

    override fun onNetworkStateChanged(isConnected: Boolean) {
        if (!isConnected) {
            Log.d(TAG, "Internet losing")
            onInternetLoss()
        } else {
            onInternetActivated()
            Log.d(TAG, "Internet connected")
        }
    }
}