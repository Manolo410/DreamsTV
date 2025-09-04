package com.dreamsworld.dreamstv.data

import com.dreamsworld.dreamstv.BuildConfig

/**
 * Streaming Configuration for DreamsTV
 * This class manages all streaming sources, content providers, and API endpoints
 */
object StreamingConfig {
    
    // Base URLs for different environments
    const val API_BASE_URL = BuildConfig.API_BASE_URL
    const val CDN_BASE_URL = BuildConfig.CDN_BASE_URL
    
    /**
     * Initialize the streaming configuration
     */
    fun initialize() {
        // Initialize any streaming-related configurations here
        // This could include setting up default values, validating configurations, etc.
    }
    
    // Streaming Quality Options
    enum class StreamQuality(val value: String, val bitrate: Int) {
        AUTO("auto", 0),
        QUALITY_4K("4k", 15000),
        QUALITY_1080P("1080p", 5000),
        QUALITY_720P("720p", 2500),
        QUALITY_480P("480p", 1000),
        QUALITY_360P("360p", 500)
    }
    
    // Content Types
    enum class ContentType(val value: String) {
        MOVIE("movie"),
        TV_SHOW("tv_show"),
        LIVE_TV("live_tv"),
        DOCUMENTARY("documentary"),
        SPORTS("sports"),
        NEWS("news"),
        KIDS("kids")
    }
    
    // Streaming Sources Configuration
    data class StreamingSource(
        val name: String,
        val baseUrl: String,
        val apiKey: String? = null,
        val supportedFormats: List<String>,
        val qualityLevels: List<StreamQuality>,
        val contentTypes: List<ContentType>
    )
    
    // API Keys Configuration
    const val TMDB_API_KEY = "29a5d28f154de8503d2a4085db7e2196"
    const val YOUTUBE_API_KEY = "AIzaSyDWLY3Bx87KnBu2Y8J_zaHbDp49YTnUn7M"
    const val OMDB_API_KEY = "27462a42"
    
    // Popular Streaming Sources
    val STREAMING_SOURCES = listOf(
        // Free Streaming Sources
        StreamingSource(
            name = "YouTube",
            baseUrl = "https://www.googleapis.com/youtube/v3/",
            apiKey = YOUTUBE_API_KEY,
            supportedFormats = listOf("mp4", "webm", "hls"),
            qualityLevels = listOf(StreamQuality.QUALITY_4K, StreamQuality.QUALITY_1080P, StreamQuality.QUALITY_720P),
            contentTypes = listOf(ContentType.MOVIE, ContentType.TV_SHOW, ContentType.DOCUMENTARY)
        ),
        
        // Public Domain Content
        StreamingSource(
            name = "Internet Archive",
            baseUrl = "https://archive.org/",
            supportedFormats = listOf("mp4", "avi", "mkv"),
            qualityLevels = listOf(StreamQuality.QUALITY_1080P, StreamQuality.QUALITY_720P, StreamQuality.QUALITY_480P),
            contentTypes = listOf(ContentType.MOVIE, ContentType.TV_SHOW, ContentType.DOCUMENTARY)
        ),
        
        // Live TV Sources
        StreamingSource(
            name = "Live TV Streams",
            baseUrl = "https://iptv-org.github.io/iptv/",
            supportedFormats = listOf("m3u8", "ts"),
            qualityLevels = listOf(StreamQuality.QUALITY_1080P, StreamQuality.QUALITY_720P, StreamQuality.QUALITY_480P),
            contentTypes = listOf(ContentType.LIVE_TV, ContentType.NEWS, ContentType.SPORTS)
        ),
        
        // Educational Content
        StreamingSource(
            name = "Khan Academy",
            baseUrl = "https://www.khanacademy.org/",
            supportedFormats = listOf("mp4", "hls"),
            qualityLevels = listOf(StreamQuality.QUALITY_1080P, StreamQuality.QUALITY_720P),
            contentTypes = listOf(ContentType.DOCUMENTARY)
        ),
        
        // Open Source Movies
        StreamingSource(
            name = "Open Movies",
            baseUrl = "https://www.opensubtitles.org/",
            supportedFormats = listOf("mp4", "avi", "mkv"),
            qualityLevels = listOf(StreamQuality.QUALITY_1080P, StreamQuality.QUALITY_720P, StreamQuality.QUALITY_480P),
            contentTypes = listOf(ContentType.MOVIE, ContentType.TV_SHOW)
        )
    )
    
    // Content Import Sources
    data class ContentImportSource(
        val name: String,
        val apiUrl: String,
        val apiKey: String? = null,
        val contentTypes: List<ContentType>,
        val updateFrequency: String // "daily", "weekly", "monthly"
    )
    
    val CONTENT_IMPORT_SOURCES = listOf(
        // The Movie Database (TMDB) - Free tier available
        ContentImportSource(
            name = "The Movie Database",
            apiUrl = "https://api.themoviedb.org/3/",
            apiKey = TMDB_API_KEY,
            contentTypes = listOf(ContentType.MOVIE, ContentType.TV_SHOW),
            updateFrequency = "daily"
        ),
        
        // Open Movie Database (OMDB) - Free tier available
        ContentImportSource(
            name = "Open Movie Database",
            apiUrl = "http://www.omdbapi.com/",
            apiKey = OMDB_API_KEY,
            contentTypes = listOf(ContentType.MOVIE, ContentType.TV_SHOW),
            updateFrequency = "weekly"
        ),
        
        // TVMaze - Free API for TV shows
        ContentImportSource(
            name = "TVMaze",
            apiUrl = "https://api.tvmaze.com/",
            contentTypes = listOf(ContentType.TV_SHOW),
            updateFrequency = "daily"
        ),
        
        // JustWatch - Free API for streaming availability
        ContentImportSource(
            name = "JustWatch",
            apiUrl = "https://apis.justwatch.com/",
            contentTypes = listOf(ContentType.MOVIE, ContentType.TV_SHOW),
            updateFrequency = "weekly"
        ),
        
        // Free TV API
        ContentImportSource(
            name = "Free TV API",
            apiUrl = "https://www.freetvapi.com/",
            contentTypes = listOf(ContentType.LIVE_TV, ContentType.NEWS, ContentType.SPORTS),
            updateFrequency = "daily"
        )
    )
    
    // Live TV Channel Sources
    data class LiveTVChannel(
        val name: String,
        val streamUrl: String,
        val logoUrl: String,
        val category: String,
        val language: String,
        val country: String
    )
    
    val LIVE_TV_CHANNELS = listOf(
        // News Channels
        LiveTVChannel(
            name = "BBC News",
            streamUrl = "https://stream.live.vc.bbcmedia.co.uk/bbc_world_service",
            logoUrl = "https://logo.clearbit.com/bbc.com",
            category = "News",
            language = "English",
            country = "UK"
        ),
        
        LiveTVChannel(
            name = "CNN",
            streamUrl = "https://cnn-cnninternational-1-eu.rakuten.wurl.tv/playlist.m3u8",
            logoUrl = "https://logo.clearbit.com/cnn.com",
            category = "News",
            language = "English",
            country = "US"
        ),
        
        // Educational Channels
        LiveTVChannel(
            name = "NASA TV",
            streamUrl = "https://ntv1.akamaized.net/hls/live/2014075/NASA-NTV1-HLS/master.m3u8",
            logoUrl = "https://logo.clearbit.com/nasa.gov",
            category = "Education",
            language = "English",
            country = "US"
        ),
        
        // Music Channels
        LiveTVChannel(
            name = "MTV",
            streamUrl = "https://mtv-ott.akamaized.net/hls/live/2014075/MTV/master.m3u8",
            logoUrl = "https://logo.clearbit.com/mtv.com",
            category = "Music",
            language = "English",
            country = "US"
        ),
        
        // Kids Channels
        LiveTVChannel(
            name = "Cartoon Network",
            streamUrl = "https://cartoonnetwork-ott.akamaized.net/hls/live/2014075/CartoonNetwork/master.m3u8",
            logoUrl = "https://logo.clearbit.com/cartoonnetwork.com",
            category = "Kids",
            language = "English",
            country = "US"
        )
    )
    
    // Content Categories for Import
    val CONTENT_CATEGORIES = listOf(
        "Action", "Adventure", "Animation", "Comedy", "Crime", "Documentary",
        "Drama", "Family", "Fantasy", "History", "Horror", "Music", "Mystery",
        "Romance", "Science Fiction", "Thriller", "War", "Western", "Kids",
        "Sports", "News", "Educational", "Reality", "Talk Show", "Game Show"
    )
    
    // Streaming Protocols
    enum class StreamingProtocol(val value: String) {
        HLS("hls"),
        DASH("dash"),
        RTSP("rtsp"),
        RTMP("rtmp"),
        HTTP("http"),
        HTTPS("https")
    }
    
    // Default Streaming Settings
    object DefaultSettings {
        const val DEFAULT_QUALITY = "720p"
        const val BUFFER_SIZE_MS = 5000L
        const val MAX_BUFFER_SIZE_MS = 30000L
        const val MIN_BUFFER_SIZE_MS = 2000L
        const val PREFERRED_AUDIO_LANGUAGE = "en"
        const val PREFERRED_SUBTITLE_LANGUAGE = "en"
        const val AUTO_PLAY_NEXT_EPISODE = true
        const val AUTO_RESUME_PLAYBACK = true
    }
}
