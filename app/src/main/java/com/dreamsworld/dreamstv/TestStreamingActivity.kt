package com.dreamsworld.dreamstv

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.dreamsworld.dreamstv.data.StreamingConfig
import com.dreamsworld.dreamstv.data.ContentImportService
import com.dreamsworld.dreamstv.utils.ApiTestUtil
import kotlinx.coroutines.runBlocking

/**
 * Test Activity for DreamsTV Streaming
 * This activity demonstrates the streaming capabilities and content import
 */
class TestStreamingActivity : AppCompatActivity() {
    
    private lateinit var playerView: PlayerView
    private lateinit var exoPlayer: ExoPlayer
    private lateinit var contentListView: ListView
    private lateinit var statusText: TextView
    private lateinit var importButton: Button
    private lateinit var testStreamButton: Button
    private lateinit var tvmazeTestButton: Button
    
    // Sample streaming URLs for testing
    private val testStreams = listOf(
        "Big Buck Bunny (Sample Video)" to "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
        "Sintel (Sample Video)" to "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4",
        "Tears of Steel (Sample Video)" to "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4",
        "Elephant Dream (Sample Video)" to "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
        "NASA TV (Live Stream)" to "https://ntv1.akamaized.net/hls/live/2014075/NASA-NTV1-HLS/master.m3u8",
        "BBC News (Live Stream)" to "https://stream.live.vc.bbcmedia.co.uk/bbc_world_service"
    )
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_streaming)
        
        initializeViews()
        setupPlayer()
        setupContentList()
        setupButtons()
    }
    
    private fun initializeViews() {
        playerView = findViewById(R.id.player_view)
        contentListView = findViewById(R.id.content_list)
        statusText = findViewById(R.id.status_text)
        importButton = findViewById(R.id.import_button)
        testStreamButton = findViewById(R.id.test_stream_button)
        tvmazeTestButton = findViewById(R.id.tvmaze_test_button)
    }
    
    private fun setupPlayer() {
        exoPlayer = ExoPlayer.Builder(this).build()
        playerView.player = exoPlayer
        
        // Set up player event listener
        exoPlayer.addListener(object : androidx.media3.common.Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                when (playbackState) {
                    androidx.media3.common.Player.STATE_BUFFERING -> {
                        statusText.text = "Buffering..."
                    }
                    androidx.media3.common.Player.STATE_READY -> {
                        statusText.text = "Ready to play"
                    }
                    androidx.media3.common.Player.STATE_ENDED -> {
                        statusText.text = "Playback ended"
                    }
                    androidx.media3.common.Player.STATE_IDLE -> {
                        statusText.text = "Player idle"
                    }
                }
            }
            
            override fun onPlayerError(error: androidx.media3.common.PlaybackException) {
                statusText.text = "Error: ${error.message}"
            }
        })
    }
    
    private fun setupContentList() {
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            testStreams.map { it.first }
        )
        contentListView.adapter = adapter
        
        contentListView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val selectedStream = testStreams[position]
            playStream(selectedStream.second, selectedStream.first)
        }
    }
    
    private fun setupButtons() {
        importButton.setOnClickListener {
            importContent()
        }
        
        testStreamButton.setOnClickListener {
            // Play the first test stream
            val firstStream = testStreams.first()
            playStream(firstStream.second, firstStream.first)
        }
        
        tvmazeTestButton.setOnClickListener {
            // Launch TVMaze test activity
            val intent = android.content.Intent(this, TVMazeTestActivity::class.java)
            startActivity(intent)
        }
    }
    
    private fun playStream(url: String, title: String) {
        try {
            statusText.text = "Loading: $title"
            
            val mediaItem = MediaItem.fromUri(Uri.parse(url))
            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.prepare()
            exoPlayer.play()
            
        } catch (e: Exception) {
            statusText.text = "Error playing stream: ${e.message}"
        }
    }
    
    private fun importContent() {
        statusText.text = "Testing your API keys..."
        
        // Test all APIs with your keys
        Thread {
            try {
                // Test API connectivity
                val apiResults = runBlocking { ApiTestUtil.testAllApis() }
                ApiTestUtil.logApiResults(apiResults)
                
                // Test content import
                val contentManager = ContentImportService.ContentImportManager()
                val movieGenres = runBlocking { contentManager.getMovieGenres() }
                val popularMovies = runBlocking { contentManager.searchMovies(query = "avengers") }
                
                // Test TVMaze (No API key required!)
                val tvmazeShows = runBlocking { contentManager.searchTVMazeShows("breaking bad") }
                val tvmazeSchedule = runBlocking { contentManager.getTVMazeSchedule("US") }
                val tvmazePeople = runBlocking { contentManager.searchTVMazePeople("bryan cranston") }
                
                runOnUiThread {
                    statusText.text = "API testing completed!"
                    
                    // Show API test results
                    val apiStatus = ApiTestUtil.getApiStatusSummary(apiResults)
                    val importedContent = """
                        🎬 DreamsTV API Test Results
                        
                        $apiStatus
                        
                        Content Import Test:
                        - Movie Genres: ${movieGenres.size} categories
                        - Sample Movies: ${popularMovies.size} titles
                        - Live TV Channels: ${StreamingConfig.LIVE_TV_CHANNELS.size} channels
                        
                        TVMaze Free API Test:
                        - Breaking Bad Shows: ${tvmazeShows.size} results
                        - Today's TV Schedule: ${tvmazeSchedule.size} shows
                        - Bryan Cranston Search: ${tvmazePeople.size} people
                        
                        Your API Keys:
                        - TMDB: ${StreamingConfig.TMDB_API_KEY.take(8)}...
                        - YouTube: ${StreamingConfig.YOUTUBE_API_KEY.take(8)}...
                        - OMDB: ${StreamingConfig.OMDB_API_KEY}
                        
                        Ready to stream! 🚀
                    """.trimIndent()
                    
                    Toast.makeText(this, importedContent, Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                runOnUiThread {
                    statusText.text = "API test failed: ${e.message}"
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }
    
    override fun onDestroy() {
        super.onDestroy()
        exoPlayer.release()
    }
    
    override fun onPause() {
        super.onPause()
        exoPlayer.pause()
    }
    
    override fun onResume() {
        super.onResume()
        if (exoPlayer.playbackState != androidx.media3.common.Player.STATE_ENDED) {
            exoPlayer.play()
        }
    }
}
