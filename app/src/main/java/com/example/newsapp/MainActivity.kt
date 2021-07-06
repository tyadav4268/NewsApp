package com.example.newsapp


import android.net.Uri
import android.os.Bundle
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.example.newsapp.databinding.ActivityMainBinding


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
        val arrayAdapter = ArrayAdapter(this,R.layout.dropdown_item,category)
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
                when(inputText){
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
        val url = "https://saurav.tech/NewsAPI/top-headlines/category/$category/in.json"
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            {
//                Log.d("response", "success")
                val newsJsonArray = it.getJSONArray("articles")
                val newsArray = ArrayList<News>()
                for(i in 0 until newsJsonArray.length()){
                    val newsJsonObject = newsJsonArray.getJSONObject(i)
                    val news = News(
                        newsJsonObject.getString("title"),
                        newsJsonObject.getString("author"),
                        newsJsonObject.getString("url"),
                        newsJsonObject.getString("urlToImage")
                    )
                    newsArray.add(news)
                }
                _adapter.updateNews(newsArray)
            },
            {

            }
        )
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    override fun onItemClicked(item: News) {
        val url: String = item.url
        val builder: CustomTabsIntent.Builder = CustomTabsIntent.Builder()
        val customTabsIntent: CustomTabsIntent  = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(url))
    }


}