package com.dreamsworld.dreamstv.utils

import android.util.Log
import com.dreamsworld.dreamstv.data.StreamingConfig
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

/**
 * API Test Utility for DreamsTV
 * Tests all configured API keys and returns status
 */
object ApiTestUtil {
    
    private const val TAG = "ApiTestUtil"
    
    // TMDB API Test
    interface TMDBTestService {
        @GET("movie/popular")
        suspend fun testConnection(@Query("api_key") apiKey: String): Any
    }
    
    // YouTube API Test
    interface YouTubeTestService {
        @GET("search")
        suspend fun testConnection(
            @Query("key") apiKey: String,
            @Query("part") part: String = "snippet",
            @Query("q") query: String = "test",
            @Query("maxResults") maxResults: Int = 1
        ): Any
    }
    
    // OMDB API Test
    interface OMDBTestService {
        @GET(".")
        suspend fun testConnection(
            @Query("apikey") apiKey: String,
            @Query("t") title: String = "test"
        ): Any
    }
    
    data class ApiTestResult(
        val apiName: String,
        val isWorking: Boolean,
        val message: String,
        val responseTime: Long
    )
    
    suspend fun testAllApis(): List<ApiTestResult> = withContext(Dispatchers.IO) {
        val results = mutableListOf<ApiTestResult>()
        
        // Test TMDB API
        results.add(testTMDB())
        
        // Test YouTube API
        results.add(testYouTube())
        
        // Test OMDB API
        results.add(testOMDB())
        
        // Test TVMaze (no key needed)
        results.add(testTVMaze())
        
        results
    }
    
    private suspend fun testTMDB(): ApiTestResult {
        val startTime = System.currentTimeMillis()
        return try {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            
            val service = retrofit.create(TMDBTestService::class.java)
            service.testConnection(StreamingConfig.TMDB_API_KEY)
            
            val responseTime = System.currentTimeMillis() - startTime
            ApiTestResult(
                apiName = "TMDB",
                isWorking = true,
                message = "✅ Connected successfully",
                responseTime = responseTime
            )
        } catch (e: Exception) {
            val responseTime = System.currentTimeMillis() - startTime
            ApiTestResult(
                apiName = "TMDB",
                isWorking = false,
                message = "❌ Error: ${e.message}",
                responseTime = responseTime
            )
        }
    }
    
    private suspend fun testYouTube(): ApiTestResult {
        val startTime = System.currentTimeMillis()
        return try {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://www.googleapis.com/youtube/v3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            
            val service = retrofit.create(YouTubeTestService::class.java)
            service.testConnection(StreamingConfig.YOUTUBE_API_KEY)
            
            val responseTime = System.currentTimeMillis() - startTime
            ApiTestResult(
                apiName = "YouTube",
                isWorking = true,
                message = "✅ Connected successfully",
                responseTime = responseTime
            )
        } catch (e: Exception) {
            val responseTime = System.currentTimeMillis() - startTime
            ApiTestResult(
                apiName = "YouTube",
                isWorking = false,
                message = "❌ Error: ${e.message}",
                responseTime = responseTime
            )
        }
    }
    
    private suspend fun testOMDB(): ApiTestResult {
        val startTime = System.currentTimeMillis()
        return try {
            val retrofit = Retrofit.Builder()
                .baseUrl("http://www.omdbapi.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            
            val service = retrofit.create(OMDBTestService::class.java)
            service.testConnection(StreamingConfig.OMDB_API_KEY)
            
            val responseTime = System.currentTimeMillis() - startTime
            ApiTestResult(
                apiName = "OMDB",
                isWorking = true,
                message = "✅ Connected successfully",
                responseTime = responseTime
            )
        } catch (e: Exception) {
            val responseTime = System.currentTimeMillis() - startTime
            ApiTestResult(
                apiName = "OMDB",
                isWorking = false,
                message = "❌ Error: ${e.message}",
                responseTime = responseTime
            )
        }
    }
    
    private suspend fun testTVMaze(): ApiTestResult {
        val startTime = System.currentTimeMillis()
        return try {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.tvmaze.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            
            // Simple test endpoint
            val response = retrofit.create(TVMazeTestService::class.java)
                .testConnection()
            
            val responseTime = System.currentTimeMillis() - startTime
            ApiTestResult(
                apiName = "TVMaze",
                isWorking = true,
                message = "✅ Connected successfully (Free API)",
                responseTime = responseTime
            )
        } catch (e: Exception) {
            val responseTime = System.currentTimeMillis() - startTime
            ApiTestResult(
                apiName = "TVMaze",
                isWorking = false,
                message = "❌ Error: ${e.message}",
                responseTime = responseTime
            )
        }
    }
    
    interface TVMazeTestService {
        @GET("shows/1")
        suspend fun testConnection(): Any
    }
    
    fun logApiResults(results: List<ApiTestResult>) {
        Log.d(TAG, "=== API Test Results ===")
        results.forEach { result ->
            Log.d(TAG, "${result.apiName}: ${result.message} (${result.responseTime}ms)")
        }
        Log.d(TAG, "========================")
    }
    
    fun getApiStatusSummary(results: List<ApiTestResult>): String {
        val workingApis = results.count { it.isWorking }
        val totalApis = results.size
        
        return """
            API Status: $workingApis/$totalApis APIs Working
            
            ${results.joinToString("\n") { "• ${it.apiName}: ${it.message}" }}
        """.trimIndent()
    }
}
