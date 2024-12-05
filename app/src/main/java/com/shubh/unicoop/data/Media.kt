package com.shubh.unicoop.data

import com.google.gson.annotations.SerializedName

data class Media(
    val approved_for_syndication: Int,
    val caption: String,
    val copyright: String,
    val subtype: String,
    @SerializedName("media-metadata")
    val mediaMetadata: MutableList<MediaMetadata>,

    val type: String
)