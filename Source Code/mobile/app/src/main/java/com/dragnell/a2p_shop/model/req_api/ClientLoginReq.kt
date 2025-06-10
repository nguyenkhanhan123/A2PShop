package com.dragnell.a2p_shop.model.req_api

import com.google.gson.annotations.SerializedName

class ClientLoginReq (
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String
)
