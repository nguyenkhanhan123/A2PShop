package com.dragnell.a2p_shop.model.req_api

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CategoryFixReq(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
     @SerializedName("description") val description: String,
    @SerializedName("thumbnail") val thumbnail: String,
    @SerializedName("position") val position: Int
 ) : Serializable
