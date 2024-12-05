package com.shubh.unicoop.repository



import com.shubh.unicoop.apihelper.Resource
import com.shubh.unicoop.data.ResponseArticle
import com.shubh.unicoop.data.ResultArticle
import java.util.HashMap

interface MainRepos {


    suspend fun getAllAticleSeven(section:String): Resource<List<ResultArticle>>






}