package com.dragnell.a2p_shop.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Category(
    @SerializedName("_id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("parent_id") val parentId: String,
    @SerializedName("description") val description: String,
    @SerializedName("thumbnail") val thumbnail: String,
    @SerializedName("status") val status: String,
    @SerializedName("position") val position: Int,
    @SerializedName("deleted") val deleted: Boolean,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String,
    @SerializedName("slug") val slug: String,
    @SerializedName("__v") val version: Int
) : Serializable
