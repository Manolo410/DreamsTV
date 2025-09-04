package com.dreamsworld.dreamstv

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.dreamsworld.dreamstv.data.StreamingConfig

/**
 * DreamsTV Application Class
 * 
 * This class initializes the application and sets up global configurations
 * for the DreamsTV streaming platform.
 */
class DreamsTVApplication : Application() {
    
    companion object {
        lateinit var instance: DreamsTVApplication
            private set
    }
    
    override fun onCreate() {
        super.onCreate()
        instance = this
        
        // Initialize application-wide configurations
        initializeApp()
    }
    
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        // Enable MultiDex support for large apps
        MultiDex.install(this)
    }
    
    /**
     * Initialize application-wide settings and configurations
     */
    private fun initializeApp() {
        // Initialize streaming configuration
        StreamingConfig.initialize()
        
        // Set up any global preferences or configurations here
        setupGlobalPreferences()
    }
    
    /**
     * Set up global application preferences
     */
    private fun setupGlobalPreferences() {
        // Initialize any global preferences or settings
        // This could include user preferences, theme settings, etc.
    }
}
