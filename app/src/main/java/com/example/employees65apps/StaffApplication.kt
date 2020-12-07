package com.example.employees65apps

import androidx.multidex.MultiDexApplication
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltAndroidApp
class StaffApplication : MultiDexApplication() {
    private val applicationScope = CoroutineScope(Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        delayedInit()
    }

    /**
     * Initialize support components on background thread using coroutine
     */
    private fun delayedInit() {
        applicationScope.launch {
            // Setup Timber
            if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())

            AndroidThreeTen.init(this@StaffApplication)
        }
    }

}