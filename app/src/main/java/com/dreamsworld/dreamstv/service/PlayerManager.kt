package com.dreamsworld.dreamstv.service

import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.datasource.DefaultDataSource
import android.content.Context

/**
 * PlayerManager for DreamsTV
 * 
 * This class manages the ExoPlayer instance and provides
 * a centralized way to access the player throughout the app.
 */
object PlayerManager {
    
    private var player: ExoPlayer? = null
    
    /**
     * Get the ExoPlayer instance
     */
    fun getPlayer(): ExoPlayer {
        if (player == null) {
            throw IllegalStateException("Player not initialized. Call initialize() first.")
        }
        return player!!
    }
    
    /**
     * Initialize the player with the given context
     */
    @UnstableApi
    fun initialize(context: Context) {
        if (player == null) {
            val dataSourceFactory = DefaultDataSource.Factory(context)
            val mediaSourceFactory = DefaultMediaSourceFactory(dataSourceFactory)
            
            player = ExoPlayer.Builder(context)
                .setMediaSourceFactory(mediaSourceFactory)
                .build()
        }
    }
    
    /**
     * Release the player
     */
    fun release() {
        player?.release()
        player = null
    }
    
    /**
     * Check if player is initialized
     */
    fun isInitialized(): Boolean {
        return player != null
    }
}
