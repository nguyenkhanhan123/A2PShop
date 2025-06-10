package com.dragnell.a2p_shop.model.req_api

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class RoleAddReq(
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("permissions") val permissions: List<String>,
    @SerializedName("deleted") val deleted: Boolean,
): Serializable