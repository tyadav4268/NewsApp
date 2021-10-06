package com.example.newsapp.newsapi

data class NewsApiResponse(val status: String, val totalResults: Int, val articles: List<News>)