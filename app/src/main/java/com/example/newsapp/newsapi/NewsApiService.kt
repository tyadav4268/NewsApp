package com.example.newsapp.newsapi

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface NewsApiService {

    @GET("top-headlines/category/{category}/in.json")
    fun getNews(@Path("category") category: String): Call<NewsApiResponse>
}