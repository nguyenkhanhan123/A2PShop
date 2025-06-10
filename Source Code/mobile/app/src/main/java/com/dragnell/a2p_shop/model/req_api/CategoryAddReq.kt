package com.dragnell.a2p_shop.model.req_api

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CategoryAddReq(
    @SerializedName("title") val title: String,
    @SerializedName("parent_id") val parentId: String,
    @SerializedName("description") val description: String,
    @SerializedName("thumbnail") val thumbnail: String,
    @SerializedName("status") val status: String
) : Serializable
