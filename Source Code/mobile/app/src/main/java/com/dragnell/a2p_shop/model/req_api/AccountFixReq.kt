package com.dragnell.a2p_shop.model.req_api

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.Date

data class AccountFixReq(
    @SerializedName("id") val id: String,
    @SerializedName("fullName") val fullName: String,
    @SerializedName("email") val email: String,
    @SerializedName("phone") val phone: String?,
    @SerializedName("role_id") val roleId: String,
) : Serializable
