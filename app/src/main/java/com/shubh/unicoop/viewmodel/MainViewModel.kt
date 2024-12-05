package com.shubh.unicoop.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.shubh.unicoop.apihelper.Resource
import com.shubh.unicoop.api.Apis
import com.shubh.unicoop.apihelper.Event
import com.shubh.unicoop.data.ResponseArticle
import com.shubh.unicoop.data.ResultArticle
import com.shubh.unicoop.repository.MainRepos
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(
    val app: Application,
    private val repos: MainRepos,
    private val dispatchers: CoroutineDispatcher = Dispatchers.Main,
    val articleAPi: Apis
) : AndroidViewModel(app) {

    private val _articleSevenResponse = MutableLiveData<Event<Resource<List<ResultArticle>>>>()
    val articleSevenResponse: LiveData<Event<Resource<List<ResultArticle>>>> = _articleSevenResponse




    fun getAllSectionArticle(section:String) {

        _articleSevenResponse.postValue(Event(Resource.Loading()))
        viewModelScope.launch(Dispatchers.Main) {
            val result = repos.getAllAticleSeven(section)
            _articleSevenResponse.postValue(Event(result))
        }
    }





    private fun hasInternetConnection(): Boolean { // you can check anywhere by this method
        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetworkState = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetworkState) ?: return false
            return when {
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }


        }
        connectivityManager.activeNetworkInfo?.run {
            return when (type) {
                TYPE_WIFI -> true
                TYPE_MOBILE -> true
                TYPE_ETHERNET -> true
                else -> false

            }


        }
        return false
    }


}

private const val TAG = "MainViewModel"


