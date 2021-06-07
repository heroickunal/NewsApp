package com.example.newsapp.api

import com.example.newsapp.model.GetNewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsService {
    @GET("everything")
    suspend fun getNewsByTopic(
        @Query("sortBy") sortBy: String,
        @Query("from") fromDate: String,
        @Query("q") topic: String
    ): GetNewsResponse
}