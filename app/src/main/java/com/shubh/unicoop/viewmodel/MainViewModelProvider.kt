package com.paybacktraders.paybacktraders.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.shubh.unicoop.api.Apis
import com.shubh.unicoop.repository.MainRepos
import com.shubh.unicoop.viewmodel.MainViewModel

import kotlinx.coroutines.CoroutineDispatcher

class MainViewModelProvider(
    val app: Application,
    private val repos: MainRepos,
    private val dispatchers: CoroutineDispatcher,
    private val fanxApi: Apis

): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(app,repos,dispatchers,fanxApi) as T
    }
}