package com.dragnell.a2p_shop.model.req_api

import com.google.gson.annotations.SerializedName

class CartFixReq (
    @SerializedName("productId")
    val productId: String,
    @SerializedName("quantity")
    val quantity: Int
)