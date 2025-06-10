package com.dragnell.a2p_shop.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Role(
    @SerializedName("_id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("permissions") val permissions: List<String>
): Serializable