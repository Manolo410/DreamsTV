package com.dreamsworld.dreamstv.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dreamsworld.dreamstv.model.Content;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<List<Content>> featuredContent;
    private MutableLiveData<List<Content>> popularContent;

    public HomeViewModel() {
        featuredContent = new MutableLiveData<>();
        popularContent = new MutableLiveData<>();

        // Load mock data for now
        loadMockData();
    }

    public LiveData<List<Content>> getFeaturedContent() {
        return featuredContent;
    }

    public LiveData<List<Content>> getPopularContent() {
        return popularContent;
    }

    private void loadMockData() {
        // Featured content
        List<Content> featured = new ArrayList<>();
        featured.add(new Content(1, "Featured Movie 1", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4", "Action", "thumbnail_url_1"));
        featured.add(new Content(2, "Featured Movie 2", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4", "Drama", "thumbnail_url_2"));
        featured.add(new Content(3, "Featured Movie 3", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4", "Comedy", "thumbnail_url_3"));
        featuredContent.setValue(featured);

        // Popular content
        List<Content> popular = new ArrayList<>();
        popular.add(new Content(4, "Popular Movie 1", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4", "Thriller", "thumbnail_url_4"));
        popular.add(new Content(5, "Popular Movie 2", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4", "Adventure", "thumbnail_url_5"));
        popular.add(new Content(6, "Popular Movie 3", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4", "Sci-Fi", "thumbnail_url_6"));
        popular.add(new Content(7, "Popular Movie 4", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerMeltdowns.mp4", "Documentary", "thumbnail_url_7"));
        popularContent.setValue(popular);
    }
}