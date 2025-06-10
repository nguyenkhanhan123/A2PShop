package com.dragnell.a2p_shop.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.Date

data class DeleteBy(
    @SerializedName("account_id") val accountId: String? = null,
    @SerializedName("deleteAt") val deleteAt: Date
): Serializable