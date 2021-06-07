package com.example.newsapp.repository

import com.example.newsapp.api.NewsService

class NewsRepository(private val newsService: NewsService):BaseRepository() {

    suspend fun getNewsByTopic(topic: String,sortBy: String,fromDate: String) = safeApiCall{
        newsService.getNewsByTopic(topic,sortBy,fromDate)
    }
}