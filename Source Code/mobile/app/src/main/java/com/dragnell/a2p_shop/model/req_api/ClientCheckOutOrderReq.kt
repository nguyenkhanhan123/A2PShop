package com.dragnell.a2p_shop.model.req_api

import com.google.gson.annotations.SerializedName

class ClientCheckOutOrderReq(
    @SerializedName("fullName") val fullName: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("address") val address: String
)
