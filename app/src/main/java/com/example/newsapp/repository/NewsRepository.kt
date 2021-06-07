package com.example.newsapp.repository

import com.example.newsapp.api.NewsService

class NewsRepository(private val newsService: NewsService):BaseRepository() {

    suspend fun getNewsByQuery(city: String) = safeApiCall{
        newsService.getNewsByQuery(city)
    }
}