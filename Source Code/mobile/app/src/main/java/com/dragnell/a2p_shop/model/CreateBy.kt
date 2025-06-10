package com.dragnell.a2p_shop.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.Date

data class CreateBy(
    @SerializedName("createAt") val createAt: Date
): Serializable
