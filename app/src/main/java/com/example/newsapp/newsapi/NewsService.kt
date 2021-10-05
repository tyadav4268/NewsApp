package com.example.newsapp.newsapi

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NewsService {
    private val newsApiService: NewsApiService
    private const val BASE_URL = "https://saurav.tech/NewsAPI/"

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        newsApiService = retrofit.create(NewsApiService::class.java)
    }

    fun getNews(category: String): Call<NewsApiResponse> {
        return newsApiService.getNews(category)
    }
}