package com.shubh.unicoop

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import com.shubh.unicoop.repository.DefaultMainRepositories
import com.shubh.unicoop.repository.MainRepos
import com.shubh.unicoop.viewmodel.MainViewModel
import com.paybacktraders.paybacktraders.viewmodel.MainViewModelProvider
import com.shubh.unicoop.adapters.ArticleAdapter
import com.shubh.unicoop.api.ApiClient
import com.shubh.unicoop.api.Apis
import com.shubh.unicoop.apihelper.Event
import com.shubh.unicoop.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    lateinit var viewModel: MainViewModel
    lateinit var articleAdapter: ArticleAdapter

    companion object {
        private const val TAG = "MainActivity"
    }

    private fun setUpViewModel() {
        val dispatchers: CoroutineDispatcher = Dispatchers.Main
        val mainRepos = DefaultMainRepositories() as MainRepos
        val articleApi: Apis = ApiClient().service
        val viewModelProviderfactory =
            MainViewModelProvider(application, mainRepos, dispatchers, articleApi)
        viewModel = ViewModelProvider(this, viewModelProviderfactory)[MainViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpViewModel()
        articleAdapter = ArticleAdapter()

        binding.rvArticles.layoutManager = LinearLayoutManager(this@MainActivity)
        binding.rvArticles.adapter = articleAdapter
        viewModel.getAllSectionArticle("all-sections")
        subscribeToObserver()




    }

    private fun subscribeToObserver() {
        viewModel.articleSevenResponse.observe(this, Event.EventObserver(
            onError = {
                Log.e(TAG, "subscribeToObserver: $it")
                binding.progress.visibility = View.GONE
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }, onLoading = {
                binding.progress.visibility = View.VISIBLE
            }, { resposne ->

                Log.e(TAG, "subscribeToObserver: ${resposne.size}")
                binding.progress.visibility = View.GONE
                if (resposne.isNotEmpty()) {
                    articleAdapter.submitList(resposne)
                    articleAdapter.notifyDataSetChanged()
                }

            }

        ))
    }
}