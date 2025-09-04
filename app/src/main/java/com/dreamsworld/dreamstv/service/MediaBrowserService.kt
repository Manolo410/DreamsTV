package com.dreamsworld.dreamstv.service

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.service.media.MediaBrowserService
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.media.MediaBrowserServiceCompat
import com.dreamsworld.dreamstv.R

/**
 * MediaBrowserService for DreamsTV
 * 
 * This service provides media browsing functionality for Android Auto,
 * Android TV, and other media browsing clients.
 */
class MediaBrowserService : MediaBrowserServiceCompat() {
    
    private var mediaSession: MediaSessionCompat? = null
    
    companion object {
        const val MEDIA_ID_ROOT = "__ROOT__"
        const val MEDIA_ID_MOVIES = "__MOVIES__"
        const val MEDIA_ID_SERIES = "__SERIES__"
        const val MEDIA_ID_LIVE = "__LIVE__"
    }
    
    override fun onCreate() {
        super.onCreate()
        
        // Initialize media session
        mediaSession = MediaSessionCompat(this, "DreamsTVMediaSession").apply {
            setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or
                MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS
            )
            
            // Set initial playback state
            setPlaybackState(
                PlaybackStateCompat.Builder()
                    .setActions(
                        PlaybackStateCompat.ACTION_PLAY or
                        PlaybackStateCompat.ACTION_PAUSE or
                        PlaybackStateCompat.ACTION_SKIP_TO_NEXT or
                        PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS or
                        PlaybackStateCompat.ACTION_SEEK_TO
                    )
                    .setState(PlaybackStateCompat.STATE_NONE, 0, 1.0f)
                    .build()
            )
            
            setSessionToken(sessionToken)
        }
    }
    
    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot? {
        // Allow all clients to browse
        return BrowserRoot(MEDIA_ID_ROOT, null)
    }
    
    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>
    ) {
        val mediaItems = mutableListOf<MediaBrowserCompat.MediaItem>()
        
        when (parentId) {
            MEDIA_ID_ROOT -> {
                // Add main categories
                mediaItems.add(
                    MediaBrowserCompat.MediaItem(
                        MediaDescriptionCompat.Builder()
                            .setMediaId(MEDIA_ID_MOVIES)
                            .setTitle("Movies")
                            .setSubtitle("Browse movies")
                            .build(),
                        MediaBrowserCompat.MediaItem.FLAG_BROWSABLE
                    )
                )
                
                mediaItems.add(
                    MediaBrowserCompat.MediaItem(
                        MediaDescriptionCompat.Builder()
                            .setMediaId(MEDIA_ID_SERIES)
                            .setTitle("Series")
                            .setSubtitle("Browse TV series")
                            .build(),
                        MediaBrowserCompat.MediaItem.FLAG_BROWSABLE
                    )
                )
                
                mediaItems.add(
                    MediaBrowserCompat.MediaItem(
                        MediaDescriptionCompat.Builder()
                            .setMediaId(MEDIA_ID_LIVE)
                            .setTitle("Live TV")
                            .setSubtitle("Watch live content")
                            .build(),
                        MediaBrowserCompat.MediaItem.FLAG_BROWSABLE
                    )
                )
            }
            
            MEDIA_ID_MOVIES -> {
                // Load movie content
                loadMovieContent(mediaItems)
            }
            
            MEDIA_ID_SERIES -> {
                // Load series content
                loadSeriesContent(mediaItems)
            }
            
            MEDIA_ID_LIVE -> {
                // Load live content
                loadLiveContent(mediaItems)
            }
        }
        
        result.sendResult(mediaItems)
    }
    
    private fun loadMovieContent(mediaItems: MutableList<MediaBrowserCompat.MediaItem>) {
        // Add sample movie items
        // In a real implementation, this would load from your content database
        mediaItems.add(
            MediaBrowserCompat.MediaItem(
                MediaDescriptionCompat.Builder()
                    .setMediaId("movie_1")
                    .setTitle("Sample Movie")
                    .setSubtitle("Action, Adventure")
                    .setMediaUri(android.net.Uri.parse("https://example.com/movie1.mp4"))
                    .build(),
                MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
            )
        )
    }
    
    private fun loadSeriesContent(mediaItems: MutableList<MediaBrowserCompat.MediaItem>) {
        // Add sample series items
        mediaItems.add(
            MediaBrowserCompat.MediaItem(
                MediaDescriptionCompat.Builder()
                    .setMediaId("series_1")
                    .setTitle("Sample Series")
                    .setSubtitle("Drama, Thriller")
                    .setMediaUri(android.net.Uri.parse("https://example.com/series1.mp4"))
                    .build(),
                MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
            )
        )
    }
    
    private fun loadLiveContent(mediaItems: MutableList<MediaBrowserCompat.MediaItem>) {
        // Add sample live content
        mediaItems.add(
            MediaBrowserCompat.MediaItem(
                MediaDescriptionCompat.Builder()
                    .setMediaId("live_1")
                    .setTitle("Live Channel 1")
                    .setSubtitle("News")
                    .setMediaUri(android.net.Uri.parse("https://example.com/live1.m3u8"))
                    .build(),
                MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
            )
        )
    }
    
    override fun onDestroy() {
        super.onDestroy()
        mediaSession?.release()
    }
}