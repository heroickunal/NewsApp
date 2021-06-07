package com.example.newsapp.api

import com.example.newsapp.model.GetNewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsService {
    @GET("/everything?")
    suspend fun getNewsByQuery(
        @Query("q") topic: String
    ): GetNewsResponse
}