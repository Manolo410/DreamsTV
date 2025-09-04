package com.dreamsworld.dreamstv

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.dreamsworld.dreamstv.data.ContentImportService
import kotlinx.coroutines.*

/**
 * TVMaze Test Activity - Demonstrates Free TV Show API
 * No API key required! TVMaze is completely free.
 */
class TVMazeTestActivity : AppCompatActivity() {
    
    private lateinit var contentListView: ListView
    private lateinit var statusText: TextView
    private lateinit var searchButton: Button
    private lateinit var scheduleButton: Button
    private lateinit var peopleButton: Button
    private lateinit var searchEditText: EditText
    
    private val contentManager = ContentImportService.ContentImportManager()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tvmaze_test)
        
        initializeViews()
        setupButtons()
        loadInitialContent()
    }
    
    private fun initializeViews() {
        contentListView = findViewById(R.id.tvmaze_content_list)
        statusText = findViewById(R.id.tvmaze_status_text)
        searchButton = findViewById(R.id.tvmaze_search_button)
        scheduleButton = findViewById(R.id.tvmaze_schedule_button)
        peopleButton = findViewById(R.id.tvmaze_people_button)
        searchEditText = findViewById(R.id.tvmaze_search_edit)
    }
    
    private fun setupButtons() {
        searchButton.setOnClickListener {
            val query = searchEditText.text.toString().trim()
            if (query.isNotEmpty()) {
                searchTVShows(query)
            } else {
                Toast.makeText(this, "Please enter a search query", Toast.LENGTH_SHORT).show()
            }
        }
        
        scheduleButton.setOnClickListener {
            loadTVSchedule()
        }
        
        peopleButton.setOnClickListener {
            val query = searchEditText.text.toString().trim()
            if (query.isNotEmpty()) {
                searchPeople(query)
            } else {
                Toast.makeText(this, "Please enter a person's name", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun loadInitialContent() {
        statusText.text = "Loading popular TV shows from TVMaze…"
        
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val shows = withContext(Dispatchers.IO) {
                    contentManager.searchTVMazeShows("popular")
                }
                
                displayShows(shows)
                statusText.text = "Loaded ${shows.size} popular TV shows"
                
            } catch (e: Exception) {
                statusText.text = "Error loading shows: ${e.message}"
                Toast.makeText(this@TVMazeTestActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun searchTVShows(query: String) {
        statusText.text = "Searching for: $query"
        
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val shows = withContext(Dispatchers.IO) {
                    contentManager.searchTVMazeShows(query)
                }
                
                displayShows(shows)
                statusText.text = "Found ${shows.size} shows for: $query"
                
            } catch (e: Exception) {
                statusText.text = "Search error: ${e.message}"
                Toast.makeText(this@TVMazeTestActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun loadTVSchedule() {
        statusText.text = "Loading today's TV schedule…"
        
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val schedule = withContext(Dispatchers.IO) {
                    contentManager.getTVMazeSchedule("US")
                }
                
                displaySchedule(schedule)
                statusText.text = "Loaded ${schedule.size} shows airing today"
                
            } catch (e: Exception) {
                statusText.text = "Schedule error: ${e.message}"
                Toast.makeText(this@TVMazeTestActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun searchPeople(query: String) {
        statusText.text = "Searching for people: $query"
        
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val people = withContext(Dispatchers.IO) {
                    contentManager.searchTVMazePeople(query)
                }
                
                displayPeople(people)
                statusText.text = "Found ${people.size} people for: $query"
                
            } catch (e: Exception) {
                statusText.text = "People search error: ${e.message}"
                Toast.makeText(this@TVMazeTestActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun displayShows(shows: List<ContentImportService.TVShow>) {
        val showTitles = shows.map { show ->
            "${show.name} (${show.first_air_date?.take(4) ?: "Unknown"}) - Rating: ${show.vote_average}/10"
        }
        
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, showTitles)
        contentListView.adapter = adapter
        
        contentListView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val selectedShow = shows[position]
            showShowDetails(selectedShow)
        }
    }
    
    private fun displaySchedule(schedule: List<ContentImportService.ScheduleItem>) {
        val scheduleItems = schedule.map { item ->
            "${item.show.name} - ${item.airtime} (${item.network?.name ?: item.webChannel?.name ?: "Unknown Network"})"
        }
        
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, scheduleItems)
        contentListView.adapter = adapter
        
        contentListView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val selectedItem = schedule[position]
            showScheduleDetails(selectedItem)
        }
    }
    
    private fun displayPeople(people: List<ContentImportService.Person>) {
        val peopleNames = people.map { person ->
            "${person.name} (${person.country?.name ?: "Unknown Country"})"
        }
        
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, peopleNames)
        contentListView.adapter = adapter
        
        contentListView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val selectedPerson = people[position]
            showPersonDetails(selectedPerson)
        }
    }
    
    private fun showShowDetails(show: ContentImportService.TVShow) {
        val details = """
            Show: ${show.name}
            First Air Date: ${show.first_air_date ?: "Unknown"}
            Rating: ${show.vote_average}/10
            Overview: ${show.overview ?: "No description available"}
            Original Language: ${show.original_language}
            Origin Country: ${show.origin_country.joinToString(", ")}
        """.trimIndent()
        
        Toast.makeText(this, details, Toast.LENGTH_LONG).show()
    }
    
    private fun showScheduleDetails(item: ContentImportService.ScheduleItem) {
        val details = """
            Show: ${item.show.name}
            Episode: ${item.name}
            Season: ${item.season}, Episode: ${item.number}
            Air Time: ${item.airtime}
            Network: ${item.network?.name ?: item.webChannel?.name ?: "Unknown"}
            Runtime: ${item.runtime ?: "Unknown"} minutes
        """.trimIndent()
        
        Toast.makeText(this, details, Toast.LENGTH_LONG).show()
    }
    
    private fun showPersonDetails(person: ContentImportService.Person) {
        val details = """
            Name: ${person.name}
            Country: ${person.country?.name ?: "Unknown"}
            Birthday: ${person.birthday ?: "Unknown"}
            Gender: ${person.gender ?: "Unknown"}
        """.trimIndent()
        
        Toast.makeText(this, details, Toast.LENGTH_LONG).show()
    }
}
