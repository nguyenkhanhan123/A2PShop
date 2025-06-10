package com.dragnell.a2p_shop.model.res_api

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CartDeleteRes(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
): Serializable