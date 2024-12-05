package com.shubh.unicoop.data

data class ResponseArticle(
    val copyright: String,
    val num_results: Int,
    var results: MutableList<ResultArticle>,
    val status: String
)