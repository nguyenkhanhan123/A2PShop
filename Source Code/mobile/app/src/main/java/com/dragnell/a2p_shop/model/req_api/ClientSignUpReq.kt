package com.dragnell.a2p_shop.model.req_api

import com.google.gson.annotations.SerializedName

class ClientSignUpReq(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("fullName") val fullName: String,
    @SerializedName("phone") val phone: String
)
