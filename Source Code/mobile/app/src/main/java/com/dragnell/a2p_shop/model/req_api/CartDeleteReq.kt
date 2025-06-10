package com.dragnell.a2p_shop.model.req_api

import com.google.gson.annotations.SerializedName

class CartDeleteReq (
    @SerializedName("productIds")
    val productId: List<String>
)