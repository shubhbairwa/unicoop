package com.shubh.unicoop

import android.R
import android.content.DialogInterface
import android.content.DialogInterface.OnClickListener
import android.content.IntentFilter
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.paybacktraders.paybacktraders.viewmodel.MainViewModelProvider
import com.shubh.unicoop.adapters.ArticleAdapter
import com.shubh.unicoop.api.ApiClient
import com.shubh.unicoop.api.Apis
import com.shubh.unicoop.apihelper.Event
import com.shubh.unicoop.databinding.ActivityMainBinding
import com.shubh.unicoop.receiver.ConnectionReceiver
import com.shubh.unicoop.repository.DefaultMainRepositories
import com.shubh.unicoop.repository.MainRepos
import com.shubh.unicoop.viewmodel.MainViewModel

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers


class MainActivity : AppCompatActivity(), ConnectionReceiver.ReceiverListener {

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


    }

    private fun subscribeToObserver() {
        viewModel.articleSevenResponse.observe(this, Event.EventObserver(
            onError = {
                Log.e(TAG, "subscribeToObserver: $it")
                binding.progress.visibility = View.GONE
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()

                AlertDialog.Builder(this).setMessage("check Internet Connection")
                    .setPositiveButton(
                        "Retry"
                    ) { dialog, which -> checkConnection() }.setCancelable(false).show()
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


    private fun checkConnection() {
        // initialize intent filter

        val intentFilter = IntentFilter()


        // add action
        intentFilter.addAction("android.new.conn.CONNECTIVITY_CHANGE")


        // register receiver
        registerReceiver(ConnectionReceiver(), intentFilter)


        // Initialize listener
        ConnectionReceiver.Listener = this


        // Initialize connectivity manager
        val manager =
            applicationContext.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager


        // Initialize network info
        val networkInfo = manager.activeNetworkInfo


        // get connection status
        val isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting


        /* // display snack bar
         showSnackBar(isConnected)*/
        if (isConnected) {
            //  Toast.makeText(this, "Internet is COnnected", Toast.LENGTH_LONG).show()
            viewModel.getAllSectionArticle("all-sections")
            subscribeToObserver()
        } else {
            AlertDialog.Builder(this).setMessage("check Internet Connection")
                .setPositiveButton(
                    "Retry"
                ) { dialog, which -> checkConnection() }.setCancelable(false).show()
        }
    }


    override fun onNetworkChange(isConnected: Boolean) {
        // display snack bar
        if (isConnected) {

            viewModel.getAllSectionArticle("all-sections")
            subscribeToObserver()
        } else {
            AlertDialog.Builder(this).setMessage("check Internet Connection")
                .setPositiveButton(
                    "Retry"
                ) { dialog, which -> checkConnection() }.setCancelable(false).show()
        }
    }

    override fun onResume() {
        super.onResume()

        // call method
        checkConnection()
    }

    override fun onPause() {
        super.onPause()

        // call method
        checkConnection()
    }
}