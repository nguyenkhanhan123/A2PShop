package com.dragnell.a2p_shop.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.Date

data class UpdatedBy(
    @SerializedName("_id") val id: String,
    @SerializedName("account_id") val accountId: String,
    @SerializedName("updateAt") val updateAt: Date
): Serializable