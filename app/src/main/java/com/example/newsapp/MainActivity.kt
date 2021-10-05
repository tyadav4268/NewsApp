package com.example.newsapp


import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.databinding.ActivityMainBinding
import com.example.newsapp.newsapi.News
import com.example.newsapp.newsapi.NewsApiResponse
import com.example.newsapp.newsapi.NewsService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity(), NewsItemClicked {

    private lateinit var binding: ActivityMainBinding
    private lateinit var _adapter: NewsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        fetchData("general")
        _adapter = NewsListAdapter(this)
        binding.recyclerView.adapter = _adapter

        val category = resources.getStringArray(R.array.category)
        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, category)
        binding.autoCompleteTextView.setAdapter(arrayAdapter)

        binding.autoCompleteTextView.onItemClickListener =
            OnItemClickListener { parent, view, position, id ->
                Toast.makeText(
                    this@MainActivity,
                    category.get(position),
                    Toast.LENGTH_SHORT
                ).show()
                val inputText = category.get(position)
                var selectedCategory = ""
                when (inputText) {
                    "General" -> selectedCategory = "general"
                    "Entertainment" -> selectedCategory = "entertainment"
                    "Sports" -> selectedCategory = "sports"
                    "Business" -> selectedCategory = "business"
                    "Technology" -> selectedCategory = "technology"
                    "Health" -> selectedCategory = "health"
                }
                fetchData(selectedCategory)
            }
    }

    private fun fetchData(category: String) {
        NewsService.getNews(category).enqueue(object : Callback<NewsApiResponse> {
            override fun onResponse(
                call: Call<NewsApiResponse>,
                response: Response<NewsApiResponse>
            ) {
                _adapter.updateNews(
                    response.body()?.let { it.articles as ArrayList<News> }
                        ?: arrayListOf()
                )
            }

            override fun onFailure(call: Call<NewsApiResponse>, t: Throwable) {
                Log.e("NEWS_API_CALL", "Failed to load news from API")
                _adapter.updateNews(arrayListOf())
            }
        })
    }

    override fun onItemClicked(item: News) {
        val url: String = item.url
        val builder: CustomTabsIntent.Builder = CustomTabsIntent.Builder()
        val customTabsIntent: CustomTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(url))
    }
}