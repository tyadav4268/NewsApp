package com.example.newsapp.newsapi

import com.google.gson.annotations.SerializedName

data class News(
    val title: String,
    val author: String,
    val url: String,
    @SerializedName("urlToImage") val imageUrl: String
)