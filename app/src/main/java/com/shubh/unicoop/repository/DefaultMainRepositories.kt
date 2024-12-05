package com.shubh.unicoop.repository

import com.shubh.unicoop.api.ApiClient
import com.shubh.unicoop.apihelper.Constant
import com.shubh.unicoop.apihelper.Resource
import com.shubh.unicoop.apihelper.safeCall
import com.shubh.unicoop.data.ResponseArticle
import com.shubh.unicoop.data.ResultArticle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class DefaultMainRepositories : MainRepos {


    override suspend fun getAllAticleSeven(section: String): Resource<List<ResultArticle>> = withContext(Dispatchers.IO) {
        safeCall {
            val response = ApiClient().service.getALlArticleSeven(section,"7", Constant.API_KEY)
            val response2 = ApiClient().service.getALlArticleOther(section,"7", Constant.API_KEY)

            var newCombineList=response.body()!!.results+response2.body()!!.results

            Resource.Success(newCombineList)
        }
    }

}