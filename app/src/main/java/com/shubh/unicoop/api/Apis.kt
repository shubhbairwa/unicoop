package com.shubh.unicoop.api


import com.google.gson.JsonObject

import com.shubh.unicoop.data.ResponseArticle
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface Apis {


    @GET("{section}/{period}.json")
    suspend fun getALlArticleSeven(
        @Path("section") section: String,
        @Path("period") period: String,
        @Query("api-key") apiKey: String
    ): Response<ResponseArticle>


    @GET("{section}/{period}.json")
    suspend fun getALlArticleOther(
        @Path("section") section: String,
        @Path("period") period: String,
        @Query("api-key") apiKey: String
    ): Response<ResponseArticle>


}
