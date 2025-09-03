package com.dreamsworld.dreamstv.data

import android.content.Context
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

/**
 * Content Import Service for DreamsTV
 * Handles importing movies, TV shows, and live TV content from various sources
 */
class ContentImportService(private val context: Context) {
    
    private val coroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    
    // Data classes for API responses
    data class Movie(
        val id: Int,
        val title: String,
        val overview: String,
        val poster_path: String?,
        val backdrop_path: String?,
        val release_date: String,
        val vote_average: Double,
        val genre_ids: List<Int>,
        val adult: Boolean,
        val original_language: String,
        val original_title: String,
        val popularity: Double,
        val video: Boolean,
        val vote_count: Int
    )
    
    data class TVShow(
        val id: Int,
        val name: String,
        val overview: String,
        val poster_path: String?,
        val backdrop_path: String?,
        val first_air_date: String,
        val vote_average: Double,
        val genre_ids: List<Int>,
        val origin_country: List<String>,
        val original_language: String,
        val original_name: String,
        val popularity: Double,
        val vote_count: Int
    )
    
    data class Genre(
        val id: Int,
        val name: String
    )
    
    data class MovieResponse(
        val page: Int,
        val results: List<Movie>,
        val total_pages: Int,
        val total_results: Int
    )
    
    data class TVShowResponse(
        val page: Int,
        val results: List<TVShow>,
        val total_pages: Int,
        val total_results: Int
    )
    
    data class GenreResponse(
        val genres: List<Genre>
    )
    
    // API Interface for The Movie Database
    interface TMDBService {
        @GET("movie/popular")
        suspend fun getPopularMovies(
            @Query("api_key") apiKey: String,
            @Query("page") page: Int = 1,
            @Query("language") language: String = "en-US"
        ): MovieResponse
        
        @GET("movie/top_rated")
        suspend fun getTopRatedMovies(
            @Query("api_key") apiKey: String,
            @Query("page") page: Int = 1,
            @Query("language") language: String = "en-US"
        ): MovieResponse
        
        @GET("movie/now_playing")
        suspend fun getNowPlayingMovies(
            @Query("api_key") apiKey: String,
            @Query("page") page: Int = 1,
            @Query("language") language: String = "en-US"
        ): MovieResponse
        
        @GET("tv/popular")
        suspend fun getPopularTVShows(
            @Query("api_key") apiKey: String,
            @Query("page") page: Int = 1,
            @Query("language") language: String = "en-US"
        ): TVShowResponse
        
        @GET("tv/top_rated")
        suspend fun getTopRatedTVShows(
            @Query("api_key") apiKey: String,
            @Query("page") page: Int = 1,
            @Query("language") language: String = "en-US"
        ): TVShowResponse
        
        @GET("tv/on_the_air")
        suspend fun getOnTheAirTVShows(
            @Query("api_key") apiKey: String,
            @Query("page") page: Int = 1,
            @Query("language") language: String = "en-US"
        ): TVShowResponse
        
        @GET("genre/movie/list")
        suspend fun getMovieGenres(
            @Query("api_key") apiKey: String,
            @Query("language") language: String = "en-US"
        ): GenreResponse
        
        @GET("genre/tv/list")
        suspend fun getTVGenres(
            @Query("api_key") apiKey: String,
            @Query("language") language: String = "en-US"
        ): GenreResponse
        
        @GET("search/movie")
        suspend fun searchMovies(
            @Query("api_key") apiKey: String,
            @Query("query") query: String,
            @Query("page") page: Int = 1,
            @Query("language") language: String = "en-US"
        ): MovieResponse
        
        @GET("search/tv")
        suspend fun searchTVShows(
            @Query("api_key") apiKey: String,
            @Query("query") query: String,
            @Query("page") page: Int = 1,
            @Query("language") language: String = "en-US"
        ): TVShowResponse
    }
    
    // API Interface for TVMaze (Free TV Show API)
    interface TVMazeService {
        @GET("shows")
        suspend fun getAllShows(
            @Query("page") page: Int = 0
        ): List<TVShow>
        
        @GET("shows/{id}")
        suspend fun getShowById(@Path("id") id: Int): TVShow
        
        @GET("search/shows")
        suspend fun searchShows(@Query("q") query: String): List<SearchResult>
        
        @GET("shows/{id}/episodes")
        suspend fun getShowEpisodes(@Path("id") id: Int): List<Episode>
        
        @GET("shows/{id}/seasons")
        suspend fun getShowSeasons(@Path("id") id: Int): List<Season>
    }
    
    data class SearchResult(
        val score: Double,
        val show: TVShow
    )
    
    data class Episode(
        val id: Int,
        val name: String,
        val season: Int,
        val number: Int,
        val summary: String?,
        val airdate: String?,
        val airtime: String?,
        val runtime: Int?,
        val image: EpisodeImage?,
        val url: String
    )
    
    data class EpisodeImage(
        val medium: String?,
        val original: String?
    )
    
    data class Season(
        val id: Int,
        val number: Int,
        val name: String,
        val episodeOrder: Int?,
        val premiereDate: String?,
        val endDate: String?,
        val summary: String?,
        val image: EpisodeImage?
    )
    
    // Live TV Channel Data
    data class LiveTVChannel(
        val name: String,
        val url: String,
        val logo: String,
        val category: String,
        val language: String,
        val country: String,
        val tvgId: String? = null,
        val tvgName: String? = null,
        val tvgLogo: String? = null,
        val groupTitle: String? = null
    )
    
    // Content Import Manager
    class ContentImportManager {
        private val tmdbService: TMDBService
        private val tvmazeService: TVMazeService
        
        init {
            val tmdbRetrofit = Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            
            val tvmazeRetrofit = Retrofit.Builder()
                .baseUrl("https://api.tvmaze.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            
            tmdbService = tmdbRetrofit.create(TMDBService::class.java)
            tvmazeService = tvmazeRetrofit.create(TVMazeService::class.java)
        }
        
        // Import Movies from TMDB
        suspend fun importMovies(apiKey: String, maxPages: Int = 5): Flow<List<Movie>> = flow {
            for (page in 1..maxPages) {
                try {
                    val popularMovies = tmdbService.getPopularMovies(apiKey, page)
                    val topRatedMovies = tmdbService.getTopRatedMovies(apiKey, page)
                    val nowPlayingMovies = tmdbService.getNowPlayingMovies(apiKey, page)
                    
                    val allMovies = (popularMovies.results + topRatedMovies.results + nowPlayingMovies.results)
                        .distinctBy { it.id }
                    
                    emit(allMovies)
                    delay(1000) // Rate limiting
                } catch (e: Exception) {
                    // Handle error
                    emit(emptyList())
                }
            }
        }
        
        // Import TV Shows from TMDB and TVMaze
        suspend fun importTVShows(tmdbApiKey: String, maxPages: Int = 5): Flow<List<TVShow>> = flow {
            for (page in 1..maxPages) {
                try {
                    val popularShows = tmdbService.getPopularTVShows(tmdbApiKey, page)
                    val topRatedShows = tmdbService.getTopRatedTVShows(tmdbApiKey, page)
                    val onTheAirShows = tmdbService.getOnTheAirTVShows(tmdbApiKey, page)
                    
                    val allShows = (popularShows.results + topRatedShows.results + onTheAirShows.results)
                        .distinctBy { it.id }
                    
                    emit(allShows)
                    delay(1000) // Rate limiting
                } catch (e: Exception) {
                    // Handle error
                    emit(emptyList())
                }
            }
        }
        
        // Import Live TV Channels
        suspend fun importLiveTVChannels(): Flow<List<LiveTVChannel>> = flow {
            val channels = mutableListOf<LiveTVChannel>()
            
            // Add channels from StreamingConfig
            StreamingConfig.LIVE_TV_CHANNELS.forEach { channel ->
                channels.add(
                    LiveTVChannel(
                        name = channel.name,
                        url = channel.streamUrl,
                        logo = channel.logoUrl,
                        category = channel.category,
                        language = channel.language,
                        country = channel.country
                    )
                )
            }
            
            // Add more free live TV channels
            val additionalChannels = listOf(
                LiveTVChannel(
                    name = "Al Jazeera English",
                    url = "https://live-hls-web-aje.getaj.net/AJE/01.m3u8",
                    logo = "https://logo.clearbit.com/aljazeera.com",
                    category = "News",
                    language = "English",
                    country = "Qatar"
                ),
                LiveTVChannel(
                    name = "France 24 English",
                    url = "https://static.france24.com/live/F24_EN_HI_HLS/live_web.m3u8",
                    logo = "https://logo.clearbit.com/france24.com",
                    category = "News",
                    language = "English",
                    country = "France"
                ),
                LiveTVChannel(
                    name = "DW English",
                    url = "https://dwamdstream102.akamaized.net/hls/live/2015525/dwstream102/index.m3u8",
                    logo = "https://logo.clearbit.com/dw.com",
                    category = "News",
                    language = "English",
                    country = "Germany"
                )
            )
            
            channels.addAll(additionalChannels)
            emit(channels)
        }
        
        // Search Content
        suspend fun searchMovies(apiKey: String, query: String): List<Movie> {
            return try {
                tmdbService.searchMovies(apiKey, query).results
            } catch (e: Exception) {
                emptyList()
            }
        }
        
        suspend fun searchTVShows(apiKey: String, query: String): List<TVShow> {
            return try {
                tmdbService.searchTVShows(apiKey, query).results
            } catch (e: Exception) {
                emptyList()
            }
        }
        
        // Get Genres
        suspend fun getMovieGenres(apiKey: String): List<Genre> {
            return try {
                tmdbService.getMovieGenres(apiKey).genres
            } catch (e: Exception) {
                emptyList()
            }
        }
        
        suspend fun getTVGenres(apiKey: String): List<Genre> {
            return try {
                tmdbService.getTVGenres(apiKey).genres
            } catch (e: Exception) {
                emptyList()
            }
        }
    }
    
    // Content Cache Manager
    class ContentCacheManager(private val context: Context) {
        private val sharedPrefs = context.getSharedPreferences("dreamstv_cache", Context.MODE_PRIVATE)
        
        fun cacheMovies(movies: List<Movie>) {
            // Implement caching logic
        }
        
        fun cacheTVShows(shows: List<TVShow>) {
            // Implement caching logic
        }
        
        fun cacheLiveTVChannels(channels: List<LiveTVChannel>) {
            // Implement caching logic
        }
        
        fun getCachedMovies(): List<Movie> {
            // Implement retrieval logic
            return emptyList()
        }
        
        fun getCachedTVShows(): List<TVShow> {
            // Implement retrieval logic
            return emptyList()
        }
        
        fun getCachedLiveTVChannels(): List<LiveTVChannel> {
            // Implement retrieval logic
            return emptyList()
        }
    }
}
