package com.dreamsworld.dreamstv.model;

import java.io.Serializable;

public class Content implements Serializable {
    private int id;
    private String title;
    private String videoUrl;
    private String category;
    private String thumbnailUrl;

    public Content(int id, String title, String videoUrl, String category, String thumbnailUrl) {
        this.id = id;
        this.title = title;
        this.videoUrl = videoUrl;
        this.category = category;
        this.thumbnailUrl = thumbnailUrl;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getCategory() {
        return category;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }
}